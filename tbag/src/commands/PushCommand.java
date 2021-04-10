package commands;

import game.Game;
import map.Room;
import map.RoomObject;

public class PushCommand extends UserCommand {
	public PushCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		
		if (room.hasObject(noun)) {
			RoomObject object = room.getObject(noun);
			
			if (object.isMoveable()) {
				if (location != null) {
					object.setDirection(location);
					
					output = "Pushed " + object.getName() + " " + location;
				} else {
					object.setDirection(object.getDirection() + "-left");	// will eventually do better placements.
					
					output = "Moved " + object.getName() + " out of the way.";
				}
			} else {
				output = "Cannot push a " + object.getName() + ".";
			}
		} else {
			output = "Cannot find " + noun + " to move.";
		}
		
		return output;
	}

}
