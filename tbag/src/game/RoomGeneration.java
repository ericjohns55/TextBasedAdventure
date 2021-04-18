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
		
		// Look command requires item : room 10
		
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
		Room room10 = new Room("You are in a room where everthing appears to be upside down. There appears to be a desk and a bed on the ceiling with a blurry painting on the wall as well as a keypad on the door.", 10);
		Room room11 = new Room("You are in a room, on the wall there is a sign that says \"There is nothing wrong here\". "
				+ "This room is very clean, quiet, and cold; and you can't help but to feel like you're not alone as you hear murmurs in the walls. On a table in the exact center of the room there is a computer that says \"What is the capital of New Jersey?\" on the screen.", 11);
		Room room12 = new Room("You are in a room with a lit candle that has a door to the south.", 12);
		Room room13 = new Room("Last current room", 13);
		
		
		
		
		
		
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
		room8.addExit("west up stairs", room9);

		room9.addExit("east down stairs", room8);
		room9.addExit("north", room10);

		room10.addExit("south", room9);
		room10.addExit("east", room11);

		room11.addExit("west", room10);
		room11.addExit("east", room12);

		room12.addExit("west", room11);
		room12.addExit("south", room13);

		room13.addExit("north", room12);
		
		
		
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
		hellhound.setLocked(true);
		room9.addObject("hellhound", hellhound);
		
		
		UnlockableObject room9Door = new UnlockableObject("door", "Probably leads to another room...", "north", true, "none");
		room9Door.setLocked(true);
		room9.addObject("room9Door", room9Door);
		
		// public Puzzle(String description, String solution, String hint, boolean writtenSolution, String unlockObstacle)
		room9.setPuzzle(new Puzzle("Hellhound Room", "meat", "I wonder if the hellhound would like some meat from one of the previous rooms.", false, "room9Door"));
				
		
		// Room 10
		// Upside down room

		
		Item circularGlassShard = new Item("circular glass shard", 0.6);
		circularGlassShard.setDescription("This shard of glass is circular.");

		room10.addItem("circular glass shard", circularGlassShard);		
		
		Item rectangularGlassShard = new Item("rectangular glass shard", 0.6);
		rectangularGlassShard.setDescription("This shard of glass is rectangular.");

		room10.addItem("rectangular glass shard", rectangularGlassShard);
		
		Item jaggedGlassShard = new Item("jagged glass shard", 0.6);
		jaggedGlassShard.setDescription("This shard of glass is jagged.");

		room10.addItem("jagged glass shard", jaggedGlassShard);
		
		Item triangularGlassShard = new Item("triangular glass shard", 0.6);
		triangularGlassShard.setDescription("This shard of glass is triangular.");

		room10.addItem("triangular glass shard", triangularGlassShard);

		
		Item lighter = new Item("lighter", 0.3);
		lighter.setDescription("This is a lighter with about 1 flicker left.");

		room10.addItem("lighter", lighter);
		
		Item glasses = new Item("glasses", 0.6);
		glasses.setDescription("This is a pair of glasses.");

		room10.addItem("glasses", glasses);
		
		Item redBall = new Item("red ball", 0.8);
		redBall.setDescription("This is a red ball.");

		room10.addItem("red ball", redBall);
		
		Item candle = new Item("candle");
		candle.setWeight(0.8);
		candle.setLightable(true);
		candle.setLit(false);
		candle.setDescription("This candle looks like it can be lit.");

		room10.addItem("candle", candle);
		
		
		// Need to add a blurryPainting object to look at. 
		
		// If the object can't be looked at now then set the items not be able to be seen either, almost like 
		// the dark room stuff. Have to set it to be locked so the item (the code) cant be seen
		
		UnlockableObject painting = new UnlockableObject("painting", "862451", "north", true, "triangular glass shard");
		painting.setCanBeLookedAtNow(false);
		painting.setLocked(true);
		painting.setCanHoldItems(true);
		room10.addObject("painting", painting);
		
		Item code = new Item("code");
		code.setReadable(true);
		code.setCanBePickedUp(false);
		code.setDescription("862451");
		
		painting.getInventory().addItem("code", code);

		
		// Desk and bed objects 
		RoomObject desk10 = new RoomObject("desk", "This desk is on the ceiling.", "north", false, false, false);
		desk10.setCanHoldItems(false);
		desk10.setInteractable(false);
		room10.addObject("desk", desk10);
		
		RoomObject bed10 = new RoomObject("bed", "This bed is on the ceiling.", "north", false, false, false);
		bed10.setCanHoldItems(false);
		bed10.setInteractable(false);
		room10.addObject("bed", bed10);
		
		
		//public ObjectPuzzle(String description, String solution, String hint, RoomObject object, Item requiredItem, String unlockObstacle) {
		Puzzle secretCode = new Puzzle("Read a secret code.", "862451", "Try reading the painting with a popular geometric shape.", true, "writtenObstacle");
		room10.setPuzzle(secretCode);
	
		
		UnlockableObject room10Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room10Door.setLocked(true);
		room10.addObject("room10Door", room10Door);
		
		
		
		
		
		// Room 11
		RoomObject table11 = new RoomObject("table", "A table that can hold things!", "north", false, false, false);
		table11.setCanHoldItems(true);
		room11.addObject("table11", table11);
		
		Puzzle newJersey = new Puzzle("Geography problem.", "Trenton", "There's a name in the solution.", true, "writtenObstacle");
		room11.setPuzzle(newJersey);
		
		
		UnlockableObject room11Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room11Door.setLocked(true);
		room11.addObject("room11door", room11Door);
		
		
		
		
		
		// Room 12
		
		// So the room starts out dark and needs an item to "light" an unlockable object to be able to reveal the room items (
		// in this case a red key) and advance. I set the opposite in here 
		
		room12.setCanSee(false);
		
		
		// Thinking the key and door will be revealed once the candle is lit and in the room.
		// Candle would be room object since it needs the lighter to illuminate the room
		// LightableObject candle = new LightableObject("candle", "Looks like you need an object to light it.", "north", true, "lighter");
		
		
		
		Item redKey = new Item("red key");
		redKey.setWeight(0.1);
		redKey.setDescription("This red key seems to be able to unlock a door.");
		
		room12.addItem("red key", redKey);

	
		UnlockableObject room12Door = new UnlockableObject("door", "Probably leads to another room but you might need a key...", "south", true, "red key");
		room12Door.setLocked(true);
		room12.addObject("room12Door", room12Door);
		
		room12.setPuzzle(new Puzzle("Dark Room", "candle", "Might need something from a previous room to light something in here.", false, "room12Door"));

		
		
		
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
