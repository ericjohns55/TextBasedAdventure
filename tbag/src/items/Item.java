package items;

public class Item {
	private String name;
	private String description;
	private double weight;
	private boolean isInteractable;
	private boolean canBePickedUp;
	private boolean consumeOnUse; 
	private boolean inInventory;
	private boolean isEquipped;
	private boolean equippable;
	private boolean readable;
	private boolean pourable;
	
	private int itemID;
	private int locationID;
	
	public Item(String name, double weight) {		
		this.name = name;
		this.weight = weight;
		this.isInteractable = false;
		this.canBePickedUp = true;
		this.consumeOnUse = false; 
		this.inInventory = false;
		this.description = "";
		this.equippable = false;
		this.isEquipped = false;
		this.readable = false;
		this.pourable = false;
	}
	
	public Item(String name) {
		this(name, 0);
	}
	
	public Item() {
		this("Item");
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	
	public int getLocationID() {
		return locationID;
	}
	
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
	public String getDescription() {
		return description;
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
	
	public void setReadable(boolean readable) {
		this.readable = readable;
	}
	
	public boolean isReadable() {
		return readable;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public boolean isInteractable() {
		return isInteractable;
	}
	
	public void setInteractable(boolean isInteractable) {
		this.isInteractable = isInteractable;
	}

	public boolean consumeOnUse() {
		return consumeOnUse;
	}
	
	public void setConsumeOnuse(boolean consumeOnUse) {
		this.consumeOnUse = consumeOnUse;
	}
	
	public boolean canBePickedUp() {
		return canBePickedUp;
	}
	
	public void setCanBePickedUp(boolean canBePickedUp) {
		this.canBePickedUp = canBePickedUp;
	}
	
	public boolean inInventory() {
		return inInventory;
	}
	
	public void setInInventory(boolean inInventory) {
		this.inInventory = inInventory;
	}
	
	public boolean isPourable() {
		return pourable;
	}
	
	public void setPourable(boolean pourable) {
		this.pourable = pourable;
	}
	
	@Override
	public boolean equals(Object compare) {
		if (!(compare instanceof Item))
			return false;
		
		Item compareItem = (Item) compare;
		
		return compareItem.getName().equals(getName())
				&& compareItem.getWeight() == getWeight()
				&& compareItem.isInteractable() == isInteractable()
				&& compareItem.canBePickedUp() == canBePickedUp();
	}

	public boolean isEquipped() {
		return isEquipped;
	}

	public void setEquipped(boolean isEquipped) {
		this.isEquipped = isEquipped;
	}

	public boolean isEquippable() {
		return equippable;
	}

	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}
}
