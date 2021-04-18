package game;

import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

import java.util.HashMap;

import actor.Player;
import input.Command;
import items.CompoundItem;
import items.Inventory;
import items.Item;

public class Game {
	private HashMap<Integer, Room> rooms;
	private int moves;
	private Player player;
	private String output;
	
	public Game(int roomID) {
		this.moves = 0;	// probs store this in servlet
		this.player = new Player(this, roomID);		// load player from DB, include inv
		this.rooms = new HashMap<Integer, Room>();	// load current room from DB, including items and objects
		this.output = "";

		RoomGeneration.generateRooms(rooms);	// should disappear after db
	}

	public Game() {
		this(1);
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

	public void runCommand(String input) {
		this.output = "";
		Command command = new Command(input, this);
		command.execute();
		moves++;
	}
	
	public void addRoom(Room room) {
		rooms.put(room.getRoomID(), room);
	}

	public Room getRoom(int ID) {
		return rooms.get(ID);
	}
	
	public int getMoves() {
		return moves;
	}
	
	public void resetMoves() {
		moves = 0;
	}
	
	public void unlockObject(RoomObject object, boolean locked) {
		object.setLocked(locked);
		// db update lock status
	}
	
	public void breakItem(RoomObject container, CompoundItem item, String noun, String location) {
		HashMap<String, Item> items = item.getItems();
		
		for (String identifier : items.keySet()) {
			container.getInventory().addItem(identifier, items.get(identifier));
			// update DB item inventory ID
		}
		
		container.getInventory().removeItem(noun);
		item.getInventory().emptyInventory();
		
		setOutput("You break apart the " + noun + " and dump the contents on the " + location + ".");
	}
	
	public void breakItem(Room room, CompoundItem item, String noun) {
		HashMap<String, Item> items = item.getItems();
		
		for (String identifier : items.keySet()) {
			room.addItem(identifier, items.get(identifier));
			// update DB item inventory ID
		}
		
		room.removeItem(noun);
		item.getInventory().emptyInventory();
		
		setOutput("You break apart the " + noun + " and dumb the contents on the floor.");
	}
	
	public void dropItem(RoomObject container, String item, Player player, Puzzle puzzle, String location) {
		Inventory objectInventory = container.getInventory();
		
		Item toRemove = player.getInventory().removeItem(item);
		objectInventory.addItem(item, toRemove);
		setOutput("You placed the " + item + " on the " + location + "."); 
		// update DB item inventory ID
		
		if (puzzle.getDescription().equals("weightPuzzle")) {
			double weightSolution = Double.parseDouble(puzzle.getSolution());
			
			if (objectInventory.getCurrentWeight() >= weightSolution) {
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	
				
				if (obstacle.isLocked()) {	// update obstacle locked and previously unlocked
					obstacle.setLocked(false);
					obstacle.setPreviouslyUnlocked(true);
					addOutput("A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.");
				}
			}
		}
	}
	
	public void dropItem(Room room, String item, Player player, Puzzle puzzle) {
		Item removed = player.getInventory().removeItem(item);
		removed.setInInventory(false);
		room.addItem(item, removed);
		setOutput("You dropped " + item + " on the floor.");	// update DB inventory ID
	}
	
	public boolean play(PlayableObject object, Item item, Player player, Puzzle puzzle, String noun, String location) {
		object.getInventory().addItem(noun, item);
		
		setOutput("Played " + noun + " on the " + location + ".");
		
		if (puzzle instanceof ObjectPuzzle) {
			ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;
			
			if (obstaclePuzzle.isSolved()) {
				return  true;
			}
		}
		
		// update DB inventoryID
		
		return false;
	}
	
	public boolean play(PlayableObject object, String note, Puzzle puzzle) {
		if (object.playNote(note)) {
			if (object.playedPassage()) {
				// update notes played in DB
				return true;
			}
		} else {
			setOutput("You entered an invalid note.");
		}
		
		return false;
	}
	
	public void pour(RoomObject object, Item item, Player player, Puzzle puzzle, String noun) {
		object.cover(noun);	// update covered in DB
		
		if (item.consumeOnUse()) {
			player.getInventory().removeItem(noun);	// update inventoryID in DB (make it something random so disappeared)
		}
		
		if (puzzle.getSolution().equals(object.getCovering())) {
			RoomObject solutionObject = player.getRoom().getObject(puzzle.getUnlockObstacle());
			
			if (solutionObject.isLocked()) {
				solutionObject.setLocked(false);	// update locked in DB
				addOutput("\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!");
			}
		}
	}
	
	public void push(RoomObject object, String direction) {
		object.setDirection(direction);
		// update location in DB
	}
	
	public void take(RoomObject object, Item item, Player player, Puzzle puzzle, String noun) {
		Inventory objectInventory = object.getInventory();
		
		player.getInventory().addItem(noun, item);
		item.setInInventory(true);
		
		setOutput("You picked up " + noun + ".");	// update itemID in DB
		
		if (puzzle.getDescription().equals("weightPuzzle")) {
			double weightSolution = Double.parseDouble(puzzle.getSolution());
			
			if (objectInventory.getCurrentWeight() < weightSolution) {
				RoomObject obstacle = player.getRoom().getObject(puzzle.getUnlockObstacle());	
				
				if (!obstacle.isLocked() && obstacle.wasPreviouslyUnlocked()) {
					obstacle.setLocked(true);	// update locked and previously unlocked in DB
					obstacle.setPreviouslyUnlocked(false);
					addOutput("\nA " + obstacle.getName() + " to the " + obstacle.getDirection() + " slams shut.");
				}
			}
		}
	}
	
	public void take(Room room, Item item, Player player, String noun) {
		item.setInInventory(true);
		player.getInventory().addItem(noun, item);	// update item inventoryID in DB
		room.removeItem(noun);
	}
	
	public void type(Puzzle puzzle, RoomObject object, String noun) {
		puzzle.setSolved(true);	// set to solved in DB
		
		if (object.isLocked()) {
			object.setLocked(false);	// update locked in DB
			setOutput("A " + object.getName() + " to the " + object.getDirection() + " swings open!");
		} else {
			setOutput("You typed " + noun + ".");
		}
	}
	
	public void unlock(UnlockableObject object, Item unlockItem, Player player) {
		object.setLocked(false);	// set locked in DB
		
		if (object.consumeItem()) {
			player.getInventory().removeItem(unlockItem);	// remove item from inventory in DB
		}
	}
	
	public void moveRooms(Player player, String direction) {		
		int roomID = player.getRoom().getExit(direction).getRoomID();
		player.setRoomID(roomID);	// update roomID in database
		
		addOutput("You walk " + direction + "\n\n");
		addOutput(player.getRoom().getDescription());	// gonna have to call the new description from DB
	}

	public void updateGameState() { }
	
	public void saveData() { }
	
	public void loadData() { }
}