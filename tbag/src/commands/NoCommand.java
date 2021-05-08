package commands;

import game.Game;
import map.Room;
import actor.NPC;
import dialogue.Link;

public class NoCommand extends UserCommand {
	@Override
	public void execute() {		
		Room room = getRoom();
		
		Game game = getGame();
		
		if (room.hasNpc()) {	// check that NPC exists
			NPC npc = room.getNpc();
			
			if (!npc.isDone() && npc.getCurrentNode().getType().equals("y/n")) {	// check that NPC is not done talking
				if(npc.isTalkedTo()) {
					npc.setPreviousNode(npc.getCurrentNode());	// move through the dialogue tree
					npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(1).getNextNode());
					
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {	// make sure you can keep going
						String t = "";
						int i = 1;
						
						for(Link l : npc.getCurrentNode().getAvailableLinks()) {	// iterate over all links
							t += l.getOption();
							
							if (npc.getCurrentNode().getType().equals("option")) {	// print out the options 
								t += " option " + i + "\n";
								i++;
							}
							else if (npc.getCurrentNode().getType().equals("y/n")) {
								t += " \n";
							}
							else if (npc.getCurrentNode().getType().equals("gCommand")) {	// account for give command
								game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());
							}
						}
						
						game.setOutput(npc.getCurrentNode().getMessage() + "\n" + t);	// set output and update dialogue position
						game.npcDialogue(npc, npc.getCurrentNode());
					} else {	// on last node, must be give command
						game.setOutput(npc.getCurrentNode().getMessage() + "\n");
						
						if (npc.getCurrentNode().getType().equals("gCommand")) {
							game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());	// force run give command to NPC
						}
						
						game.npcDialogue(npc, npc.getCurrentNode());
					}
				} else {	// cannot run while not talking to NPC
					game.setOutput("You should talk to " + npc.getName() + ".");
				}
			}
			else {
				game.setOutput("I don't understand that command.");
			}
		}
		else {
			game.setOutput("I don't understand that command.");
		}
	}

}
