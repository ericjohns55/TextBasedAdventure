package cs320.windows98.tbag.input;

import java.util.ArrayList;

import cs320.windows98.tbag.actor.Player;
import cs320.windows98.tbag.game.Game;
import cs320.windows98.tbag.inventory.Inventory;
import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.obstacles.Door;
import cs320.windows98.tbag.obstacles.Obstacle;
import cs320.windows98.tbag.room.Room;

public class Command {
	public final static String invalidCommand = "I do not understand that command";
	private String input;
	private Game game;
	
	private String verb;
	private String noun;
	
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
			if (!word.equalsIgnoreCase("the") && !word.equalsIgnoreCase("a") && !word.equalsIgnoreCase("an")) {
				breakdown.add(word);
			}
		}
		
		if (breakdown.size() >= 1) {
			verb = breakdown.get(0);
			noun = breakdown.get(breakdown.size() - 1);
			
			// account for items with names multiple words long
			if (verb.equals("grab") || verb.equals("take") || verb.equals("drop") || verb.equals("examine") || verb.equals("look")) {
				String fullNoun = "";
				
				for (int i = 1; i < individualWords.length; i++) {
					fullNoun += individualWords[i] + " ";
				}
				
				noun = fullNoun.trim();
			}
		} else {
			noun = null;
		}
	}
	
	public String execute() {
		String output = null;
		
		Player player = game.getPlayer();
		Room room = player.getRoom();
		Inventory inventory = player.getInventory();
		
		System.out.println("VERB: " + verb);
		System.out.println("NOUN: " + noun);
		
		if (verb != null) {
			switch (verb) {
				case "examine":
				case "look":
					if (noun == "" || noun.equals("room")) {
						output = room.getDescription();
					} else {
						if (room.contains(noun)) {
							output = room.getItem(noun).getDescription();
						} else if (inventory.contains(noun)) {
							output = inventory.getItem(noun).getDescription();							
						} else {
							output = "That item doesn't exist!";
						}
					}
					
					break;
				case "open":
					if (noun.equals("inventory")) {
						output = inventory.openInventory();
					}
					
					break;
				case "grab":
				case "take":
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
					} else {
						output = "This item does not exist in your current room.";
					}
					
					break;
				case "drop":
					if (inventory.contains(noun)) {
						Item removed = inventory.removeItem(noun);
						removed.setInInventory(false);
						room.addItem(noun, removed);
						output = "You dropped " + noun + " on the floor.";
					} else {
						output = "You do not possess this item.";
					}
					
					break;
				case "move":
				case "walk":
					if (room.hasExit(noun)) {
						if (room.getAllObstacles().size() == 0) {
							output = moveRooms(player, room, noun);
						} else {
							for (Obstacle obstacle : room.getAllObstacles().values()) {
								if (obstacle.getDirection().equals(noun)) {
									if (obstacle.isBlockingExit()) {
										if (obstacle instanceof Door) {
											Door door = (Door) obstacle;
											
											if (door.isLocked()) {
												output = "This door appears to be locked... perhaps there is something in the room that can help you.";
											} else {
												output = moveRooms(player, room, noun);
											}
										} else {
											// IMPLEMENT OTHERS LATER
										}
									} else {
										output = moveRooms(player, room, noun);
									}									
								} else {
									output = moveRooms(player, room, noun);
								}
							}
						}
					} else {
						output = "You cannot move this direction.";
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
}
