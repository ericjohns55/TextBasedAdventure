package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import actor.Player;
import game.Game;
import items.Inventory;
import items.Item;
import obstacles.Door;
import obstacles.Obstacle;
import map.Room;
import object.Puzzle;
import object.RoomObject;

public class Command {
	private static Set<String> LOCATION_COMMANDS = new HashSet<>(Arrays.asList("grab", "take", "drop", "examine", "look", "push"));
	private static Set<String> ARTICLES = new HashSet<>(Arrays.asList("the", "a", "an"));
	
	public final static String invalidCommand = "I do not understand that command";
	private String input;
	private Game game;
	
	private String verb;
	private String noun;
	private String location;
	
	public Command(String input, Game game) {
		this.input = input;
		this.game = game;
		parseCommands();
	}
	
	private void parseCommands() {
		String[] individualWords = input.split(" ");
		
		ArrayList<String> breakdown = new ArrayList<String>();
		
		// REMOVE ARTICLES
		
		for (String word : individualWords) {
			if (!ARTICLES.contains(word.toLowerCase())) {
				breakdown.add(word.toLowerCase());
			} 
		}
		
		if (breakdown.size() > 1) {
			verb = breakdown.get(0);
			noun = breakdown.get(breakdown.size() - 1); 
			
			// account for items with names multiple words long
			// also account for locations
			if (LOCATION_COMMANDS.contains(verb)) {
				String fullNoun = "";
				String fullLocation = "";
				
				boolean addLocation = false;

				for (int i = 1; i < breakdown.size(); i++) {					
					if (breakdown.get(i).equals("on") || breakdown.get(i).equals("from") || breakdown.get(i).equals("to")) {
						System.out.println(breakdown.get(i));
						addLocation = true;
					} else {
						if (addLocation) {
							fullLocation += breakdown.get(i) + " ";
						} else {
							fullNoun += breakdown.get(i) + " ";
						}
					}
				}
				
				noun = fullNoun.trim();
				
				if (fullLocation != "") {
					location = fullLocation.trim();
				}
			}
		} else if (breakdown.size() == 1) {
			verb = breakdown.get(0);
		}
	}
	
