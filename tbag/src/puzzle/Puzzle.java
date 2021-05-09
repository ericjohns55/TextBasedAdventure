package puzzle;

public class Puzzle {
	private String description;
	private String solution; 
	private boolean solved;
	private String hint;
	private boolean writtenSolution;
	private String unlockObstacle;
	private int roomID;
	private int puzzleID;
	private int unlockObstacleID;
	
	public Puzzle(String description, String solution, String hint, boolean writtenSolution, String unlockObstacle, int roomID) {
		this.description = description;
		this.solution = solution;
		this.solved = false;
		this.hint = hint;
		this.writtenSolution = writtenSolution;
		this.unlockObstacle = unlockObstacle;
		this.roomID = roomID;
		this.puzzleID = roomID;
	}
	
	// another class that is just getters and setters!
	
	public int getRoomID() {
		return roomID;
	}
	
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	
	public boolean isSolved() {
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

	public String getUnlockObstacle() {
		return unlockObstacle;
	}

	public void setUnlockObstacle(String unlockObstacle) {
		this.unlockObstacle = unlockObstacle;
	}

	public int getPuzzleID() {
		return puzzleID;
	}

	public void setPuzzleID(int puzzleID) {
		this.puzzleID = puzzleID;
	}

	public int getUnlockObstacleID() {
		return unlockObstacleID;
	}

	public void setUnlockObstacleID(int unlockObstacleID) {
		this.unlockObstacleID = unlockObstacleID;
	}
}