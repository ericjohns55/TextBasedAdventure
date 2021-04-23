package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import actor.Player;
import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;
import map.Room;

public class TestValidateDatabase {
	// manually check rooms to see console output on creation
	
	@Test
	public void generateRoom() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		IDatabase db = DatabaseProvider.getInstance();	
		
		Room room = db.getRoom(6);
		assertEquals(6, room.getRoomID());
	}
	
	@Test
	public void grabPlayer() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		IDatabase db = DatabaseProvider.getInstance();	
		
		Player player = db.getPlayer(0);
		assertEquals(0, player.getActorID());
	}
	
	@Test
	public void movePlayer() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		IDatabase db = DatabaseProvider.getInstance();	
		
		Player player = db.getPlayer(0);
		db.moveRooms(player, 6);
		assertEquals(6, player.getRoomID());
	}
}
