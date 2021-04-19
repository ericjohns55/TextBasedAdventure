package commands;

import game.Game;
import map.Room;
import actor.NPC;

public class NoCommand extends UserCommand {
	public NoCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		Room room = getRoom();
		
		if (room.hasNpc("bob")) {
			NPC npc = room.getNpc("bob");
			if (!npc.isDone()) {
				if(npc.isTalkedTo()) {
					npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(1).getNextNode());
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {
						output = npc.getCurrentNode().getMessage() + "\n" + npc.getCurrentNode().getAvailableLinks().get(0).getOption() + "(Y) \n" + npc.getCurrentNode().getAvailableLinks().get(1).getOption() + "(N) \n";
					}
					else {
						output = npc.getCurrentNode().getMessage();
						npc.setDone(true);
					}
				}
				else {
					output = "You should talk to " + npc.getName() + " first.";
				}
			}
			else {
				output = "I don't understand that command.";
			}
		}
		else {
			output = "I don't understand that command.";
		}
		return output;
	}

}
