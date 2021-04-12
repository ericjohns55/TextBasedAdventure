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
		ReadCSV readCompoundItems = new ReadCSV("compoundItems.csv");
		
		try {
			int itemID = 1;
			
			while (true) {
				List<String> itemRow = readCompoundItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				// name, weight, breakable, breakItem
				
				String name = iter.next();
				String description = iter.next();
				double weight = Double.parseDouble(iter.next());
				boolean interactable = Integer.parseInt(iter.next()) == 1;
				boolean canBePickedUp = Integer.parseInt(iter.next()) == 1;
				boolean consumeOnUse = Integer.parseInt(iter.next()) == 1;
				boolean inInventory = Integer.parseInt(iter.next()) == 1;
				boolean equipped = Integer.parseInt(iter.next()) == 1;
				boolean equippable = Integer.parseInt(iter.next()) == 1;
				boolean readable = Integer.parseInt(iter.next()) == 1;
				boolean pourable = Integer.parseInt(iter.next()) == 1;
				int locationID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				int breakItemID = Integer.parseInt(iter.next());
				boolean breakable = Integer.parseInt(iter.next()) == 1;
				
				CompoundItem item = new CompoundItem(name, weight, breakable, null);
				item.setItemID(itemID++);
				item.setDescription(description);
				item.setInteractable(interactable);
				item.setCanBePickedUp(canBePickedUp);
				item.setConsumeOnuse(consumeOnUse);
				item.setInInventory(inInventory);
				item.setEquipped(equipped);
				item.setEquippable(equippable);
				item.setReadable(readable);
				item.setPourable(pourable);
				item.setLocationID(locationID);
				item.setInventoryID(inventoryID);
				item.setBreakItem(getAllItems().get(breakItemID));
				item.setBreakable(breakable);
			}
			
			System.out.println("compoundItemList loaded");
			return compoundItemList;
		} finally {
			readCompoundItems.close();
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
