package commands;
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
		
		if (location != null) 
		{
			if (room.hasObject(location)) 
			{
				if (room.getObject(location) instanceof RoomObject) 
				{
					RoomObject object = (RoomObject) room.getObject(location);
					if (object.canScan() == true) 
					{
						if (inventory.contains(noun)) 
						{
							if (inventory.contains(puzzle.getSolution()))
							{	
								if (noun.equals(puzzle.getSolution()))
								{	
									game.setOutput("Scanned " + noun + " on the " + location + ".");

									RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
									toUnlock.setLocked(false);
									game.setOutput("\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open!");

								}								
								else 
								{
									game.setOutput("The " + location + " can't scan that!");

								}
							}
							else 
							{
								game.setOutput("The " + location + " can't scan that!");

							}
						} 
						else 
						{
							game.setOutput("You do not have that item!");

						}	
					}	
					else
					{
						game.setOutput("The " + location + " can't scan that!");

					}		
				} 		
				else 
				{
					game.setOutput("You can't scan that!");

				}		
			} 	
			else 
			{
				game.setOutput("Could not find a " + location + ".");

			}
		} 
		else 
		{
			game.setOutput("Not sure what you want me to scan...");

		}
	}
}
