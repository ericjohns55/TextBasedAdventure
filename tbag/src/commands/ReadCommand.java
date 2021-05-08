package commands;

import game.Game;
import items.Inventory;
import items.Item;

public class ReadCommand extends UserCommand {
	@Override
	public void execute() {
		String noun = getNoun();
		
		Game game = getGame();
		
		Inventory inventory = getInventory();
		
		if (inventory.contains(noun)) {
			Item toRead = inventory.getItem(noun);	// grab item from inventory
			
			if (toRead.isReadable()) {	// check if readable
				game.setOutput("This " + noun + " says \"" + toRead.getDescription() + "\"");	// set output to item's text
			} else {
				game.setOutput("Cannot read this item.");
			}
		} else {
			game.setOutput("Could not find a " + noun + ".");
		}
	}

}
