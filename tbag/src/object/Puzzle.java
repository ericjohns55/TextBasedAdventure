package object;

public class Puzzle{
	private String description;
	private String solution; 
	private boolean solved;
	private String hint;
	private boolean writtenSolution;
	
	public Puzzle(String description, String solution, String hint, boolean writtenSolution) {
		this.setDescription(description);
		this.solution = solution;
		this.solved = false;
		this.hint = hint;
		this.setWrittenSolution(writtenSolution);
	}
	
	public boolean getSovled() {
		return solved;
	}
	
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public String getHint() {
		return hint;
	}
	
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public String getSolution() {
		return solution;
	}
	
	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isWrittenSolution() {
		return writtenSolution;
	}

	public void setWrittenSolution(boolean writtenSolution) {
		this.writtenSolution = writtenSolution;
	}
}