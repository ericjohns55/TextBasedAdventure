package commands;

import actor.NPC;
import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;

public class GiveCommand extends UserCommand {
	public GiveCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		NPC npc = getNpc();
		
		if (room.hasNpc()) {
			if (location.equals(npc.getName())) {
				if (inventory.contains(noun)) {
					Item toGive = inventory.getItem(noun);
				
					if (toGive != null) {
						toGive.setInInventory(false);
						npc.getInventory().addItem(noun, toGive);
						inventory.removeItem(noun);
					
						if (toGive == npc.getRequiredItem()) {
							RoomObject roomObject = npc.getUnlockObstacle();
							roomObject.setLocked(false);
							output = "Thanks for the " + noun + ". The " + roomObject.getName() + " is unlocked now.";
						}
						else {
							output = "You gave " + noun + " to " + npc.getName() + ".";
						}
					} else {
						output = "You don't have that item.";
					}
				}
				else if (noun == null) {
					output = "Please specify an item.";
				} else {
					output = "You don't have that item.";
				}
			}
			else {
				output = "There is no one named " + location + " in this room.";
			}
		}
		else {
			output = "There is no one else in the room with you.";
		}
		
		return output;
	}

}
