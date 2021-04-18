package commands;

public class HintCommand extends UserCommand {
	@Override
	public void execute() {
		getGame().setOutput(getPuzzle().getHint());
	}
}
