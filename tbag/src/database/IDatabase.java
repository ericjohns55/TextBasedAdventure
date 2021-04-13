package database;

import java.util.List;

import actor.Actor;
import actor.Player;
import game.Game;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

public interface IDatabase {
	public Inventory getPlayerInventory(Player player);
	public Inventory getInventory(RoomObject roomObject);
	public Inventory getInventory(Room room);
	public Inventory getInventoryByID(int id);
	public List<RoomObject> findAllObjects(Room room);	
	public List<Player> findAllPlayers();
	public Puzzle getPuzzle(Room room);
	
	public void addItemToInventory(Inventory inventory, Item item);
	public Item removeItemFromInventory(Inventory inventory, Item item);
	public void toggleLocks(UnlockableObject object, boolean locked);
	public void moveRooms(Player player, int roomID);
	public void pushObject(RoomObject object, String direction);
	public void breakItem();
}
