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
				if(npc.CanTalkTo()) {	// check that there is an NPC that can be talked to
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {	// make sure that dialogue can continue
						String t = "";
						int i = 1;
						
						for(Link l : npc.getCurrentNode().getAvailableLinks()) {	// loop through links and add new options
							t += l.getOption();
							
							if (npc.getCurrentNode().getType().equals("option")) {
								t += " option " + i + "\n";
								i++;
							} else if (npc.getCurrentNode().getType().equals("y/n")) {
								t += "\n";
							}
						}
						
						game.setOutput(npc.getCurrentNode().getMessage() + "\n");	// print out new options
						game.addOutput(t);
						npc.setTalkedTo(true);
						
						if(npc.getPreviousNode() == null) {
							npc.setPreviousNode(npc.getRootNode());	// set original node to the beginning if it cannot find another one
						}
						
						game.npcDialogue(npc, npc.getCurrentNode());	// update DB
					} else {
						game.setOutput(npc.getCurrentNode().getMessage());
						npc.setTalkedTo(true);
						game.npcDialogue(npc, npc.getCurrentNode());	// update DB and set output to message
					}
				} else {
					game.setOutput(npc.getName() + " doesn't want to talk.");
				}
			} else {
				game.setOutput("There is no one named " + location + " in this room.");
			}
		} else {
			game.setOutput("There is no one else in the room with you.");
		}
	}
}