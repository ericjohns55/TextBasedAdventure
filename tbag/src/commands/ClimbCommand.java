package commands;
import actor.Player;
import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.Puzzle;

public class ClimbCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();

		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		Player player = getPlayer();

		if (noun != null) {
			// So its looking for up ladder in the noun
			// It is going in and checking roomObjects instead of unlockableObjects I believe 
			if (room.hasObject(noun)) {
				RoomObject roomObject = room.getObject(noun);

				// it is not climbable if it is not unlockable
				// you need the key item (gloves in this case) to climb it
				if (roomObject instanceof UnlockableObject) {
					UnlockableObject object = (UnlockableObject) roomObject;		

					// check that it can be climbed
					if (object.canBeClimbed()) {
						// confirm needed item is there
						if (inventory.contains(puzzle.getSolution())) {	
							game.climbObject(object, player, puzzle, noun);	// redirect to Game and update database
						} else {	
							game.setOutput("You do not have what is needed to climb this " + room.getObject(noun).getName() + "!");
						}
					} else {									
						game.setOutput("That can't be climbed!");
					}	
				} else {
					game.setOutput("That can't be climbed!");
				}
			} else {
				game.setOutput("A " + noun + " doesn't exist in this room!");
			}	
		} else {
			game.setOutput("Not sure what you want me to climb...");
		}
	}
}