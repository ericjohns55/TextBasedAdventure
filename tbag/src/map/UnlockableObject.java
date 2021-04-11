package map;

public class UnlockableObject extends RoomObject {
	private boolean consumeItem;
	private String unlockItem;
	
	public UnlockableObject(String name, String description, String direction, boolean blockingExit, String unlockItem, int roomID) {
		super(name, description, direction, true, blockingExit, false, roomID);
		this.setUnlockable(true);
		this.setLocked(true);
		this.unlockItem = unlockItem;
		this.consumeItem = true;
	}
	
	public String getUnlockItem() {
		return unlockItem;
	}
	
	public void setUnlockItem(String unlockItem) {
		this.unlockItem = unlockItem;
	}
	
	public boolean consumeItem() {
		return consumeItem;
	}
	
	public void setConsumeItem(boolean consumeItem) {
		this.consumeItem = consumeItem;
	}
}
