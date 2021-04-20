package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;
import map.Room;

public class TestDatabaseRoomCreate {
	// manually check rooms to see console output on creation
	
	@Test
	public void generateRoom() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		IDatabase db = DatabaseProvider.getInstance();	
		
		Room room = db.getRoom(6);
		assertEquals(6, room.getRoomID());
	}
}
