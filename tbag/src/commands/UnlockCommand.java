package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public class UnlockCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (room.hasObject(noun)) {
			RoomObject roomObject = room.getObject(noun);
			
			if (roomObject.isUnlockable()) {
				if (roomObject.isLocked()) {
					if (roomObject instanceof UnlockableObject) {
						UnlockableObject unlockableObject = (UnlockableObject) roomObject;
						Item unlockItem = unlockableObject.getUnlockItem();
						
						if (inventory.contains(unlockItem)) {
							game.unlock(unlockableObject, unlockItem, getPlayer());
							game.setOutput("You successfully unlocked the " + unlockableObject.getName() + ".");
						} else {
							game.setOutput("You do not have the required item to unlock this " + unlockableObject.getName() + ".");
						}
					} else {
						game.setOutput("Not implemented.");
					}
				} else {
					game.setOutput("This is already unlocked.");
				}
			} else {
				game.setOutput("You cannot unlock that!");
			}
		} else {
			game.setOutput("This obstacle does not exist...");
		}
	}
}
