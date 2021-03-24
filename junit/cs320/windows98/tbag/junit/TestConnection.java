package cs320.windows98.tbag.junit;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cs320.windows98.tbag.connection.Connection;
import cs320.windows98.tbag.room.Room;


public class TestConnection {

	private Connection connection;
	private Room room;
	
	@Before
	public void setUp() {
		room = new Room("Description1", 1);
		connection = new Connection("String", room);
	}
	
	@Test
	public void testSetConnection() {
		// This is creating a new room to add as a connection to our current room 
		Room roomTwo = new Room("Description2", 2);
		
		connection.setConnection("west", roomTwo);
		assertTrue(connection.getConnections().containsKey("west") == true);
	}
	
	
}
