package database;

import java.util.List;

import actor.NPC;
import actor.Player;
import dialogue.Link;
import dialogue.Node;
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
	// grab object queries	
	public Item getItemByID(int itemID);
	public RoomObject getRoomObjectByID(int objectID);
	public Inventory getInventoryByID(int id);
	public List<RoomObject> findAllObjects(Room room);	
	public List<Player> getAllPlayers();
	public Connections getAllConnections(int roomID);
	public Puzzle getPuzzle(Room room);
	public UnlockableObject getUnlockableObjectByID(int objectID);
	public NPC getNpc(int roomID);
	public List<Node> getAllNodes(int npcID);
	public List<Link> getAllLinks(int NodeID);
	
	// update game state in players table query
	public Integer updateGameState(String output, int moves, Player player);
	
	// login screen queries
	public boolean validateLogin(String username, String password);
	public Integer addUser(String username, String password);
	public Integer getGameID(String username, String password);
	
	// assembling room queries
	public Room getRoom(int roomID);
	public Player getPlayer(int playerID);

	// actually updating the DB queries
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
	public Integer npcDialogue(NPC npc, boolean talkedTo, int nextNodeID, boolean canTalkTo);
	
	// testing purposes queries
	public void deleteData(int gameID);
	public void loadInitialData(int gameID);
}