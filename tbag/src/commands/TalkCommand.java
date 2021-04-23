package commands;

import game.Game;
import map.Room;
import actor.NPC;

public class TalkCommand extends UserCommand {
	@Override
	public void execute() {
		Room room = getRoom();
		String location = getLocation();
		
		Game game = getGame();
		
		if (room.hasNpc()) {
			NPC npc = room.getNpc();
			if(location.equals(npc.getName())) {
				if(npc.CanTalkTo()) {
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {
						game.setOutput(npc.getCurrentNode().getMessage() + "\n" + npc.getCurrentNode().getAvailableLinks().get(0).getOption() + " Y/N \n");
						npc.setTalkedTo(true);
					}
					else {
						game.setOutput(npc.getCurrentNode().getMessage());
						npc.setTalkedTo(true);
					}
				}
				else {
					game.setOutput(npc.getName() + " doesn't want to talk.");
				}
			}
			else {
				game.setOutput("There is no one named " + location + " in this room.");
			}
		}
		else {
			game.setOutput("There is no one else in the room with you.");
		}
	}

}
