package game;

import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

import java.util.HashMap;

import actor.Player;
import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;
import input.Command;
import items.CompoundItem;
import items.Inventory;
import items.Item;

public class Game {
	
	private Player player;
	private Room room;
	
	private String output;
	
	private IDatabase db;
	
	public Game() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();	
		
		this.player = db.getPlayer(0);
		this.player.setGame(this);
		this.room = db.getRoom(player.getRoomID());
		this.output = "";
	}
	
	public Game(int roomID) {
		this();
		player.setRoomID(roomID);
		room = db.getRoom(roomID);
	}
	public void setOutput(String output) {
		this.output = output;
	}
	
	public void addOutput(String output) {
		this.output += output;
	}
	
	public String getOutput() {
		return output;
	}

	public Player getPlayer() {
		return player;
	}
//	
//	public String getLastOutput() {
//		return player.getLastOutput();
//	}
//	
//	public void setLastOutput(String lastOutput) {
//		player.setLastOutput(lastOutput);
//	}

	public void runCommand(String input) {
		this.output = "";
		Command command = new Command(input, this);
		command.execute();
	}
	
//	public void addRoom(Room room) {
//		rooms.put(room.getRoomID(), room);
//	}
	
	public Room getRoom() {
		return room;
	}

	public Room getRoom(int ID) {
		return db.getRoom(ID);
	}
	
	public void updateGameState(String lastOutput, int moves) {
		db.updateGameState(lastOutput, moves, player);
	}
	
	public void unlock(UnlockableObject object, Item unlockItem, Player player) {
		object.setLocked(false);	
		db.toggleLocks(object, false);	// set locked in DB
		
		if (object.consumeItem()) {
			player.getInventory().removeItem(unlockItem);	// remove item from inventory in DB
			db.destroyItem(unlockItem);
		}
	}
	
	public void unlockObject(RoomObject object, boolean locked) {
		object.setLocked(locked);
		db.toggleLocks((UnlockableObject) object, locked);	// db update lock status
	}
	
	public void breakItem(RoomObject container, CompoundItem item, String noun, String location) {
//		HashMap<String, Item> items = item.getItems();
//		
//		for (String identifier : items.keySet()) {
//			Item containedItem = items.get(identifier);
//			container.getInventory().addItem(identifier, containedItem);
//			db.addItemToInventory(container.getInventory(), containedItem);	// update DB item inventory ID, also destroys item
//		}
//		
//		container.getInventory().removeItem(noun);
//		item.getInventory().emptyInventory();
//		
//		db.destroyCompoundItem(item);	// nuke item from existence
		
		db.breakItem(item, container.getInventory());
		setOutput("You break apart the " + noun + " and dump the contents on the " + location + ".");
	}
	
	public void breakItem(Room room, CompoundItem item, String noun) {
		HashMap<String, Item> items = item.getItems();
		
		for (String identifier : items.keySet()) {
			Item containedItem = items.get(identifier);
			room.addItem(identifier, containedItem);
			db.addItemToInventory(room.getInventory(), containedItem);	// update DB item inventory ID
		}
		
		room.removeItem(noun);
		item.getInventory().emptyInventory();
		
		db.destroyCompoundItem(item);	// nuke item from existence
		
		setOutput("You break apart the " + noun + " and dumb the contents on the floor.");
	}
	
	public void dropItem(RoomObject container, String item, Player player, Puzzle puzzle, String location) {
		Inventory objectInventory = container.getInventory();
		
		Item toRemove = player.getInventory().removeItem(item);
		objectInventory.addItem(item, toRemove);
		setOutput("You placed the " + item + " on the " + location + "."); 
		db.removeItemFromInventory(objectInventory, toRemove); // update DB item inventory ID
		
		if (puzzle.getDescription().equals("weightPuzzle")) {
			double weightSolution = Double.parseDouble(puzzle.getSolution());
			
			if (objectInventory.getCurrentWeight() >= weightSolution) {
				System.out.println(puzzle.getUnlockObstacle());
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	
				
				if (obstacle.isLocked()) {	
					obstacle.setLocked(false);
					obstacle.setPreviouslyUnlocked(true);
					db.toggleLocks((UnlockableObject) obstacle, false); // update obstacle locked and previously unlocked
					addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
				}
			}
		}
	}
	
	public void dropItem(Room room, String item, Player player, Puzzle puzzle) {
		Item toDrop = player.getInventory().getItem(item);
		
//		Item removed = player.getInventory().removeItem(item);
//		room.getInventory().addItem(item, removed);
		
//		System.out.println(room.listItems());
		System.out.println("ROOM ID " + room.getRoomID());
		db.removeItemFromInventory(room.getInventory(), toDrop); // update DB inventory ID
		
		if (room.getInventory().contains(item)) {
			System.out.println("what the fuck");
		}
		
		setOutput("You dropped " + item + " on the floor.");	
	}
	
	public void feedItem(RoomObject object, Player player, Puzzle puzzle, String noun, String location) {

				// pour(RoomObject object, Item item, Player player, Puzzle puzzle, String noun, String location)
				// This transfers the object 
				object.feed(noun);
				
				Item toDrop = player.getInventory().getItem(noun);
				//db.removeItemFromInventory(room.getInventory(), toDrop); // update DB inventory ID
			
				setOutput("You fed " + noun + " to the " + location + ".");
				
				if (toDrop.consumeOnUse()) {
					player.getInventory().removeItem(noun);	// update inventoryID in DB (make it something random so disappeared)
					db.consumeItem(toDrop);
				}
				
				if (puzzle.getSolution().equals(object.getFed())) 
				{
					
					RoomObject toUnlock = player.getRoom().getObject(puzzle.getUnlockObstacle());
					
					if (toUnlock.isLocked()) 
					{
						toUnlock.setLocked(false);
						// What is this doing? because it should be unlocking the door
						db.toggleLocks((UnlockableObject) toUnlock, false);	// db update lock status
						db.pushObject(object, "west");

						// This changes the status of the hellhound, but we want the door and the hellhound
						// to be updated. Want hellhound to move out of the way and for the door to open.
						addOutput("\nThe " + location + " is occupied away from an open " + toUnlock.getName() + " to the " + toUnlock.getDirection() + "!!");
						object.isFed();
					}
				}
				
	}
	
	
	// Put your scan and climb commands in here
	public void scanItem(RoomObject object, Player player, Puzzle puzzle, String noun, String location) {

		// pour(RoomObject object, Item item, Player player, Puzzle puzzle, String noun, String location)
		// This transfers the object 
		object.scan(noun);
		
		Item toScan = player.getInventory().getItem(noun);
		//db.removeItemFromInventory(room.getInventory(), toDrop); // update DB inventory ID
	
		setOutput("You scanned " + noun + " on the " + location + ".");
		
		if (toScan.consumeOnUse()) {
			player.getInventory().removeItem(noun);	// update inventoryID in DB (make it something random so disappeared)
			db.consumeItem(toScan);
		}
		
		if (puzzle.getSolution().equals(object.getFed())) 
		{
			
			RoomObject toUnlock = player.getRoom().getObject(puzzle.getUnlockObstacle());
			
			if (toUnlock.isLocked()) 
			{
				toUnlock.setLocked(false);
				// What is this doing? because it should be unlocking the door
				db.toggleLocks((UnlockableObject) toUnlock, false);	// db update lock status
				

				// This changes the status of the hellhound, but we want the door and the hellhound
				// to be updated. Want hellhound to move out of the way and for the door to open.
				addOutput("\nThe " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open!");
				object.isScanned();
			}
		}
		
}
	
	
	public boolean play(PlayableObject object, Item item, Player player, Puzzle puzzle, String noun, String location) {
		object.getInventory().addItem(noun, item);
		db.addItemToInventory(object.getInventory(), item);	// update DB inventoryID
		
		if (puzzle instanceof ObjectPuzzle) {
			ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;
			
			if (obstaclePuzzle.isSolved(item.getItemID())) {
				return true;
			}
		}

		setOutput("Played " + noun + " on the " + location + ".");
		
		return false;
	}
	
	public boolean play(PlayableObject object, String note, Puzzle puzzle) {
		if (object.playNote(note)) {
			String notes = object.getPlayedNotes() + note;
			db.playNotes(object, notes);	// update notes played in DB	
			
			if (object.playedPassage()) {			
				return true;
			}
		} else {
			setOutput("You entered an invalid note.");
		}
		
		return false;
	}
	
	public void pour(RoomObject object, Item item, Player player, Puzzle puzzle, String noun, String location) {
		object.cover(noun);	// update covered in DB
		setOutput("You poured the " + noun + " on the " + location + ".");
		
		if (item.consumeOnUse()) {
			player.getInventory().removeItem(noun);	// update inventoryID in DB (make it something random so disappeared)
			db.consumeItem(item);
		}
		
		if (puzzle.getSolution().equals(object.getCovering())) {
			RoomObject solutionObject = player.getRoom().getObject(puzzle.getUnlockObstacle());
			
			if (solutionObject.isLocked()) {
				solutionObject.setLocked(false);	// update locked in DB
				db.toggleLocks((UnlockableObject) solutionObject, false);	// db update lock status
				
				if (room.getRoomID() == 8)
				{
					addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open! Revealing a set of stairs going up!");

				}
				
				else
				{	
					addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!");
				}
			}
		}
	}
	
	public void push(RoomObject object, String direction) {
		object.setDirection(direction);
		db.pushObject(object, direction);	// update location in DB
	}
	
	public void take(RoomObject object, Item item, Player player, Puzzle puzzle, String noun) {
		Inventory objectInventory = object.getInventory();
		
		player.getInventory().addItem(noun, item);
		item.setInInventory(true);
		
		db.addItemToInventory(player.getInventory(), item);	// update itemID in DB
		
		setOutput("You picked up " + noun + ".");	
		
		if (puzzle.getDescription().equals("weightPuzzle")) {
			double weightSolution = Double.parseDouble(puzzle.getSolution());
			
			if (objectInventory.getCurrentWeight() < weightSolution) {
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	
				
				if (!obstacle.isLocked() && obstacle.wasPreviouslyUnlocked()) {
					obstacle.setLocked(true);	
					obstacle.setPreviouslyUnlocked(false);
					db.toggleLocks((UnlockableObject) obstacle, true);	// update locked and previously unlocked in DB
					addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " slams shut.");
				}
			}
		}
	}
	
	public void take(Room room, Item item, Player player, String noun) {
		item.setInInventory(true);
		player.getInventory().addItem(noun, item);
		room.removeItem(noun);
		db.addItemToInventory(player.getInventory(), item);	// update item inventoryID in DB
	}
	
	public void type(Puzzle puzzle, RoomObject object, String noun) {
		puzzle.setSolved(true);	// set to solved in DB
		
		if (object.isLocked()) {
			object.setLocked(false);
			db.toggleLocks((UnlockableObject) object, false);	// update locked in DB
			setOutput("A " + object.getName() + " to the " + object.getDirection() + " swings open!");
		} else {
			setOutput("You typed " + noun + ".");
		}
	}
	
	public void moveRooms(Player player, String direction) {		
		int roomID = player.getRoom().getExit(direction);
		
		if (roomID != -1) {
			player.setRoomID(roomID);	
			
			db.moveRooms(player, roomID);  // update roomID in database
			
			addOutput("You walk " + direction + "\n\n");
			addOutput(db.getDescription(player.getRoomID()));	// grab room description from DB
		} else {
			addOutput("There is not an exit here!");
		}
	}
}