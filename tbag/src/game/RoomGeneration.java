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
import actor.NPC;
import dialogue.Link;
import dialogue.Node;

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
		Room room12 = new Room("You are in a bedroom with a bed, nightstand and a scanner.", 12);
		Room room13 = new Room("You are in a room with a keypad. On the kepad is a standard set of numbers but the 1, 3, 6, and 8 buttons are clearly worn.", 13);
		Room room14 = new Room("You are in a dark room where you can not see a thing.", 14);
		Room room15 = new Room("You are in a room with an undead doorman.", 15);	
		Room room16 = new Room("You are in a room where to the north there is a ladder covered in grease going through the ceiling.", 16);
		Room room17 = new Room("Last current room.", 17);
		Room room20 = new Room("You move into a living room with a westward door and a pentagram marked on the ground and blood vial on a table with a note next to it.", 20);
		Room room21 = new Room("Big Room with a guy named bob in it and a door to the west.", 21);
		Room room22 = new Room("There is nothing in this room but the door you came in through.", 22);


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
		room13.addExit("south", room14);
		
		room14.addExit("north", room13);
		room14.addExit("west", room15);

		room15.addExit("east", room14);
		room15.addExit("west", room16);
		
		room16.addExit("east", room15);
		room16.addExit("ladder", room17);
		
		room17.addExit("ladder", room16);
/*		room17.addExit("east", room18);
		
		room18.addExit("west", room17);
		room18.addExit("east", room19);
		
*/	

	/*	room20.addExit("west", room9);
		
		room9.addExit("east", room8);
		room9.addExit("west", room10);
		
		room10.addExit("east", room9);

*/
		// Room 1
		Item key = new Item("key", 0.1);
		key.setDescription("This key seems to be able to unlock a door.");

		room1.addItem("key", key);

		UnlockableObject door = new UnlockableObject("door", "Probably leads to another room...", "west", true, key, 1);
		door.setLocked(true);
		room1.addObject("door", door);

		RoomObject table = new RoomObject("Table", "A table that can hold things!", "north", false, false, false, 1);
		table.setCanHoldItems(true);
		room1.addObject("table", table);

		room1.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "Maybe the key will do something...", false, "", 1));

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

		UnlockableObject lockedDoor = new UnlockableObject("door", "Probably leads to another room...", "west", true, null, 2);
		lockedDoor.setLocked(true);
		room2.addObject("weightObstacle", lockedDoor);

		Puzzle weightPuzzle = new Puzzle("weightPuzzle", "5.3", "The sensor seems to be triggered by some amount of weight...", true, "weightObstacle", 2);
		room2.setPuzzle(weightPuzzle);

		RoomObject sensor = new RoomObject("sensor", "Triggers something by weight...", "north", false, false, false, 2);
		sensor.setCanHoldItems(true);
		room2.addObject("sensory pad", sensor);

		// Room 3
		UnlockableObject chest = new UnlockableObject("chest", "Holds items.", "north", false, smallKey, 3);
		chest.setCanHoldItems(true);
		chest.setLocked(true);

		Item note = new Item("note");
		note.setReadable(true);
		note.setWeight(0.1);
		note.setDescription("(8/2(2+2)) + 1000");

		chest.getInventory().addItem("note", note);

		room3.addObject("chest", chest);

		Puzzle math = new Puzzle("Math problem.", "1016", "Maybe PEMDAS can help you solve the problem?", true, "writtenObstacle", 3);

		room3.setPuzzle(math);

		UnlockableObject writtenDoor = new UnlockableObject("door", "Probably leads to another room...", "south", true, null, 3);
		writtenDoor.setLocked(true);
		room3.addObject("writtenObstacle", writtenDoor);


		// Room 4
		RoomObject dresser = new RoomObject("dresser", "Holds clothes.","south", true, true, true, 4);
		RoomObject bed = new RoomObject("bed", "Place to sleep.","east", true, true, true, 4);
		RoomObject desk = new RoomObject("desk", "Workspace.","west", true, true, true, 4);

		room4.addObject("dresser", dresser);
		room4.addObject("bed", bed);
		room4.addObject("desk", desk);

		room4.setPuzzle(new Puzzle("Furniture puzzle", "Move the furniture to reveal the door", "The furniture can be moved...", false, "", 4));


		// Room 5
		//	public RoomObject(String name, String description, boolean canHoldItems, boolean interactable, boolean locked) {
		String cScale = "CDEFGAB";
		PlayableObject piano = new PlayableObject("piano", "Could play music.", "south", cScale, true, 5);
		room5.addObject("piano", piano);

		PlayableObject cello = new PlayableObject("cello", "Could play music.", "north", cScale, true, 5);
		room5.addObject("cello", cello);

		PlayableObject guitar = new PlayableObject("guitar", "Could play music.", "west", cScale, true, 5);
		room5.addObject("guitar", guitar);

		room5.setPuzzle(new Puzzle("Music puzzle", "Play a scale on any instrument.", "Perhaps try playing a C scale (consists of 7 naturals)...?", false, "musicalObstacle", 5));

		UnlockableObject musicDoor = new UnlockableObject("door", "Probably leads to another room...", "east", true, null, 5);
		musicDoor.setLocked(true);
		room5.addObject("musicalObstacle", musicDoor);

		// Room 6
		PlayableObject recordPlayer = new PlayableObject("record player", "Plays music.", "north", "", false, 6);
		room6.addObject("record player", recordPlayer);

		RoomObject table6 = new RoomObject("Table", "A table that can hold things!", "south", true, true, false, 6);
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

		UnlockableObject recordDoor = new UnlockableObject("door", "Probably leads to another room...", "east", true, null, 6);
		recordDoor.setLocked(true);
		room6.addObject("room6Door", recordDoor);

		ObjectPuzzle puzzle = new ObjectPuzzle("Play a record on a record player", "Play 'Crumbling Land'", "Zabriskie Point sountrack", recordPlayer, record1, "room6Door", 6);
		room6.setPuzzle(puzzle);

		// Room 7
		Item butcherKnife = new Item("butcher knife", 1.2);
		butcherKnife.setDescription("This butcher knife is used for cutting.");

		Item blackKey = new Item("black key", 0.1);
		blackKey.setDescription("This small black key seems to be able to unlock something...");

		Item meat = new Item("meat", 1.0);
		meat.setDescription("Could be used to eat or feed something?");

		CompoundItem lambHeart = new CompoundItem("lamb heart", 2.0, true, butcherKnife);
		lambHeart.setDescription("A lamb heart; suspiciously heavy.");
		lambHeart.getInventory().addItem("black key", blackKey);
		lambHeart.getInventory().addItem("meat", meat);

		Item note7 = new Item("note", 0.1);
		note7.setReadable(true);
		note7.setDescription("That lamb heart looks like it has something in it...");

		RoomObject table7 = new RoomObject("Table", "A table that can hold things!", "east", true, true, false, 7);
		table7.setCanHoldItems(true);
		table7.getInventory().addItem("butcher knife", butcherKnife);
		table7.getInventory().addItem("lamb heart", lambHeart);
		table7.getInventory().addItem("note", note7);
		room7.addObject("table", table7);
    
		UnlockableObject door7 = new UnlockableObject("door", "Probably leads to another room...", "north", true, blackKey, 7);
		door7.setLocked(true);
		room7.addObject("door", door7);

		room7.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "The key may be hidden somewhere.", false, "", 7));

		
		
