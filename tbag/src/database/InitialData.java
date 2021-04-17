package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import actor.Actor;
import actor.Player;
import game.Game;
import items.CompoundItem;
import items.Item;
import map.Connections;
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
	
	public static List<CompoundItem> getAllCompoundItems() throws IOException {
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
	
	public static List<Player> getAllPlayers() throws IOException {
		List<Player> playerList = new ArrayList<Player>();
		ReadCSV readItems = new ReadCSV("players.csv");
		
		try {
			int actorID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				int roomID = Integer.parseInt(iter.next());
				
				Player player = new Player(new Game(), roomID);
				player.setInventoryID(Integer.parseInt(iter.next()));
				player.setActorID(actorID);
				
				playerList.add(player);
			}
			
			System.out.println("actorList loaded");
			return playerList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Room> getAllRooms() throws IOException {
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
	
	public static List<RoomObject> getAllObjects() throws IOException {
		List<RoomObject> roomObjectList = new ArrayList<RoomObject>();
		ReadCSV readItems = new ReadCSV("roomObjects.csv");
		
		try {
			int objectID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				String name = iter.next();
				String description = iter.next();
				String direction = iter.next();
				boolean isObstacle = Integer.parseInt(iter.next()) == 1;
				boolean blockingExit = Integer.parseInt(iter.next()) == 1;
				boolean moveable = Integer.parseInt(iter.next()) == 1;
				String covered = iter.next();
				boolean unlockable = Integer.parseInt(iter.next()) == 1;
				boolean locked = Integer.parseInt(iter.next()) == 1;
				boolean isInteractable = Integer.parseInt(iter.next()) == 1;
				boolean canHoldItems = Integer.parseInt(iter.next()) == 1;
				boolean coverable = Integer.parseInt(iter.next()) == 1;
				boolean previouslyUnlocked = Integer.parseInt(iter.next()) == 1;
				int roomID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				
				RoomObject roomObject = new RoomObject(name, description, direction, isObstacle, blockingExit, moveable, roomID);
				roomObject.cover(covered);
				roomObject.setUnlockable(unlockable);
				roomObject.setLocked(locked);
				roomObject.setInteractable(isInteractable);
				roomObject.setCanHoldItems(canHoldItems);
				roomObject.setCoverable(coverable);
				roomObject.setPreviouslyUnlocked(previouslyUnlocked);
				roomObject.setInventoryID(inventoryID);
				roomObject.setObjectID(objectID++);
				
				roomObjectList.add(roomObject);
			}
			
			System.out.println("roomObjectList loaded");
			return roomObjectList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<PlayableObject> getAllPlayableObjects() throws IOException {

		List<PlayableObject> playableObjectList = new ArrayList<PlayableObject>();
		ReadCSV readItems = new ReadCSV("playableObjects.csv");
		
		try {
			int objectID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				String name = iter.next();
				String description = iter.next();
				String direction = iter.next();
				boolean isObstacle = Integer.parseInt(iter.next()) == 1;
				boolean blockingExit = Integer.parseInt(iter.next()) == 1;
				boolean moveable = Integer.parseInt(iter.next()) == 1;
				String covered = iter.next();
				boolean unlockable = Integer.parseInt(iter.next()) == 1;
				boolean locked = Integer.parseInt(iter.next()) == 1;
				boolean isInteractable = Integer.parseInt(iter.next()) == 1;
				boolean canHoldItems = Integer.parseInt(iter.next()) == 1;
				boolean coverable = Integer.parseInt(iter.next()) == 1;
				boolean previouslyUnlocked = Integer.parseInt(iter.next()) == 1;
				int roomID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				boolean isInstrument = Integer.parseInt(iter.next()) == 1;
				String playedNotes = iter.next();
				String requiredNotes = iter.next();
				
				PlayableObject playableObject = new PlayableObject(name, description, direction, requiredNotes, isInstrument, roomID);
				playableObject.cover(covered);
				playableObject.setObstacle(isObstacle);
				playableObject.setBlockingExit(blockingExit);
				playableObject.setMoveable(moveable);
				playableObject.setUnlockable(unlockable);
				playableObject.setLocked(locked);
				playableObject.setInteractable(isInteractable);
				playableObject.setCanHoldItems(canHoldItems);
				playableObject.setCoverable(coverable);
				playableObject.setPreviouslyUnlocked(previouslyUnlocked);
				playableObject.setInventoryID(inventoryID);
				playableObject.setObjectID(objectID++);
				playableObject.setPlayedNotes(playedNotes);
				
				playableObjectList.add(playableObject);
			}
			
			System.out.println("playableObjectList loaded");
			return playableObjectList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<UnlockableObject> getAllUnlockableObjects() throws IOException {
		List<UnlockableObject> unlockableObjectsList = new ArrayList<UnlockableObject>();
		ReadCSV readItems = new ReadCSV("unlockableObjects.csv");
		
		try {
			int objectID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				String name = iter.next();
				String description = iter.next();
				String direction = iter.next();
				boolean isObstacle = Integer.parseInt(iter.next()) == 1;
				boolean blockingExit = Integer.parseInt(iter.next()) == 1;
				boolean moveable = Integer.parseInt(iter.next()) == 1;
				String covered = iter.next();
				boolean unlockable = Integer.parseInt(iter.next()) == 1;
				boolean locked = Integer.parseInt(iter.next()) == 1;
				boolean isInteractable = Integer.parseInt(iter.next()) == 1;
				boolean canHoldItems = Integer.parseInt(iter.next()) == 1;
				boolean coverable = Integer.parseInt(iter.next()) == 1;
				boolean previouslyUnlocked = Integer.parseInt(iter.next()) == 1;
				int roomID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				boolean consumeItem = Integer.parseInt(iter.next()) == 1;
				int unlockItemID = Integer.parseInt(iter.next());
				
				UnlockableObject unlockableObject = new UnlockableObject(name, description, direction, blockingExit, getAllItems().get(unlockItemID), roomID);
				unlockableObject.cover(covered);
				unlockableObject.setObstacle(isObstacle);
				unlockableObject.setMoveable(moveable);
				unlockableObject.setUnlockable(unlockable);
				unlockableObject.setLocked(locked);
				unlockableObject.setInteractable(isInteractable);
				unlockableObject.setCanHoldItems(canHoldItems);
				unlockableObject.setCoverable(coverable);
				unlockableObject.setPreviouslyUnlocked(previouslyUnlocked);
				unlockableObject.setInventoryID(inventoryID);
				unlockableObject.setObjectID(objectID++);
				unlockableObject.setUnlockItemID(unlockItemID);
				unlockableObject.setConsumeItem(consumeItem);
				
				unlockableObjectsList.add(unlockableObject);
			}
			
			System.out.println("unlockableObjectsList loaded");
			return unlockableObjectsList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Puzzle> getAllPuzzles() throws IOException {
		List<Puzzle> puzzlesList = new ArrayList<Puzzle>();
		ReadCSV readItems = new ReadCSV("puzzles.csv");
		
		try {
			int puzzleID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				String description = iter.next();
				String solution = iter.next();
				String hint = iter.next();
				boolean writtenSolution = Integer.parseInt(iter.next()) == 1;
				String unlockObstacle = iter.next();
				boolean solved = Integer.parseInt(iter.next()) == 1;
				int roomID = Integer.parseInt(iter.next());
				
				Puzzle puzzle = new Puzzle(description, solution, hint, writtenSolution, unlockObstacle, roomID);
				puzzle.setSolved(solved);
				
				puzzle.setPuzzleID(puzzleID++);
				
				puzzlesList.add(puzzle);
			}
			
			System.out.println("puzzlesList loaded");
			return puzzlesList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<ObjectPuzzle> getAllObjectPuzzles() throws IOException {
		List<ObjectPuzzle> objectPuzzlesList = new ArrayList<ObjectPuzzle>();
		ReadCSV readItems = new ReadCSV("objectPuzzle.csv");
		
		try {
			int puzzleID = 1;
			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				Integer.parseInt(iter.next());
				
				String description = iter.next();
				String solution = iter.next();
				String hint = iter.next();
				boolean writtenSolution = Integer.parseInt(iter.next()) == 1;
				String unlockObstacle = iter.next();
				boolean solved = Integer.parseInt(iter.next()) == 1;
				int roomID = Integer.parseInt(iter.next());
				int objectID = Integer.parseInt(iter.next());
				int itemID = Integer.parseInt(iter.next());
				
				ObjectPuzzle puzzle = new ObjectPuzzle(description, solution, hint, getAllObjects().get(objectID), getAllItems().get(itemID), unlockObstacle, roomID);
				puzzle.setPuzzleID(puzzleID++);
				puzzle.setWrittenSolution(writtenSolution);
				puzzle.setSolved(solved);
				
				objectPuzzlesList.add(puzzle);
				
				return objectPuzzlesList;
			}
			
			System.out.println("objectPuzzlesList loaded");
			return objectPuzzlesList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Connections> getAllConnections() throws IOException {
		List<Connections> connectionsList = new ArrayList<Connections>();
		ReadCSV readItems = new ReadCSV("connections.csv");
		
		try {			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}				
				
				Iterator<String> iter = itemRow.iterator();
				
				int locationID = Integer.parseInt(iter.next());
				int destinationID = Integer.parseInt(iter.next());
				String direction = iter.next();

				Connections connections = new Connections(locationID);
				connections.setConnectionID(locationID);
				connections.addConnection(direction, getAllRooms().get(destinationID));
				
				connectionsList.add(connections);
				
				return connectionsList;
			}
			
			System.out.println("connectionsList loaded");
			return connectionsList;
		} finally {
			readItems.close();
		}
	}
}