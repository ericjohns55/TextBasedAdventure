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

			game.setOutput(room.getDescription());
		} 
		else if (room.hasNpc() && noun.equals(room.getNpc().getName())) {
			game.setOutput(room.getNpc().getDescription());
		} else {
			if (room.hasItem(noun)) {
				game.setOutput(room.getItem(noun).getDescription());
			} else if (inventory.contains(noun)) {
				game.setOutput(inventory.getItem(noun).getDescription());
			} else if (room.hasObject(noun)) {
				RoomObject object = room.getObject(noun);
				
				if (object.isLocked()) {
					UnlockableObject painting = (UnlockableObject) object;
					
					// It isnt registering this line right here, maybe it has something to do with the instance above
					if (painting.getCanBeLookedAtNow() == false)
					{	
						// So this checks if our unlockable object is an object where you need something to look at it with
						// and checking if you have what it needs in your inventory
						// Before it was just setting it to unlocked and you would have to examine again,
						// but now it sends it out immediately.
						
						if (inventory.contains(painting.getUnlockItem())) {
							game.setOutput("This " + room.getObject(noun).getName() + " reads " + room.getObject(noun).getDescription() + ".");
						} else {	
							game.setOutput("You do not have what is needed to see this object.");
						}
					} else {	
						game.setOutput("This object is locked, I cannot see what is inside.");
					}
				} else {
					game.setOutput(room.getObject(noun).getDescription());
				}
			} else {
				game.setOutput("That item doesn't exist!");
			}
		}
	}
}
