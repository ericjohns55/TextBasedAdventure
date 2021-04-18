package database;

import java.io.IOException;
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

public class FakeDatabase implements IDatabase {
	List<Item> items;
	List<CompoundItem> compoundItems;
	
	public FakeDatabase() {
		
		readInitialData();
	}
	
	public void readInitialData() {
		try {
			items.addAll(InitialData.getAllItems());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}

	@Override
	public Item getItemByID(int itemID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getPlayerInventory(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventory(RoomObject roomObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventory(Room room) {
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
}
