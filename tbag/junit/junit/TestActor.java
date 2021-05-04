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
		game = new Game(1);
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
	}
	
	@Test
	public void testGettersSetters() {
		actor.setRoomID(100);
		actor.setInventoryID(100);
		actor.setActorID(100);
		actor.setGameID(100);

		assertEquals(100, actor.getRoomID());
		assertEquals(100, actor.getInventoryID());
		assertEquals(100, actor.getActorID());
		assertEquals(100, actor.getGameID());
	}
}
