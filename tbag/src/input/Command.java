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
import puzzle.ObjectPuzzle;
import puzzle.Puzzle;

public class Command {
	private static Set<String> SINGLE_WORD_COMMANDS = new HashSet<>(Arrays.asList("look", "examine", "hint"));
	private static Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList("on", "from", "to", "in"));
	private static Set<String> ARTICLES = new HashSet<>(Arrays.asList("the", "a", "an"));
	
	private static Set<String> VALID_COMMANDS = new HashSet<>(Arrays.asList("examine", "look", "open", "list", "grab", "take", "place",
			"drop", "move", "walk", "unlock", "type", "solve", "read", "push", "play", "cut", "pour", "hint", "feed", "scan", "climb"));
	
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
							
							
							
							if (room.getCanSee() == false)
							{
								//Item candle = room.getItem("candle");
								
								// If it just has the candle
								if (inventory.contains("candle"))
								{
									// If it has a candle and also the candle is lit
									if (inventory.getItem("candle").isLit() == true)
									{
								
										room.setCanSee(true);
										output = "You are in a bright room.";
									}
									// When you have a candle, but its not lit
									else
									{
										output = "This room is dark, you need to light something to see.";
									}
								}
								// If you dont have a candle but the room is dark
								else
								{
									output = "This room is dark, you need something to see.";
								}
							}
							
							else
							{	
								output = room.getDescription();
							}
							
							
						} else {
							if (room.hasItem(noun)) {
									
								
									output = room.getItem(noun).getDescription();

							} else if (inventory.contains(noun)) {
								output = inventory.getItem(noun).getDescription();
							} else if (room.hasObject(noun)) {
								RoomObject object = room.getObject(noun);
								
								if (object.isLocked()) {

									UnlockableObject painting = (UnlockableObject) object;
									
									if (painting.getCanBeLookedAtNow() == false)
									{	
										
										// So this checks if our unlockable object is an object where you need something to look at it with
										// and checking if you have what it needs in your inventory
										
										// Before it was just setting it to unlocked and you would have to examine again,
										// but now it sends it out immediately.
										if (inventory.contains(painting.getUnlockItem()))
										{
	
											output = "This " + room.getObject(noun).getName() + " reads " + room.getObject(noun).getDescription() + ".";
	
										}
										
										else
										{
				
											output = "You do not have what is needed to see this object.";
											
										}
									
									}									
									
									else
									{	
										output = "This object is locked, I cannot see what is inside.";
									}
									
								} 
								
								else 
								{	
									// It sends back a description but our description is the solution so we want 
									// but only some stuff has a description like this
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
							
							if (room.getCanSee() == false)
							{
								output = "This room is too dark to see items.";
							}
							
							if (room.listItems() == null)
							{
								output = "This room doesn't have any items!";
							}
							
							if (room.getCanSee() == true)
							{	
								output = "This room has a " + room.listItems();
							}
							
							
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
									
									output = "You picked up " + noun + ".";
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
											
											output = "You picked up " + noun + ".";
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
										
										// We will want to use the "cadaverPuzzle" in here
										
										
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
										output = "Not implemented.";
									}
								} else {
									output = "This is already unlocked.";
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
							output = "Could not find a " + noun + ".";
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
								output = "Cannot push a " + object.getName() + ".";
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
									
									output = "You played " + noun + " on the " + location + ".";
									
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
											
											output = "Played " + noun + " on the " + location + ".";
											
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
								output = "Could not find a " + location + ".";
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
												
												output = "You break apart the " + noun + " and dump the contents on the " + location + ".";
											} else {
												output = "You do not possess the needed item to cut this.";
											}
										} else {
											output = "You cannot cut this item.";
										}
									} else {
										output = "Cannot cut this item.";
									}
								} else {
									output = "That " + location + " does not countain a " + noun + ".";
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
					case "pour":
						if (location != null) {
							if (room.hasObject(location)) {
								if (inventory.contains(noun)) {
									RoomObject object = room.getObject(location);
									Item item = inventory.getItem(noun);
									
									if (item.isPourable()) {
										if (object.isCoverable()) {
											if (!object.isCovered()) {
												object.cover(noun);
												
												output = "You poured the " + noun + " on the " + location + ".";
												
												if (item.consumeOnUse()) {
													inventory.removeItem(noun);
												}
												
												if (puzzle.getSolution().equals(object.getCovering())) {
													RoomObject solutionObject = room.getObject(puzzle.getUnlockObstacle());
													
													if (solutionObject.isLocked()) {
														solutionObject.setLocked(false);
														
														if (room.getRoomID() == 8)
														{
															output += "\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open! Revealing a set of stairs going up!";

														}
														
														else
														{
															output += "\nA " + solutionObject.getName() + " to the " + solutionObject.getDirection() + " swings open!";

														}
													}
												}
											} else {
												output = "This object is already covered.";
											}
										} else {
											output = "Cannot pour " + noun + " on " + location + ".";
										}
									} else {
										output = "You cannot pour a " + noun + ".";
									}
								} else {
									output = "You do not possess a " + noun + ".";
								}
							} else {
								output = "A " + location + " does not exist in your room.";
							}
						} else {
							output = "I am not sure where to pour that.";
						}
						
						break;
					case "hint":
						output = room.getPuzzle().getHint();
						break;
						
					case "light":
						if (inventory.getItem(noun).isLightable()) 
						{

							// Set the lighter to "can light" and check if anything from the inventory  
							
							if (inventory.getItem(noun).isLit() == true)
							{
								inventory.getItem(noun).setLit(true);
								room.setCanSee(true);
								output = "This item is already lit.";
							}
							
							
							if (inventory.getItem(noun).isLit() == false && inventory.getItem(inventory.listItems()).producesFire() == true)
							{
								
								inventory.getItem(noun).setLit(true);
								room.setCanSee(true);
								output = "The " + noun + " is lit!";
							}
							
							if (inventory.getItem(noun).isLit() == false && inventory.contains("lighter"))
							{
								inventory.getItem(noun).setLit(true);
								room.setCanSee(true);
								
								output = "The " + noun + " is lit!";
							}
						
						} 
						
						else {
							if (inventory.contains(noun)) {
								output = "This " + inventory.getItem(noun).getName() + " can not be lit.";
							} else {
								output = "This " + noun + " is not in your inventory.";
							}
						}
						
							break;

							
						case "feed":
						// Okay so Im assuming location must take account for the preposition and articles happening
						// So it would be like "feed meat to the hellhound" for the commands 
						if (location != null) 
						{
							if (room.hasObject(location)) 
							{
								if (room.getObject(location) instanceof RoomObject) 
								{
									RoomObject object = (RoomObject) room.getObject(location);
									
									// output = "You fed " + noun + " to the " + location + ".";
									
									boolean unlock = false;
									
									if (object.canBeFed() == true) 
									{
										
										if (inventory.contains(noun)) 
										{

											
											if (inventory.contains(puzzle.getSolution()))
											{	
												// Man it doesnt like this at all for like no reason, ok so strings are wusses,
												// it was trying to accesss the memory of the object instead of the contents 
												if (noun.equals(puzzle.getSolution()))
												{	
													// This transfers the object 
													Item toDrop = inventory.removeItem(noun);
													object.getInventory().addItem(noun, toDrop);
												
													output = "Fed " + noun + " to the " + location + ".";
												
												
													RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
												
													if (toUnlock.isLocked()) 
													{
														toUnlock.setLocked(false);
											
														output += "\nThe " + location + " is occupied away from an open " + toUnlock.getName() + " to the " + toUnlock.getDirection() + "!!";
													
													}
												}
												
												else 
												{
													output ="The " + location + " doesn't want to be fed that!";
												}
												
											}
											
											else 
											{
												output = "The " + location + " doesn't want to be fed that!";
											}
											
											
										} 
										
										else 
										{
											output = "You do not have that item!";
										}
									}
									
									if (unlock) 
									{
										RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
										
										if (toUnlock.isLocked()) 
										{
											toUnlock.setLocked(false);
									
												output += "\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open.";
											
										}
									}
									
								} 
								
								else 
								{
									output = "You can't feed that!";
								}
								
							} 
							
							else 
							{
								output = "Could not find a " + location + ".";
							}
							
						} 
						
						else 
						{
							output = "Not sure what you want me to feed...";
						}
						break;

						
						// Okay so Im assuming location must take account for the preposition and articles happening
						// So it would be like "feed meat to the hellhound" for the commands 
						case "scan":
						if (location != null) 
						{
							if (room.hasObject(location)) 
							{
								if (room.getObject(location) instanceof RoomObject) 
								{
									RoomObject object = (RoomObject) room.getObject(location);
									
									if (object.canScan() == true) 
									{
										
										if (inventory.contains(noun)) 
										{

											
											if (inventory.contains(puzzle.getSolution()))
											{	

												if (noun.equals(puzzle.getSolution()))
												{	

													output = "Scanned " + noun + " on the " + location + ".";
												
												
													RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
												
											
													toUnlock.setLocked(false);
											
													output += "\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open!";

													
												}
												
												else 
												{
													output ="The " + location + " can't scan that!";
												}
												
											}
											
											else 
											{
												output ="The " + location + " can't scan that!";
											}
											
											
										} 
										
										else 
										{
											output = "You do not have that item!";
										}
										
									}
									
									else
									{
										output ="The " + location + " can't scan that!";
									}
								
									
								} 
								
								else 
								{
									output = "You can't scan that!";
								}
								
							} 
							
							else 
							{
								output = "Could not find a " + location + ".";
							}
							
						} 
						
						else 
						{
							output = "Not sure what you want me to scan...";
						}
						
						break;
						
						
						
						case "climb":
						if (noun != null) 
						{
							// So its looking for up ladder in the noun
							if (room.hasObject(noun)) 
							{
								if (room.getObject(noun) instanceof RoomObject) 
								{
									UnlockableObject object = (UnlockableObject) room.getObject(noun);									
									if (object.canBeClimbed() == true) 
									{
											if (inventory.contains(puzzle.getSolution()))
											{	
													RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
													toUnlock.setLocked(false);
													output = "You climbed the " + room.getObject(noun).getDirection() + ".";
													output += moveRooms(player, room, noun);
											}											
											else 
											{	
												output = "You do not have what is needed to climb this " + room.getObject(noun).getName() + "!";				
											}
									} 	
									else
									{									
										output ="That can't be climbed!";									
									}	
										
								} 
								
								else 
								{
									output = "You can't climb that!";
								}
								
							} 
							
							else 
							{
								output = "Could not find that!";
							}
							
						} 
						
						else 
						{
							output = "Not sure what you want me to climb...";
						}
						
						break;
	
				}
			} else {
				if (VALID_COMMANDS.contains(verb)) {
					output = "Missing argument for command \"" + verb + "\"";
				} else {
					output = "Unknown command.";
				}
			}
		} else {
			output = "Unknown command.";
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
