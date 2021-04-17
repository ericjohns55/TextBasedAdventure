package map;

public class UnlockableObject extends RoomObject {
	private boolean consumeItem;
	private String unlockItem;
	
	private boolean canBeLookedAtNow;
	
	public UnlockableObject(String name, String description, String direction, boolean blockingExit, String unlockItem) {
		super(name, description, direction, true, blockingExit, false);
		this.setUnlockable(true);
		this.setLocked(true);
		this.unlockItem = unlockItem;
		this.consumeItem = true;
		this.canBeLookedAtNow = true;
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
	
	public boolean getCanBeLookedAtNow() {
		return canBeLookedAtNow;
	}
	
	public void setCanBeLookedAtNow(boolean setter) {
		this.canBeLookedAtNow = setter;
	}
	
}
