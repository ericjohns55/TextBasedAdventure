package map;

import items.Inventory;

public class RoomObject {
	private String name;
	private String description;
	private String direction;
	
	private String covered;
	private String fed;
	private String scanned;
	
	private boolean unlockable;
	private boolean locked;
	private boolean isInteractable;
	private boolean canHoldItems;
	private boolean blockingExit;
	private boolean moveable;
	private boolean isObstacle;
	private boolean coverable;	
	private boolean canBeFed;
	private boolean canBeScanned;
	private boolean previouslyUnlocked;
	private Inventory inventory;
	
	private int roomID;
	private int inventoryID;
	private int objectID;
	
	public RoomObject(String name, String description, String direction, boolean isObstacle, boolean blockingExit, boolean moveable, int roomID) {
		this.name = name;
		this.description = description;
		this.direction = direction;
		
		this.isObstacle = isObstacle;
		this.blockingExit = blockingExit;
		this.moveable = moveable;
		
		this.unlockable = false;
		this.locked = false;
		this.isInteractable = false;
		this.canHoldItems = false;
		this.locked = false;
		this.coverable = false;
		this.canBeFed = false;
		this.previouslyUnlocked = false;		
		this.covered = "";
		this.fed = "";
		
		this.canBeScanned = false;
		this.scanned = "";
		
		this.inventory = new Inventory();
		this.roomID = roomID;
	}
	
	// yeah this entire class is basically getters and setters
	
	public int getRoomID() {
		return roomID;
	}
	
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getInventoryID() {
		return inventoryID;
	}
	
	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}
	
	public boolean canHoldItems() {
		return canHoldItems;
	}
	
	public void setCanHoldItems(boolean canHoldItems) {
		this.canHoldItems = canHoldItems; 
	}
	
	public String getDescription() {
		String desc = description;
		
		if (!inventory.isEmpty()) { 
			desc += "\n" + inventory.listItems();	// list items if inventory is not empty
		}
		
		return desc;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isInteractable() {
		return isInteractable;
	}
	
	public void setInteractable(boolean isInteractable) {
		this.isInteractable = isInteractable;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public boolean isLocked() {
		return locked && unlockable;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
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

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}
	
	public boolean isCoverable() {
		return coverable;
	}
	
	public void setCoverable(boolean coverable) {
		this.coverable = coverable;
	}
	
	public void cover(String toCover) {
		covered = toCover;
	}
	
	public String getCovering() {
		return covered;
	}
	
	public boolean isCovered() {
		return covered.length() > 1;
	}
	
	public boolean wasPreviouslyUnlocked() {
		return previouslyUnlocked;
	}
	
	public void setPreviouslyUnlocked(boolean previouslyUnlocked) {
		this.previouslyUnlocked = previouslyUnlocked;
	}

	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public boolean canBeFed() {
		return canBeFed;
	}
	
	public void setCanBeFed(boolean canBeFed) {
		this.canBeFed = canBeFed;
	}

	public void feed(String toFeed) {
		fed = toFeed;
	}
	
	public String getFed() {
		return fed;
	}
	
	public boolean isFed() {
		return fed.length() > 1;
	}

	public boolean canBeScanned() {
		return canBeScanned;
	}
	
	public void setCanBeScanned(boolean canBeScanned) {
		this.canBeScanned = canBeScanned;
	}
	
	public void scanned(String toScan) {
		scanned = toScan;
	}
	
	public String getScanned() {
		return scanned;
	}
	
	public boolean isScanned() {
		return scanned.length() > 1;
	}	
	
}