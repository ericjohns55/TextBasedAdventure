package database;

import java.util.List;

import actor.Player;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.Connections;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.Puzzle;

public interface IDatabase {
	public Item getItemByID(int itemID);
	public RoomObject getRoomObjectByID(int objectID);
	public Inventory getInventoryByID(int id);
	public List<RoomObject> findAllObjects(Room room);	
	public List<Player> getAllPlayers();
	public Connections getAllConnections(int roomID);
	public Puzzle getPuzzle(Room room);
	public UnlockableObject getUnlockableObjectByID(int objectID);
	
	public Integer updateGameState(String output, int moves, Player player);
	
	public boolean validateLogin(String username, String password);
	public Integer addUser(String username, String password);
	
	public Room getRoom(int roomID);
	public Player getPlayer(int playerID);
	public Integer addItemToInventory(Inventory destinationInventory, Item item);
	public Integer removeItemFromInventory(Inventory inventory, Item item);
	public Integer toggleLocks(UnlockableObject object, boolean locked);
	public Integer moveRooms(Player player, int roomID);
	public Integer pushObject(RoomObject object, String direction);
	public Integer breakItem(CompoundItem compoundItem, Inventory destinationInventory);
	public Integer consumeItem(Item item);
	public Integer destroyCompoundItem(CompoundItem item);
	public Integer destroyItem(Item item);
	public Integer playNotes(PlayableObject playableObject, String notes);
	public String getDescription(int roomID);
}