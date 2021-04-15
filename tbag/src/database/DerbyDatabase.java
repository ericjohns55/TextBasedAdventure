package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import puzzle.Puzzle;

// REFERENCED FROM LIBRARY EXAMPLE
public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	
	
	
	// wrapper SQL transaction function that calls actual transaction function (which has retries)
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	// SQL transaction function which retries the transaction MAX_ATTEMPTS times before failing
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	// TODO: Here is where you name and specify the location of your Derby SQL database
	// TODO: Change it here and in SQLDemo.java under CS320_LibraryExample_Lab06->edu.ycp.cs320.sqldemo
	// TODO: DO NOT PUT THE DB IN THE SAME FOLDER AS YOUR PROJECT - that will cause conflicts later w/Git
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320-2019-LibraryExample-DB/library.db;create=true");		
		
		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	@Override
	public Item getItemByID(int itemID) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.*" + 
								"from items" +
									"where items.itemID = ?");	
					
					stmt.setInt(1, itemID);
					
					Item item = null;
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						double weight = resultSet.getDouble(index++);
						boolean isInteractable = resultSet.getInt(index++) == 1;
						boolean canBePickedUp = resultSet.getInt(index++) == 1;
						boolean consumeOnUse = resultSet.getInt(index++) == 1;
						boolean inInventory = resultSet.getInt(index++) == 1;
						boolean isEquipped = resultSet.getInt(index++) == 1;
						boolean equippable = resultSet.getInt(index++) == 1;
						boolean readable = resultSet.getInt(index++) == 1;
						boolean pourable = resultSet.getInt(index++) == 1;
						int locationID = resultSet.getInt(index++);
						
						item = new Item(name, weight);
						item.setItemID(itemID);
						item.setDescription(description);
						item.setInteractable(isInteractable);
						item.setCanBePickedUp(canBePickedUp);
						item.setConsumeOnuse(consumeOnUse);
						item.setInInventory(inInventory);
						item.setEquipped(isEquipped);
						item.setEquippable(equippable);
						item.setReadable(readable);
						item.setPourable(pourable);
						item.setLocationID(locationID);
					}
					
					if (!found) {
						System.out.println("Could not find any item with that ID");
					}
					
					return item;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Inventory getPlayerInventory(Player player) {
		return executeTransaction(new Transaction<Inventory>() {
			@Override
			public Inventory execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.itemID" + 
								"from items, inventories" +
									"where inventories.inventoryID = ? and items.inventoryID = inventories.inventoryID");	
					
					stmt.setInt(1, player.getInventoryID());
					
					
					Inventory inventory = new Inventory();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						
						Item item = getItemByID(itemID);
						
						inventory.addItem(item.getName(), item);
					}
					
					if (!found) {
						System.out.println("Could not find any items to populate player inventory.");
					}
					
					return inventory;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Inventory getInventory(RoomObject roomObject) {
		return executeTransaction(new Transaction<Inventory>() {
			@Override
			public Inventory execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.itemID" + 
								"from items, roomObjects" +
									"where roomObjects.inventoryID = ? and items.inventoryID = roomObjects.inventoryID");	
					
					stmt.setInt(1, roomObject.getInventoryID());
					
					
					Inventory inventory = new Inventory();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						
						Item item = getItemByID(itemID);
						
						inventory.addItem(item.getName(), item);
					}
					
					if (!found) {
						System.out.println("Could not populate inventory.");
					}
					
					
					stmt2 = conn.prepareStatement(
							"select compoundItems.*" + 
								"from compoundItems, roomObjects" +
									"where roomObjects.inventoryID = ? and compoundItems.inventoryID = roomObjects.inventoryID");	
					
					stmt2.setInt(1, roomObject.getInventoryID());
					
					resultSet2 = stmt2.executeQuery();
					
					while (resultSet2.next()) {						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						double weight = resultSet.getDouble(index++);
						boolean isInteractable = resultSet.getInt(index++) == 1;
						boolean canBePickedUp = resultSet.getInt(index++) == 1;
						boolean consumeOnUse = resultSet.getInt(index++) == 1;
						boolean inInventory = resultSet.getInt(index++) == 1;
						boolean isEquipped = resultSet.getInt(index++) == 1;
						boolean equippable = resultSet.getInt(index++) == 1;
						boolean readable = resultSet.getInt(index++) == 1;
						boolean pourable = resultSet.getInt(index++) == 1;
						int locationID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						int breakID = resultSet.getInt(index++);
						boolean breakable = resultSet.getInt(index++) == 1;
						
						Item breakItem = getItemByID(breakID);
						
						CompoundItem item = new CompoundItem(name, weight, breakable, breakItem);
						item.setItemID(itemID);
						item.setDescription(description);
						item.setInteractable(isInteractable);
						item.setCanBePickedUp(canBePickedUp);
						item.setConsumeOnuse(consumeOnUse);
						item.setInInventory(inInventory);
						item.setEquipped(isEquipped);
						item.setEquippable(equippable);
						item.setReadable(readable);
						item.setPourable(pourable);
						item.setLocationID(locationID);
						item.setInventoryID(inventoryID);
						item.setBreakItem(breakItem);
						
						inventory.addItem(name, item);
					}
					
					return inventory;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Inventory getInventory(Room room) {
		return executeTransaction(new Transaction<Inventory>() {
			@Override
			public Inventory execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.itemID" + 
								"from items, rooms" +
									"where rooms.inventoryID = ? and items.inventoryID = rooms.inventoryID");	
					
					stmt.setInt(1, room.getInventoryID());
					
					Inventory inventory = new Inventory();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						
						Item item = getItemByID(itemID);
						
						inventory.addItem(item.getName(), item);
					}
					
					if (!found) {
						System.out.println("Could not populate inventory.");
					}
					
					
					stmt2 = conn.prepareStatement(
							"select compoundItems.*" + 
								"from compoundItems, rooms" +
									"where rooms.inventoryID = ? and compoundItems.inventoryID = rooms.inventoryID");	
					
					stmt2.setInt(1, room.getInventoryID());
					
					resultSet2 = stmt2.executeQuery();
					
					while (resultSet2.next()) {						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						double weight = resultSet.getDouble(index++);
						boolean isInteractable = resultSet.getInt(index++) == 1;
						boolean canBePickedUp = resultSet.getInt(index++) == 1;
						boolean consumeOnUse = resultSet.getInt(index++) == 1;
						boolean inInventory = resultSet.getInt(index++) == 1;
						boolean isEquipped = resultSet.getInt(index++) == 1;
						boolean equippable = resultSet.getInt(index++) == 1;
						boolean readable = resultSet.getInt(index++) == 1;
						boolean pourable = resultSet.getInt(index++) == 1;
						int locationID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						int breakID = resultSet.getInt(index++);
						boolean breakable = resultSet.getInt(index++) == 1;
						
						Item breakItem = getItemByID(breakID);
						
						CompoundItem item = new CompoundItem(name, weight, breakable, breakItem);
						item.setItemID(itemID);
						item.setDescription(description);
						item.setInteractable(isInteractable);
						item.setCanBePickedUp(canBePickedUp);
						item.setConsumeOnuse(consumeOnUse);
						item.setInInventory(inInventory);
						item.setEquipped(isEquipped);
						item.setEquippable(equippable);
						item.setReadable(readable);
						item.setPourable(pourable);
						item.setLocationID(locationID);
						item.setInventoryID(inventoryID);
						item.setBreakItem(breakItem);
						
						inventory.addItem(name, item);
					}
					
					return inventory;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Inventory getInventoryByID(int id) {
		return executeTransaction(new Transaction<Inventory>() {
			@Override
			public Inventory execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.itemID" + 
								"from items, inventories" +
									"where inventories.inventoryID = ? and items.inventoryID = inventories.inventoryID");	
					
					stmt.setInt(1, id);
					
					Inventory inventory = new Inventory();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						
						Item item = getItemByID(itemID);
						
						inventory.addItem(item.getName(), item);
					}
					
					if (!found) {
						System.out.println("Could not populate inventory.");
					}
					
					
					stmt2 = conn.prepareStatement(
							"select compoundItems.*" + 
								"from compoundItems, inventories" +
									"where inventories.inventoryID = ? and compoundItems.inventoryID = inventories.inventoryID");	
					
					stmt2.setInt(1, id);
					
					resultSet2 = stmt2.executeQuery();
					
					while (resultSet2.next()) {						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						double weight = resultSet.getDouble(index++);
						boolean isInteractable = resultSet.getInt(index++) == 1;
						boolean canBePickedUp = resultSet.getInt(index++) == 1;
						boolean consumeOnUse = resultSet.getInt(index++) == 1;
						boolean inInventory = resultSet.getInt(index++) == 1;
						boolean isEquipped = resultSet.getInt(index++) == 1;
						boolean equippable = resultSet.getInt(index++) == 1;
						boolean readable = resultSet.getInt(index++) == 1;
						boolean pourable = resultSet.getInt(index++) == 1;
						int locationID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						int breakID = resultSet.getInt(index++);
						boolean breakable = resultSet.getInt(index++) == 1;
						
						Item breakItem = getItemByID(breakID);
						
						CompoundItem item = new CompoundItem(name, weight, breakable, breakItem);
						item.setItemID(itemID);
						item.setDescription(description);
						item.setInteractable(isInteractable);
						item.setCanBePickedUp(canBePickedUp);
						item.setConsumeOnuse(consumeOnUse);
						item.setInInventory(inInventory);
						item.setEquipped(isEquipped);
						item.setEquippable(equippable);
						item.setReadable(readable);
						item.setPourable(pourable);
						item.setLocationID(locationID);
						item.setInventoryID(inventoryID);
						item.setBreakItem(breakItem);
						
						inventory.addItem(name, item);
					}
					
					return inventory;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<RoomObject> findAllObjects(Room room) {
		return executeTransaction(new Transaction<List<RoomObject>>() {
			@Override
			public List<RoomObject> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomObjects.*" +
								"from roomObjects, rooms" + 
									"where roomObjects.roomID = ?"
					);	
					
					stmt.setInt(1, room.getRoomID());
					
					List<RoomObject> roomObjects = new ArrayList<RoomObject>();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int objectID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						String direction = resultSet.getString(index++);
						boolean isObstacle = resultSet.getInt(index++) == 1;
						boolean blockingExit = resultSet.getInt(index++) == 1;
						boolean moveable = resultSet.getInt(index++) == 1;
						String covered = resultSet.getString(index++);
						boolean unlockable = resultSet.getInt(index++) == 1;
						boolean locked = resultSet.getInt(index++) == 1;
						boolean interactable = resultSet.getInt(index++) == 1;
						boolean canHoldItems = resultSet.getInt(index++) == 1;
						boolean coverable = resultSet.getInt(index++) == 1;
						boolean previouslyUnlocked = resultSet.getInt(index++) == 1;
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						
						RoomObject object = new RoomObject(name, description, direction, isObstacle, blockingExit, moveable, roomID);
						object.setObjectID(objectID);
						object.cover(covered);
						object.setUnlockable(unlockable);
						object.setLocked(locked);
						object.setInteractable(interactable);
						object.setCanHoldItems(canHoldItems);
						object.setCoverable(coverable);
						object.setPreviouslyUnlocked(previouslyUnlocked);
						object.setInventoryID(inventoryID);
						object.setInventory(getInventoryByID(inventoryID));
						
						roomObjects.add(object);
					}
					
					if (!found) {
						System.out.println("Could not find any room objects.");
					}
					
					return roomObjects;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<Player> findAllPlayers() {
		return executeTransaction(new Transaction<List<Player>>() {
			@Override
			public List<Player> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select players.* from players");	
					
					List<Player> players = new ArrayList<Player>();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;

						int actorID = resultSet.getInt(index++);
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						
						Player actor = new Player(new Game(), roomID);
						actor.setActorID(actorID);
						actor.setRoomID(roomID);
						actor.setInventoryID(inventoryID);
						actor.setInventory(getInventoryByID(inventoryID));
						
						players.add(actor);
					}
					
					if (!found) {
						System.out.println("Could not find any players.");
					}
					
					return players;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Integer addItemToInventory(Inventory inventory, Item item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ?");

					stmt.setInt(1, inventory.getInventoryID());
					stmt.setInt(2, item.getItemID());

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Item removeItemFromInventory(Inventory destinationInventory, Item item) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ?");

					stmt.setInt(1, destinationInventory.getInventoryID());
					stmt.setInt(2, item.getItemID());

					stmt.executeUpdate();
					
					return getItemByID(item.getItemID());
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Integer toggleLocks(UnlockableObject object, boolean locked) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update unlockableObjects set locked = ? where unlockableObjects.objectID = ?");

					stmt.setInt(1, locked ? 1 : 0);
					stmt.setInt(2, object.getObjectID());

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Integer moveRooms(Player player, int roomID) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update players set roomID = ? where players.actorID = ?");

					stmt.setInt(1, roomID);
					stmt.setInt(2, player.getActorID());

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Integer pushObject(RoomObject object, String direction) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					if (object instanceof PlayableObject) {
						stmt = conn.prepareStatement("update playableObjects set direction = ? where playableObjects.objectID = ?");
					} else if (object instanceof UnlockableObject) {
						stmt = conn.prepareStatement("update unlockableObjects set direction = ? where unlockableObjects.objectID = ?");
					} else {
						stmt = conn.prepareStatement("update roomObjects set direction = ? where roomObjects.objectID = ?");
					}

					stmt.setString(1, direction);
					stmt.setInt(2, object.getObjectID());

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void breakItem(CompoundItem compoundItem) {
		// TODO Auto-generated method stub
		
	}
}
