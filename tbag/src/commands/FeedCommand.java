package commands;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;
import map.RoomObject;
import puzzle.Puzzle;

public class FeedCommand extends UserCommand {
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
					
					// output = "You fed " + noun + " to the " + location + ".";
					
					boolean unlock = false;
					
					if (object.canBeFed() == true) 
					{
						if (inventory.contains(noun)) 
						{
							if (inventory.contains(puzzle.getSolution()))
							{	
								// Man it doesnt like this at all for like no reason, ok so strings are wusses,
								// it was trying to accesss the memory of the object instead of the contents 
								if (noun.equals(puzzle.getSolution()))
								{	
									// This transfers the object 
									Item toDrop = inventory.removeItem(noun);
									object.getInventory().addItem(noun, toDrop);
									game.setOutput("Fed " + noun + " to the " + location + ".");

									RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
									if (toUnlock.isLocked()) 
									{
										toUnlock.setLocked(false);
										game.setOutput("\nThe " + location + " is occupied away from an open " + toUnlock.getName() + " to the " + toUnlock.getDirection() + "!!");
									}
								}
								else 
								{
									game.setOutput("The " + location + " doesn't want to be fed that!");

								}
							}
							
							else 
							{
								game.setOutput("The " + location + " doesn't want to be fed that!");

							}
							
							
						} 
						
						else 
						{
							game.setOutput("You do not have that item!");

						}
					}
					
					if (unlock) 
					{
						RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
						
						if (toUnlock.isLocked()) 
						{
							toUnlock.setLocked(false);
					
								game.setOutput("\nA " + toUnlock.getName() + " to the " + toUnlock.getDirection() + " swings open.");

						}
					}
					
				} 
				
				else 
				{
					game.setOutput("You can't feed that!");

				}
				
			} 
			
			else 
			{
				game.setOutput("Could not find a " + location + ".");

			}

		} 
		
		else 
		{
			game.setOutput("Not sure what you want me to feed...");
		}
	}
}