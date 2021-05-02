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
		String location = getLocation();
		
		Room room = getRoom();
		Inventory inventory = getInventory();
		Puzzle puzzle = getPuzzle();
		Game game = getGame();
		Player player = getPlayer();
		
		if (noun != null) 
		{
			   // So its looking for up ladder in the noun
			   // It is going in and checking roomObjects instead of unlockableObjects I believe 
			   if (room.hasObject(noun))
			   {
					UnlockableObject object = (UnlockableObject) room.getObject(noun);									
					if (object.canBeClimbed() == true) 
					{
							if (inventory.contains(puzzle.getSolution()))
							{	
								game.climbObject(object, player, puzzle, noun);	
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
					game.setOutput("A " + noun + " doesn't exist in this room!");
				}
		} 
	
		else 
		{
			game.setOutput("Not sure what you want me to climb...");
			
		}
	
	}
}