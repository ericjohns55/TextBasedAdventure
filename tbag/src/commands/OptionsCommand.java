package commands;

import game.Game;
import map.Room;
import actor.NPC;
import dialogue.Link;

public class OptionsCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		Room room = getRoom();

		Game game = getGame();

		if (room.hasNpc()) {
			NPC npc = room.getNpc();

			if (npc.getCurrentNode().getType().equals("option")) {	// make sure the dialogue type is option
				if(npc.isTalkedTo()) { // can not run an option command without talking first
					if(npc.getCurrentNode().getType().equals("option")) {
						if(noun != null && noun.matches("[1-3.]+")) {	// confirm that a number 1-3 is specified as noun
							npc.setPreviousNode(npc.getCurrentNode());
							
							if(Integer.valueOf(noun) <= npc.getCurrentNode().getOptions().size()) {	// make sure number is in range
								npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(Integer.valueOf(noun) - 1).getNextNode());
								
								if (!npc.getCurrentNode().getAvailableLinks().isEmpty()) {	// make sure dialogue can continue
									String t = "";
									int i = 1;
									
									for(Link l : npc.getCurrentNode().getAvailableLinks()) {	// loop through links add output
										t += l.getOption();
										
										if (npc.getCurrentNode().getType().equals("option")) {
											t += " option " + i + "\n";
											i++;
										}
										else if (npc.getCurrentNode().getType().equals("y/n")) {
											t += "\n";
										}
										else if (npc.getCurrentNode().getType().equals("gCommand")) {
											game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());	// force give command if applicable
										}
									}
									
									// set output and update DB
									game.setOutput(npc.getCurrentNode().getMessage() + "\n" + t);
									game.npcDialogue(npc, npc.getCurrentNode());
								} else {
									npc.setDone(true);
									game.setOutput(npc.getCurrentNode().getMessage() + "\n");
									
									if (npc.getCurrentNode().getType().equals("gCommand")) {	// force run give command if on last node
										game.runCommand("give " + npc.getRequiredItem().getName() + " to " + npc.getName());
									}
									
									game.npcDialogue(npc, npc.getCurrentNode());
								}
							}
							else {
								game.setOutput("Select and available option.");
							}
						} 
						else {
							game.setOutput("Select and available option.");
						}
					}
					else {
						game.setOutput("I don't understand that command.");
					}
				}
				else {
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
