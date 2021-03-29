package game;

import items.Item;
import map.Room;

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
		Room room1 = new Room("You wake up in a room with a set of keys on a table and a door to the west.", 1);
		Room room2 = new Room("You advance into a dimly lit kitchen with a knife on the counter that has a door to the west.", 2);
		Room room3 = new Room("You walk into a living room area with a couch, candles on the walls, a coffee table with neatly stacked books, and a blood vial on display next to the books with a locked door to the south.", 3);
		Room room4 = new Room("You move into an empty room with a locked door to the south.", 4);
		Room room5 = new Room("You enter into room with a table and a locked door to the east.", 5);
		Room room6 = new Room("You enter into an empty room with a locked door to the east.", 6);
		Room room7 = new Room("You enter into room with a table and a locked door to the north.", 7);
		Room room8 = new Room("You enter into an empty room with a locked door to the west.", 8);
		Room room9 = new Room("You enter into room with a table and a locked door to the north.", 9);
		Room room10 = new Room("You enter into an empty room with a locked door to the east.", 10);
		Room room11 = new Room("You enter into room with a table and a locked door to the east.", 11);
		Room room12 = new Room("You enter into an empty room with a locked door to the south.", 12);
		Room room13 = new Room("You enter into room with a table and a locked door to the south.", 13);
		Room room14 = new Room("You enter into an empty room with a locked door to the west.", 14);
		Room room15 = new Room("You enter into room with a table and a locked door to the west.", 15);
		Room room16 = new Room("You enter into a closet with a ladder on the west wall.", 16);
		Room room17 = new Room("You climb up the ladder and a door to the east.", 17);
		Room room18 = new Room("You enter into an empty room with a locked door to the east.", 18);
		Room room19 = new Room("You enter into a room with a table in it and a locked door to the north.", 19);
		Room room20 = new Room("You enter into an empty room with a locked door to the north.", 20);
		Room room21 = new Room("You enter into room with a table and a locked door to the west.", 21);
		Room room22 = new Room("You enter into an empty room with a locked door to the west.", 22);
		Room room23 = new Room("You enter into room with a table and a locked door to the south.", 23);
		Room room24 = new Room("You enter into an empty room with a locked door to the west.", 24);
		Room outsideRoom = new Room("You win!", 25);
		
		
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
		room10.addExit("west", room11);
		
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
		room16.addExit("up", room17);
		
		
		room17.addExit("down", room16);
		room17.addExit("east", room18);
		
		
		
		
		room18.addExit("west", room17);
		room18.addExit("east", room19);
		
		room19.addExit("west", room18);
		room19.addExit("north", room20);
		
		room20.addExit("south", room19);
		room20.addExit("north", room21);
		
		room21.addExit("south", room20);
		room21.addExit("west", room22);
		
		room22.addExit("east", room21);
		room22.addExit("west", room23);
		
		room23.addExit("east", room22);
		room23.addExit("south", room24);
		
		room24.addExit("north", room23);
		room24.addExit("west", outsideRoom);


		Item key = new Item("key");
		key.setWeight(0.1);
		key.setDescription("This key seems to be able to unlock a door.");
		
		room1.addItem("key", key);
		
		Item knife = new Item("knife");
		knife.setWeight(1.0);
		knife.setDescription("A knife, good for cutting and stabbing. ");
		
		room2.addItem("knife", knife);
		
		Item note = new Item("note");
		note.setWeight(0.1);
		note.setDescription("this will be a clue to a puzzle");
		
		room3.addItem("note", note);
		
		Item vile = new Item("vile");
		note.setWeight(1.2);
		note.setDescription("A vile filled with someones blood");
		
		room4.addItem("vile", vile);
		
		Item record = new Item("record");
		note.setWeight(0.1);
		note.setDescription("A vinyl record that says play me.");
		
		room5.addItem("record", record);
		
		Item jar = new Item("jar of eyes");
		note.setWeight(5.0);
		note.setDescription("Each eye is staring at you.");
		
		room6.addItem("jar of eyes", jar);
		
		Item shovel = new Item("shovel");
		note.setWeight(6.5);
		note.setDescription("A dirty shovel, good for digging.");
		
		room7.addItem("shovel", shovel);
		
		Item ax = new Item("ax");
		note.setWeight(6.0);
		note.setDescription("An ax, good for chopping wood.");
		
		room8.addItem("ax", ax);
		
		Item doll = new Item("doll");
		note.setWeight(4.0);
		note.setDescription("A raggedy doll.");
		
		room9.addItem("doll", doll);
		
		Item mask = new Item("mask");
		note.setWeight(1.1);
		note.setDescription("A classic WW1 gas mask.");
		
		room10.addItem("mask", mask);
		
		

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
		rooms.put(18, room18);
		rooms.put(19, room19);
		rooms.put(20, room20);
		rooms.put(21, room21);
		rooms.put(22, room22);
		rooms.put(23, room23);
		rooms.put(24, room24);
		
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
	
	public void saveData() { };
	
	public void loadData() { };
}