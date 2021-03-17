package cs320.windows98.tbag.junit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import cs320.windows98.tbag.connection.Connection;
import cs320.windows98.tbag.room.Room;


public class TestConnection {

	private Connection connection;
	private String string;
	private Room room;
	private Room blank;
	
	@Before
	public void SetUp()
	{
		string = "COM";
		room = new Room();
		blank = new Room();
		connection = new Connection(string, room);
	
	}
	
	@Test
	public void getConnectionsTestForString()
	{
		
		assertTrue(connection.getConnections().containsKey("COM"));
		
	}
	
	
	@Test
	public void getConnectionsTestForRoom()
	{
		
		
		assertTrue(connection.getConnections().containsValue(room));
		
	}
	
	
	
	
}
