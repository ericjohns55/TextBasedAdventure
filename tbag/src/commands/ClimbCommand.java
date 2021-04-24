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
		Player player = getPlayer();
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		
	if (noun != null) 
	{
		// So its looking for up ladder in the noun
		if (room.hasObject(noun)) 
		{
			if (room.getObject(noun) instanceof RoomObject) 
			{
				UnlockableObject object = (UnlockableObject) room.getObject(noun);									
				if (object.canBeClimbed() == true) 
				{
						if (inventory.contains(puzzle.getSolution()))
						{	
								RoomObject toUnlock = room.getObject(puzzle.getUnlockObstacle());
								toUnlock.setLocked(false);
								game.setOutput("You climbed the " + room.getObject(noun).getDirection() + ".");
								game.moveRooms(player, noun);

						}											
						else 
						{	
							game.setOutput("You do not have what is needed to climb this " + room.getObject(noun).getName() + "!");

						}
				} 	
				else
				{									
					game.setOutput("That can't be climbed!");

				}	
					
			} 
			
			else 
			{
				game.setOutput("You can't climb that!");

			}
			
		} 
		
		else 
		{
			game.setOutput("Could not find that!");

		}
		
	} 
	
	else 
	{
		game.setOutput("Not sure what you want me to climb...");

	}
	}
}
