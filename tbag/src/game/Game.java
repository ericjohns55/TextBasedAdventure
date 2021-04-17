package game;

import map.PlayableObject;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

import java.util.HashMap;

import actor.Player;
import input.Command;
import items.CompoundItem;
import items.Item;

public class Game {
	private HashMap<Integer, Room> rooms;
	private int moves;
	private Player player;
	private String output;
	
	public Game(int roomID) {
		this.moves = 0;
		this.player = new Player(this, roomID);
		this.rooms = new HashMap<Integer, Room>();
		this.output = "";

		RoomGeneration.generateRooms(rooms);
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
	
	public void breakItem(RoomObject container, CompoundItem item) {
		
	}
	
	public void breakItem(Room room, CompoundItem item) {
		
	}
	
	public void dropItem(RoomObject container, Item item, Player player, Puzzle puzzle) {
		
	}
	
	public void dropItem(Room room, Item item, Player player, Puzzle puzzle) {
		
	}
	
	public void play(PlayableObject object, Item item, Player player, Puzzle puzzle) {
		
	}
	
	public void play(PlayableObject object, String note, Puzzle puzzle) {
		
	}
	
	public void pour(RoomObject object, Item item, Player player, Puzzle puzzle) {
		
	}
	
	public void push(RoomObject object, String direction) {
		
	}
	
	public void take(RoomObject object, Item item, Player player, Puzzle puzzle) {
		
	}
	
	public void take(Room room, Item item, Player player) {
		
	}
	
	public void type(Puzzle puzzle, RoomObject object) {
		
	}
	
	public void unlock(RoomObject object, Item unlockItem) {
		
	}
	
	public void moveRooms(Player player, int roomID) {
		
	}

	public void updateGameState() { }
	
	public void saveData() { }
	
	public void loadData() { }
}