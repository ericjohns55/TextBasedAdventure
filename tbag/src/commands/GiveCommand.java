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
		
		if (room.hasNpc()) {	// check that there is an NPC
			if (location.equals(npc.getName())) {	// check that NPC was specified
				if (inventory.contains(noun)) {	// make sure the item to give exists
					Item toGive = inventory.getItem(noun);
				
					if (toGive != null) {	// make sure the item to give exists again apparently?
						if (toGive.equals(npc.getRequiredItem())) {	// confirm the NPC actually wants this item
							RoomObject roomObject = npc.getUnlockObstacle();
							roomObject.setLocked(false);
							game.setOutput("Thanks for the " + noun + ". The " + roomObject.getName() + " is unlocked now.");
							toGive.setCanBePickedUp(false);
							toGive.setInInventory(false);
							inventory.removeItem(noun);
							npc.getInventory().addItem(noun, toGive);
							npc.setCanTalkTo(false);
							game.give(npc, getPlayer(), toGive);	// update DB with NPC's new item
							game.unlockObject(roomObject, false);	// unlock the obstacle the NPC wants to solve for you
							game.npcDialogue(npc, npc.getCurrentNode());	// update the dialogue tree
						} else {
							game.setOutput(npc.getName() + " doesn't want that item.");
						}
					} else {
						game.setOutput("You don't have that item.");
					}
				} else if (noun == null) {	// noun not specified
					game.setOutput("Please specify an item.");
				} else {	// player doesnt have the item
					game.setOutput("You don't have that item.");
				}
			} else {
				game.setOutput("There is no one named " + location + " in this room.");
			}
		} else {
			game.setOutput("There is no one else in the room with you.");
		}
	}
}
