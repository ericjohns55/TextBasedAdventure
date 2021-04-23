package commands;

import game.Game;
import map.Room;
import actor.NPC;
import dialogue.Link;

public class OptionsCommand extends UserCommand {
	public OptionsCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		Room room = getRoom();
		
		
		if (room.hasNpc()) {
			NPC npc = room.getNpc();
			if (!npc.isDone()) {
				if(npc.isTalkedTo()) {
					if(npc.getCurrentNode().getType().equals("option")) {
						if(noun != null && noun.matches("[0-9.]+")) {
							npc.setPreviousNode(npc.getCurrentNode());
							npc.setCurrentNode(npc.getCurrentNode().getAvailableLinks().get(Integer.valueOf(noun) - 1).getNextNode());
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
										t += " y/n \n";
									}
									else {
										
									}
								}
								output = t;							}
							else {
								output = npc.getCurrentNode().getMessage();
								if(npc.getCurrentNode().getType().equals("WC")) {
									npc.setCurrentNode(npc.getPreviousNode());
								}
								else if (npc.getCurrentNode().getType().equals("DE")) {
									npc.setCurrentNode(npc.getRootNode());
									npc.setTalkedTo(false);
								}
							}
						} 
						else {
							output = "Select and available option.";
						}
					}
					else {
						output = "I don't understand that command.";
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
