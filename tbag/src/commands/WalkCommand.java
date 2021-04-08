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
	public String getOutput() {
		String output = null;
		
		String noun = getNoun();
		
		Player player = getPlayer();
		Room room = getRoom();
				
		if (room.getAllObjects().size() == 0) {
			if (room.hasExit(noun)) {
				output = moveRooms(player, room, noun);
			} else {
				output = "You cannot move this direction.";
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
								output = "This door appears to be locked... perhaps there is something in the room that can help you.";
							} else {
								output = moveRooms(player, room, noun);
							}
						} else {
							output = "A " + roomObject.getName() + " is blocking your path!";
						}
					} else {
						output = moveRooms(player, room, noun);
					}									
				}
			}
			
			if (!foundObstacleInPath) {
				if (room.hasExit(noun)) {
					output = moveRooms(player, room, noun);
				} else {
					output = "There is not an exit here.";
				}
			}
		}
		
		return output;
	}
	
	private String moveRooms(Player player, Room room, String direction) {
		String noun = getNoun();
		
		int roomID = room.getExit(noun).getRoomID();
		player.setRoomID(roomID);
		
		String output = "You walk " + noun + "\n\n";
		output += player.getRoom().getDescription();
		
		return output;
	}
}
