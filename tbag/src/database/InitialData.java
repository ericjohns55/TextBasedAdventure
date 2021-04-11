package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

public class InitialData {
	public static List<Item> getAllItems() throws IOException {
		List<Item> itemList = new ArrayList<Item>();
		ReadCSV readItems = new ReadCSV("items.csv");
		
		try {
			int itemID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				Item item = new Item();
				
				Integer.parseInt(iter.next());
				item.setItemID(itemID++);
				item.setName(iter.next());
				item.setDescription(iter.next());
				item.setWeight(Double.parseDouble(iter.next()));
				item.setInteractable(Integer.parseInt(iter.next()) == 1);
				item.setCanBePickedUp(Integer.parseInt(iter.next()) == 1);
				item.setConsumeOnuse(Integer.parseInt(iter.next()) == 1);
				item.setInInventory(Integer.parseInt(iter.next()) == 1);
				item.setEquipped(Integer.parseInt(iter.next()) == 1);
				item.setEquippable(Integer.parseInt(iter.next()) == 1);
				item.setReadable(Integer.parseInt(iter.next()) == 1);
				item.setPourable(Integer.parseInt(iter.next()) == 1);
				item.setLocationID(Integer.parseInt(iter.next()));
				
				itemList.add(item);
			}
			
			System.out.println("itemList loaded");
			return itemList;
		} finally {
			readItems.close();
		}
	}
	
	public List<CompoundItem> getAllCompoundItems() throws IOException {
		List<CompoundItem> compoundItemList = new ArrayList<CompoundItem>();
		ReadCSV readItems = new ReadCSV("compoundItems.csv");
		
		try {
			int itemID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				
			}
			
			System.out.println("compoundItemList loaded");
			return compoundItemList;
		} finally {
			readItems.close();
		}
	}
	
	public List<Actor> getAllActors() throws IOException {
		
	}
	
	public List<Room> getAllRooms() throws IOException {
		List<Room> roomList = new ArrayList<Room>();
		ReadCSV readItems = new ReadCSV("rooms.csv");
		
		try {
			int roomID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				

				Integer.parseInt(iter.next());
				
				
				Room room = new Room(iter.next(), roomID++);
				
				// connections, items, objects, puzzles
				
				roomList.add(room);
			}
			
			System.out.println("roomList loaded");
			return roomList;
		} finally {
			readItems.close();
		}
	}
	
	public List<RoomObject> getAllObjects() throws IOException {
		
	}
	
	public List<PlayableObject> getAllPlayableObjects() throws IOException {
		
	}
	
	public List<UnlockableObject> getAllUnlockableObjects() throws IOException {
		
	}
	
	public List<Puzzle> getAllPuzzles() throws IOException {
		
	}
	
	public List<ObjectPuzzle> getAllObjectPuzzles() throws IOException {
		
	}
	
	public Game getGameState() throws IOException {
		
	}
}
