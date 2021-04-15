package database;

import java.util.List;

import actor.Actor;
import actor.Player;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public interface IDatabase {
	public Item getItemByID(int itemID);
	public Inventory getPlayerInventory(Player player);
	public Inventory getInventory(RoomObject roomObject);
	public Inventory getInventory(Room room);
	public Inventory getInventoryByID(int id);
	public List<RoomObject> findAllObjects(Room room);	
	public List<Player> findAllPlayers();
	
	public Integer addItemToInventory(Inventory destinationInventory, Item item);
	public Item removeItemFromInventory(Inventory inventory, Item item);
	public Integer toggleLocks(UnlockableObject object, boolean locked);
	public Integer moveRooms(Player player, int roomID);
	public Integer pushObject(RoomObject object, String direction);
	public void breakItem(CompoundItem compoundItem);
}
