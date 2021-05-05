package commands;

import actor.NPC;
import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;

public class GiveCommand extends UserCommand {
	@Override
	public void execute() {		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		NPC npc = getNpc();
		
		Game game = getGame();
		
		if (room.hasNpc()) {
			if (location.equals(npc.getName())) {
				if (inventory.contains(noun)) {
					Item toGive = inventory.getItem(noun);
				
					if (toGive != null) {
						if (toGive.equals(npc.getRequiredItem())) {
							RoomObject roomObject = npc.getUnlockObstacle();
							roomObject.setLocked(false);
							game.setOutput("Thanks for the " + noun + ". The " + roomObject.getName() + " is unlocked now.");
							toGive.setCanBePickedUp(false);
							toGive.setInInventory(false);
							inventory.removeItem(noun);
							npc.getInventory().addItem(noun, toGive);
							npc.setCanTalkTo(false);
							game.give(npc, getPlayer(), toGive);
							game.unlockObject(roomObject, false);
							game.npcDialogue(npc, npc.getCurrentNode());
						
						}
						else {
							game.setOutput(npc.getName() + " doesn't want that item.");
						}
					} else {
						game.setOutput("You don't have that item.");
					}
				}
				else if (noun == null) {
					game.setOutput("Please specify an item.");
				} else {
					game.setOutput("You don't have that item.");
				}
			}
			else {
				game.setOutput("There is no one named " + location + " in this room.");
			}
		}
		else {
			game.setOutput("There is no one else in the room with you.");
		}
	}
}
