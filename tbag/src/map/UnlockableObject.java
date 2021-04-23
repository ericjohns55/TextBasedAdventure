package map;

import items.Item;

public class UnlockableObject extends RoomObject {
	private boolean consumeItem;
	private Item unlockItem;
	
	private int unlockItemID;
	
	public UnlockableObject(String name, String description, String direction, boolean blockingExit, Item unlockItem, int roomID) {
		super(name, description, direction, true, blockingExit, false, roomID);
		this.setUnlockable(true);
		this.setLocked(true);
		this.unlockItem = unlockItem;
		this.consumeItem = true;
//		this.unlockItemID = unlockItem.getItemID();
	}
	
	public Item getUnlockItem() {
		return unlockItem;
	}
	
	public void setUnlockItem(Item unlockItem) {
		this.unlockItem = unlockItem;
	}
	
	public boolean consumeItem() {
		return consumeItem;
	}
	
	public void setConsumeItem(boolean consumeItem) {
		this.consumeItem = consumeItem;
	}

	public int getUnlockItemID() {
		return unlockItemID;
	}

	public void setUnlockItemID(int unlockItemID) {
		this.unlockItemID = unlockItemID;
	}
}
