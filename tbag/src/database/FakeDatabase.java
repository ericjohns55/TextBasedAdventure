package database;

import java.io.IOException;
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

public class FakeDatabase implements IDatabase {
	// lmao if you think im actually gonna implement any of this
	
	public FakeDatabase() {
		
		readInitialData();
	}
	
	public void readInitialData() {
		try {
			InitialData.getAllItems();	// getting rid of the warning by pretending to do something
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}

	@Override
	public Item getItemByID(int itemID) {
		// TODO kill self
		return null;
	}

	@Override
	public Inventory getPlayerInventory(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventoryByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoomObject> findAllObjects(Room room) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> getAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addItemToInventory(Inventory destinationInventory, Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeItemFromInventory(Inventory inventory, Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer toggleLocks(UnlockableObject object, boolean locked) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer moveRooms(Player player, int roomID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer pushObject(RoomObject object, String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer breakItem(CompoundItem compoundItem, Inventory destinationInventory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer consumeItem(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer destroyCompoundItem(CompoundItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer playNotes(PlayableObject playableObject, String notes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription(int roomID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomObject getRoomObjectByID(int objectID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connections getAllConnections(int roomID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Puzzle getPuzzle(Room room) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnlockableObject getUnlockableObjectByID(int objectID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room getRoom(int roomID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(int playerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer destroyItem(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateGameState(String output, int moves, Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateLogin(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer addUser(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}
