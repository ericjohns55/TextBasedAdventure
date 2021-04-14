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
import items.Inventory;
import items.Item;
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
					
					stmt.setString(1, room.getRoomID() + "");
					
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
	public List<Actor> findAllActors() {
		return executeTransaction(new Transaction<List<Actor>>() {
			@Override
			public List<Actor> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement("select actors.* from actors");	
					
					List<Actor> actors = new ArrayList<Actor>();
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;

						int actorID = resultSet.getInt(index++);
						int roomID = resultSet.getInt(index++);
						int inventoryID = resultSet.getInt(index++);
						
						Actor actor = new Actor(new Game(), roomID);
						actor.setActorID(actorID);
						actor.setRoomID(roomID);
						actor.setInventoryID(inventoryID);
						actor.setInventory(getInventoryByID(inventoryID));
						
						actors.add(actor);
					}
					
					if (!found) {
						System.out.println("Could not find any actors.");
					}
					
					return actors;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public Puzzle getPuzzle(Room room) {
		return executeTransaction(new Transaction<Puzzle>() {
			@Override
			public Puzzle execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select puzzles.*" +
								"from puzzles" +
									"where puzzle.roomID = ?"
					);
					
					stmt.setString(1, room.getRoomID() + "");
					
					resultSet = stmt.executeQuery();
					
					boolean found = false;
					
					Puzzle puzzle = null;
					
					while (resultSet.next()) {
						found = true;
						
						int index = 1;
						
						int puzzleID = resultSet.getInt(index++);
						String description = resultSet.getString(index++);
						String solution = resultSet.getString(index++);
						String hint = resultSet.getString(index++);
						boolean writtenSolution = resultSet.getInt(index++) == 1;
						String unlockObstacle = resultSet.getString(index++);
						boolean solved = resultSet.getInt(index++) == 1;
						int roomID = resultSet.getInt(index++);
						
						puzzle = new Puzzle(description, solution, hint, writtenSolution, unlockObstacle, roomID);
						puzzle.setPuzzleID(puzzleID);
						puzzle.setSolved(solved);
					}
					
					if (!found) {
						System.out.println("Could not find puzzle.");
					}		
					
					return puzzle;		
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void addItemToInventory(Inventory inventory, Item item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Item removeItemFromInventory(Inventory inventory, Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toggleLocks(UnlockableObject object, boolean locked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRooms(Player player, int roomID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushObject(RoomObject object, String direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakItem() {
		// TODO Auto-generated method stub
		
	}

}
