package cs320.windows98.tbag.game;

import cs320.windows98.tbag.input.Input;
import cs320.windows98.tbag.player.Player;
import cs320.windows98.tbag.room.Room;
import cs320.windows98.tbag.connection.Connection;



public class Game{
	
	private Room room;
	private int moves; 
	private Player player;
	
	
	public Game(Room room,  Player player) {

		this.room = room;
		this.moves = moves;
		this.player = player;
		
		createRoom();
		
	}
	

	// public void play()
	
	
	public void createRoom()
	{
		Room room1 = new Room("description", 1);
		
		Room room2 = new Room("description2", 2);
		
		room1.setExit("west", room2);
		
		room2.setExit("east", room1);
		
	}
	
	
	
	
	public void update()
	{
		
		
		
	}
	
}