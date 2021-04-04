package game;

import items.Item;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import object.Puzzle;

import java.util.HashMap;

import actor.Player;
import input.Command;

public class Game {
	private HashMap<Integer, Room> rooms;
	private int moves;
	private Player player;

	public Game() {
		this.moves = 0;
		this.player = new Player(this, 1);
		this.rooms = new HashMap<Integer, Room>();

		createRooms();
	}

	public Player getPlayer() {
		return player;
	}

	public String runCommand(String input) {
		Command command = new Command(input, this);
		moves++;
		return command.execute();
	}

	// public void play()

	public void createRooms() {
		// 24 rooms and an exit 
		Room room1 = new Room("You are in a room with a westward door containing nothing but a table.", 1);
		Room room2 = new Room("You are in a dimly lit kitchen with some random items laying about. There is a door to the west and there appears to be some sensory pad contained in the room...", 2);
		Room room3 = new Room("You are in a room with a keypad on the door to the south and a chest on the other side of the room.", 3);
		Room room4 = new Room("You are in a room with a dresser, bed, and desk but do not appear to see an exit.", 4);
		Room room5 = new Room("This is the current last room. More will be added later.", 5);

		
		
		room1.addExit("west", room2);
		
		room2.addExit("east", room1);
		room2.addExit("west", room3);
		
		room3.addExit("east", room2);
		room3.addExit("south", room4);
		
		room4.addExit("north", room3);
		room4.addExit("south", room5);
		
		room5.addExit("north", room4);
	//	room5.addExit("east", room6);
		


		// Room 1
		Item key = new Item("key");
		key.setWeight(0.1);
		key.setDescription("This key seems to be able to unlock a door.");
		
		room1.addItem("key", key);
		
		UnlockableObject door = new UnlockableObject("door", "Probably leads to another room...", "west", true, "key");
		door.setLocked(true);
		room1.addObject("door", door);
		
		RoomObject table = new RoomObject("Table", "A table that can hold things!", "north", false, false, false);
		room1.addObject("table", table);
		
		room1.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "Maybe the key will do something...", false));
		
		// Room 2
		Item banana = new Item("banana");
		banana.setWeight(0.3);
		banana.setDescription("Fruit.");
		
		room2.addItem("banana", banana);
		
		Item apple = new Item("apple");
		apple.setWeight(0.5);
		apple.setDescription("Fruit.");
		
		room2.addItem("apple", apple);

		Item phonebook = new Item("phonebook");
		phonebook.setWeight(2.5);
		phonebook.setDescription("Full of numbers.");
		
		room2.addItem("phonebook", phonebook);
		
		Item cookbook = new Item("cookbook");
		cookbook.setWeight(2.0);
		cookbook.setDescription("Full of recipes.");
		
		room2.addItem("cookbook", cookbook);
		
		Item plate = new Item("plate");
		plate.setWeight(1.0);
		plate.setDescription("Can hold food.");
		
		room2.addItem("plate", plate);
		
		Item smallKey = new Item("small key");
		smallKey.setWeight(0.1);
		smallKey.setDescription("This small key seems to be able to unlocks something...");
		
		room2.addItem("small key", smallKey);
		
		UnlockableObject lockedDoor = new UnlockableObject("door", "Probably leads to another room...", "west", true, "none");
		lockedDoor.setLocked(true);
		room2.addObject("weightObstacle", lockedDoor);
		
		Puzzle weightPuzzle = new Puzzle("weightPuzzle", "5.3", "The sensor seems to be triggered by some amount of weight...", true);
		room2.setPuzzle(weightPuzzle);
		
		RoomObject sensor = new RoomObject("weight sensor", "Triggers something by weight...", "north", false, false, false);
		sensor.setCanHoldItems(true);
		room2.addObject("weight sensor", sensor);
		
		// Room 3
		UnlockableObject chest = new UnlockableObject("chest", "Holds items.", "north", false, "small key");
		chest.setCanHoldItems(true);
		chest.setLocked(true);
		
		Item note = new Item("note");
		note.setReadable(true);
		note.setWeight(0.1);
		note.setDescription("(8/2(2+2)) + 1000");
		
		chest.getInventory().addItem("note", note);
		
		room3.addObject("chest", chest);

		Puzzle math = new Puzzle("Math problem.", "1016", "PEMDAS", true);
		
		room3.setPuzzle(math);
		
		UnlockableObject writtenDoor = new UnlockableObject("writtenObstacle", "Probably leads to another room...", "south", true, "none");
		writtenDoor.setLocked(true);
		room3.addObject("writtenObstacle", writtenDoor);
		
		
		// Room 4
		RoomObject dresser = new RoomObject("dresser", "Holds clothes.","south", true, true, true);
		RoomObject bed = new RoomObject("bed", "Place to sleep.","east", true, true, true);
		RoomObject desk = new RoomObject("desk", "Workspace.","west", true, true, true);
		
		room4.addObject("dresser", dresser);
		room4.addObject("bed", bed);
		room4.addObject("desk", desk);

		
		room4.setPuzzle(new Puzzle("Furniture puzzle", "Move the furniture to reveal the door", "The furniture can be moved...", false));
		
		rooms.put(1, room1);
		rooms.put(2, room2);
		rooms.put(3, room3);
		rooms.put(4, room4);
		rooms.put(5, room5);

		
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

	public void updateGameState() { }
	
	public void saveData() { }
	
	public void loadData() { }
}