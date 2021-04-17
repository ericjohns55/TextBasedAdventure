package commands;

import game.Game;

public class HintCommand extends UserCommand {
	public HintCommand(Game game, String verb, String noun, String location) {
		super(game, verb, noun, location);
	}

	@Override
	public void execute() {
		getGame().setOutput(getPuzzle().getHint());
	}
}
