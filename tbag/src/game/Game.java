package game;

import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

import java.util.HashMap;

import actor.NPC;
import actor.Player;
import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;
import dialogue.Node;
import input.Command;
import items.CompoundItem;
import items.Inventory;
import items.Item;

public class Game {

	private Player player;
	private Room room;

	private String output;

	private IDatabase db;

	private int gameID;

	public Game(int gameID) {
		DatabaseProvider.setInstance(new DerbyDatabase(gameID));
		db = DatabaseProvider.getInstance();	// instantiate DB

		this.player = db.getPlayer(0);	// grab player and room from DB
		this.player.setGame(this);	// set player's game instance to this instasnce
		this.room = db.getRoom(player.getRoomID());
		this.output = "";
		this.gameID = gameID;
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

	// run command from user input

	public void runCommand(String input) {
		this.output = "";
		Command command = new Command(input, this);
		command.execute();
	}

	public Room getRoom() {
		return room;
	}

	public Room getRoom(int ID) {
		return db.getRoom(ID);	// grab room from DB with ID (used in junits)
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public void updateGameState(String lastOutput, int moves) {
		db.updateGameState(lastOutput, moves, player);	// update players table with move count and last output
	}

	public void unlock(UnlockableObject object, Item unlockItem, Player player) {
		object.setLocked(false);	// unlock the obstacle
		db.toggleLocks(object, false);	// set locked in DB

		if (object.consumeItem()) {	// remove item from DB if it should be consumed on use
			player.getInventory().removeItem(unlockItem);	// remove item from inventory in DB
			db.destroyItem(unlockItem);
		}
	}

	public void unlockObject(RoomObject object, boolean locked) {
		object.setLocked(locked);	// set locked to boolean
		db.toggleLocks((UnlockableObject) object, locked);	// db update lock status
	}

	public void breakItem(RoomObject container, CompoundItem item, String noun, String location) {
		db.breakItem(item, container.getInventory());	// break item in the DB
		setOutput("You break apart the " + noun + " and dump the contents on the " + location + ".");
	}

	public void breakItem(Inventory inventory, CompoundItem item, String noun, String output) {
		db.breakItem(item, inventory);	// break item into the inventory
		setOutput("You break apart the " + noun + " and dump the contents " + output);
	}

	// Added these 2 next methods
	public void popItem(RoomObject container, CompoundItem item, String noun, String location) {
		if (item.getItems().size() == 0) {	// check if empty
			setOutput("You pop the " + noun + ", but there is nothing inside.");
		} else {	
			setOutput("You pop the " + noun + " and dump the contents on the " + location + ".");
		}

		db.breakItem(item, container.getInventory());		// break item
	}

	public void popItem(Room room, CompoundItem item, String noun) {
		if (item.getItems().size() == 0) {	// check empty
			setOutput("You pop the " + noun + ", but there is nothing inside.");
		} else {	
			setOutput("You pop the " + noun + " and dump the contents on the floor!");
		}	

		db.breakItem(item, room.getInventory());		// break item
	}

	public void dropItem(RoomObject container, String item, Player player, Puzzle puzzle, String location) {
		Inventory objectInventory = container.getInventory();

		Item toRemove = player.getInventory().getItem(item);	// grab the remove item from the inventory

		if (puzzle.getDescription().equals("Lab Room") && location.equals("cadaver")) {		// edge case for lab room	
			if (container.getInventory().getCurrentWeight() + toRemove.getWeight() > 0.2) {	// make sure the cadaver can only hold one set of eyes
				setOutput("\nThis " + location + "'s eye sockets are full already!");
				return;
			} else {	
				objectInventory.addItem(item, toRemove);	// add item to inventory and update DB
				addOutput("You placed the " + item + " in the " + location + "."); 
				db.removeItemFromInventory(objectInventory, toRemove); // update DB item inventory ID

				if (objectInventory.contains(puzzle.getSolution())) {	// check if the puzzle solution has been met
					RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	

					if (obstacle.isLocked()) {		// unlock door and update DB if puzzle is solved
						obstacle.setLocked(false);
						db.toggleLocks((UnlockableObject) obstacle, false);	// db update lock status
						addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
					}
				}
			}
		} else if (puzzle.getDescription().equals("Witch's Lair") && location.equals("witch's pot")) {	// witch's lair edge case
			double weightSolution = Double.parseDouble(puzzle.getSolution());

			if (container.getInventory().getInventorySize() >= 3) {	// make sure more than 3 items cannot be tried at once
				setOutput("\nThis " + location + " is full! Try taking items from the witch's pot and replacing them with different ones.");
				return;
			}

			objectInventory.addItem(item, toRemove);
			addOutput("You placed the " + item + " in the " + location + "."); // perform actual drop command
			db.removeItemFromInventory(objectInventory, toRemove); // update DB item inventory ID

			if (container.getInventory().getCurrentWeight() >= weightSolution && container.getInventory().getInventorySize() == 3) {
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	

				if (obstacle.isLocked()) {	// check if the puzzle is solved and unlock door if so
					obstacle.setLocked(false);
					obstacle.setPreviouslyUnlocked(true);
					db.toggleLocks((UnlockableObject) obstacle, false); // update obstacle locked and previously unlocked
					setOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
				}
			}
		} else {
			objectInventory.addItem(item, toRemove);	// perform real drop if edge case not hit
			db.removeItemFromInventory(objectInventory, toRemove); // update DB item inventory ID
			addOutput("You placed the " + item + " on the " + location + ".\n"); 
		}

		if (puzzle.getDescription().equals("weightPuzzle")) {	// weight puzzle edge case (generalized)
			double weightSolution = Double.parseDouble(puzzle.getSolution());

			if (objectInventory.getCurrentWeight() >= weightSolution) {	// check that weight requirement has been hit
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	

				if (obstacle.isLocked()) {	// unlock door and update DB if puzzle has been solved
					obstacle.setLocked(false);
					obstacle.setPreviouslyUnlocked(true);
					db.toggleLocks((UnlockableObject) obstacle, false); // update obstacle locked and previously unlocked
					addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
				}
			}
		}
	}

	public void dropItem(Room room, String item, Player player, Puzzle puzzle) {
		Item toDrop = player.getInventory().getItem(item);	// drop item on the floor
		db.removeItemFromInventory(room.getInventory(), toDrop); // update DB inventory ID		
	}

	public void dropAll(Inventory destination, HashMap<String, Item> items, Puzzle puzzle, String location) {
		for (String identifier : items.keySet()) {
			destination.addItem(identifier, items.get(identifier));
			db.removeItemFromInventory(destination, items.get(identifier));
			addOutput("You dropped " + identifier + " on the " + location + ".\n");
		}

		// weight puzzle edge case
		// wish i had time to clean this up considering the upper method
		if (puzzle.getDescription().equals("weightPuzzle")) {	// weight puzzle edge case (generalized)
			double weightSolution = Double.parseDouble(puzzle.getSolution());

			if (destination.getCurrentWeight() >= weightSolution) {	// check that weight requirement has been hit
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	

				if (obstacle.isLocked()) {	// unlock door and update DB if puzzle has been solved
					obstacle.setLocked(false);
					obstacle.setPreviouslyUnlocked(true);
					db.toggleLocks((UnlockableObject) obstacle, false); // update obstacle locked and previously unlocked
					addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
				}
			}
		}
	}

	public void feedItem(RoomObject object, Player player, Puzzle puzzle, String noun, String location) {
		// This transfers the object 
		object.feed(noun);

		Item toDrop = player.getInventory().getItem(noun);
		db.removeItemFromInventory(object.getInventory(), toDrop); // update DB inventory ID

		setOutput("You fed " + noun + " to the " + location + ".");

		if (toDrop.consumeOnUse()) {
			player.getInventory().removeItem(noun);	// update inventoryID in DB 
			db.consumeItem(toDrop);	// remove if consume on use
		}

		if (puzzle.getSolution().equals(object.getFed())) {
			RoomObject toUnlock = player.getRoom().getObject(puzzle.getUnlockObstacle());

			if (toUnlock.isLocked()) {
				toUnlock.setLocked(false);
				db.toggleLocks((UnlockableObject) toUnlock, false);	// db update lock status
				db.pushObject(object, "west");

				// This changes the status of the hellhound, but we want the door and the hellhound
				// to be updated. Want hellhound to move out of the way and for the door to open.
				addOutput("\nThe " + location + " is occupied away from an open " + toUnlock.getName() + " to the " + toUnlock.getDirection() + "!!");
			}
		}	
	}

	// Put your scan and climb commands in here
	public void climbObject(RoomObject object, Player player, Puzzle puzzle, String noun) 
	{
		int roomID = player.getRoom().getExit(object.getName());

		if (roomID != -1) {
			player.setRoomID(roomID);	// update room ID

			db.moveRooms(player, roomID);  // update roomID in database

			addOutput("You climbed the " + object.getName() + ".\n\n");	
			addOutput(db.getDescription(player.getRoomID()));	// grab room description from DB
		} else {
			addOutput("There is not an exit here!" + object.getName());
		}
	}

	public void scanItem(RoomObject object, Player player, Puzzle puzzle, String noun, String location) {
		// This transfers the object 
		object.scanned(noun);

		Item toScan = player.getInventory().getItem(noun);
		//db.removeItemFromInventory(room.getInventory(), toDrop); // update DB inventory ID

		setOutput("You scanned " + noun + " on the " + location + ".");

		if (toScan.consumeOnUse()) {	// remove item if consume on use
			player.getInventory().removeItem(noun);	// update inventoryID in DB (make it something random so disappeared)
			db.consumeItem(toScan);
		}

		if (puzzle.getSolution().equals(object.getScanned())) {	// if puzzle is solved
			RoomObject toUnlock = player.getRoom().getObject(puzzle.getUnlockObstacle());

			if (toUnlock.isLocked()) {	// unlock doors and update DB if necessary
				toUnlock.setLocked(false);
				db.toggleLocks((UnlockableObject) toUnlock, false);	// db update lock status

				addOutput("\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open!");
				object.isScanned();
			}
		}
	}


	public boolean play(PlayableObject object, Item item, Player player, Puzzle puzzle, String noun, String location) {
		object.getInventory().addItem(noun, item);
		db.addItemToInventory(object.getInventory(), item);	// update DB inventoryID

		if (puzzle instanceof ObjectPuzzle) {	// make sure the puzzle is an object puzzle
			ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;

			if (obstaclePuzzle.isSolved(item.getItemID())) {	// make sure it contains the required item
				return true;
			}
		}

		setOutput("Played " + noun + " on the " + location + ".");

		return false;
	}

	public boolean play(PlayableObject object, String note, Puzzle puzzle) {
		if (object.playNote(note)) {
			String notes = object.getPlayedNotes() + note;	// append new notes to old ones played
			db.playNotes(object, notes);	// update notes played in DB	

			if (object.playedPassage()) {	// return whether the required passage has been completed
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
			db.consumeItem(item);	// consume item if required
		}

		if (puzzle.getSolution().equals(object.getCovering())) {	// check if the solution equals what it has been covered in
			RoomObject solutionObject = player.getRoom().getObject(puzzle.getUnlockObstacle());

			if (solutionObject.isLocked()) {
				solutionObject.setLocked(false);	// update locked in DB
				db.toggleLocks((UnlockableObject) solutionObject, false);	// db update lock status

				if (room.getRoomID() == 8) {	// change output based on the room ID (special on stage changes)
					addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open! Revealing a set of stairs going up!");
				} else {	
					addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!");
				}
			}
		}
	}

	public void push(RoomObject object, String direction) {
		object.setDirection(direction);	// set direction
		db.pushObject(object, direction);	// update location in DB
	}

	public void take(RoomObject object, Item item, Player player, Puzzle puzzle, String noun) {
		Inventory objectInventory = object.getInventory();

		player.getInventory().addItem(noun, item);
		item.setInInventory(true);

		db.addItemToInventory(player.getInventory(), item);	// update itemID in DB

		if (puzzle.getDescription().equals("weightPuzzle")) {	// check weight puzzle
			double weightSolution = Double.parseDouble(puzzle.getSolution());

			if (objectInventory.getCurrentWeight() < weightSolution) {
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	

				if (!obstacle.isLocked() && obstacle.wasPreviouslyUnlocked()) {	// relock the door if the weight is taken away
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

	public void give(NPC npc, Player player, Item item) {
		db.removeItemFromInventory(npc.getInventory(), item);	// take item out of inventory, add to NPCs in DB
	}

	public void type(Puzzle puzzle, RoomObject object, String noun) {
		puzzle.setSolved(true);	// set to solved in DB

		if (object.isLocked()) {	// unlock door and update DB
			object.setLocked(false);
			db.toggleLocks((UnlockableObject) object, false);	// update locked in DB
			setOutput("A " + object.getName() + " to the " + object.getDirection() + " swings open!");
		} else {
			setOutput("You typed " + noun + ".");	// nothing to unlock
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

	public void npcDialogue(NPC npc, Node currentNode) {
		db.npcDialogue(npc, npc.isTalkedTo(), currentNode.getNodeID(), npc.CanTalkTo(), npc.isDone());	// update dialogue tree in DB
	}

	public void reloadDatabaseForJUnit() {
		db.deleteData(gameID);	// reload all database for test user in DB
		db.loadInitialData(gameID);

		reloadData();
	}

	public void movePlayerJUnit(int roomID) {
		player = db.getPlayer(0);	// move the player to a new room and regenerate cases
		player.setRoomID(roomID);
		player.setGame(this);
		room = db.getRoom(player.getRoomID());
		output = "";
	}

	public void reloadData() {
		player = db.getPlayer(0);	// reload data from contructor
		player.setGame(this);
		room = db.getRoom(player.getRoomID());
		output = "";
	}

	public IDatabase getDatabase() {
		return db;
	}
}