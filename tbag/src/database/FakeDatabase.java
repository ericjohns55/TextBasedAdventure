package database;

import java.io.IOException;
import java.util.List;

import actor.Actor;
import game.Game;
import items.CompoundItem;
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
}
