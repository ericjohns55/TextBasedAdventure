package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import game.Game;
import input.Command;
import map.Room;


public class TestGame {
	private Game game;
	
	@Before
	public void setUp() {
		game = new Game();
	}
	
	@Test
	public void testMakeMove() {
		int moves = game.getMoves();
		game.runCommand("walk east");
		assertEquals(moves + 1, game.getMoves());
	}
	
	@Test
	public void testAddRoom() {
		Room addedRoom = new Room("testRoom", 1);
		game.addRoom(addedRoom);
		assertEquals(addedRoom, game.getRoom(addedRoom.getRoomID()));
	}
	
	@Test
	public void testBadCommand() {
		game.runCommand("eat chocolate");
		String feedback = game.getOutput();
		assertEquals(feedback, Command.invalidCommand);
	}
}
