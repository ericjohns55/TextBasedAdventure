package commands;

import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public class UnlockCommand extends UserCommand {
	public UnlockCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		
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
		
		return output;
	}

}
