package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import actor.NPC;
import actor.Player;
import dialogue.Link;
import dialogue.Node;
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
	private static List<Item> items = null;
	private static List<UnlockableObject> unlockableObjects = null;
	private static List<PlayableObject> playableObjects = null;
	private static List<Node> nodes = null;
	
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
			
			items = itemList;
			
			return itemList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<CompoundItem> getAllCompoundItems() throws IOException {
		List<CompoundItem> compoundItemList = new ArrayList<CompoundItem>();
		ReadCSV readCompoundItems = new ReadCSV("compoundItems.csv");
		
		try {			
			while (true) {
				List<String> itemRow = readCompoundItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				// name, weight, breakable, breakItem
				
				int itemID = Integer.parseInt(iter.next());
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
				boolean poppable = Integer.parseInt(iter.next()) == 1;
				
				CompoundItem item = new CompoundItem(name, weight, breakable, null);
				item.setItemID(itemID);
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
				item.setBreakItem(getItemByID(breakItemID));
				item.setBreakable(breakable);
				item.setPoppable(poppable);
				
				compoundItemList.add(item);
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
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				int actorID = Integer.parseInt(iter.next());
				int roomID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				int moves = Integer.parseInt(iter.next());
				String lastOutput = iter.next();
				
				Player player = new Player(null, roomID);
				player.setInventoryID(inventoryID);
				player.setActorID(actorID);
				player.setMoves(moves);
				player.setLastOutput(lastOutput);
				
				playerList.add(player);
			}
			
			System.out.println("playerList loaded");
			return playerList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Room> getAllRooms() throws IOException {
		List<Room> roomList = new ArrayList<Room>();
		ReadCSV readItems = new ReadCSV("rooms.csv");
		
		try {
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				

				int roomID = Integer.parseInt(iter.next());
				String description = iter.next();
				int inventoryID = Integer.parseInt(iter.next());
				
				Room room = new Room(description, roomID);
				room.setInventoryID(inventoryID);
				
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
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				int objectID = Integer.parseInt(iter.next());
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
				boolean canBeFed = Integer.parseInt(iter.next()) == 1;
				String fed = iter.next();
				boolean canBeScanned = Integer.parseInt(iter.next()) == 1;
				String scanned = iter.next();
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
				roomObject.setCanBeFed(canBeFed);
				roomObject.feed(fed);
				roomObject.setCanBeScanned(canBeScanned);
				roomObject.scanned(scanned);
				roomObject.setInventoryID(inventoryID);
				roomObject.setObjectID(objectID);
				
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
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				
				int objectID = Integer.parseInt(iter.next());
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
				String fed = iter.next();
				boolean canBeFed = Integer.parseInt(iter.next()) == 1;
				
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
				playableObject.setObjectID(objectID);
				playableObject.setPlayedNotes(playedNotes);
				playableObject.feed(fed);
				playableObject.setCanBeFed(canBeFed);
				
				playableObjectList.add(playableObject);
			}
			
			playableObjects = playableObjectList;
			
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
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				int objectID = Integer.parseInt(iter.next());
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
				String fed = iter.next();
				boolean canBeFed = Integer.parseInt(iter.next()) == 1;
				boolean canBeLookedAtNow = Integer.parseInt(iter.next()) == 1;
				boolean canBeClimbed = Integer.parseInt(iter.next()) == 1;
				int unlockItemID = Integer.parseInt(iter.next());
				
				Item unlockItem = null;
				if (items.size() >= unlockItemID) {
					unlockItem = items.get(unlockItemID);
				}
				
				UnlockableObject unlockableObject = new UnlockableObject(name, description, direction, blockingExit, unlockItem, roomID);
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
				unlockableObject.setObjectID(objectID);
				unlockableObject.setUnlockItemID(unlockItemID);
				unlockableObject.setConsumeItem(consumeItem);
				unlockableObject.feed(fed);
				unlockableObject.setCanBeFed(canBeFed);	
				unlockableObject.setCanBeLookedAtNow(canBeLookedAtNow);
				unlockableObject.setCanBeClimbed(canBeClimbed);
				
				unlockableObjectsList.add(unlockableObject);
			}
			
			System.out.println("unlockableObjectsList loaded");
			
			unlockableObjects = unlockableObjectsList;
			
			return unlockableObjectsList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Puzzle> getAllPuzzles() throws IOException {
		List<Puzzle> puzzlesList = new ArrayList<Puzzle>();
		ReadCSV readItems = new ReadCSV("puzzles.csv");
		
		try {
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();

				int roomID = Integer.parseInt(iter.next());
				String description = iter.next();
				String solution = iter.next();
				String hint = iter.next();
				boolean writtenSolution = Integer.parseInt(iter.next()) == 1;
				int unlockObstacleID = Integer.parseInt(iter.next());
				boolean solved = Integer.parseInt(iter.next()) == 1;
				
				UnlockableObject unlockObstacle = getUnlockableObjectByID(unlockObstacleID);
				String unlockObstacleName = unlockObstacle != null ? unlockObstacle.getName() : "";
				
				Puzzle puzzle = new Puzzle(description, solution, hint, writtenSolution, unlockObstacleName, roomID);
				puzzle.setSolved(solved);
				puzzle.setUnlockObstacleID(unlockObstacleID);
				puzzle.setPuzzleID(roomID);
				
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
		ReadCSV readItems = new ReadCSV("objectPuzzles.csv");
		
		try {
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}
				
				Iterator<String> iter = itemRow.iterator();
				
				int puzzleID = Integer.parseInt(iter.next());
				String description = iter.next();
				String solution = iter.next();
				String hint = iter.next();
				boolean writtenSolution = Integer.parseInt(iter.next()) == 1;
				boolean solved = Integer.parseInt(iter.next()) == 1;
				int unlockObstacleID = Integer.parseInt(iter.next());
				int roomID = Integer.parseInt(iter.next());
				int objectID = Integer.parseInt(iter.next());
				int itemID = Integer.parseInt(iter.next());

				UnlockableObject unlockObstacle = getUnlockableObjectByID(unlockObstacleID);
				String unlockObstacleName = unlockObstacle != null ? unlockObstacle.getName() : "";

				ObjectPuzzle puzzle = new ObjectPuzzle(description, solution, hint, getObjectByID(objectID), getItemByID(itemID), unlockObstacleName, roomID);
				puzzle.setPuzzleID(puzzleID);
				puzzle.setWrittenSolution(writtenSolution);
				puzzle.setUnlockObstacleID(unlockObstacleID);
				puzzle.setSolved(solved);
				
				objectPuzzlesList.add(puzzle);
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
				connections.addConnection(direction, destinationID);
				
				connectionsList.add(connections);
			}
			
			System.out.println("connectionsList loaded");
			return connectionsList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Node> getAllNodes() throws IOException {
		List<Node> nodeList = new ArrayList<Node>();
		ReadCSV readItems = new ReadCSV("nodes.csv");
		
		try {
			while (true) {
				List<String> nodeRow = readItems.next();
				
				if (nodeRow == null) {
					break;
				}
				
				Iterator<String> iter = nodeRow.iterator();
				
				int npcID = Integer.parseInt(iter.next());
				int nodeID = Integer.parseInt(iter.next());
				String message = iter.next();
				String type = iter.next();
				Node node = new Node(npcID, nodeID, message, type);
				
				nodeList.add(node);
			}
			
			System.out.println("nodeList loaded");
			
			nodes = nodeList;
			
			return nodeList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<NPC> getAllNpcs() throws IOException {
		List<NPC> npcList = new ArrayList<NPC>();
		ReadCSV readItems = new ReadCSV("npcs.csv");
		
		try {			
			while (true) {
				List<String> itemRow = readItems.next();
				
				if (itemRow == null) {
					break;
				}				
				
				Iterator<String> iter = itemRow.iterator();
				
				int npcID = Integer.parseInt(iter.next());
				int roomID = Integer.parseInt(iter.next());
				int inventoryID = Integer.parseInt(iter.next());
				String name = iter.next();
				String description = iter.next();
				int currentNodeID = Integer.parseInt(iter.next());
				int previousNodeID = Integer.parseInt(iter.next());
				int rootNodeID = Integer.parseInt(iter.next());
				boolean talkedTo = Integer.parseInt(iter.next()) == 1;
				boolean canTalkTo = Integer.parseInt(iter.next()) == 1;
				boolean done = Integer.parseInt(iter.next()) == 1;
				int requiredItemID = Integer.parseInt(iter.next());
				int unlockObstacleID = Integer.parseInt(iter.next());
				
				Item requiredItem = null;
				if (items.size() >= requiredItemID) {
					requiredItem = items.get(requiredItemID);
				}
				
				RoomObject unlockObstacle = null;
				if (unlockableObjects.size() >= unlockObstacleID) {
					unlockObstacle = unlockableObjects.get(unlockObstacleID);
				}
				
				Node currentNode = null;
				if (nodes.size() >= currentNodeID) {
					currentNode = nodes.get(currentNodeID);
				}
				
				Node previousNode = null;
				if (nodes.size() >= previousNodeID) {
					previousNode = nodes.get(previousNodeID);
				}
		
				Node rootNode = null;
				if (nodes.size() >= rootNodeID) {
					rootNode = nodes.get(rootNodeID);
				}
				
				NPC npc = new NPC(null, roomID, name, description, requiredItem, unlockObstacle);
				npc.setActorID(npcID);
				npc.setRoomID(roomID);
				npc.setInventoryID(inventoryID);
				npc.setCurrentNode(currentNode);
				npc.setPreviousNode(previousNode);
				npc.setRootNode(rootNode);
				npc.setTalkedTo(talkedTo);
				npc.setCanTalkTo(canTalkTo);
				npc.setDone(done);
				npc.setRequiredItemID(requiredItemID);
				npc.setUnlockObstacleID(unlockObstacleID);
				npcList.add(npc);
			}
			
			System.out.println("npcList loaded");
			return npcList;
		} finally {
			readItems.close();
		}
	}
	
	public static List<Link> getAllLinks() throws IOException {
		List<Link> linksList = new ArrayList<Link>();
		ReadCSV readItems = new ReadCSV("links.csv");
		
		try {			
			while (true) {
				List<String> linkRow = readItems.next();
				
				if (linkRow == null) {
					break;
				}				
				
				Iterator<String> iter = linkRow.iterator();
				
				int linkID = Integer.parseInt(iter.next());
				int nextNodeID = Integer.parseInt(iter.next());
				int previousNodeID = Integer.parseInt(iter.next());
				boolean isAvailable = Integer.parseInt(iter.next()) == 1;
				String option = iter.next();
				
				Node nextNode = null;
				if (nodes.size() >= nextNodeID) {
					nextNode = nodes.get(nextNodeID);
				}
				
				Node previousNode = null;
				if (nodes.size() >= previousNodeID) {
					previousNode = nodes.get(previousNodeID);
				}

				Link link = new Link(linkID, nextNode, previousNode, isAvailable, option);
				
				linksList.add(link);
			}
			
			System.out.println("linksList loaded");
			return linksList;
		} finally {
			readItems.close();
		}
	}
	
	private static UnlockableObject getUnlockableObjectByID(int objectID) {
		for (UnlockableObject object : unlockableObjects) {
			if (object.getObjectID() == objectID) {
				return object;
			}
		}
		
		return null;
	}
	
	private static Item getItemByID(int itemID) {
		for (Item item : items) {
			if (item.getItemID() == itemID) {
				return item;
			}
		}
		
		return null;
	}
	
	private static PlayableObject getObjectByID(int objectID) {
		for (PlayableObject object : playableObjects) {
			if (object.getObjectID() == objectID) {
				return object;
			}
		}
		
		return null;
	}
	
	private static Node getNodeByID(int nodeID) {
		for (Node node : nodes) {
			if (node.getNodeID() == nodeID) {
				return node;
			}
		}
		
		return null;
	}
}