/*				
		UnlockableObject door = new UnlockableObject("door", "Probably leads to another room...", "west", true, "key");
		door.setLocked(true);
		room1.addObject("door", door);

		room1.setPuzzle(new Puzzle("Unlock door", "Use key to unlock door", "Maybe the key will do something...", false, ""));
		
		Puzzle doorman = new Puzzle("Doorman problem.", "hello", "Type hello on keypad", true, "sayHello");

		room13.setPuzzle(doorman);

		UnlockableObject room13Door = new UnlockableObject("door", "Probably leads to another room...", "south", true, "none");
		room13Door.setLocked(true);
		room13.addObject("sayHello", room13Door);
*/		
		
		// Keeps saying the obstacle doesnt exist when I try to "unlock door"
		
		
		
		// Room 8
		Item bloodVial = new Item("blood vial", 0.2);
		bloodVial.setPourable(true);
		bloodVial.setDescription("This vial contains blood.");

		room8.addItem("blood vial", bloodVial);

		Item note8 = new Item("note", 0.1);
		note8.setReadable(true);
		note8.setDescription("You may want to pour that vial on something.");

		room8.addItem("note", note8);

		RoomObject pentagram = new RoomObject("pentagram", "Altar to Satan", "north", true, true, false, 8);
		pentagram.setCoverable(true);
		room8.addObject("pentagram", pentagram);

		UnlockableObject room8Door = new UnlockableObject("door", "Probably leads to another room...", "west up stairs", true, "none");
		room8Door.setLocked(true);
		room8.addObject("room8Door", room8Door);

		room8.setPuzzle(new Puzzle("Bloody Pentagram", "blood vial", "Maybe the vial can be used to cover something?", false, "room8Door"));

	
		
		
		// Room 9 
		RoomObject hellhound = new RoomObject("hellhound", "Looks hungry for meat.", "north", false, false, false);
		hellhound.setCanHoldItems(true);
		hellhound.setCanBeFed(true);
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
		lighter.setProducesFire(true);
		
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
		Puzzle secretCode = new Puzzle("Read a secret code.", "862451", "Try looking at the painting when you have a popular geometric shape.", true, "room10Door");
		room10.setPuzzle(secretCode);
	
		
		UnlockableObject room10Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room10Door.setLocked(true);
		room10.addObject("room10Door", room10Door);		
		
		// Room 11
		RoomObject table11 = new RoomObject("table", "A table that can hold things!", "north", false, false, false);
		table11.setCanHoldItems(true);
		room11.addObject("table11", table11);
		
		Puzzle newJersey = new Puzzle("Geography problem.", "trenton", "There's a name in the solution.", true, "room11Door");
		room11.setPuzzle(newJersey);
		
		UnlockableObject room11Door = new UnlockableObject("door", "Probably leads to another room...", "east", true, "none");
		room11Door.setLocked(true);
		room11.addObject("room11Door", room11Door);

		
		// Room 12
		RoomObject room12Bed = new RoomObject("bed", "A bed to sleep in.", "south", true, true, false);
		room12Bed.setCanHoldItems(true);
		room12Bed.setInteractable(true);

		RoomObject nightstand = new RoomObject("nightstand", "A common bedside table.", "south", true, true, false);
		nightstand.setCanHoldItems(true);
		nightstand.setInteractable(true);

		// public CompoundItem(String name, double weight, boolean breakable, String breakIdentifier) {
		Item finger = new Item("finger", 0.3);
		finger.setDescription("It's a severed finger!");
		
		CompoundItem pillow = new CompoundItem("pillow", 1.7, true, "switchblade");
		pillow.setDescription("A pillow, seeming a little lopsided.");
		pillow.getInventory().addItem("finger", finger);
		room12.addItem("pillow", pillow);
		
		Item blanket = new Item("blanket");
		blanket.setWeight(1.2);
		blanket.setDescription("This blanket could keep you warm.");
		room12.addItem("blanket", blanket);
		
		Item lamp = new Item("lamp");
		lamp.setWeight(2.3);
		lamp.setDescription("An object to see in the dark.");
		room12.addItem("lamp", lamp);

		Item waterGlass = new Item("water glass");
		waterGlass.setWeight(0.5);
		waterGlass.setDescription("This glass has water in it.");
		room12.addItem("water glass", waterGlass);
		
		Item switchblade = new Item("switchblade");
		switchblade.setWeight(0.8);
		switchblade.setDescription("This is a type of knife.");
		room12.addItem("swtichblade", switchblade);

		room12Bed.getInventory().addItem("blanket", blanket);
		room12Bed.getInventory().addItem("pillow", pillow);
		room12.addObject("bed", room12Bed);

		nightstand.getInventory().addItem("lamp", lamp);
		nightstand.getInventory().addItem("switchblade", switchblade);
		nightstand.getInventory().addItem("water glass", waterGlass);
		room12.addObject("nightstand", nightstand);

		RoomObject scanner = new RoomObject("scanner", "This is used to scan things.", "north", false, false, false);
		scanner.setCanScan(true);
		room12.addObject("scanner", scanner);
		
		UnlockableObject room12Door = new UnlockableObject("door", "Probably leads to another room...", "south", true, "none");
		room12Door.setLocked(true);
		room12.addObject("room12Door", room12Door);
		
		// public Puzzle(String description, String solution, String hint, boolean writtenSolution, String unlockObstacle)
		room12.setPuzzle(new Puzzle("Bedroom", "finger", "Looks like you might have to scan something from the room.", false, "room12Door"));
				
		
		// Room 13

		Puzzle guess = new Puzzle("Guess the order.", "6381","The first number is 6 and there are no repeats.", true, "writtenObstacle");

		room13.setPuzzle(guess);
		
		UnlockableObject room13Door = new UnlockableObject("door", "Probably leads to another room...", "south", true, "none");
		room13Door.setLocked(true);
		room13.addObject("writtenObstacle", room13Door);		
		

	/*	
		
		// Room 14
		// Planning for sticky gloves to be grabbed before advancing up greasy ladder in room 16
		Item stickyGloves = new Item("sticky gloves");
		stickyGloves.setWeight(0.5);
		stickyGloves.setDescription("These sticky gloves can be good for climbing.");
		room14.addItem("sticky gloves", stickyGloves);
		
		Puzzle doorman = new Puzzle("Doorman problem.", "hello", "Type hello on keypad", true, "sayHello");

		room15.setPuzzle(doorman);

		UnlockableObject room14Door = new UnlockableObject("door", "Probably leads to another room...", "west", true, "none");
		room14Door.setLocked(true);
		room14.addObject("sayHello", room14Door);
				

	*/
		
		// Room 15	
		// So the room starts out dark and needs an item to "light" an unlockable object to be able to reveal the room items (
		// in this case a red key) and advance. I set the opposite in here 
		
		room14.setCanSee(false);
		
		
		// Thinking the key and door will be revealed once the candle is lit and in the room.
		// Candle would be room object since it needs the lighter to illuminate the room
		// LightableObject candle = new LightableObject("candle", "Looks like you need an object to light it.", "north", true, "lighter");
		
		// Might cause problems, it probably generates a key eveytime you enter into that room and it's lit
		if (room14.getCanSee() == true)
		{
			Item redKey = new Item("red key");
			redKey.setWeight(0.1);
			redKey.setDescription("This red key seems to be able to unlock a door.");
			room14.addItem("red key", redKey);

		}
		

	
		UnlockableObject room14Door = new UnlockableObject("door", "Probably leads to another room but you might need a key...", "west", true, "red key");
		room14Door.setLocked(true);
		room14.addObject("room14Door", room14Door);
		
		room14.setPuzzle(new Puzzle("Dark Room", "candle", "Might need something from a previous room to light something in here.", false, "room14Door"));



		// Room 16
		// Room room16 = new Room("You are in a room where to the north there is a ladder covered in grease going through the ceiling.", 15);
		// public RoomObject(String name, String description, String direction, boolean isObstacle, boolean blockingExit, boolean moveable) 
		Item stickyGloves = new Item("sticky gloves");
		stickyGloves.setWeight(0.8);
		stickyGloves.setDescription("These gloves seem good for climbing.");
		room16.addItem("sticky gloves", stickyGloves);

		
		UnlockableObject ladder = new UnlockableObject("ladder", "Probably leads to another room but you might need something for your hands...", "ladder", true, "sticky gloves");
		ladder.setCanBeClimbed(true);
		room16.addObject("ladder", ladder);
		
		room16.setPuzzle(new Puzzle("Ladder Room", "sticky gloves", "Might need to have some sort of item with a lot of grip for this ladder.", false, "ladder"));

	/*	
		UnlockableObject room8Door = new UnlockableObject("door", "Probably leads to another room...", "west up stairs", true, "none");
		room8Door.setLocked(true);
		room8.addObject("room8Door", room8Door);

		room8.setPuzzle(new Puzzle("Bloody Pentagram", "blood vial", "Maybe the vial can be used to cover something?", false, "room8Door"));
	*/
	
		
	
