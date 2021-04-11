package game;

import java.util.HashMap;

import items.CompoundItem;
import items.Item;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

public class RoomGeneration {
	public static void generateRooms(HashMap<Integer, Room> rooms) {
		// 24 rooms and an exit 
		Room room1 = new Room("You are in a room with a westward door containing nothing but a table.", 1);
		Room room2 = new Room("You are in a dimly lit kitchen with some random items laying about. There is a door to the west and there appears to be some sensory pad contained in the room...", 2);
		Room room3 = new Room("You are in a room with a keypad on the door to the south and a chest on the other side of the room.", 3);
		Room room4 = new Room("You are in a room with a dresser (S), bed (E), and desk (W) but do not appear to see an exit.", 4);

		Room room5 = new Room("You are in a room with instruments including a cello, a guitar, and a piano.", 5);
		Room room6 = new Room("You are in a cold room with a wooden table in the center. On the"
				+ " table lies a record player, and some records of Pink Floyd's songs "
				+ "and on the wall in spraypaint it reads \"Wining, dining, shining king\" ", 6);
		Room room7 = new Room("You are in a dining room area and in front of the head seat of the table sits a lamb heart on a plate with "
				+ "a butcher knife and a note next to it. There is a door on the northern wall of the room.", 7);
		Room room8 = new Room("You are in a living room with a westward door and a pentagram marked on the ground and blood vial on a table with a note next to it.", 8);
		
		Room room9 = new Room("You are in a room with metal walls and a hellhound in front of a door to the north.", 9);
		Room room8 = new Room("You are in a room where everthing appears to be upside down. There appears to be a desk and a bed on the ceiling with a blurry painting on the wall.", 10);
		Room room11 = new Room("You are in a room, on the wall there is a sign that says \"There is nothing wrong here\". "
				+ "This room is very clean, quiet, and cold; and you can't help but to feel like you're not alone as you hear murmurs in the walls. On a table in the exact center of the room there is a computer that says \"What is the capital of New Jersey?\" on the screen.", 11);
		Room room8 = new Room("You are in a room where it is so dark that you can not see a thing.", 12);
		Room room8 = new Room("Last current room", 13);
		
		
		
		
		
		
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
		room8.addExit("west", room9);

		room9.addExit("east", room8);
		room9.addExit("north", room10);

		room10.addExit("south", room9);
		room10.addExit("east", room11);

		room11.addExit("west", room10);
		room11.addExit("east", room12);

		room12.addExit("west", room11);
		room12.addExit("south", room13);

		
		
		// Room 1
		Item key = new Item("key");
		key.setWeight(0.1);
		key.setDescription("This key seems to be able to unlock a door.");

		room1.addItem("key", key);

		UnlockableObject door = new UnlockableObject("door", "Probably leads to another room...", "west", true, "key");
		door.setLocked(true);
		room1.addObject("door", door);

		RoomObject table = new RoomObject("Table", "A table that can hold things!", "north", false, false, false);
		table.setCanHoldItems(true);
		room1.addObject("table", table);

		room1.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "Maybe the key will do something...", false, ""));

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

		Puzzle weightPuzzle = new Puzzle("weightPuzzle", "5.3", "The sensor seems to be triggered by some amount of weight...", true, "weightObstacle");
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

		Puzzle math = new Puzzle("Math problem.", "1016", "Maybe PEMDAS can help you solve the problem?", true, "writtenObstacle");

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

		room4.setPuzzle(new Puzzle("Furniture puzzle", "Move the furniture to reveal the door", "The furniture can be moved...", false, ""));


		// Room 5
		//	public RoomObject(String name, String description, boolean canHoldItems, boolean interactable, boolean locked) {
		String[] cScale = new String[] {"C", "D", "E", "F", "G", "A", "B"};
		PlayableObject piano = new PlayableObject("piano", "Could play music.", "south", cScale, true);
		room5.addObject("piano", piano);

		PlayableObject cello = new PlayableObject("cello", "Could play music.", "north", cScale, true);
		room5.addObject("cello", cello);

		PlayableObject guitar = new PlayableObject("guitar", "Could play music.", "west", cScale, true);
		room5.addObject("guitar", guitar);

		room5.setPuzzle(new Puzzle("Music puzzle", "Play a scale on any instrument.", "Perhaps try playing a C scale (consists of 7 naturals)...?", false, "musicalObstacle"));

		UnlockableObject musicDoor = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		musicDoor.setLocked(true);
		room5.addObject("musicalObstacle", musicDoor);

		// Room 6
		PlayableObject recordPlayer = new PlayableObject("record player", "Plays music.", "north", null, false);
		room6.addObject("record player", recordPlayer);

		RoomObject table6 = new RoomObject("Table", "A table that can hold things!", "south", true, true, false);
		table6.setCanHoldItems(true);
		table6.setInteractable(true);
		room6.addObject("table", table6);

		Item record1 = new Item("Crumbling Land Record", 0.7);
		record1.setDescription("This record has a song by Pink Floyd on it.");

		Item record2 = new Item("Another Brick Wall Record", 0.7);
		record2.setDescription("This record has a song by Pink Floyd on it.");

		Item record3 = new Item("Hey You Record", 0.7);
		record3.setDescription("This record has a song by Pink Floyd on it.");

		Item record4 = new Item("Young Lust Record", 0.7);
		record4.setDescription("This record has a song by Pink Floyd on it.");

		Item record5 = new Item("Arnold Layne Record", 0.7);
		record5.setDescription("This record has a song by Pink Floyd on it.");

		table6.getInventory().addItem("Crumbling Land Record", record1);
		table6.getInventory().addItem("Another Brick Wall Record", record2);
		table6.getInventory().addItem("Hey You Record", record3);
		table6.getInventory().addItem("Young Lust Record", record4);
		table6.getInventory().addItem("Arnold Layne Record", record5);

		UnlockableObject recordDoor = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		recordDoor.setLocked(true);
		room6.addObject("room6Door", recordDoor);

		ObjectPuzzle puzzle = new ObjectPuzzle("Play a record on a record player", "Play 'Crumbling Land'", "Zabriskie Point sountrack", recordPlayer, record1, "room6Door");
		room6.setPuzzle(puzzle);

		// Room 7
		Item butcherKnife = new Item("butcher knife", 1.2);
		butcherKnife.setDescription("This butcher knife is used for cutting.");

		Item blackKey = new Item("black key", 0.1);
		blackKey.setDescription("This small black key seems to be able to unlock something...");

		Item meat = new Item("meat", 1.0);
		meat.setDescription("Could be used to eat or feed something?");

		CompoundItem lambHeart = new CompoundItem("lamb heart", 2.0, true, "butcher knife");
		lambHeart.setDescription("A lamb heart; suspiciously heavy.");
		lambHeart.getInventory().addItem("black key", blackKey);
		lambHeart.getInventory().addItem("meat", meat);

		Item note7 = new Item("note", 0.1);
		note7.setReadable(true);
		note7.setDescription("That lamb heart looks like it has something in it...");

		RoomObject table7 = new RoomObject("Table", "A table that can hold things!", "east", true, true, false);
		table7.setCanHoldItems(true);
		table7.getInventory().addItem("butcher knife", butcherKnife);
		table7.getInventory().addItem("lamb heart", lambHeart);
		table7.getInventory().addItem("note", note7);
		room7.addObject("table", table7);

		UnlockableObject door7 = new UnlockableObject("door", "Probably leads to another room...", "north", true, "black key");
		door7.setLocked(true);
		room7.addObject("door", door7);

		room7.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "The key may be hidden somewhere.", false, ""));

		// Room 8
		Item bloodVial = new Item("blood vial", 0.2);
		bloodVial.setPourable(true);
		bloodVial.setDescription("This vial contains blood.");

		room8.addItem("blood vial", bloodVial);

		Item note8 = new Item("note", 0.1);
		note8.setReadable(true);
		note8.setDescription("You may want to pour that vial on something.");

		room8.addItem("note", note8);

		RoomObject pentagram = new RoomObject("pentagram", "Altar to Satan", "north", true, true, false);
		pentagram.setCoverable(true);
		room8.addObject("pentagram", pentagram);

		UnlockableObject room8Door = new UnlockableObject("door", "Probably leads to another room...", "west", true, "none");
		room8Door.setLocked(true);
		room8.addObject("room8Door", room8Door);

		room8.setPuzzle(new Puzzle("Bloody Pentagram", "blood vial", "Maybe the vial can be used to cover something?", false, "room8Door"));

	
		
		
		// Room 9 
		//public UnlockableObject(String name, String description, String direction, boolean blockingExit, String unlockItem)
		// UnlockableObject hellhound = new UnlockableObject("hellhound", "Looks hungry for meat.", "north", true, "meat");
		// hellhound.setCanHoldItems(true);
		// hellhound.setLocked(true);
		
		// public RoomObject(String name, String description, String direction, boolean isObstacle, boolean blockingExit, boolean moveable)
		// I made the hellhound a roomobject instead of a unlockable object because I feel like it would act the same as 
		// the weight sensor room. But I included the unlockable object for it above too with the parameter field
		RoomObject hellhound = new RoomObject("hellhound", "Looks hungry for meat.", "north", false, false, false);
		hellhound.setCanHoldItems(true);
		room9.addObject("hellhound", hellhound);
		
		
		UnlockableObject room9Door = new UnlockableObject("door", "Probably leads to another room...", "north", true, "none");
		room9Door.setLocked(true);
		room9.addObject("room9Door", room9Door);
		
		// public Puzzle(String description, String solution, String hint, boolean writtenSolution, String unlockObstacle)
		room9.setPuzzle(new Puzzle("Hellhound Room", "meat", "I wonder if the hellhound would like some meat from one of the previous rooms.", false, "room9Door"));
				
		
		// Room 10
		// Upside down room

		
		Item circularShard = new Item("circular shard", 0.6);
		circularShard.setDescription("This shard of glass is circular.");

		room10.addItem("circular shard", circularShard);		
		
		Item rectangularShard = new Item("rectangular shard", 0.6);
		rectangularShard.setDescription("This shard of glass is rectangular.");

		room10.addItem("rectangular shard", rectangularShard);
		
		Item jaggedShard = new Item("jagged shard", 0.6);
		jaggedShard.setDescription("This shard of glass is jagged.");

		room10.addItem("jagged shard", jaggedShard);
		
		Item triangularShard = new Item("triangular shard", 0.6);
		triangularShard.setDescription("This shard of glass is triangular.");

		room10.addItem("triangular shard", triangularShard);

		
		// Need to add a blurryPainting object to look at
		
		
		//public ObjectPuzzle(String description, String solution, String hint, RoomObject object, Item requiredItem, String unlockObstacle) {
		
		ObjectPuzzle upsideDownRoomPuzzle = new ObjectPuzzle("Look at a painting with a glass shard.", "Play 'Crumbling Land'", "This shard of glass is a well known geometric shape.", blurryPainting, triangularShard, "room10Door");
		room10.setPuzzle(upsideDownRoomPuzzle);
		
		UnlockableObject room10Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room10Door.setLocked(true);
		room10.addObject("room10Door", room10Door);
		
		
		// Room 11
		RoomObject table11 = new RoomObject("Table", "A table that can hold things!", "north", false, false, false);
		table11.setCanHoldItems(true);
		room11.addObject("table", table11);
		
		Puzzle newJersey = new Puzzle("Geography problem.", "Trenton", "There's a name in the solution.", true, "writtenObstacle");
		room11.setPuzzle(newJersey);
		
		
		UnlockableObject room11Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room11Door.setLocked(true);
		room11.addObject("room11door", room11Door);
		
		
		
		// Room 12
		Item redKey = new Item("redKey");
		redKey.setWeight(0.1);
		redKey.setDescription("This red key seems to be able to unlock a door.");

		room12.addItem("red key", redKey);
		
		UnlockableObject room12Door = new UnlockableObject("door", "Probably leads to another room...", "south", true, "key");
		room12Door.setLocked(true);
		room12.addObject("room12Door", room12Door);
		
		room12.setPuzzle(new Puzzle("Bloody Pentagram", "blood vial", "Maybe the vial can be used to cover something?", false, "room8Door"));

		
		
		
		rooms.put(1, room1);
		rooms.put(2, room2);
		rooms.put(3, room3);
		rooms.put(4, room4);
		rooms.put(5, room5);
		rooms.put(6, room6);
		rooms.put(7, room7);
		rooms.put(8, room8);
		rooms.put(9, room9);
		
	
		rooms.put(10, room10);
		rooms.put(11, room11);
		rooms.put(12, room12);
		rooms.put(13, room13);
		
		
		
	}
}
