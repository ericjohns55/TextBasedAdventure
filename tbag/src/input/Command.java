package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import actor.Player;
import game.Game;
import items.CompoundItem;
import items.Inventory;
import items.Item;
import map.PlayableObject;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import object.ObjectPuzzle;
import object.Puzzle;

public class Command {
	private static Set<String> SINGLE_WORD_COMMANDS = new HashSet<>(Arrays.asList("look", "examine", "hint"));
//	private static Set<String> LOCATION_COMMANDS = new HashSet<>(Arrays.asList("grab", "take", "place", "drop", "examine", "look", "push", "play"));
	private static Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList("on", "from", "to", "in"));
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
			} else {	// MUSIC EDGE CASE RIGHT HERE !!!
				if (word.toLowerCase().equals("a") && breakdown.get(0).equals("play")) {
					breakdown.add(word.toLowerCase());
				}
			}
		}
		
		if (breakdown.size() > 1) {
			verb = breakdown.get(0);
			
			// account for items with names multiple words long
			// also account for locations
//			if (LOCATION_COMMANDS.contains(verb)) {
//				the following block of code was in here for some reason, not quite sure why	
//			}
//			
			String fullNoun = "";
			String fullLocation = "";
			
			boolean addLocation = false;

			for (int i = 1; i < breakdown.size(); i++) {					
				if (PREPOSITIONS.contains(breakdown.get(i))) {
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
		
		System.out.println("VERB: " + verb + " | NOUN: " + noun + " | LOCATION: " + location);
		
		if (verb != null) {
			if (noun != null || SINGLE_WORD_COMMANDS.contains(verb)) {
				switch (verb) {
					case "examine":
					case "look":
						if (noun == null || noun == "" || noun.equals("room")) {
							output = room.getDescription();
						} else {
							if (room.hasItem(noun)) {
								output = room.getItem(noun).getDescription();
							} else if (inventory.contains(noun)) {
								output = inventory.getItem(noun).getDescription();
							} else if (room.hasObject(noun)) {
								RoomObject object = room.getObject(noun);
								
								if (object.isLocked()) {
									output = "This object is locked, I cannot see what is inside.";
								} else {
									output = room.getObject(noun).getDescription();
								}
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
							if (room.getObject(noun).isLocked()) {
								output = "This " + room.getObject(noun).getName() + " is locked.";
							} else {
								output = room.getObject(noun).getInventory().listItems(noun);
							}
						}
						
						break;
					case "list":
						if (noun == null || noun.equals("room")) {
							output = "This room has a " + room.listItems();
						} else if (noun.equals("inventory")) {
							output = inventory.openInventory();
						} else if (noun.equals("objects")) {
							output = room.listObjects();
						} else if (room.hasObject(noun)) {
							output = room.getObject(noun).getInventory().listItems();
						}
						
						break;
					case "grab":
					case "take":
						if (location == null || location.equals("room") || location.equals("floor")) {
							if (room.hasItem(noun)) {
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
									
									if (!roomObject.isLocked()) {
										if (objectInventory.contains(noun)) {
											Item toGrab = objectInventory.removeItem(noun);
											inventory.addItem(noun, toGrab);
											toGrab.setInInventory(true);
											
											output = "You picked up " + noun;
										} else {
											output = "This object does not have that item.";
										}
									} else {
										output = "This " + roomObject.getName() + " is locked.";
									}									
								} else {
									output = "This object does not possess any items.";
								}
							} else {
								output = "Could not find that object.";
							}
						}
						
						break;
					case "place":
					case "drop":
						if (inventory.contains(noun)) {
							if (location == null || location.equals("room") || location.equals("floor")) {
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
												RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());	
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
						if (room.getAllObjects().size() == 0) {
							if (room.hasExit(noun)) {
								output = moveRooms(player, room, noun);
							} else {
								output = "You cannot move this direction.";
							}
						} else {
							boolean foundObstacleInPath = false;
							
							for (RoomObject roomObject : room.getAllObjects().values()) {
								if (roomObject.getDirection().equals(noun)) {
									
									foundObstacleInPath = true;
									
									if (roomObject.isBlockingExit()) {
										if (roomObject instanceof UnlockableObject) {
											UnlockableObject door = (UnlockableObject) roomObject;
											
											if (door.isLocked()) {
												output = "This door appears to be locked... perhaps there is something in the room that can help you.";
											} else {
												output = moveRooms(player, room, noun);
											}
										} else {
											output = "A " + roomObject.getName() + " is blocking your path!";
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
						if (room.hasObject(noun)) {
							RoomObject roomObject = room.getObject(noun);
							
							if (roomObject.isUnlockable()) {
								if (roomObject.isLocked()) {
									if (roomObject instanceof UnlockableObject) {
										UnlockableObject unlockableObject = (UnlockableObject) roomObject;
										
										if (inventory.contains(unlockableObject.getUnlockItem())) {
											unlockableObject.setLocked(false);
											
											if (unlockableObject.consumeItem()) {
												inventory.removeItem(unlockableObject.getUnlockItem());
											}
											
											output = "You successfully unlocked the " + unlockableObject.getName() + ".";
										} else {
											output = "You do not have the required item to unlock this " + unlockableObject.getName() + ".";
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
									
									RoomObject obstacle = room.getObject(puzzle.getUnlockObstacle());
									
									if (obstacle.isLocked()) {
										obstacle.setLocked(false);
										output = "A " + obstacle.getName() + " to the " + obstacle.getDirection() + " swings open!";
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
						if (room.hasObject(noun)) {
							RoomObject object = room.getObject(noun);
							
							if (object.isMoveable()) {
								if (location != null) {
									object.setDirection(location);
									
									output = "Pushed " + object.getName() + " " + location;
								} else {
									object.setDirection(object.getDirection() + "-left");	// will eventually do better placements.
									
									output = "Moved " + object.getName() + " out of the way.";
								}
							} else {
								output = "Cannot push a " + object.getName();
							}
						} else {
							output = "Cannot find " + noun + " to move.";
						}
						
						break;
					case "play":
						if (location != null) {
							if (room.hasObject(location)) {
								if (room.getObject(location) instanceof PlayableObject) {
									PlayableObject object = (PlayableObject) room.getObject(location);
									
									output = "You played " + noun + " on the " + location;
									
									boolean unlock = false;
									
									if (object.isInstrument()) {
										if (object.playNote(noun)) {
											if (object.playedPassage()) {
												unlock = true;
											}
										} else {
											output = "You entered an invalid note.";
										}
									} else {
										if (inventory.contains(noun)) {
											Item toDrop = inventory.removeItem(noun);
											object.getInventory().addItem(noun, toDrop);
											
											output = "Played " + noun + " on the " + location;
											
											if (puzzle instanceof ObjectPuzzle) {
												ObjectPuzzle obstaclePuzzle = (ObjectPuzzle) puzzle;
												
												if (obstaclePuzzle.isSolved()) {
													unlock = true;
												}
											}
										} else {
											output = "You do not have that item!";
										}
									}
									
									if (unlock) {
										RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
										
										if (toUnlock.isLocked()) {
											toUnlock.setLocked(false);
											
											output += "\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open.";
										}
									}
								} else {
									output = "You cannot play anything on that!";
								}
							} else {
								output = "Could not find a " + location;
							}
						} else {
							output = "Not sure where you want me to play that...";
						}
						
						break;
					case "cut":
						if (location != null) {
							if (room.hasObject(location)) {
								RoomObject object = room.getObject(location);
								
								if (object.getInventory().contains(noun)) {
									if (object.getInventory().getItem(noun) instanceof CompoundItem) {
										CompoundItem item = (CompoundItem) object.getInventory().getItem(noun);
										
										if (item.isBreakable()) {
											if (inventory.contains(item.getBreakIdentifier())) {
												HashMap<String, Item> items = item.getItems();
												
												for (String identifier : items.keySet()) {
													object.getInventory().addItem(identifier, items.get(identifier));
													System.out.println("Adding " + identifier);
												}
												
												object.getInventory().removeItem(noun);
												item.getInventory().emptyInventory();
												
												output = "You break apart the " + noun + " and dumb the contents on the " + location;
											} else {
												output = "You do not possess the needed item to cut this.";
											}
										} else {
											output = "You cannot cut this item.";
										}
									} else {
										output = "Cannot cut this item";
									}
								} else {
									output = "That " + location + " does not countain a " + noun;
								}
							} else {
								output = "That location does not exist.";
							}
						} else {
							if (room.hasItem(noun)) {
								if (room.getItem(noun) instanceof CompoundItem) {
									CompoundItem item = (CompoundItem) room.getItem(noun);
									
									if (item.isBreakable()) {
										if (inventory.contains(item.getBreakIdentifier())) {
											HashMap<String, Item> items = item.getItems();
											
											for (String identifier : items.keySet()) {
												room.addItem(identifier, items.get(identifier));
												System.out.println("Adding " + identifier);
											}
											
											room.removeItem(noun);
											item.getInventory().emptyInventory();
											
											output = "You break apart the " + noun + " and dumb the contents on the floor.";
										} else {
											output = "You do not possess the needed item to cut this.";
										}
									} else {
										output = "You cannot cut this item.";
									}
								}
							} else {
								output = "The " + noun + " item does not exist.";
							}
						}
						
						break;
					case "hint":
						output = room.getPuzzle().getHint();
						break;
				}
			} else {
				output = "Missing argument for command \"" + verb + "\"";
			}
		}
		
		return output != null ? output.trim() : invalidCommand;
	}
	
	private String moveRooms(Player player, Room room, String direction) {
		int roomID = room.getExit(noun).getRoomID();
		player.setRoomID(roomID);
		
		String output = "You walk " + noun + "\n\n";
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
