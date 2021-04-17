package commands;

import actor.Player;
import game.Game;
import map.Room;
import map.RoomObject;
import map.UnlockableObject;

public class WalkCommand extends UserCommand {
	public WalkCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {
		String noun = getNoun();
		
		Player player = getPlayer();
		Room room = getRoom();
		Game game = getGame();
				
		if (room.getAllObjects().size() == 0) {
			if (room.hasExit(noun)) {
				moveRooms(player, room, noun);
			} else {
				game.setOutput("You cannot move this direction.");
			}
		} else {
			boolean foundObstacleInPath = false;
			
			for (RoomObject roomObject : room.getAllObjects().values()) {
				if (roomObject.getDirection().equals(noun)) {
					
					foundObstacleInPath = true;
					
					if (roomObject.isBlockingExit()) {
						if (roomObject instanceof UnlockableObject) {
							UnlockableObject door = (UnlockableObject) roomObject;
							
							if (door.isLocked()) {
								game.setOutput("This door appears to be locked... perhaps there is something in the room that can help you.");
							} else {
								moveRooms(player, room, noun);
							}
						} else {
							game.setOutput("A " + roomObject.getName() + " is blocking your path!");
						}
					} else {
						moveRooms(player, room, noun);
					}									
				}
			}
			
			if (!foundObstacleInPath) {
				if (room.hasExit(noun)) {
					moveRooms(player, room, noun);
				} else {
					game.setOutput("There is not an exit here.");
				}
			}
		}
	}
	
	private String moveRooms(Player player, Room room, String direction) {
		String noun = getNoun();
		
		int roomID = room.getExit(noun).getRoomID();
		player.setRoomID(roomID);
		
		String output = "You walk " + noun + "\n\n";
		getGame().addOutput(player.getRoom().getDescription());
		
		return output;
	}
}