/*		UnlockableObject room8Door = new UnlockableObject("door", "Probably leads to another room...", "west", true, null, 8);
		room8Door.setLocked(true);
		room8.addObject("room8Door", room8Door);
    room8.setPuzzle(new Puzzle("Bloody Pentagram", "blood vial", "Maybe the vial can be used to cover something?", false, "room8Door", 8));

		
		// Room 9
		Item cake = new Item("cake");
		cake.setWeight(0.3);
		cake.setDescription("cake");
		room9.addItem("cake", cake);
		
		Item key2 = new Item("key", 0.1);
		key2.setDescription("This key does nothing.");

		room9.addItem("key", key2);
		
		UnlockableObject room9Door = new UnlockableObject("door", "Probably leads to another room...", "west", true, null, 9);
		room9Door.setLocked(true);
		room9.addObject("room9Door", room9Door);
   
		
		NPC bob = new NPC(null, 1, "bob", "Just a nice guy.", cake, room9Door);
	0	Node n = new Node(0, "Hi im Bob.", false, "y/n");
	1	Node p = new Node(1, "Im just a real swell guy, but I am quite hungry.", false, "y/n");
	2	Node o = new Node(2, "Ok then. Why not?", true, "option");
	3	Node q = new Node(3, "Give me the cake then.", false, "command");
	4	Node r = new Node(4, "Come back when you have a cake for me.", false, "WC");
	5	Node s = new Node(5, "That's ok.", false, "DE");
	6	Node t = new Node(6, "Come back when you have time.", false, "DE");
	7	Node u = new Node(7, "That's rude.", false, "DE");
		Link l1 = new Link(p, n, true, "Would you like to know more about me?");
		Link l2 = new Link(o, n, true, "");
		Link l3 = new Link(q, p, true, "Do you have any cake?");
		Link l4 = new Link(r, p, true, "");
		Link l5 = new Link(s, o, true, "I just don't want to.");
		Link l6 = new Link(t, o, true, "I don't have the time.");
		Link l7 = new Link(u, o, true, "I just hate you.");
		
		n.addLink(l1);
		n.addLink(l2);
		p.addLink(l3);
		p.addLink(l4);
		o.addLink(l5);
		o.addLink(l6);
		o.addLink(l7);
		
		bob.setCurrentNode(n);
		bob.setRootNode(n);
		
		Item pie = new Item("pie");
		pie.setWeight(0.3);
		pie.setDescription("apple pie.");
		bob.getInventory().addItem("pie", pie);
		room9.addNpc(bob);
*/

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
		rooms.put(14, room14);
		rooms.put(15, room15);
		rooms.put(16, room16);
		rooms.put(17, room17);
	}
}
