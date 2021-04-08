package commands;

import game.Game;
import items.Inventory;
import items.Item;

public class ReadCommand extends UserCommand {
	public ReadCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output;
		
		String noun = getNoun();
		
		Inventory inventory = getInventory();
		
		if (inventory.contains(noun)) {
			Item toRead = inventory.getItem(noun);
			
			if (toRead.isReadable()) {
				output = "This " + noun + " says \"" + toRead.getDescription() + "\"";
			} else {
				output = "Cannot read this item.";
			}
		} else {
			output = "Could not find a " + noun + ".";
		}
		
		return output;
	}

}
