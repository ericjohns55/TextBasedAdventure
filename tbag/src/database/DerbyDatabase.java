package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import actor.Player;
import game.Game;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.Connections;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.ObjectPuzzle;
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
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320-Windows98-DB/tbag.db;create=true");		
		
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
					
					// TODO: OBJECT PUZZLES
					
					return roomObjects;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<Player> getAllPlayers() {
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
	public Integer breakItem(CompoundItem compoundItem, Inventory destinationInventory) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.inventoryID = ?");

					stmt.setInt(1, destinationInventory.getInventoryID());
					stmt.setInt(2, compoundItem.getInventoryID());

					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement("update compoundItems set inventoryID = ? where compoundItems.itemID = ?");
					
					stmt2.setInt(1, -1);
					stmt2.setInt(2, compoundItem.getItemID());
					
					return stmt2.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmtCmpdItms = null;
				PreparedStatement stmtInvs = null;
				PreparedStatement stmtItms = null;	
				PreparedStatement stmtObjPuzls = null;	
				PreparedStatement stmtPlybleObjs = null;	
				PreparedStatement stmtPlyrs = null;	
				PreparedStatement stmtPuzls = null;	
				PreparedStatement stmtRmObjs = null;
				PreparedStatement stmtRms = null;
				PreparedStatement stmtUnlckbleObjs = null;	
				PreparedStatement stmtCnnctns = null;				
			
				try {
					stmtCmpdItms = conn.prepareStatement(
						"create table compoundItems (" +
						"	itemID integer," +								
						"	name varchar(40)," +
						"	description varchar(255)," +
						"	weight double," +
						"	isInteractable integer," +
						"	canBePickedUp integer," +
						"	consumeOnUse integer," +
						"	inInventory integer," +
						"	isEquipped integer," +
						"	equippable integer," +
						"	readable integer," +
						"	pourable integer," +
						"	locationID integer," +
						"	inventoryID integer," +
						"	breakItemID integer," +
						"	breakable integer" +
						")"
					);	
					stmtCmpdItms.executeUpdate();
					
					System.out.println("compoundItems table created");
					
					stmtInvs = conn.prepareStatement("create table inventories (inventoryID integer)");	
					stmtInvs.executeUpdate();
					
					System.out.println("inventories table created");	
					
					stmtItms = conn.prepareStatement(
						"create table items (" +
						"	itemID integer," +								
						"	name varchar(40)," +
						"	description varchar(255)," +
						"	weight double," +
						"	isInteractable integer," +
						"	canBePickedUp integer," +
						"	consumeOnUse integer," +
						"	inInventory integer," +
						"	isEquipped integer," +
						"	equippable integer," +
						"	readable integer," +
						"	pourable integer," +
						"	inventoryID integer" +
						")"
					);	
					stmtItms.executeUpdate();
					
					System.out.println("items table created");	
					
					stmtObjPuzls = conn.prepareStatement(
						"create table objectPuzzles (" +
						"	puzzleID integer," +								
						"	description varchar(40)," +
						"	solution varchar(255)," +
						"	hint varchar(255)," +
						"	solved integer," +
						"	writtenSolution integer," +
						"	unlockObstacle varchar(40)," +
						"	roomID integer," +
						"	objectID integer," +
						"	itemID integer" +
						")"
					);	
					stmtObjPuzls.executeUpdate();
					
					System.out.println("objectPuzzles table created");	
					
					stmtPlybleObjs = conn.prepareStatement(
						"create table playableObjects (" +
						"	objectID integer," +								
						"	name varchar(40)," +
						"	description varchar(255)," +								
						"	direction varchar(40)," +
						"	isObstacle integer," +
						"	blockingExit integer," +
						"	moveable integer," +
						"	covered varchar(40)," +
						"	unlockable integer," +
						"	locked integer," +
						"	isInteractable integer," +
						"	canHoldItems integer," +
						"	coverable integer," +
						"	previouslyUnlocked integer," +
						"	roomID integer," +
						"	inventoryID integer," +
						"	isInstrument integer," +							
						"	playedNotes varchar(40)," +							
						"	requiredNotes varchar(40)" +
						")"
					);	
					stmtPlybleObjs.executeUpdate();
					
					System.out.println("playableObjects table created");						
								
					stmtPlyrs = conn.prepareStatement(
						"create table players (" +
						"	actorID integer," +
						"	roomID integer," +
						"	inventoryID integer" +
						")"
					);	
					stmtPlyrs.executeUpdate();
					
					System.out.println("players table created");	
					
					stmtPuzls = conn.prepareStatement(
						"create table puzzles (" +
						"	puzzleID integer," +								
						"	description varchar(40)," +
						"	solution varchar(255)," +
						"	hint varchar(255)," +
						"	writtenSolution integer," +
						"	unlockObstacle varchar(40)," +
						"	solved integer," +
						"	roomID integer" +
						")"
					);	
					stmtPuzls.executeUpdate();
					
					System.out.println("puzzles table created");
					
					stmtRmObjs = conn.prepareStatement(
						"create table roomObjects (" +
						"	objectID integer," +								
						"	name varchar(40)," +
						"	description varchar(255)," +								
						"	direction varchar(40)," +
						"	isObstacle integer," +
						"	blockingExit integer," +
						"	moveable integer," +
						"	covered varchar(40)," +
						"	unlockable integer," +
						"	locked integer," +
						"	isInteractable integer," +
						"	canHoldItems integer," +
						"	coverable integer," +
						"	previouslyUnlocked integer," +
						"	roomID integer," +
						"	inventoryID integer" +
						")"
					);	
					stmtRmObjs.executeUpdate();
					
					System.out.println("roomObjects table created");	
					
					stmtRms = conn.prepareStatement(
						"create table rooms (" +
						"	roomID integer," +			
						"	description varchar(255)," +
						"	inventoryID integer" +
						")"
					);	
					stmtRms.executeUpdate();
					
					System.out.println("rooms table created");		
					
					stmtUnlckbleObjs = conn.prepareStatement(
						"create table unlockableObjects (" +
						"	objectID integer," +								
						"	name varchar(40)," +
						"	description varchar(255)," +								
						"	direction varchar(40)," +
						"	isObstacle integer," +
						"	blockingExit integer," +
						"	moveable integer," +
						"	covered varchar(40)," +
						"	unlockable integer," +
						"	locked integer," +
						"	isInteractable integer," +
						"	canHoldItems integer," +
						"	coverable integer," +
						"	previouslyUnlocked integer," +
						"	roomID integer," +
						"	inventoryID integer," +
						"	consumeItem integer," +		
						"	unlockItemID integer" +		
						")"
					);	
					stmtUnlckbleObjs.executeUpdate();
					
					stmtCnnctns = conn.prepareStatement(
						"create table connections (" +
						"	locationID integer," +
						"	destinationID integer," +
						"	direction varchar(40)" +
						")"
					);
					stmtCnnctns.executeUpdate();
					
					System.out.println("unlockableObjects table created");		
					return true;
				} finally {
					DBUtil.closeQuietly(stmtCmpdItms);
					DBUtil.closeQuietly(stmtInvs);
					DBUtil.closeQuietly(stmtItms);
					DBUtil.closeQuietly(stmtObjPuzls);
					DBUtil.closeQuietly(stmtPlybleObjs);
					DBUtil.closeQuietly(stmtPlyrs);
					DBUtil.closeQuietly(stmtPuzls);
					DBUtil.closeQuietly(stmtRmObjs);
					DBUtil.closeQuietly(stmtRms);
					DBUtil.closeQuietly(stmtUnlckbleObjs);	
					DBUtil.closeQuietly(stmtCnnctns);
				}
			}
		});
	}
	
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Item> items;
				List<CompoundItem> compoundItems;
				List<Player> players;
				List<Room> rooms;
				List<RoomObject> roomObjects;
				List<PlayableObject> playableObjects;
				List<UnlockableObject> unlockableObjects;
				List<Puzzle> puzzles;
				List<ObjectPuzzle> objectPuzzles;
				List<Connections> connections;
				
				try {
					items = InitialData.getAllItems();
					compoundItems = InitialData.getAllCompoundItems();
					players = InitialData.getAllPlayers();
					rooms = InitialData.getAllRooms();
					roomObjects = InitialData.getAllObjects();
					playableObjects = InitialData.getAllPlayableObjects();
					unlockableObjects = InitialData.getAllUnlockableObjects();
					puzzles = InitialData.getAllPuzzles();
					objectPuzzles = InitialData.getAllObjectPuzzles();
					connections = InitialData.getAllConnections();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertItems = null;
				PreparedStatement insertCompoundItems = null;
				PreparedStatement insertPlayers = null;
				PreparedStatement insertRooms = null;
				PreparedStatement insertRoomObjects = null;
				PreparedStatement insertPlayableObjects = null;
				PreparedStatement insertUnlockableObjects = null;
				PreparedStatement insertPuzzles = null;
				PreparedStatement insertObjectPuzzles = null;
				PreparedStatement insertConnections = null;
				
				try {
					insertItems = conn.prepareStatement("insert into items (itemID, name, description, weight, isInteractable, canBePickedUp, consumeOnUse, inInventory, isEquipped, equippable, readable, pourable, inventoryID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (Item item : items) {
						insertItems.setInt(1, item.getItemID());
						insertItems.setString(2, item.getName());
						insertItems.setString(3, item.getDescription());
						insertItems.setDouble(4, item.getWeight());
						insertItems.setInt(5, item.isInteractable() ? 1 : 0);
						insertItems.setInt(6, item.canBePickedUp() ? 1 : 0);
						insertItems.setInt(7, item.consumeOnUse() ? 1 : 0);
						insertItems.setInt(8, item.inInventory() ? 1 : 0);
						insertItems.setInt(9, item.isEquipped() ? 1 : 0);
						insertItems.setInt(10, item.isEquippable() ? 1 : 0);
						insertItems.setInt(11, item.isReadable() ? 1 : 0);
						insertItems.setInt(12, item.isPourable() ? 1 : 0);
						insertItems.setInt(13, item.getLocationID());
						insertItems.addBatch();
					}
					
					insertItems.executeBatch();
					
					
					insertCompoundItems = conn.prepareStatement("insert into compoundItems (itemID, name, description, weight, isInteractable, canBePickedUp, consumeOnUse, inInventory, isEquipped, equippable, readable, pourable, locationID, inventoryID, breakItemID, breakable) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");	
					
					for (CompoundItem compoundItem : compoundItems) {
						insertCompoundItems.setInt(1, compoundItem.getItemID());
						insertCompoundItems.setString(2, compoundItem.getName());
						insertCompoundItems.setString(3, compoundItem.getDescription());
						insertCompoundItems.setDouble(4, compoundItem.getWeight());
						insertCompoundItems.setInt(5, compoundItem.isInteractable() ? 1 : 0);
						insertCompoundItems.setInt(6, compoundItem.canBePickedUp() ? 1 : 0);
						insertCompoundItems.setInt(7, compoundItem.consumeOnUse() ? 1 : 0);
						insertCompoundItems.setInt(8, compoundItem.inInventory() ? 1 : 0);
						insertCompoundItems.setInt(9, compoundItem.isEquipped() ? 1 : 0);
						insertCompoundItems.setInt(10, compoundItem.isEquippable() ? 1 : 0);
						insertCompoundItems.setInt(11, compoundItem.isReadable() ? 1 : 0);
						insertCompoundItems.setInt(12, compoundItem.isPourable() ? 1 : 0);
						insertCompoundItems.setInt(13, compoundItem.getLocationID());
						insertCompoundItems.setInt(14, compoundItem.getInventoryID());
						insertCompoundItems.setInt(15, compoundItem.getBreakItem().getItemID());
						insertCompoundItems.setInt(16, compoundItem.isBreakable() ? 1 : 0);
						insertCompoundItems.addBatch();
					}
					
					insertCompoundItems.executeBatch();
					
					
					insertPlayers = conn.prepareStatement("insert into players (actorID, roomID, inventoryID) values (?, ?, ?");
					
					for (Player player : players) {
						insertPlayers.setInt(1, player.getActorID());
						insertPlayers.setInt(2, player.getRoomID());
						insertPlayers.setInt(3, player.getInventoryID());
						insertPlayers.addBatch();
					}
					
					insertPlayers.executeBatch();
					
					
					insertRooms = conn.prepareStatement("insert into rooms (roomID, description, inventoryID) values (?, ?, ?");
					
					for (Room room : rooms) {
						insertRooms.setInt(1, room.getRoomID());
						insertRooms.setString(2, room.getDescription());
						insertRooms.setInt(3, room.getInventoryID());
						insertRooms.addBatch();
					}
					
					insertRooms.executeBatch();
					
					
					insertRoomObjects = conn.prepareStatement("insert into roomObjects (objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, roomID, inventoryID) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (RoomObject roomObject : roomObjects) {
						insertRoomObjects.setInt(1, roomObject.getObjectID());
						insertRoomObjects.setString(2, roomObject.getName());
						insertRoomObjects.setString(3, roomObject.getDescription());
						insertRoomObjects.setString(4, roomObject.getDirection());
						insertRoomObjects.setInt(5, roomObject.isObstacle() ? 1 : 0);
						insertRoomObjects.setInt(6, roomObject.isBlockingExit() ? 1 : 0);
						insertRoomObjects.setInt(7, roomObject.isMoveable() ? 1 : 0);
						insertRoomObjects.setString(8, roomObject.getCovering());
						insertRoomObjects.setInt(9, roomObject.isUnlockable() ? 1 : 0);
						insertRoomObjects.setInt(10, roomObject.isLocked() ? 1 : 0);
						insertRoomObjects.setInt(11, roomObject.isInteractable() ? 1 : 0);
						insertRoomObjects.setInt(12, roomObject.canHoldItems() ? 1 : 0);
						insertRoomObjects.setInt(13, roomObject.isCoverable() ? 1 : 0);
						insertRoomObjects.setInt(14, roomObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertRoomObjects.setInt(15, roomObject.getRoomID());
						insertRoomObjects.setInt(16, roomObject.getInventoryID());
						insertRoomObjects.addBatch();
					}
					
					insertRoomObjects.executeBatch();
					
					
					insertPlayableObjects = conn.prepareStatement("insert into playableObjects (objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, roomID, inventoryID, isInstrument, playedNotes, requiredNotes) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (PlayableObject playableObject : playableObjects) {
						insertPlayableObjects.setInt(1, playableObject.getObjectID());
						insertPlayableObjects.setString(2, playableObject.getName());
						insertPlayableObjects.setString(3, playableObject.getDescription());
						insertPlayableObjects.setString(4, playableObject.getDirection());
						insertPlayableObjects.setInt(5, playableObject.isObstacle() ? 1 : 0);
						insertPlayableObjects.setInt(6, playableObject.isBlockingExit() ? 1 : 0);
						insertPlayableObjects.setInt(7, playableObject.isMoveable() ? 1 : 0);
						insertPlayableObjects.setString(8, playableObject.getCovering());
						insertPlayableObjects.setInt(9, playableObject.isUnlockable() ? 1 : 0);
						insertPlayableObjects.setInt(10, playableObject.isLocked() ? 1 : 0);
						insertPlayableObjects.setInt(11, playableObject.isInteractable() ? 1 : 0);
						insertPlayableObjects.setInt(12, playableObject.canHoldItems() ? 1 : 0);
						insertPlayableObjects.setInt(13, playableObject.isCoverable() ? 1 : 0);
						insertPlayableObjects.setInt(14, playableObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertPlayableObjects.setInt(15, playableObject.getRoomID());
						insertPlayableObjects.setInt(16, playableObject.getInventoryID());
						insertPlayableObjects.setInt(17, playableObject.isInstrument() ? 1 : 0);
						insertPlayableObjects.setString(18, playableObject.getPlayedNotes());
						insertPlayableObjects.setString(19, String.valueOf(playableObject.getRequiredNotes()));
						insertPlayableObjects.addBatch();
					}
					
					insertPlayableObjects.executeBatch();
					
					
					insertUnlockableObjects = conn.prepareStatement("insert into unlockableObjects (objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, roomID, inventoryID, consumeItem, unlockItemID) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (UnlockableObject unlockableObject : unlockableObjects) {
						insertRoomObjects.setInt(1, unlockableObject.getObjectID());
						insertRoomObjects.setString(2, unlockableObject.getName());
						insertRoomObjects.setString(3, unlockableObject.getDescription());
						insertRoomObjects.setString(4, unlockableObject.getDirection());
						insertRoomObjects.setInt(5, unlockableObject.isObstacle() ? 1 : 0);
						insertRoomObjects.setInt(6, unlockableObject.isBlockingExit() ? 1 : 0);
						insertRoomObjects.setInt(7, unlockableObject.isMoveable() ? 1 : 0);
						insertRoomObjects.setString(8, unlockableObject.getCovering());
						insertRoomObjects.setInt(9, unlockableObject.isUnlockable() ? 1 : 0);
						insertRoomObjects.setInt(10, unlockableObject.isLocked() ? 1 : 0);
						insertRoomObjects.setInt(11, unlockableObject.isInteractable() ? 1 : 0);
						insertRoomObjects.setInt(12, unlockableObject.canHoldItems() ? 1 : 0);
						insertRoomObjects.setInt(13, unlockableObject.isCoverable() ? 1 : 0);
						insertRoomObjects.setInt(14, unlockableObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertRoomObjects.setInt(15, unlockableObject.getRoomID());
						insertRoomObjects.setInt(16, unlockableObject.getInventoryID());
						insertRoomObjects.setInt(17, unlockableObject.consumeItem() ? 1 : 0);
						insertRoomObjects.setInt(18, unlockableObject.getUnlockItemID());
						insertRoomObjects.addBatch();
					}
					
					insertRoomObjects.executeBatch();
					
					
					insertPuzzles = conn.prepareStatement("insert into puzzles (puzzleID, description, solution, hint, writtenSolution, unlockObstacle, solved, roomID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?");
					
					for (Puzzle puzzle : puzzles) {
						insertPuzzles.setInt(1, puzzle.getPuzzleID());
						insertPuzzles.setString(2, puzzle.getDescription());
						insertPuzzles.setString(3, puzzle.getSolution());
						insertPuzzles.setString(4, puzzle.getHint());
						insertPuzzles.setInt(5, puzzle.isWrittenSolution() ? 1 : 0);
						insertPuzzles.setString(6, puzzle.getUnlockObstacle());
						insertPuzzles.setInt(7, puzzle.isSolved() ? 1 : 0);
						insertPuzzles.setInt(8, puzzle.getRoomID());
						insertPuzzles.addBatch();
					}
					
					insertPuzzles.executeBatch();
					
					
					insertObjectPuzzles = conn.prepareStatement("insert into puzzles (puzzleID, description, solution, hint, solved, writtenSolution, unlockObstacle, roomID, objectID, itemID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
					
					for (ObjectPuzzle objectPuzzle : objectPuzzles) {
						insertObjectPuzzles.setInt(1, objectPuzzle.getPuzzleID());
						insertObjectPuzzles.setString(2, objectPuzzle.getDescription());
						insertObjectPuzzles.setString(3, objectPuzzle.getSolution());
						insertObjectPuzzles.setString(4, objectPuzzle.getHint());
						insertObjectPuzzles.setInt(5, objectPuzzle.isSolved() ? 1 : 0);
						insertObjectPuzzles.setInt(6, objectPuzzle.isWrittenSolution() ? 1 : 0);
						insertObjectPuzzles.setString(7, objectPuzzle.getUnlockObstacle());
						insertObjectPuzzles.setInt(8, objectPuzzle.getRoomID());
						insertObjectPuzzles.setInt(9, objectPuzzle.getObjectID());
						insertObjectPuzzles.setInt(10, objectPuzzle.getItemID());
						insertObjectPuzzles.addBatch();
					}
					
					insertObjectPuzzles.executeBatch();
					
					
					insertConnections = conn.prepareStatement("insert into connections (locationID, destinationID, direction) values (?, ?, ?)");
					
					for (Connections connection : connections) {
						insertConnections.setInt(1, connection.getConnectionID());
						insertConnections.setInt(2, connection.getDestinationID());
						insertConnections.setString(3, connection.getDirection());
						insertConnections.addBatch();
					}
					
					insertConnections.executeBatch();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertItems);
					DBUtil.closeQuietly(insertCompoundItems);
					DBUtil.closeQuietly(insertPlayers);
					DBUtil.closeQuietly(insertRooms);
					DBUtil.closeQuietly(insertRoomObjects);
					DBUtil.closeQuietly(insertPlayableObjects);
					DBUtil.closeQuietly(insertUnlockableObjects);
					DBUtil.closeQuietly(insertPuzzles);
					DBUtil.closeQuietly(insertObjectPuzzles);
					DBUtil.closeQuietly(insertConnections);
				}
			}
		});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Library DB successfully initialized!");
	}
}