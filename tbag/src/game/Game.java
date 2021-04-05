package game;

import items.Item;
import map.PlayableObject;
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
		this.player = new Player(this, 5);
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
		Room room4 = new Room("You are in a room with a dresser (S), bed (E), and desk (W) but do not appear to see an exit.", 4);
		
		Room room5 = new Room("You enter into a room with instruments including a cello, a set of drums, and a piano.", 5);
		Room room6 = new Room("You enter into a cold room with a wooden table in the center. On the"
				+ " table lies a record player, and a complete set of records for Pink Floyd's songs "
				+ "and on the wall in spraypaint it reads \"Free to play along with time Evening never comes\" ", 6);
		Room room7 = new Room("You walk into a dining room area and in front of the head seat of the table sits a lamb heart on a plate with "
				+ "a butcher knife and a note next to it.", 7);
		Room room8 = new Room("You move into a living room with a pentagram marked on the ground and blood vial on a table with a note next to it.", 8);
		
		
	/*	Room room5 = new Room("You enter into a cold room with a wooden table in the center. On the"
				+ " table lies a record player, and a complete set of records for Pink Floyd's songs next to a doll "
				+ "with a locked door to the east that reads Free to play along with time Evening never comes ", 5);
	*/
		
		
		room1.addExit("west", room2);
		
		room2.addExit("east", room1);
		room2.addExit("west", room3);
		
		room3.addExit("east", room2);
		room3.addExit("south", room4);
		
		room4.addExit("north", room3);
		room4.addExit("south", room5);
		
		room5.addExit("north", room4);
		room5.addExit("east", room6);
		
		room6.addExit("west", room5);
		room6.addExit("east", room7);
		
		room7.addExit("west", room6);
		room7.addExit("north", room8);
		
		room8.addExit("south", room7);
	//	room8.addExit("west", room9);
		


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
		
		RoomObject sensor = new RoomObject("sensor", "Triggers something by weight...", "north", false, false, false);
		sensor.setCanHoldItems(true);
		room2.addObject("sensory pad", sensor);
		
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

		Puzzle math = new Puzzle("Math problem.", "1016", "Maybe PEMDAS can help you solve the problem?", true);
		
		room3.setPuzzle(math);
		
		UnlockableObject writtenDoor = new UnlockableObject("door", "Probably leads to another room...", "south", true, "none");
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
    
    
		// Room 5
		//	public RoomObject(String name, String description, boolean canHoldItems, boolean interactable, boolean locked) {
		String[] cScale = new String[] {"C", "D", "E", "F", "G", "A", "B"};
		PlayableObject piano = new PlayableObject("piano", "Could play music.", "south", cScale);
		room5.addObject("piano", piano);
		
		PlayableObject cello = new PlayableObject("cello", "Could play music.", "north", cScale);
		room5.addObject("cello", cello);
		
		PlayableObject guitar = new PlayableObject("guitar", "Could play music.", "west", cScale);
		room5.addObject("guitar", guitar);
		
		room5.setPuzzle(new Puzzle("Music puzzle", "Play a scale on any instrument.", "Perhaps try playing a C scale (consists of 7 naturals)...?", false));

		UnlockableObject musicDoor = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		musicDoor.setLocked(true);
		room5.addObject("musicalObstacle", musicDoor);
		
		// Room 6
		RoomObject recordPlayer = new RoomObject("record player", "Plays music.", "north", false, true, false);
		room6.addObject("record player", recordPlayer);
		
		Item record = new Item("Remember A Day");
		record.setWeight(0.7);
		record.setDescription("This record has a song by Pink Floyd on it.");
		
		room6.addItem("Remember A Day", record);
		
		RoomObject table6 = new RoomObject("Table", "A table that can hold things!", "south", true, true, false);
		room6.addObject("table", table6);
		
		// Room 7
		Item butcherKnife = new Item("butcher knife");
		butcherKnife.setWeight(1.2);
		butcherKnife.setDescription("This butcher knife is used for cutting.");
		
		room7.addItem("butcher knife", butcherKnife);
		
		Item blackKey = new Item("black key");
		blackKey.setWeight(0.1);
		blackKey.setDescription("This small black key seems to be able to unlock something...");
		
		room7.addItem("black key", blackKey);
		
		Item lambHeart = new Item("lamb heart");
		lambHeart.setWeight(2.0);
		lambHeart.setDescription("This is a lamb heart.");
		
		room7.addItem("lamb heart", lambHeart);
		
		Item note7 = new Item("note");
		note7.setWeight(0.1);
		note7.setReadable(true);
		note7.setDescription("That lamb heart looks like it has something in it...");
		
		room7.addItem("note", note7);
		
		RoomObject table7 = new RoomObject("Table", "A table that can hold things!", "east", true, true, false);
		room7.addObject("table", table7);
		
		UnlockableObject door7 = new UnlockableObject("door", "Probably leads to another room...", "north", true, "black key");
		door7.setLocked(true);
		room7.addObject("door", door7);
		
		// Room 8
		Item bloodVial = new Item("blood vial");
		bloodVial.setWeight(0.2);
		bloodVial.setDescription("This vial contains blood.");
		
		room8.addItem("blood vial", bloodVial);
		
		Item note8 = new Item("note");
		note8.setWeight(0.1);
		note8.setReadable(true);
		note8.setDescription("You may want to pour that vial on something.");
		
		room8.addItem("note", note8);
		
		RoomObject table8 = new RoomObject("Table", "A table that can hold things!", "north", true, true, false);
		room8.addObject("table", table8);
		
		rooms.put(1, room1);
		rooms.put(2, room2);
		rooms.put(3, room3);
		rooms.put(4, room4);
		rooms.put(5, room5);
		rooms.put(6, room6);
		rooms.put(7, room7);
		rooms.put(8, room8);

		
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