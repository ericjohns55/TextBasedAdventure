package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import actor.NPC;
import actor.Player;
import dialogue.Link;
import dialogue.Node;
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
	private int gameID;

	public DerbyDatabase(int gameID) {
		this.gameID = gameID;	// store game ID in instance for filling in later
	}
	
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
	public Room getRoom(int roomID) {
		return executeTransaction(new Transaction<Room>() {
			@Override
			public Room execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select rooms.* from rooms where rooms.roomID = ? and rooms.gameID = ?");
					stmt.setInt(1, roomID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					Room room = null;
					
					while (resultSet.next()) {	// grab room from DB with room ID
						int index = 1;
						
						resultSet.getInt(index++);	// CONSUME GAME ID
						int roomID = resultSet.getInt(index++);
						String description = resultSet.getString(index++);
						int inventoryID = resultSet.getInt(index++);
												
						room = new Room(description, roomID);
						room.setInventoryID(inventoryID);	// load inventory
						room.setInventory(getInventoryByID(inventoryID));
						System.out.println("Loaded room " + roomID);
						System.out.println("Created room inventory (" + inventoryID + ")");
					}
					
					if (room != null) {
						List<RoomObject> objects = findAllObjects(room);
						
						for (RoomObject object : objects) {	// loop through room objects and add them to room
							System.out.println("Added " + object.getName() + " with ID " + object.getObjectID());
							room.addObject(object.getName(), object);
						}
						
						room.setPuzzle(getPuzzle(room));	// load puzzle
						System.out.println("Added puzzle");
						
						room.setConnections(getAllConnections(room.getRoomID())); // load connections map
						System.out.println("Added connections");
						
						room.addNpc(getNpc(roomID));	// add NPC if needed
						System.out.println("Added npc");
					}
					
					return room;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// loads all player fields from DB (then returns it)
	
	@Override
	public Player getPlayer(int playerID) {
		return executeTransaction(new Transaction<Player>() {
			@Override
			public Player execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select players.* from players where players.playerID = ? and players.gameID = ?");
					stmt.setInt(1, playerID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					Player player = null;
					
					while (resultSet.next()) {
						int index = 1;
						
						int gameID = resultSet.getInt(index++);
						int playerID = resultSet.getInt(index++);
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						int moves = resultSet.getInt(index++);
						String lastOutput = resultSet.getString(index++);
						
						player = new Player(roomID);
						player.setGameID(gameID);
						player.setActorID(playerID);
						player.setMoves(moves);
						player.setLastOutput(lastOutput);
						player.setInventoryID(inventoryID);
						player.setInventory(getInventoryByID(inventoryID));
						System.out.println("Created player inventory");
					}
					
					return player;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// grabs item using its ID from the database
	
	@Override
	public Item getItemByID(int itemID) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select items.* " + 
								"from items " +
									"where items.itemID = ? and items.gameID = ?");	
					
					stmt.setInt(1, itemID);
					stmt.setInt(2, gameID);	// confirm pulling from the right game ID
					
					Item item = null;
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						resultSet.getInt(index++);	// CONSUME GAME ID
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
						if (!(itemID == 9999 || itemID == -1)) {
							System.out.println("Could not find any item with that ID (" + itemID + ").");
						}
					}
					
					return item;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// grab room object by ID
	// there is inheritence here in room objects, so we will have 3 queries (playable, regular, unlockable)
	
	@Override
	public RoomObject getRoomObjectByID(int objectID) {
		return executeTransaction(new Transaction<RoomObject>() {
			@Override
			public RoomObject execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomObjects.* " +
								"from roomObjects " + 
									"where roomObjects.objectID = ? and roomObjects.gameID = ?"
					);	
					
					stmt.setInt(1, objectID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {	// regular room object
						found = true;
						
						int index = 1;

						resultSet.getInt(index++);	// CONSUME GAME ID
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
						boolean canBeFed = resultSet.getInt(index++) == 1;
						String fed = resultSet.getString(index++);
						boolean canBeScanned = resultSet.getInt(index++) == 1;
						String scanned = resultSet.getString(index++);
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
						object.setCanBeFed(canBeFed);
						object.feed(fed);
						object.setCanBeScanned(canBeScanned);
						object.scanned(scanned);
						object.setInventoryID(inventoryID);
						object.setInventory(getInventoryByID(inventoryID));
						
						
						return object;
					}
					
					if (!found) {
						stmt2 = conn.prepareStatement(
								"select unlockableObjects.* " +
									"from unlockableObjects " + 
										"where unlockableObjects.objectID = ? and unlockableObjects.gameID = ?"
						);	
						
						stmt2.setInt(1, objectID);
						stmt2.setInt(2, gameID);
						
						resultSet2 = stmt2.executeQuery();
						
						while (resultSet2.next()) {	// unlockable object
							found = true;
							int index = 1;

							resultSet2.getInt(index++);	// CONSUME GAME ID
							int objectID = resultSet2.getInt(index++);
							String name = resultSet2.getString(index++);
							String description = resultSet2.getString(index++);
							String direction = resultSet2.getString(index++);
							boolean isObstacle = resultSet2.getInt(index++) == 1;
							boolean blockingExit = resultSet2.getInt(index++) == 1;
							boolean moveable = resultSet2.getInt(index++) == 1;
							String covered = resultSet2.getString(index++);
							boolean unlockable = resultSet2.getInt(index++) == 1;
							boolean locked = resultSet2.getInt(index++) == 1;
							boolean interactable = resultSet2.getInt(index++) == 1;
							boolean canHoldItems = resultSet2.getInt(index++) == 1;
							boolean coverable = resultSet2.getInt(index++) == 1;
							boolean previouslyUnlocked = resultSet2.getInt(index++) == 1;
							int roomID = resultSet2.getInt(index++);
							int inventoryID = resultSet2.getInt(index++);
							boolean consumeItem = resultSet2.getInt(index++) == 1;
							boolean canBeLookedAtNow = resultSet2.getInt(index++) == 1;
							String fed = resultSet2.getString(index++);
							boolean canBeFed = resultSet2.getInt(index++) == 1;
							boolean canBeClimbed = resultSet2.getInt(index++) == 1;
							int unlockItemID = resultSet2.getInt(index++);
							
							Item unlockItem = null;
							
							if (unlockItemID != -1) {
								unlockItem = getItemByID(unlockItemID);	// grab the unlock object from its ID, then load it into the object
							}
							
							UnlockableObject object = new UnlockableObject(name, description, direction, blockingExit, unlockItem, roomID);
							object.setObjectID(objectID);
							object.setObstacle(isObstacle);
							object.setMoveable(moveable);
							object.cover(covered);
							object.setUnlockable(unlockable);
							object.setLocked(locked);
							object.setInteractable(interactable);
							object.setCanHoldItems(canHoldItems);
							object.setCoverable(coverable);
							object.setPreviouslyUnlocked(previouslyUnlocked);
							object.setInventoryID(inventoryID);
							object.setInventory(getInventoryByID(inventoryID));
							object.setConsumeItem(consumeItem);
							object.feed(fed);
							object.setCanBeFed(canBeFed);
							object.setCanBeLookedAtNow(canBeLookedAtNow);
							object.setCanBeClimbed(canBeClimbed);
							
							return object;
						}
					}
					
					
					
					if (!found) {
						stmt3 = conn.prepareStatement(
								"select playableObjects.* " +
									"from playableObjects " + 
										"where playableObjects.objectID = ? and playableObjects.gameID = ?"
						);	
						
						stmt3.setInt(1, objectID);
						stmt3.setInt(2, gameID);
						
						resultSet3 = stmt3.executeQuery();
						
						while (resultSet3.next()) {	// playable objects
							found = true;
							int index = 1;

							resultSet3.getInt(index++);	// CONSUME GAME ID
							int objectID = resultSet3.getInt(index++);
							String name = resultSet3.getString(index++);
							String description = resultSet3.getString(index++);
							String direction = resultSet3.getString(index++);
							boolean isObstacle = resultSet3.getInt(index++) == 1;
							boolean blockingExit = resultSet3.getInt(index++) == 1;
							boolean moveable = resultSet3.getInt(index++) == 1;
							String covered = resultSet3.getString(index++);
							boolean unlockable = resultSet3.getInt(index++) == 1;
							boolean locked = resultSet3.getInt(index++) == 1;
							boolean interactable = resultSet3.getInt(index++) == 1;
							boolean canHoldItems = resultSet3.getInt(index++) == 1;
							boolean coverable = resultSet3.getInt(index++) == 1;
							boolean previouslyUnlocked = resultSet3.getInt(index++) == 1;
							int roomID = resultSet3.getInt(index++);
							int inventoryID = resultSet3.getInt(index++);
							boolean isInstrument = resultSet3.getInt(index++) == 1;
							String playedNotes = resultSet3.getString(index++);
							String requiredNotes = resultSet3.getString(index++);
							String fed = resultSet3.getString(index++);
							boolean canBeFed = resultSet3.getInt(index++) == 1;
							
														
							PlayableObject object = new PlayableObject(name, description, direction, requiredNotes, isInstrument, roomID);
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
							object.setObstacle(isObstacle);
							object.setBlockingExit(blockingExit);
							object.setMoveable(moveable);
							object.setPlayedNotes(playedNotes);
							object.feed(fed);
							object.setCanBeFed(canBeFed);
							
							return object;
						}
					}
					
					if (!found) {
						System.out.println("Could not find any room objects.");
					}
					
					return null;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(resultSet3);
				}
			}
		});
	}

	// basically return whether the login exists in the database or not
	
	@Override
	public boolean validateLogin(String username, String password) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select * " + 
								"from users " +
									"where users.username = ? and users.password = ?");	

					stmt.setString(1, username);
					stmt.setString(2, password);
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
					}
					
					if (!found) {
						System.out.println("Could not find user " + username);
					}
					
					return found;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
			
		});
	}

	// add a new user to the database using a username and password
	// autogenerates a game ID by nature of table
	
	@Override
	public Integer addUser(String username, String password) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("insert into users (username, password, playerID) values (?, ?, ?)");

					stmt.setString(1, username);
					stmt.setString(2, password);
					stmt.setInt(3, 0);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// grab a game ID from the users table assuming the username and password exists
	// return 0 if does not exist
	
	@Override
	public Integer getGameID(String username, String password) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select users.gameID from users where users.username = ? and users.password = ?");
					
					stmt.setString(1, username);
					stmt.setString(2, password);
					
					resultSet = stmt.executeQuery();
					
					int gameID = 0;
					
					while (resultSet.next()) {
						gameID = resultSet.getInt(1);
					}
					
					return gameID;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// grab an unlockable object by ID
	// same as get object by ID but just checks unlockable objects
	
	@Override
	public UnlockableObject getUnlockableObjectByID(int objectID) {
		return executeTransaction(new Transaction<UnlockableObject>() {
			@Override
			public UnlockableObject execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select unlockableObjects.* " +
								"from unlockableObjects " + 
									"where unlockableObjects.objectID = ? and unlockableObjects.gameID = ?"
					);	
					
					stmt.setInt(1, objectID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					UnlockableObject object = null;
					
					while (resultSet.next()) {
						int index = 1;

						resultSet.getInt(index++);	// CONSUME GAME ID
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
						boolean consumeItem = resultSet.getInt(index++) == 1;
						String fed = resultSet.getString(index++);
						boolean canBeFed = resultSet.getInt(index++) == 1;
						boolean canBeLookedAtNow = resultSet.getInt(index++) == 1;
						boolean canBeClimbed = resultSet.getInt(index++) == 1;
						int unlockItemID = resultSet.getInt(index++);
						
						Item unlockItem = null;
						
						if (unlockItemID != -1) {
							unlockItem = getItemByID(unlockItemID);	// grabs unlock item using the get item by id method earlier
						}
						
						object = new UnlockableObject(name, description, direction, blockingExit, unlockItem, roomID);
						object.setObjectID(objectID);
						object.setObstacle(isObstacle);
						object.setMoveable(moveable);
						object.cover(covered);
						object.setUnlockable(unlockable);
						object.setLocked(locked);
						object.setInteractable(interactable);
						object.setCanHoldItems(canHoldItems);
						object.setCoverable(coverable);
						object.setPreviouslyUnlocked(previouslyUnlocked);
						object.setInventoryID(inventoryID);
						object.setInventory(getInventoryByID(inventoryID));
						object.setConsumeItem(consumeItem);
						object.feed(fed);
						object.setCanBeFed(canBeFed);
						object.setCanBeLookedAtNow(canBeLookedAtNow);
						object.setCanBeClimbed(canBeClimbed);
					}
					
					return object;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// grabs the puzzle and all of its fields from the database
	
	@Override
	public Puzzle getPuzzle(Room room) {
		return executeTransaction(new Transaction<Puzzle>() {
			@Override
			public Puzzle execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				
				try {
					stmt = conn.prepareStatement("select puzzles.* " +
							"from puzzles " + 
								"where puzzles.roomID = ? and puzzles.gameID = ?"
					);
					
					stmt.setInt(1, room.getRoomID());
					stmt.setInt(2, gameID);
					
					Puzzle puzzle = null;
					
					resultSet = stmt.executeQuery();
					
					while (resultSet.next()) {
						int index = 1;

						resultSet.getInt(index++);	// CONSUME GAME ID
						int puzzleID = resultSet.getInt(index++);
						String description = resultSet.getString(index++);
						String solution = resultSet.getString(index++);
						String hint = resultSet.getString(index++);
						boolean writtenSolution = resultSet.getInt(index++) == 1;
						int unlockObstacle = resultSet.getInt(index++);	
						boolean solved = resultSet.getInt(index++) == 1;
						int roomID = resultSet.getInt(index++);
						
						RoomObject object = getUnlockableObjectByID(unlockObstacle);
						String unlockName = object != null ? object.getName() : null;

						puzzle = new Puzzle(description, solution, hint, writtenSolution, unlockName, roomID);
						puzzle.setSolved(solved);
						puzzle.setPuzzleID(puzzleID);
					}
					
					if (puzzle == null) {	// if a puzzle was not found, it must be an object puzzle; load all fields
						stmt2 = conn.prepareStatement("select objectPuzzles.* " +
								"from objectPuzzles " + 
									"where objectPuzzles.roomID = ? and objectPuzzles.gameID = ?"
						);
						
						stmt2.setInt(1, room.getRoomID());
						stmt2.setInt(2, gameID);
						
						resultSet2 = stmt2.executeQuery();
						
						while (resultSet2.next()) {							
							int index = 1;

							resultSet2.getInt(index++);	// CONSUME GAME ID
							int puzzleID = resultSet2.getInt(index++);
							String description = resultSet2.getString(index++);
							String solution = resultSet2.getString(index++);
							String hint = resultSet2.getString(index++);
							boolean solved = resultSet2.getInt(index++) == 1;
							boolean writtenSolution = resultSet2.getInt(index++) == 1;
							int unlockObstacle = resultSet2.getInt(index++);	
							int roomID = resultSet2.getInt(index++);
							int objectID = resultSet2.getInt(index++);
							int itemID = resultSet2.getInt(index++);
							
							RoomObject object = getUnlockableObjectByID(unlockObstacle);	// find the unlockable object from the ID
							String unlockName = object != null ? object.getName() : null;	// if this is not found then something went wrong (or not required)
							
							puzzle = new ObjectPuzzle(description, solution, hint, getRoomObjectByID(objectID), getItemByID(itemID), unlockName, roomID);
							puzzle.setSolved(solved);
							puzzle.setPuzzleID(puzzleID);
							puzzle.setWrittenSolution(writtenSolution);
						}
					}
					
					return puzzle;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(resultSet2);
				}
			}
		}); 
	}
	
	// generate the connections map for the room
	
	@Override
	public Connections getAllConnections(int roomID) {
		return executeTransaction(new Transaction<Connections>() {
			@Override
			public Connections execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select connections.* " +
							"from connections " +
								"where connections.locationID = ? and connections.gameID = ?");
					
					stmt.setInt(1, roomID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					Connections roomConnections = new Connections(roomID);
					
					while (resultSet.next()) {
						int index = 1;

						resultSet.getInt(index++);	// consume gameID
						resultSet.getInt(index++);	// consume locationID
						int destinationID = resultSet.getInt(index++);
						String direction = resultSet.getString(index++);
						
						roomConnections.addConnection(direction, destinationID);	// add connection to map for each room
					}
					
					return roomConnections;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}

	// generates an inventory using its ID
	// loop through all items and checks that the inventory and game ID match, then adds it to an inventory object
	// grabs both compound and regular items
	
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
							"select items.itemID " + 
								"from items " +
									"where items.inventoryID = ? and items.gameID = ?");	
					
					stmt.setInt(1, id);
					stmt.setInt(2, gameID);
					
					Inventory inventory = new Inventory();
					inventory.setInventoryID(id);
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int itemID = resultSet.getInt(index++);
						
						Item item = getItemByID(itemID);
						
						inventory.addItem(item.getName(), item);
					}
					
					
					stmt2 = conn.prepareStatement(
							"select compoundItems.* " + 
								"from compoundItems " +
									"where compoundItems.locationID = ? and compoundItems.gameID = ?");	
					
					stmt2.setInt(1, id);
					stmt2.setInt(2, gameID);
					
					resultSet2 = stmt2.executeQuery();
					
					while (resultSet2.next()) {	// compound items query
						int index = 1;
						
						found = true;

						resultSet2.getInt(index++);	// CONSUME GAME ID
						int itemID = resultSet2.getInt(index++);
						String name = resultSet2.getString(index++);
						String description = resultSet2.getString(index++);
						double weight = resultSet2.getDouble(index++);
						boolean isInteractable = resultSet2.getInt(index++) == 1;
						boolean canBePickedUp = resultSet2.getInt(index++) == 1;
						boolean consumeOnUse = resultSet2.getInt(index++) == 1;
						boolean inInventory = resultSet2.getInt(index++) == 1;
						boolean isEquipped = resultSet2.getInt(index++) == 1;
						boolean equippable = resultSet2.getInt(index++) == 1;
						boolean readable = resultSet2.getInt(index++) == 1;
						boolean pourable = resultSet2.getInt(index++) == 1;
						int locationID = resultSet2.getInt(index++);
						int inventoryID = resultSet2.getInt(index++);
						int breakID = resultSet2.getInt(index++);
						boolean breakable = resultSet2.getInt(index++) == 1;
						boolean poppable = resultSet2.getInt(index++) == 1;
						
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
						item.setBreakItem(breakItem);
						item.setInventory(getInventoryByID(inventoryID));
						item.setInventoryID(inventoryID);
						item.setPoppable(poppable);
						
						inventory.addItem(name, item);
					}

					if (!found) {
						System.out.println("Inventory empty or failed to populate (ID: " + id + ").");
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
	public List<RoomObject> findAllObjects(Room room) {	// get ALL room objects
		return executeTransaction(new Transaction<List<RoomObject>>() {
			@Override
			public List<RoomObject> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;	// 3 queries for each room object type
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				ResultSet resultSet3 = null;
				
				try {
					stmt = conn.prepareStatement(
							"select roomObjects.* " +
								"from roomObjects " + 
									"where roomObjects.roomID = ? and roomObjects.gameID = ?"
					);	
					
					stmt.setInt(1, room.getRoomID());
					stmt.setInt(2, gameID);
					
					List<RoomObject> roomObjects = new ArrayList<RoomObject>();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {	// regular room object
						found = true;
						
						int index = 1;

						resultSet.getInt(index++);	// CONSUME GAME ID
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
						boolean canBeFed = resultSet.getInt(index++) == 1;
						String fed = resultSet.getString(index++);
						boolean canBeScanned = resultSet.getInt(index++) == 1;
						String scanned = resultSet.getString(index++);
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
						object.setCanBeFed(canBeFed);
						object.feed(fed);
						object.setCanBeScanned(canBeScanned);
						object.scanned(scanned);
						object.setInventoryID(inventoryID);
						object.setInventory(getInventoryByID(inventoryID));
						
						roomObjects.add(object);
					}
					
					stmt2 = conn.prepareStatement(
							"select unlockableObjects.* " +
								"from unlockableObjects " + 
									"where unlockableObjects.roomID = ? and unlockableObjects.gameID = ?"
					);	
					
					stmt2.setInt(1, room.getRoomID());
					stmt2.setInt(2, gameID);
					
					resultSet2 = stmt2.executeQuery();
					
					while (resultSet2.next()) {	// unlockable object type
						int index = 1;
						
						found = true;

						resultSet2.getInt(index++);	// CONSUME GAME ID
						int objectID = resultSet2.getInt(index++);
						String name = resultSet2.getString(index++);
						String description = resultSet2.getString(index++);
						String direction = resultSet2.getString(index++);
						boolean isObstacle = resultSet2.getInt(index++) == 1;
						boolean blockingExit = resultSet2.getInt(index++) == 1;
						boolean moveable = resultSet2.getInt(index++) == 1;
						String covered = resultSet2.getString(index++);
						boolean unlockable = resultSet2.getInt(index++) == 1;
						boolean locked = resultSet2.getInt(index++) == 1;
						boolean interactable = resultSet2.getInt(index++) == 1;
						boolean canHoldItems = resultSet2.getInt(index++) == 1;
						boolean coverable = resultSet2.getInt(index++) == 1;
						boolean previouslyUnlocked = resultSet2.getInt(index++) == 1;
						int roomID = resultSet2.getInt(index++);
						int inventoryID = resultSet2.getInt(index++);
						boolean consumeItem = resultSet2.getInt(index++) == 1;
						String fed = resultSet2.getString(index++);
						boolean canBeFed = resultSet2.getInt(index++) == 1;
						boolean canBeLookedAtNow = resultSet2.getInt(index++) == 1;
						boolean canBeClimbed = resultSet2.getInt(index++) == 1;
						int unlockItemID = resultSet2.getInt(index++);
							
						Item unlockItem = null;
						
						if (unlockItemID != -1) {	// get item from unlock ID and load into room object
							unlockItem = getItemByID(unlockItemID);
						}
						
						UnlockableObject object = new UnlockableObject(name, description, direction, blockingExit, unlockItem, roomID);
						object.setObjectID(objectID);
						object.setObstacle(isObstacle);
						object.setMoveable(moveable);
						object.cover(covered);
						object.setUnlockable(unlockable);
						object.setLocked(locked);
						object.setInteractable(interactable);
						object.setCanHoldItems(canHoldItems);
						object.setCoverable(coverable);
						object.setPreviouslyUnlocked(previouslyUnlocked);
						object.setInventoryID(inventoryID);
						object.setInventory(getInventoryByID(inventoryID));
						object.setConsumeItem(consumeItem);
						object.feed(fed);
						object.setCanBeFed(canBeFed);
						object.setCanBeLookedAtNow(canBeLookedAtNow);
						object.setCanBeClimbed(canBeClimbed);
						object.setUnlockItemID(unlockItemID);
						
						
						
						
						roomObjects.add(object);
					}
					
					stmt3 = conn.prepareStatement(
							"select playableObjects.* " +
								"from playableObjects " + 
									"where playableObjects.roomID = ? and playableObjects.gameID = ?"
					);	
					
					stmt3.setInt(1, room.getRoomID());
					stmt3.setInt(2, gameID);
					
					resultSet3 = stmt3.executeQuery();
					
					while (resultSet3.next()) {	// playable objects
						int index = 1;
						
						found = true;

						resultSet3.getInt(index++);	// CONSUME GAME ID
						int objectID = resultSet3.getInt(index++);
						String name = resultSet3.getString(index++);
						String description = resultSet3.getString(index++);
						String direction = resultSet3.getString(index++);
						boolean isObstacle = resultSet3.getInt(index++) == 1;
						boolean blockingExit = resultSet3.getInt(index++) == 1;
						boolean moveable = resultSet3.getInt(index++) == 1;
						String covered = resultSet3.getString(index++);
						boolean unlockable = resultSet3.getInt(index++) == 1;
						boolean locked = resultSet3.getInt(index++) == 1;
						boolean interactable = resultSet3.getInt(index++) == 1;
						boolean canHoldItems = resultSet3.getInt(index++) == 1;
						boolean coverable = resultSet3.getInt(index++) == 1;
						boolean previouslyUnlocked = resultSet3.getInt(index++) == 1;
						int roomID = resultSet3.getInt(index++);
						int inventoryID = resultSet3.getInt(index++);
						boolean isInstrument = resultSet3.getInt(index++) == 1;
						String playedNotes = resultSet3.getString(index++);
						String requiredNotes = resultSet3.getString(index++);
						String fed = resultSet3.getString(index++);
						boolean canBeFed = resultSet3.getInt(index++) == 1;
						
						PlayableObject object = new PlayableObject(name, description, direction, requiredNotes, isInstrument, roomID);
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
						object.setObstacle(isObstacle);
						object.setBlockingExit(blockingExit);
						object.setMoveable(moveable);
						object.setPlayedNotes(playedNotes);
						object.feed(fed);
						object.setCanBeFed(canBeFed);
						
						roomObjects.add(object);
					}
					
					
					if (!found) {
						System.out.println("Could not find any room objects.");
					}
					
					return roomObjects;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(resultSet3);
				}
			}
		});
	}
	
	// grabs all players from players table
	// returns a list of all them
	// simple enough

	@Override
	public List<Player> getAllPlayers() {
		return executeTransaction(new Transaction<List<Player>>() {
			@Override
			public List<Player> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select players.* from players where players.playerID = ?");
					
					stmt.setInt(1, gameID);	
					
					List<Player> players = new ArrayList<Player>();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;

						int gameID = resultSet.getInt(index++);
						int actorID = resultSet.getInt(index++);
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						
						Player actor = new Player(new Game(gameID), roomID);
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
	
	// grab all links for the NPC
	
	@Override
	public List<Link> getAllLinks(int nodeID) {
		return executeTransaction(new Transaction<List<Link>>()  {
			@Override
			public List<Link> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select links.* " +
							"from links, nodes " +
								"where nodes.nodeID = ? and links.previousNodeID = nodes.nodeID and links.gameID = ? and links.gameID = nodes.gameID");
					
					stmt.setInt(1, nodeID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					List<Link> options = new ArrayList<Link>();
					
					while (resultSet.next()) {
						int index = 1;

						resultSet.getInt(index++);	// consume gameID
						int linkID = resultSet.getInt(index++);
						int nextNodeID = resultSet.getInt(index++);
						resultSet.getInt(index++);	// consume previous node ID (not sure why this isnt used - ask Josh)
						boolean isAvailable = resultSet.getInt(index++) == 1;
						String option = resultSet.getString(index++);
						
						Node nextNode = null;
						Node previousNode = null;
						Link link = new Link(linkID, nextNode, previousNode, isAvailable, option);
						link.setNextNodeID(nextNodeID);
						options.add(link);
					}
				
					return options;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// grab all nodes for the links 
	
	@Override
	public List<Node> getAllNodes(int npcID) {
		return executeTransaction(new Transaction<List<Node>>() {
			@Override
			public List<Node> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select nodes.* " +
								"from nodes, npcs " + 
									"where nodes.npcID = ? and npcs.gameID = ? and nodes.gameID = npcs.gameID"
					);	
					
					stmt.setInt(1, npcID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					List<Node> nodes = new ArrayList<Node>();
					
					while (resultSet.next()) {
						int index = 1;

						resultSet.getInt(index++);	// consume gameID
						int npcID = resultSet.getInt(index++);
						int nodeID = resultSet.getInt(index++);
						String message = resultSet.getString(index++);
						String type = resultSet.getString(index++);
						
						Node node = new Node(npcID, nodeID, message, type);
						
						if (node != null) {
							List<Link> options = getAllLinks(nodeID);
							
							for (Link link : options) {	// grab all the links and iterate
								link.setPreviousNode(node);	// load the nodes into the links in the correct spot
								node.addLink(link);
							}
						}
						nodes.add(node);
					}
					
					return nodes;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// generate the room NPC
	
	@Override
	public NPC getNpc(int roomID) {
		return executeTransaction(new Transaction<NPC>() {
			@Override
			public NPC execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select npcs.* " +
								"from npcs " + 
									"where npcs.roomID = ? and npcs.gameID = ?"
					);	
					
					stmt.setInt(1, roomID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					NPC npc = null;
					
					while (resultSet.next()) {
						int index = 1;
						
						resultSet.getInt(index++);
						int actorID = resultSet.getInt(index++);
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						String name = resultSet.getString(index++);
						String description = resultSet.getString(index++);
						int currentNodeID = resultSet.getInt(index++);
						int previousNodeID = resultSet.getInt(index++);
						int rootNodeID = resultSet.getInt(index++);
						boolean talkedTo = resultSet.getInt(index++) == 1;
						boolean canTalkTo = resultSet.getInt(index++) == 1;
						boolean done = resultSet.getInt(index++) == 1;
						int requiredItemID = resultSet.getInt(index++);
						int unlockObstacleID = resultSet.getInt(index++);
						Item requiredItem = null;
						
						if (requiredItemID != -1) {
							requiredItem = getItemByID(requiredItemID);	// grab required item
						}
						
						UnlockableObject unlockObstacle = null;
						
						if (unlockObstacleID != -1) {	// grab item that will be unlocked eventually
							unlockObstacle = getUnlockableObjectByID(unlockObstacleID);
						}
						
						npc = new NPC(null, roomID, name, description, requiredItem, unlockObstacle);
						npc.setActorID(actorID);
						npc.setInventoryID(inventoryID);
						npc.setInventory(getInventoryByID(inventoryID));
						npc.setTalkedTo(talkedTo);
						npc.setCanTalkTo(canTalkTo);
						npc.setDone(done);
						npc.setRequiredItemID(requiredItemID);
						npc.setUnlockObstacleID(unlockObstacleID);
						
						if (npc != null) {	// load the links and nodes simultaneously
							List<Node> nodes = getAllNodes(actorID);
							
							for (Node node : nodes) {
								if(node.getNodeID() == currentNodeID) {
									npc.setCurrentNode(node);
								}
								else if (node.getNodeID() == previousNodeID) {
									npc.setPreviousNode(node);
								}
								else if (node.getNodeID() == rootNodeID) {
									npc.setRootNode(node);
								}
								for (Link link : node.getOptions()) {
									link.setNextNode(nodes.get(link.getNextNodeID()));
								}
							}
							
						}
					}
					
					return npc;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// updates game status
	// moves and output
	
	@Override
	public Integer updateGameState(String output, int moves, Player player) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				
				try {
					stmt = conn.prepareStatement("update players set moves = ? where players.playerID = ? and players.gameID = ?");
					stmt.setInt(1, moves);
					stmt.setInt(2, player.getActorID());
					stmt.setInt(3, gameID);
					
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement("update players set lastOutput = ? where players.playerID = ? and players.gameID = ?");
					stmt2.setString(1, output);
					stmt2.setInt(2, player.getActorID());
					stmt2.setInt(3, gameID);
					
					return stmt2.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	// add an item to the specified inventory
	// updates inventory ID of said item in database

	@Override
	public Integer addItemToInventory(Inventory inventory, Item item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					if (item instanceof CompoundItem) {
						stmt = conn.prepareStatement("update compoundItems set locationID = ? where compoundItems.itemID = ? and compoundItems.gameID = ?");
					} else {
						stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ? and items.gameID = ?");
					}

					stmt.setInt(1, inventory.getInventoryID());
					stmt.setInt(2, item.getItemID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// remove an item from an inventory, by putting it into a new one
	
	@Override
	public Integer removeItemFromInventory(Inventory destinationInventory, Item item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ? and items.gameID = ?");

					stmt.setInt(1, destinationInventory.getInventoryID());
					stmt.setInt(2, item.getItemID());
					stmt.setInt(3, gameID);
					
					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// toggles the locks on an unlockable object
	// updates lock status and previously unlocked status

	@Override
	public Integer toggleLocks(UnlockableObject object, boolean locked) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				
				try {
					stmt = conn.prepareStatement("update unlockableObjects set locked = ? where unlockableObjects.objectID = ? and unlockableObjects.gameID = ?");

					stmt.setInt(1, locked ? 1 : 0);
					stmt.setInt(2, object.getObjectID());
					stmt.setInt(3, gameID);
					
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement("update unlockableObjects set previouslyUnlocked = ? where unlockableObjects.objectID = ? and unlockableObjects.gameID = ?");
					
					stmt2.setInt(1, !locked ? 1 : 0);
					stmt2.setInt(2, object.getObjectID());
					stmt2.setInt(3, gameID);

					return stmt2.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	// move the player into a new room
	// basically updates the player room ID so a new room is generated on next command

	@Override
	public Integer moveRooms(Player player, int roomID) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update players set roomID = ? where players.playerID = ? and players.gameID = ?");

					stmt.setInt(1, roomID);
					stmt.setInt(2, player.getActorID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// push object by updating its direction

	@Override
	public Integer pushObject(RoomObject object, String direction) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					if (object instanceof PlayableObject) {	// check the object types and replace the query with what is needed
						stmt = conn.prepareStatement("update playableObjects set direction = ? where playableObjects.objectID = ? and playableObjects.gameID = ?");
					} else if (object instanceof UnlockableObject) {
						stmt = conn.prepareStatement("update unlockableObjects set direction = ? where unlockableObjects.objectID = ? and unlockableObjects.gameID = ?");
					} else {
						stmt = conn.prepareStatement("update roomObjects set direction = ? where roomObjects.objectID = ? and roomObjects.gameID = ?");
					}

					stmt.setString(1, direction);
					stmt.setInt(2, object.getObjectID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// breaks an item and dumps its contents into the destination inventory
	
	@Override
	public Integer breakItem(CompoundItem compoundItem, Inventory destinationInventory) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.inventoryID = ? and items.itemID = ? and items.gameID = ?");

					for (Item item : compoundItem.getInventory().getAllItems().values()) {
						stmt.setInt(1, destinationInventory.getInventoryID());
						stmt.setInt(2, compoundItem.getInventoryID());
						stmt.setInt(3, item.getItemID());
						stmt.setInt(4, gameID);
						stmt.addBatch();
					}	// update all items in DB by looping through what is inside

					stmt.executeBatch();
					
					stmt2 = conn.prepareStatement("update compoundItems set locationID = ? where compoundItems.itemID = ? and compoundItems.gameID = ?");
					
					stmt2.setInt(1, -9999);
					stmt2.setInt(2, compoundItem.getItemID());	// nuke compound item basically
					stmt2.setInt(3, gameID);
					
					return stmt2.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// consume item by setting its inventory ID to be a number that will never be used
	
	@Override
	public Integer consumeItem(Item item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ? and items.gameID = ?");

					stmt.setInt(1, -9999);
					stmt.setInt(2, item.getItemID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// same thing as consume item, but it is a compound item instead 
	
	@Override
	public Integer destroyCompoundItem(CompoundItem item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update compoundItems set inventoryID = ? where compoundItems.itemID = ? and compoundItems.gameID = ?");

					stmt.setInt(1, -9999);
					stmt.setInt(2, item.getItemID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// same as consume item
	// this stuff should probably be combined but we are too close to the deadline

	@Override
	public Integer destroyItem(Item item) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update items set inventoryID = ? where items.itemID = ?");

					stmt.setInt(1, -9999);
					stmt.setInt(2, item.getItemID());

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// play notes on a playable object, then update the played notes in the table
	
	@Override
	public Integer playNotes(PlayableObject playableObject, String notes) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("update playableObjects set playedNotes = ? where playableObjects.objectID = ? and playableObjects.gameID = ?");

					stmt.setString(1, notes);
					stmt.setInt(2, playableObject.getObjectID());
					stmt.setInt(3, gameID);

					return stmt.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// get description from room ID
	
	@Override
	public String getDescription(int roomID) {
		return executeTransaction(new Transaction<String>() {
			@Override
			public String execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select rooms.description from rooms where rooms.roomID = ? and rooms.gameID = ?");
					
					stmt.setInt(1, roomID);
					stmt.setInt(2, gameID);
					
					resultSet = stmt.executeQuery();
					
					String description = "";
					
					while (resultSet.next()) {
						description = resultSet.getString(1);
					}
					
					return description;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(resultSet);
				}
			}
		});
	}
	
	// update the NPC node and talked to status when talked to
	
	public Integer npcDialogue(NPC npc, boolean talkedTo, int currentNodeID, boolean canTalkTo, boolean isDone) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				
				try {
					stmt = conn.prepareStatement("update npcs set talkedTo = ? where npcs.actorID = ? and npcs.gameID = ?");
					
					stmt.setInt(1, talkedTo ? 1 : 0);
					stmt.setInt(2, npc.getActorID());
					stmt.setInt(3, gameID);
					
					stmt.executeUpdate();
					
					stmt2 = conn.prepareStatement("update npcs set currentNodeID = ? where npcs.actorID = ? and npcs.gameID = ?");
					
					stmt2.setInt(1, currentNodeID);
					stmt2.setInt(2, npc.getActorID());
					stmt2.setInt(3, gameID);
					
					stmt2.executeUpdate();
					
					stmt3 = conn.prepareStatement("update npcs set canTalkTo = ? where npcs.actorID = ? and npcs.gameID = ?");
					
					stmt3.setInt(1, canTalkTo ? 1 : 0);
					stmt3.setInt(2, npc.getActorID());
					stmt3.setInt(3, gameID);
					
					stmt3.executeUpdate();
					
					stmt4 = conn.prepareStatement("update npcs set done = ? where npcs.actorID = ? and npcs.gameID = ?");
					
					stmt4.setInt(1, isDone ? 1 : 0);
					stmt4.setInt(2, npc.getActorID());
					stmt4.setInt(3, gameID);
					
					return stmt4.executeUpdate();
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

	// create tables for everything in the project
	
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmtCmpdItms = null;
				PreparedStatement stmtItms = null;	
				PreparedStatement stmtObjPuzls = null;	
				PreparedStatement stmtPlybleObjs = null;	
				PreparedStatement stmtPlyrs = null;	
				PreparedStatement stmtPuzls = null;	
				PreparedStatement stmtRmObjs = null;
				PreparedStatement stmtRms = null;
				PreparedStatement stmtUnlckbleObjs = null;	
				PreparedStatement stmtCnnctns = null;		
				PreparedStatement stmtNpcs = null;
				PreparedStatement stmtNodes = null;
				PreparedStatement stmtLinks = null;
				PreparedStatement stmtUsers = null;
			
				try {
					stmtCmpdItms = conn.prepareStatement(
						"create table compoundItems (" +
						"   gameID integer," + 
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
						"	breakable integer," +
						"	poppable integer" +
						")"
					);	
					stmtCmpdItms.executeUpdate();
					
					System.out.println("compoundItems table created");
					
					stmtItms = conn.prepareStatement(
						"create table items (" +
						"   gameID integer," + 
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
						"   gameID integer," + 
						"	puzzleID integer," +								
						"	description varchar(40)," +
						"	solution varchar(255)," +
						"	hint varchar(255)," +
						"	solved integer," +
						"	writtenSolution integer," +
						"	unlockObstacle integer," +
						"	roomID integer," +
						"	objectID integer," +
						"	itemID integer" +
						")"
					);	
					stmtObjPuzls.executeUpdate();
					
					System.out.println("objectPuzzles table created");	
					
					stmtPlybleObjs = conn.prepareStatement(
						"create table playableObjects (" +
						"   gameID integer," + 
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
						"	requiredNotes varchar(40)," +
						"	fed varchar(40)," +
						"	canBeFed integer" +
						")"
					);	
					stmtPlybleObjs.executeUpdate();
					
					System.out.println("playableObjects table created");						
								
					stmtPlyrs = conn.prepareStatement(
						"create table players (" +
						"   gameID integer," + 
						"	playerID integer," +
						"	roomID integer," +
						"	inventoryID integer," +
						"	moves integer," +
						"	lastOutput varchar(8000)" +
						")"
					);	
					stmtPlyrs.executeUpdate();
					
					System.out.println("players table created");	
					
					stmtUsers = conn.prepareStatement(
							"create table users (" +
									"	gameID integer primary key " +
									"		generated always as identity (start with 1, increment by 1), " +									
									"	username varchar(40)," +
									"	password varchar(40)," +
									"	playerID integer" +
									")"
					);	
					stmtUsers.executeUpdate();
					
					System.out.println("users table created");
					
					stmtPuzls = conn.prepareStatement(
						"create table puzzles (" +
						"   gameID integer," + 
						"	puzzleID integer," +								
						"	description varchar(40)," +
						"	solution varchar(255)," +
						"	hint varchar(255)," +
						"	writtenSolution integer," +
						"	unlockObstacle integer," +
						"	solved integer," +
						"	roomID integer" +
						")"
					);	
					stmtPuzls.executeUpdate();
					
					System.out.println("puzzles table created");
					
					stmtRmObjs = conn.prepareStatement(
						"create table roomObjects (" +
						"   gameID integer," + 
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
						"   canBeFed integer," +
						"	fed varchar(40)," +
						"   canBeScanned integer," +
						"	scanned varchar(40)," +
						"	roomID integer," +
						"	inventoryID integer" +
						")"
					);	
					stmtRmObjs.executeUpdate();
					
					System.out.println("roomObjects table created");	
					
					stmtRms = conn.prepareStatement(
						"create table rooms (" +
						"   gameID integer," + 
						"	roomID integer," +			
						"	description varchar(400)," +
						"	inventoryID integer" +
						")"
					);	
					stmtRms.executeUpdate();
					
					System.out.println("rooms table created");		
					
					stmtUnlckbleObjs = conn.prepareStatement(
						"create table unlockableObjects (" +
						"   gameID integer," + 
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
						"	fed varchar(40)," +
						"   canBeFed integer," +
						"	canBeLookedAtNow integer," +
						"	canBeClimbed integer," +
						"	unlockItemID integer" +		
						")"
					);	
					stmtUnlckbleObjs.executeUpdate();
					
					stmtCnnctns = conn.prepareStatement(
						"create table connections (" +
						"	gameID integer," +
						"	locationID integer," +
						"	destinationID integer," +
						"	direction varchar(40)" +
						")"
					);
					stmtCnnctns.executeUpdate();
					
					System.out.println("unlockableObjects table created");	
					
					stmtNpcs = conn.prepareStatement(
							"create table npcs (" +
							"	gameID integer," +
							"	actorID integer," +
							"	roomID integer," +
							"	inventoryID integer," +
							"	name varchar(40)," +
							"	description varchar(200)," +
							"	currentNodeID integer," +
							"	previousNodeID integer," +
							"	rootNodeID integer," +
							"	talkedTo integer," +
							"	canTalkTo integer," +
							"	done integer," +
							"	requiredItemID integer," +
							"	unlockObstacleID integer" +
							")"
						);	
					stmtNpcs.executeUpdate();
						
					System.out.println("npcs table created");
					
					stmtNodes = conn.prepareStatement(
							"create table nodes (" +
							"	gameID integer," +
							"	npcID integer," +
							"	nodeID integer," +
							"	message varchar(400)," +
							"	type varchar(40)" +
							")"
						);	
					stmtNodes.executeUpdate();
						
					System.out.println("nodes table created");
					
					stmtLinks = conn.prepareStatement(
							"create table links (" +
							"	linkID integer," +
							"	gameID integer," +
							"	nextNodeID integer," +
							"	previousNodeID integer," +
							"	isAvailable integer," +
							"	message varchar(200)" +
							")"
						);	
					stmtLinks.executeUpdate();
						
					System.out.println("links table created");
						
					return true;
				} finally {
					DBUtil.closeQuietly(stmtCmpdItms);
					DBUtil.closeQuietly(stmtItms);
					DBUtil.closeQuietly(stmtObjPuzls);
					DBUtil.closeQuietly(stmtPlybleObjs);
					DBUtil.closeQuietly(stmtPlyrs);
					DBUtil.closeQuietly(stmtPuzls);
					DBUtil.closeQuietly(stmtRmObjs);
					DBUtil.closeQuietly(stmtRms);
					DBUtil.closeQuietly(stmtUnlckbleObjs);	
					DBUtil.closeQuietly(stmtCnnctns);
					DBUtil.closeQuietly(stmtNpcs);
					DBUtil.closeQuietly(stmtNodes);
					DBUtil.closeQuietly(stmtLinks);
					DBUtil.closeQuietly(stmtUsers);
				}
			}
		});
	}
	
	// delete data for everything in the project
	
	@Override
	public void deleteData(int gameID) {	// KEEPS LOGIN IN FILE, MUST REGENERATE DEFAULT DATA TO USE; THIS SHOULD ONLY BE USED IN JUNITS
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement deleteItems = null;
				PreparedStatement deleteCompoundItems = null;
				PreparedStatement deletePlayers = null;
				PreparedStatement deleteRooms = null;
				PreparedStatement deleteRoomObjects = null;
				PreparedStatement deletePlayableObjects = null;
				PreparedStatement deleteUnlockableObjects = null;
				PreparedStatement deletePuzzles = null;
				PreparedStatement deleteObjectPuzzles = null;
				PreparedStatement deleteConnections = null;
				PreparedStatement deleteNodes = null;
				PreparedStatement deleteLinks = null;
				PreparedStatement deleteNPCs = null;
				
				try {
					deleteItems = conn.prepareStatement("delete from items where gameID = ?");
					deleteItems.setInt(1, gameID);
					deleteItems.executeUpdate();

					deleteCompoundItems = conn.prepareStatement("delete from compoundItems where gameID = ?");
					deleteCompoundItems.setInt(1, gameID);
					deleteCompoundItems.executeUpdate();

					deletePlayers = conn.prepareStatement("delete from players where gameID = ?");
					deletePlayers.setInt(1, gameID);
					deletePlayers.executeUpdate();

					deleteRooms = conn.prepareStatement("delete from rooms where gameID = ?");
					deleteRooms.setInt(1, gameID);
					deleteRooms.executeUpdate();

					deleteRoomObjects = conn.prepareStatement("delete from roomObjects where gameID = ?");
					deleteRoomObjects.setInt(1, gameID);
					deleteRoomObjects.executeUpdate();

					deletePlayableObjects = conn.prepareStatement("delete from playableObjects where gameID = ?");
					deletePlayableObjects.setInt(1, gameID);
					deletePlayableObjects.executeUpdate();

					deleteUnlockableObjects = conn.prepareStatement("delete from unlockableObjects where gameID = ?");
					deleteUnlockableObjects.setInt(1, gameID);
					deleteUnlockableObjects.executeUpdate();

					deletePuzzles = conn.prepareStatement("delete from puzzles where gameID = ?");
					deletePuzzles.setInt(1, gameID);
					deletePuzzles.executeUpdate();

					deleteObjectPuzzles = conn.prepareStatement("delete from objectPuzzles where gameID = ?");
					deleteObjectPuzzles.setInt(1, gameID);
					deleteObjectPuzzles.executeUpdate();

					deleteConnections = conn.prepareStatement("delete from connections where gameID = ?");
					deleteConnections.setInt(1, gameID);
					deleteConnections.executeUpdate();

					deleteNodes = conn.prepareStatement("delete from nodes where gameID = ?");
					deleteNodes.setInt(1, gameID);
					deleteNodes.executeUpdate();

					deleteLinks = conn.prepareStatement("delete from links where gameID = ?");
					deleteLinks.setInt(1, gameID);
					deleteLinks.executeUpdate();

					deleteNPCs = conn.prepareStatement("delete from npcs where gameID = ?");
					deleteNPCs.setInt(1, gameID);
					deleteNPCs.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(deleteItems);
					DBUtil.closeQuietly(deleteCompoundItems);
					DBUtil.closeQuietly(deletePlayers);
					DBUtil.closeQuietly(deleteRooms);
					DBUtil.closeQuietly(deleteRoomObjects);
					DBUtil.closeQuietly(deletePlayableObjects);
					DBUtil.closeQuietly(deleteUnlockableObjects);
					DBUtil.closeQuietly(deletePuzzles);
					DBUtil.closeQuietly(deleteObjectPuzzles);
					DBUtil.closeQuietly(deleteConnections);
					DBUtil.closeQuietly(deleteNodes);
					DBUtil.closeQuietly(deleteLinks);
					DBUtil.closeQuietly(deleteNPCs);
				}
			}
		});
	}
	
	// load data into the tables for a specified game ID (auto filled in using parameter)
	
	@Override
	public void loadInitialData(int gameID) {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Item> items;
				List<Room> rooms;
				List<UnlockableObject> unlockableObjects;
				List<CompoundItem> compoundItems;
				List<Player> players;
				List<RoomObject> roomObjects;
				List<PlayableObject> playableObjects;
				List<Puzzle> puzzles;
				List<ObjectPuzzle> objectPuzzles;
				List<Connections> connections;
				List<NPC> npcs;
				List<Node> nodes;
				List<Link> links;
				
				try {
					items = InitialData.getAllItems();
					rooms = InitialData.getAllRooms();
					unlockableObjects = InitialData.getAllUnlockableObjects();
					compoundItems = InitialData.getAllCompoundItems();
					players = InitialData.getAllPlayers();
					roomObjects = InitialData.getAllObjects();
					playableObjects = InitialData.getAllPlayableObjects();
					puzzles = InitialData.getAllPuzzles();
					objectPuzzles = InitialData.getAllObjectPuzzles();
					connections = InitialData.getAllConnections();
					nodes = InitialData.getAllNodes();
					npcs = InitialData.getAllNpcs();
					links = InitialData.getAllLinks();
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
				PreparedStatement insertNpcs = null;
				PreparedStatement insertNodes = null;
				PreparedStatement insertLinks = null;
				
				try {
					insertItems = conn.prepareStatement("insert into items (gameID, itemID, name, description, weight, isInteractable, canBePickedUp, consumeOnUse, inInventory, isEquipped, equippable, readable, pourable, inventoryID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (Item item : items) {
						insertItems.setInt(1, gameID);
						insertItems.setInt(2, item.getItemID());
						insertItems.setString(3, item.getName());
						insertItems.setString(4, item.getDescription());
						insertItems.setDouble(5, item.getWeight());
						insertItems.setInt(6, item.isInteractable() ? 1 : 0);
						insertItems.setInt(7, item.canBePickedUp() ? 1 : 0);
						insertItems.setInt(8, item.consumeOnUse() ? 1 : 0);
						insertItems.setInt(9, item.inInventory() ? 1 : 0);
						insertItems.setInt(10, item.isEquipped() ? 1 : 0);
						insertItems.setInt(11, item.isEquippable() ? 1 : 0);
						insertItems.setInt(12, item.isReadable() ? 1 : 0);
						insertItems.setInt(13, item.isPourable() ? 1 : 0);
						insertItems.setInt(14, item.getLocationID());
						insertItems.addBatch();
					}
					
					insertItems.executeBatch();
					
					
					insertCompoundItems = conn.prepareStatement("insert into compoundItems (gameID, itemID, name, description, weight, isInteractable, canBePickedUp, consumeOnUse, inInventory, isEquipped, equippable, readable, pourable, locationID, inventoryID, breakItemID, breakable, poppable) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");	
					
					for (CompoundItem compoundItem : compoundItems) {
						insertCompoundItems.setInt(1, gameID);
						insertCompoundItems.setInt(2, compoundItem.getItemID());
						insertCompoundItems.setString(3, compoundItem.getName());
						insertCompoundItems.setString(4, compoundItem.getDescription());
						insertCompoundItems.setDouble(5, compoundItem.getWeight());
						insertCompoundItems.setInt(6, compoundItem.isInteractable() ? 1 : 0);
						insertCompoundItems.setInt(7, compoundItem.canBePickedUp() ? 1 : 0);
						insertCompoundItems.setInt(8, compoundItem.consumeOnUse() ? 1 : 0);
						insertCompoundItems.setInt(9, compoundItem.inInventory() ? 1 : 0);
						insertCompoundItems.setInt(10, compoundItem.isEquipped() ? 1 : 0);
						insertCompoundItems.setInt(11, compoundItem.isEquippable() ? 1 : 0);
						insertCompoundItems.setInt(12, compoundItem.isReadable() ? 1 : 0);
						insertCompoundItems.setInt(13, compoundItem.isPourable() ? 1 : 0);
						insertCompoundItems.setInt(14, compoundItem.getLocationID());
						insertCompoundItems.setInt(15, compoundItem.getInventoryID());
						insertCompoundItems.setInt(16, compoundItem.getBreakItem().getItemID());
						insertCompoundItems.setInt(17, compoundItem.isBreakable() ? 1 : 0);
						insertCompoundItems.setInt(18, compoundItem.isPoppable() ? 1 : 0);
						insertCompoundItems.addBatch();
					}
					
					insertCompoundItems.executeBatch();
					
					
					insertPlayers = conn.prepareStatement("insert into players (gameID, playerID, roomID, inventoryID, moves, lastOutput) values (?, ?, ?, ?, ?, ?)");
					
					for (Player player : players) {
						insertPlayers.setInt(1, gameID);
						insertPlayers.setInt(2, player.getActorID());
						insertPlayers.setInt(3, player.getRoomID());
						insertPlayers.setInt(4, player.getInventoryID());
						insertPlayers.setInt(5, player.getMoves());
						insertPlayers.setString(6, player.getLastOutput());
						insertPlayers.addBatch();
					}
					
					insertPlayers.executeBatch();
					
					
					insertRooms = conn.prepareStatement("insert into rooms (gameID, roomID, description, inventoryID) values (?, ?, ?, ?)");
					
					for (Room room : rooms) {
						insertRooms.setInt(1, gameID);
						insertRooms.setInt(2, room.getRoomID());
						insertRooms.setString(3, room.getDescription());
						insertRooms.setInt(4, room.getInventoryID());
						insertRooms.addBatch();
					}
					
					insertRooms.executeBatch();
					
					
					insertRoomObjects = conn.prepareStatement("insert into roomObjects (gameID, objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, canBeFed, fed, canBeScanned, scanned, roomID, inventoryID) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (RoomObject roomObject : roomObjects) {
						insertRoomObjects.setInt(1, gameID);
						insertRoomObjects.setInt(2, roomObject.getObjectID());
						insertRoomObjects.setString(3, roomObject.getName());
						insertRoomObjects.setString(4, roomObject.getDescription());
						insertRoomObjects.setString(5, roomObject.getDirection());
						insertRoomObjects.setInt(6, roomObject.isObstacle() ? 1 : 0);
						insertRoomObjects.setInt(7, roomObject.isBlockingExit() ? 1 : 0);
						insertRoomObjects.setInt(8, roomObject.isMoveable() ? 1 : 0);
						insertRoomObjects.setString(9, roomObject.getCovering());
						insertRoomObjects.setInt(10, roomObject.isUnlockable() ? 1 : 0);
						insertRoomObjects.setInt(11, roomObject.isLocked() ? 1 : 0);
						insertRoomObjects.setInt(12, roomObject.isInteractable() ? 1 : 0);
						insertRoomObjects.setInt(13, roomObject.canHoldItems() ? 1 : 0);
						insertRoomObjects.setInt(14, roomObject.isCoverable() ? 1 : 0);
						insertRoomObjects.setInt(15, roomObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertRoomObjects.setInt(16, roomObject.canBeFed() ? 1 : 0);
						insertRoomObjects.setString(17, roomObject.getFed());
						insertRoomObjects.setInt(18, roomObject.canBeScanned() ? 1 : 0);
						insertRoomObjects.setString(19, roomObject.getScanned());
						insertRoomObjects.setInt(20, roomObject.getRoomID());
						insertRoomObjects.setInt(21, roomObject.getInventoryID());
						insertRoomObjects.addBatch();
					}
					
					insertRoomObjects.executeBatch();
					
					
					insertPlayableObjects = conn.prepareStatement("insert into playableObjects (gameID, objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, roomID, inventoryID, isInstrument, playedNotes, requiredNotes, fed, canBeFed) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (PlayableObject playableObject : playableObjects) {
						insertPlayableObjects.setInt(1, gameID);
						insertPlayableObjects.setInt(2, playableObject.getObjectID());
						insertPlayableObjects.setString(3, playableObject.getName());
						insertPlayableObjects.setString(4, playableObject.getDescription());
						insertPlayableObjects.setString(5, playableObject.getDirection());
						insertPlayableObjects.setInt(6, playableObject.isObstacle() ? 1 : 0);
						insertPlayableObjects.setInt(7, playableObject.isBlockingExit() ? 1 : 0);
						insertPlayableObjects.setInt(8, playableObject.isMoveable() ? 1 : 0);
						insertPlayableObjects.setString(9, playableObject.getCovering());
						insertPlayableObjects.setInt(10, playableObject.isUnlockable() ? 1 : 0);
						insertPlayableObjects.setInt(11, playableObject.isLocked() ? 1 : 0);
						insertPlayableObjects.setInt(12, playableObject.isInteractable() ? 1 : 0);
						insertPlayableObjects.setInt(13, playableObject.canHoldItems() ? 1 : 0);
						insertPlayableObjects.setInt(14, playableObject.isCoverable() ? 1 : 0);
						insertPlayableObjects.setInt(15, playableObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertPlayableObjects.setInt(16, playableObject.getRoomID());
						insertPlayableObjects.setInt(17, playableObject.getInventoryID());
						insertPlayableObjects.setInt(18, playableObject.isInstrument() ? 1 : 0);
						insertPlayableObjects.setString(19, playableObject.getPlayedNotes());
						insertPlayableObjects.setString(20, String.valueOf(playableObject.getRequiredNotes()));
						insertPlayableObjects.setString(21, playableObject.getFed());
						insertPlayableObjects.setInt(22, playableObject.canBeFed() ? 1 : 0);
						
						insertPlayableObjects.addBatch();
					}
					
					insertPlayableObjects.executeBatch();
					
					
					insertUnlockableObjects = conn.prepareStatement("insert into unlockableObjects (gameID, objectID, name, description, direction, isObstacle, blockingExit, moveable, covered, unlockable, locked, isInteractable, canHoldItems, coverable, previouslyUnlocked, roomID, inventoryID, consumeItem, fed, canBeFed, canBeLookedAtNow, canBeClimbed, unlockItemID) " + 
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (UnlockableObject unlockableObject : unlockableObjects) {
						insertUnlockableObjects.setInt(1, gameID);
						insertUnlockableObjects.setInt(2, unlockableObject.getObjectID());
						insertUnlockableObjects.setString(3, unlockableObject.getName());
						insertUnlockableObjects.setString(4, unlockableObject.getDescription());
						insertUnlockableObjects.setString(5, unlockableObject.getDirection());
						insertUnlockableObjects.setInt(6, unlockableObject.isObstacle() ? 1 : 0);
						insertUnlockableObjects.setInt(7, unlockableObject.isBlockingExit() ? 1 : 0);
						insertUnlockableObjects.setInt(8, unlockableObject.isMoveable() ? 1 : 0);
						insertUnlockableObjects.setString(9, unlockableObject.getCovering());
						insertUnlockableObjects.setInt(10, unlockableObject.isUnlockable() ? 1 : 0);
						insertUnlockableObjects.setInt(11, unlockableObject.isLocked() ? 1 : 0);
						insertUnlockableObjects.setInt(12, unlockableObject.isInteractable() ? 1 : 0);
						insertUnlockableObjects.setInt(13, unlockableObject.canHoldItems() ? 1 : 0);
						insertUnlockableObjects.setInt(14, unlockableObject.isCoverable() ? 1 : 0);
						insertUnlockableObjects.setInt(15, unlockableObject.wasPreviouslyUnlocked() ? 1 : 0);
						insertUnlockableObjects.setInt(16, unlockableObject.getRoomID());
						insertUnlockableObjects.setInt(17, unlockableObject.getInventoryID());
						insertUnlockableObjects.setInt(18, unlockableObject.consumeItem() ? 1 : 0);
						insertUnlockableObjects.setString(19, unlockableObject.getFed());
						insertUnlockableObjects.setInt(20, unlockableObject.canBeFed() ? 1 : 0);
						insertUnlockableObjects.setInt(21, unlockableObject.getCanBeLookedAtNow() ? 1 : 0);
						insertUnlockableObjects.setInt(22, unlockableObject.canBeClimbed() ? 1 : 0);
						insertUnlockableObjects.setInt(23, unlockableObject.getUnlockItemID());
						insertUnlockableObjects.addBatch();
					}
					
					insertUnlockableObjects.executeBatch();
					
					
					insertPuzzles = conn.prepareStatement("insert into puzzles (gameID, puzzleID, description, solution, hint, writtenSolution, unlockObstacle, solved, roomID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (Puzzle puzzle : puzzles) {
						insertPuzzles.setInt(1, gameID);
						insertPuzzles.setInt(2, puzzle.getPuzzleID());
						insertPuzzles.setString(3, puzzle.getDescription());
						insertPuzzles.setString(4, puzzle.getSolution());
						insertPuzzles.setString(5, puzzle.getHint());
						insertPuzzles.setInt(6, puzzle.isWrittenSolution() ? 1 : 0);
						insertPuzzles.setInt(7, puzzle.getUnlockObstacleID());
						insertPuzzles.setInt(8, puzzle.isSolved() ? 1 : 0);
						insertPuzzles.setInt(9, puzzle.getRoomID());
						insertPuzzles.addBatch();
					}
					
					insertPuzzles.executeBatch();
					
					
					insertObjectPuzzles = conn.prepareStatement("insert into objectPuzzles (gameID, puzzleID, description, solution, hint, solved, writtenSolution, unlockObstacle, roomID, objectID, itemID) " +
							"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (ObjectPuzzle objectPuzzle : objectPuzzles) {
						insertObjectPuzzles.setInt(1, gameID);
						insertObjectPuzzles.setInt(2, objectPuzzle.getPuzzleID());
						insertObjectPuzzles.setString(3, objectPuzzle.getDescription());
						insertObjectPuzzles.setString(4, objectPuzzle.getSolution());
						insertObjectPuzzles.setString(5, objectPuzzle.getHint());
						insertObjectPuzzles.setInt(6, objectPuzzle.isSolved() ? 1 : 0);
						insertObjectPuzzles.setInt(7, objectPuzzle.isWrittenSolution() ? 1 : 0);
						insertObjectPuzzles.setInt(8, objectPuzzle.getUnlockObstacleID());
						insertObjectPuzzles.setInt(9, objectPuzzle.getRoomID());
						insertObjectPuzzles.setInt(10, objectPuzzle.getObjectID());
						insertObjectPuzzles.setInt(11, objectPuzzle.getItemID());
						insertObjectPuzzles.addBatch();
					}
					
					insertObjectPuzzles.executeBatch();
					
					
					insertConnections = conn.prepareStatement("insert into connections (gameID, locationID, destinationID, direction) values (?, ?, ?, ?)");
					
					for (Connections connection : connections) {
						insertConnections.setInt(1, gameID);
						insertConnections.setInt(2, connection.getConnectionID());
						insertConnections.setInt(3, connection.getDestinationID());
						insertConnections.setString(4, connection.getDirection());
						insertConnections.addBatch();
					}
					
					insertConnections.executeBatch();
					
					insertNpcs = conn.prepareStatement("insert into npcs (gameID, actorID, roomID, inventoryID, name, description, currentNodeID, previousNodeID, rootNodeID, talkedTo, canTalkTo, done, requiredItemID, unlockObstacleID) "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
					for (NPC npc : npcs) {
						insertNpcs.setInt(1, gameID);
						insertNpcs.setInt(2, npc.getActorID());
						insertNpcs.setInt(3, npc.getRoomID());
						insertNpcs.setInt(4, npc.getInventoryID());
						insertNpcs.setString(5, npc.getName());
						insertNpcs.setString(6, npc.getDescription());
						insertNpcs.setInt(7, npc.getCurrentNode().getNodeID());
						insertNpcs.setInt(8, npc.getPreviousNode().getNodeID());
						insertNpcs.setInt(9, npc.getRootNode().getNodeID());
						insertNpcs.setInt(10, npc.isTalkedTo() ? 1 : 0);
						insertNpcs.setInt(11, npc.CanTalkTo() ? 1 : 0);
						insertNpcs.setInt(12, npc.isDone() ? 1 : 0);
						insertNpcs.setInt(13, npc.getRequiredItemID());
						insertNpcs.setInt(14, npc.getUnlockObstacleID());
						insertNpcs.addBatch();
					}
					
					insertNpcs.executeBatch();
					
					insertNodes = conn.prepareStatement("insert into nodes (gameID, npcID, nodeID, message, type) "
							+ "values (?, ?, ?, ?, ?)");
					
					for (Node node : nodes) {
						insertNodes.setInt(1, gameID);
						insertNodes.setInt(2, node.getNpcID());
						insertNodes.setInt(3, node.getNodeID());
						insertNodes.setString(4, node.getMessage());
						insertNodes.setString(5, node.getType());
						insertNodes.addBatch();
					}
					
					insertNodes.executeBatch();
					
					insertLinks = conn.prepareStatement("insert into links (gameID, linkID, nextNodeID, previousNodeID, isAvailable, message) "
							+ "values (?, ?, ?, ?, ?, ?)");
					
					for (Link link : links) {
						insertLinks.setInt(1, gameID);
						insertLinks.setInt(2, link.getLinkID());
						insertLinks.setInt(3, link.getNextNode().getNodeID());
						insertLinks.setInt(4, link.getPreviousNode().getNodeID());
						insertLinks.setInt(5, link.isAvailable() ? 1 : 0);
						insertLinks.setString(6, link.getOption());
						insertLinks.addBatch();
					}
					
					insertLinks.executeBatch();
					
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
					DBUtil.closeQuietly(insertNpcs);
					DBUtil.closeQuietly(insertNodes);
					DBUtil.closeQuietly(insertLinks);
				}
			}
		});
	}
	
	// create a test user by default everytime the project is run
	
	public Integer createTestUser() {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement insertTestUser = null;
				
				try {
					insertTestUser = conn.prepareStatement("insert into users (username, password, playerID) values (?, ?, ?)");
					insertTestUser.setString(1, "test");
					insertTestUser.setString(2, "test");
					insertTestUser.setInt(3, 0);
					insertTestUser.executeUpdate();
					
					System.out.println("Added test user");
					
					return 0;
				} finally {
					DBUtil.closeQuietly(insertTestUser);
				}
			}
		});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase(1);
		db.createTables();
		
		System.out.println("\nLoading initial data...");
		db.loadInitialData(1);
		db.createTestUser();	// add in one default user
		
		System.out.println("\nText Based Adventure Game DB successfully initialized!");
	}
}