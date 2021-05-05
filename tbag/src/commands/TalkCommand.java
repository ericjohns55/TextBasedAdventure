package commands;

import game.Game;
import map.Room;
import actor.NPC;
import dialogue.Link;

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
						String t = "";
						int i = 1;
						for(Link l : npc.getCurrentNode().getAvailableLinks()) {
							t += l.getOption();
							if (npc.getCurrentNode().getType().equals("option")) {
								t += " option " + i + "\n";
								i++;
							}
							else if (npc.getCurrentNode().getType().equals("y/n")) {
								t += "\n";
							}
							else {
								
							}
						}
						game.setOutput(npc.getCurrentNode().getMessage() + "\n");
						game.addOutput(t);
						npc.setTalkedTo(true);
						if(npc.getPreviousNode() == null) {
							npc.setPreviousNode(npc.getRootNode());
						}
						System.out.println(npc + " " + npc.getCurrentNode()  + " " +  npc.getPreviousNode());
						game.npcDialogue(npc, npc.getCurrentNode());
					}
					else {
						game.setOutput(npc.getCurrentNode().getMessage());
						npc.setTalkedTo(true);
						game.npcDialogue(npc, npc.getCurrentNode());
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
