package commands;

import game.Game;
import map.Room;
import actor.NPC;

public class TalkCommand extends UserCommand {
	public TalkCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String location = getLocation();
		
		Room room = getRoom();
		
		if (room.hasNpc(location)) {
			NPC npc = room.getNpc(location);
			if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {
				output = npc.getCurrentNode().getMessage() + "\n" + npc.getCurrentNode().getAvailableLinks().get(0).getOption() + " (Y) \n" + npc.getCurrentNode().getAvailableLinks().get(1).getOption() + " (N) \n";
				npc.setTalkedTo(true);
			}
			else {
				output = npc.getCurrentNode().getMessage();
				npc.setTalkedTo(true);
			}
		}
		else {
			output = "There is no one else in the room with you.";
		}
		return output;
	}

}
