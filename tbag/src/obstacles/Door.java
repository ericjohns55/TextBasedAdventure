package obstacles;

public class Door extends Obstacle {
	private String unlockItem;
	
	public Door(String description, String direction, boolean blockingExit, String unlockItem) {
		super("door", description, direction, blockingExit, true);
		this.unlockItem = unlockItem;
	}
	
	public Door(String description, String direction, boolean blockingExit) {
		this(description, direction, blockingExit, "key");
	}
	
	public String getUnlockItem() {
		return unlockItem;
	}
}
