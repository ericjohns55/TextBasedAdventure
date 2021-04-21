package game;

import map.Room;

import java.util.HashMap;

import actor.Player;
import input.Command;

public class Game {
	private HashMap<Integer, Room> rooms;
	private int moves;
	private Player player;
	
	public Game(int roomID) {
		this.moves = 0;
		this.player = new Player(this, roomID);
		this.rooms = new HashMap<Integer, Room>();

		RoomGeneration.generateRooms(rooms);
	}

	public Game() {
		this(7);
	}

	public Player getPlayer() {
		return player;
	}

	public String runCommand(String input) {
		Command command = new Command(input, this);
		moves++;
		return command.execute();
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