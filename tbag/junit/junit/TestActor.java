package junit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import actor.Actor;
import game.Game;
import items.Item;

public class TestActor {
	private Game game;
	private Actor actor;
	
	@Before
	public void setUp() {
		game = new Game();
		actor = new Actor(game, 1);
	}
	
	@Test
	public void testInventory() {
		assertEquals(0, actor.getInventory().getInventorySize());
		
		Item item = new Item("yay!", 1);
		actor.getInventory().addItem("item", item);
		
		assertEquals(1, actor.getInventory().getInventorySize());
	}
	
	@Test
	public void testRoomID() {
		actor.setRoomID(8);
		assertEquals(8, actor.getRoomID());
		
		assertEquals(game.getRoom(8).getDescription(), actor.getRoom().getDescription());
	}
}
