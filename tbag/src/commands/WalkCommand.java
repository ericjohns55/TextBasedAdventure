package commands;

import actor.Player;
import game.Game;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public class WalkCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		
		Player player = getPlayer();
		Room room = getRoom();
		Game game = getGame();
				
		if (room.getAllObjects().size() == 0) {	// check if there are any objects: none in this case
			if (room.hasExit(noun)) {	// if there is an exit there, move rooms and update DB
				game.moveRooms(player, noun);
			} else {
				game.setOutput("You cannot move this direction.");
			}
		} else {
			boolean foundObstacleInPath = false;	// check to see if there are objects blocking
			
			for (RoomObject roomObject : room.getAllObjects().values()) {	// loop through all room objects	
				if (roomObject.getDirection().equals(noun)) {	// check if object is blocking the path
					
					foundObstacleInPath = true;
					
					if (roomObject.isBlockingExit()) {	// check if the object is blocking the exit
						if (roomObject instanceof UnlockableObject) {	// check if unlockable object (stuff can be worked out here)
							UnlockableObject door = (UnlockableObject) roomObject;
							
							if (door.isLocked()) {	// if it is locked, block move
								game.setOutput("This door appears to be locked... perhaps there is something in the room that can help you.");
							} else {
								game.moveRooms(player, noun);	// door not locked, allow player to move
							}
						} else {
							game.setOutput("A " + roomObject.getName() + " is blocking your path!");
						}
					} else {
						game.moveRooms(player, noun);	// exit not blocked, move player
					}									
				}
			}
			
			if (!foundObstacleInPath) {	// obstacle is not in path, if exit is there let them leave
				if (room.hasExit(noun)) {
					game.moveRooms(player, noun);
				} else {
					game.setOutput("There is not an exit here.");
				}
			}
		}
	}
}