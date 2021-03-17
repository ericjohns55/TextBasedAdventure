package cs320.windows98.tbag.input;

public class Command {
	private Input input;
	private String output;
	
	public Command(Input input) {
		this.input = input;
	}
	
	public Input getUserInput() {
		return input;
	}
	
	public boolean validate() {
		String action = input.getAction().toLowerCase().trim();
		String subject = input.getSubject().toLowerCase().trim();
		
		if ((action != null && action != "") && (subject != null && subject != "")) {
			if (!action.equals(subject)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void execute() {
		this.output = "Successfully ran command \"" + input.getInput() + "\"";
	}
	
	public String getOutput() {
		return this.output;
	}
}
