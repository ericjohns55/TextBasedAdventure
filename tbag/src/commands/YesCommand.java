package commands;

import game.Game;
import map.Room;
import actor.NPC;
import dialogue.Link;

public class YesCommand extends UserCommand {
	@Override
	public void execute() {		
		Room room = getRoom();
		
		Game game = getGame();
		
		if (room.hasNpc()) {
			NPC npc = room.getNpc();
			
			if (!npc.isDone() && npc.getCurrentNode().getType().equals("y/n")) {	// make sure NPC can still talk	
				if(npc.isTalkedTo()) {
					npc.setPreviousNode(npc.getCurrentNode());
					npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(0).getNextNode());
					
					if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {	// as long as links are not empty...
						String t = "";
						int i = 1;
						for(Link l : npc.getCurrentNode().getAvailableLinks()) {	// loop through all links and print out options
							t += l.getOption();
							if (npc.getCurrentNode().getType().equals("option")) {
								t += " option " + i + "\n";
								i++;
							} else if (npc.getCurrentNode().getType().equals("y/n")) {
								t += "\n";
							} else if (npc.getCurrentNode().getType().equals("gCommand")) {
								game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());
							}
						}
						
						game.setOutput(npc.getCurrentNode().getMessage() + "\n" + t);	// set output with new options
						game.npcDialogue(npc, npc.getCurrentNode());	// update dialogue tree in DB
					} else {
						game.setOutput(npc.getCurrentNode().getMessage() + "\n");
						
						if (npc.getCurrentNode().getType().equals("gCommand")) {	// force give command to run if necessary
							game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());
						}
						
						game.npcDialogue(npc, npc.getCurrentNode());	// update dialogue tree
					}
				} else {
					game.setOutput("You should talk to " + npc.getName() + ".");
				}
			} else {
				game.setOutput("I don't understand that command.");
			}
		} else {
			game.setOutput("I don't understand that command.");
		}
	}
}