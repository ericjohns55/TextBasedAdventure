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
		
		if (room.hasNpc()) {
			NPC npc = room.getNpc();
			if (!npc.isDone()) {
				if(npc.isTalkedTo()) {
					npc.setPreviousNode(npc.getCurrentNode());
					npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(1).getNextNode());
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {
						output = npc.getCurrentNode().getMessage() + "\n" + npc.getCurrentNode().getAvailableLinks().get(1).getOption() + " Y/N \n";
					}
					else {
						output = npc.getCurrentNode().getMessage();
						if(npc.getCurrentNode().isWrong()) {
							npc.setCurrentNode(npc.getPreviousNode());
							npc.setTalkedTo(false);
						}
						else {
							npc.setDone(true);
						}
					}
				}
				else {
					output = "You should talk to " + npc.getName() + ".";
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
