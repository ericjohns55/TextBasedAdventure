package commands;
import actor.Player;
import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class ScanCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();

		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		Player player = getPlayer();

		if (location != null) {
			if (room.hasObject(location)) {
				if (room.getObject(location) instanceof RoomObject) {	// check that the object exists and is a room object
					// Its pretty ugly but just by going off of the feed command with setting the object 
					// to a similar thing is easier to implement
					RoomObject object = (RoomObject) room.getObject(location);

					if (object.canBeScanned()) {	// check that the object can scan other items
						if (inventory.contains(noun)) {
							if (inventory.contains(puzzle.getSolution())) {	// check that the inventory has the item needed to be scanned
								if (noun.equals(puzzle.getSolution())) {	
									if (!object.isScanned()) {	// if object has not scanned anything yet
										game.scanItem(object, player, puzzle, noun, location);	// scan item and update DB											
									} else {
										game.setOutput("No need to scan anything else here.");
									}
								} else {
									game.setOutput("The " + location + " can't scan that!");
								}
							} else {
								game.setOutput("The " + location + " can't scan that!");
							}
						} else {
							game.setOutput("You do not have that item!");

						}	
					} else {
						game.setOutput("The " + location + " doesn't scan items.");

					}		
				} else {
					game.setOutput("You can't scan that!");

				}		
			} else {
				game.setOutput("Could not find a " + location + ".");

			}
		} else {
			game.setOutput("Not sure where you want me to scan...");
		}
	}
}
