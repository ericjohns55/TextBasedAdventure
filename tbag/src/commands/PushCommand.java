package commands;

import game.Game;
import map.Room;
import map.RoomObject;

public class PushCommand extends UserCommand {
	public PushCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {
		String noun = getNoun();
		String location = getLocation();
		
		Room room = getRoom();
		Game game = getGame();
		
		if (room.hasObject(noun)) {
			RoomObject object = room.getObject(noun);
			
			if (object.isMoveable()) {
				if (location != null) {
					object.setDirection(location);
					
					game.setOutput("Pushed " + object.getName() + " " + location);
				} else {
					object.setDirection(object.getDirection() + "-left");	// will eventually do better placements.
					
					game.setOutput("Moved " + object.getName() + " out of the way.");
				}
			} else {
				game.setOutput("Cannot push a " + object.getName() + ".");
			}
		} else {
			game.setOutput("Cannot find " + noun + " to move.");
		}
	}
}
