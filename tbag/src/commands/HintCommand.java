package commands;

import game.Game;

public class HintCommand extends UserCommand {
	public HintCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public String getOutput() {
		String output = getPuzzle().getHint();
		return output;
	}
}
