package commands;
import actor.Player;
import game.Game;
import items.Inventory;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;
import puzzle.Puzzle;

public class PopCommand extends UserCommand {
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
				
				if (roomObject instanceof UnlockableObject) {
					UnlockableObject object = (UnlockableObject) roomObject;		
			
					
					/*
					
					if (object.canBePopped()) {
							if (inventory.contains(puzzle.getSolution())) {	
								game.popObject(object, player, puzzle, noun);	
							} else {	
								game.setOutput("You do not have a sharp enough item to pop this " + room.getObject(noun).getName() + "!");
							}
					} else {									
						game.setOutput("That can't be popped!");
					}
					
					
					*/
					
					
					
					
					
					
				} else {
					game.setOutput("That can't be popped!");
				}
			} else {
				game.setOutput("A " + noun + " doesn't exist in this room!");
			}	
		} else {
			game.setOutput("Not sure what you want me to pop...");
		}
	}
}