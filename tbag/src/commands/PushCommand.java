package commands;

import game.Game;
import map.Room;
import map.RoomObject;

public class PushCommand extends UserCommand {
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
					game.push(object, location);
					game.setOutput("Pushed " + object.getName() + " " + location);
				} else {					
					game.push(object, object.getDirection() + "-left");
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
