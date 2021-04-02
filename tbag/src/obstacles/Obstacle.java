package obstacles;

public class Obstacle {
	private boolean blockingExit;
	private boolean unlockable;
	private boolean locked;
	private boolean moveable;
	private String description;
	private String direction;
	private String name;
	
	public Obstacle(String name, String description, String direction, boolean blockingExit, boolean unlockable, boolean moveable) {
		this.name = name;
		this.description = description;
		this.blockingExit = blockingExit;
		this.direction = direction;
		this.unlockable = unlockable;
		this.locked = unlockable;
		this.moveable = moveable;
	}
	
	public boolean isLocked() {
		return locked && unlockable;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isUnlockable() {
		return unlockable;
	}

	public void setUnlockable(boolean unlockable) {
		this.unlockable = unlockable;
	}

	public boolean isBlockingExit() {
		return blockingExit;
	}

	public void setBlockingExit(boolean blockingExit) {
		this.blockingExit = blockingExit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMoveable() {
		return moveable;
	}
	
	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
}