	public String execute() {
		String output = null;
		
		Player player = game.getPlayer();
		Room room = player.getRoom();
		Inventory inventory = player.getInventory();
		Puzzle puzzle = room.getPuzzle();
		
		System.out.println("VERB: " + verb);
		System.out.println("NOUN: " + noun);
		System.out.println("LOCATION: " + location);
		
		if (verb != null) {
			if (noun != null || (verb.equals("look") || verb.equals("examine"))) {
				switch (verb) {
					case "examine":
					case "look":
						if (noun == null || noun == "" || noun.equals("room")) {
							output = room.getDescription();
						} else {
							if (room.contains(noun)) {
								output = room.getItem(noun).getDescription();
							} else if (inventory.contains(noun)) {
								output = inventory.getItem(noun).getDescription();
							} else if (room.hasObject(noun)) {
								output = room.getObject(noun).getDescription();
							} else if (room.hasObstacle(noun)) {
								output = room.getObstacle(noun).getDescription();
							}
							
							else {
								output = "That item doesn't exist!";
							}
						}
						
						break;
					case "open":
						if (noun.equals("inventory")) {
							output = inventory.openInventory();
						} else if (room.hasObject(noun)) {
							output = room.getObject(noun).getInventory().listItems(noun);
						}
						
						break;
					case "list":
						if (noun == null || noun.equals("room")) {
							output = "This room has a " + room.listItems();
						} else if (noun.equals("inventory")) {
							output = inventory.openInventory();
						} else if (noun.equals("objects")) {
							output = room.listObjects();
						}
						
						break;
					case "grab":
					case "take":
						if (location == null) {
							if (room.contains(noun)) {
								Item toGrab = room.getItem(noun);
								
								if (toGrab != null) {
									toGrab.setInInventory(true);
									inventory.addItem(noun, toGrab);
									room.removeItem(noun);
									
									output = "You picked up " + noun;
								} else {
									output = "This item does not exist in your current room.";
								}
							} else if (noun == null) {
								output = "Please specify an item.";
							} else {
								output = "This item does not exist in your current room.";
							}
						} else {
							if (room.hasObject(location)) {
								RoomObject roomObject = room.getObject(location);
								
								if (roomObject.canHoldItems()) {
									Inventory objectInventory = roomObject.getInventory();
									
									if (objectInventory.contains(noun)) {
										Item toGrab = objectInventory.removeItem(noun);
										inventory.addItem(noun, toGrab);
										toGrab.setInInventory(true);
										
										output = "You picked up " + noun;
									} else {
										output = "This object does not have that item.";
									}
								} else {
									output = "This object does not possess any items.";
								}
							} else {
								output = "Could not find that object.";
							}
						}
						
						break;
					case "drop":
						if (inventory.contains(noun)) {
							if (location == null) {
								Item removed = inventory.removeItem(noun);
								removed.setInInventory(false);
								room.addItem(noun, removed);
								output = "You dropped " + noun + " on the floor.";
							} else {
								if (room.hasObject(location)) {
									RoomObject roomObject = room.getObject(location);
									
									if (roomObject.canHoldItems()) {
										Inventory objectInventory = roomObject.getInventory();
										
										Item toRemove = inventory.removeItem(noun);
										objectInventory.addItem(noun, toRemove);
										output = "You placed the " + noun + " on the " + location + "."; 
										
										if (puzzle.getDescription().equals("weightPuzzle")) {
											double weightSolution = Double.parseDouble(puzzle.getSolution());
											
											if (objectInventory.getCurrentWeight() >= weightSolution) {
												// all objects thatll be unlockable through weight sensors will be named weightObstacle (and will typically unlock)
												Obstacle obstacle = room.getObstacle("weightObstacle");	
												if (obstacle.isLocked()) {
													obstacle.setLocked(false);
													output = "A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open.";
												}
											}
										}
									} else {
										output = "This object cannot hold that...";
									}
								} else {
									output = "Could not find that object.";
								}
							}
						} else if (noun == null) {
							output = "Please specify an item.";
						} else {
							output = "You do not possess this item.";
						}
						
						break;
					case "move":
					case "walk":
						if (room.getAllObstacles().size() == 0) {
							if (room.hasExit(noun)) {
								output = moveRooms(player, room, noun);
							} else {
								output = "You cannot move this direction.";
							}
						} else {
							boolean foundObstacleInPath = false;
							
							for (Obstacle obstacle : room.getAllObstacles().values()) {
								if (obstacle.getDirection().equals(noun)) {
									foundObstacleInPath = true;
									
									if (obstacle.isBlockingExit()) {
										if (obstacle instanceof Door) {
											Door door = (Door) obstacle;
											
											if (door.isLocked()) {
												output = "This door appears to be locked... perhaps there is something in the room that can help you.";
											} else {
												output = moveRooms(player, room, noun);
											}
										} else {
											output = "A " + obstacle.getName() + " is blocking your path!";
										}
									} else {
										output = moveRooms(player, room, noun);
									}									
								}
							}
							
							if (!foundObstacleInPath) {
								if (room.hasExit(noun)) {
									output = moveRooms(player, room, noun);
								} else {
									output = "There is not an exit here.";
								}
							}
						}
						
						break;
					case "unlock":
						if (room.hasObstacle(noun)) {
							Obstacle obstacle = room.getObstacle(noun);
							
							if (obstacle.isUnlockable()) {
								if (obstacle.isLocked()) {
									if (obstacle instanceof Door) {
										Door door = (Door) obstacle;
										
										if (inventory.contains(door.getUnlockItem())) {
											door.setLocked(false);
											output = "You successfully unlocked the door.";
										} else {
											output = "You do not have the required item to unlock this door.";
										}
									} else {
										output = "Not implemented";
									}
								} else {
									output = "This is already unlocked";
								}
							} else {
								output = "You cannot unlock that!";
							}
						} else {
							output = "This obstacle does not exist...";
						}
						
						break;
					case "solve":
					case "type":
						if (puzzle.isWrittenSolution()) {
							if (!puzzle.isSolved()) {
								if (puzzle.getSolution().equals(noun)) {
									puzzle.setSolved(true);
									
									Obstacle obstacle = room.getObstacle("writtenObstacle");
									
									if (obstacle.isLocked()) {
										obstacle.setLocked(false);
										output = "A door to the " + obstacle.getDirection() + " swings open!";
									}								
								} else {
									output = noun + " is not correct.";
								}
							} else {
								output = "You already solved this puzzle!";
							}						
						} else {
							output = "Could not find anything to solve...";
						}
						
						break;
					case "read":
						if (inventory.contains(noun)) {
							Item toRead = inventory.getItem(noun);
							
							if (toRead.isReadable()) {
								output = "This " + noun + " says \"" + toRead.getDescription() + "\"";
							} else {
								output = "Cannot read this item.";
							}
						} else {
							output = "Could not find a " + noun;
						}
						
						break;
					case "push":
						if (room.hasObstacle(noun)) {
							Obstacle obstacle = room.getObstacle(noun);
							
							if (obstacle.isMoveable()) {
								if (location != null) {
									obstacle.setDirection(location);
									
									output = "Pushed " + obstacle.getName() + " " + location;
								} else {
									obstacle.setDirection(obstacle.getDirection() + "-left");	// will eventually do better placements.
									
									output = "Moved " + obstacle.getName() + " out of the way.";
								}
							} else {
								output = "Cannot push a " + obstacle.getName();
							}
						} else {
							output = "Cannot find " + noun + " to move.";
						}
						
						break;
				}
			}
		}
		
		return output != null ? output : invalidCommand;
	}
	
	private String moveRooms(Player player, Room room, String direction) {
		int roomID = room.getExit(noun).getRoomID();
		player.setRoomID(roomID);
		
		String output = "You walk " + noun + "\n";
		output += player.getRoom().getDescription();
		
		return output;
	}
	
	public String getBreakdown() {
		String breakdown = "";
		
		if (verb != null) {
			breakdown += verb;
			
			if (noun != null) {
				breakdown += "/" + noun;
				
				if (location != null) {
					breakdown += "/" + location;
				}
			}
		}
		
		return breakdown;
	}
}
