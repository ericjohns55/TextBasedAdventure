package commands;

import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public class LookCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Game game = getGame();
		
		if (noun == null || noun == "" || noun.equals("room")) {
			game.setOutput(room.getDescription());	// print out room description
		}  else if (room.hasNpc() && noun.equals(room.getNpc().getName())) {
			game.setOutput(room.getNpc().getDescription());	/// print NPC description
		} else {
			if (room.hasItem(noun)) {
				game.setOutput(room.getItem(noun).getDescription());	// print out item description if it exists
			} else if (inventory.contains(noun)) {
				game.setOutput(inventory.getItem(noun).getDescription());	// print out item description if in inventory
			} else if (room.hasObject(noun)) {		// check if the room has said object
				RoomObject object = room.getObject(noun);
				
				if (object.isLocked()) {	// check if it is locked
					UnlockableObject painting = (UnlockableObject) object;	// cast to painting for some reason even though this is not always the case
					
					// It isnt registering this line right here, maybe it has something to do with the instance above
					if (!painting.getCanBeLookedAtNow())
					{	
						// So this checks if our unlockable object is an object where you need something to look at it with
						// and checking if you have what it needs in your inventory
						// Before it was just setting it to unlocked and you would have to examine again,
						// but now it sends it out immediately.
						
						if (inventory.contains(painting.getUnlockItem())) {	// make sure you have the item needed to look
							game.setOutput("This " + room.getObject(noun).getName() + " reads " + room.getObject(noun).getDescription() + ".");
						} else {	// fail the player if not
							game.setOutput("You do not have what is needed to see this object.");
						}
					} else {	
						game.setOutput("This object is locked, I cannot see what is inside.");
					}
				} else {
					game.setOutput(room.getObject(noun).getDescription());	// print out object description if not locked
				}
			} else {
				game.setOutput("That item doesn't exist!");
			}
		}
	}
}
