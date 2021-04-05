package items;

public class Item {
	private String name;
	private String description;
	private double weight;
	private boolean isInteractable;
	private boolean canBePickedUp;
	private boolean canBeConsumed; 
	private boolean inInventory;
	private boolean readable;
	
	public Item(String name, double weight) {
		this.name = name;
		this.weight = weight;
		this.isInteractable = false;
		this.canBePickedUp = true;
		this.canBeConsumed = false; 
		this.inInventory = false;
		this.description = "";
		this.readable = false;
	}
	
	public Item(String name) {
		this(name, 0);
	}
	
	public Item() {
		this("Item");
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

	public boolean isConsumable() {
		return canBeConsumed;
	}
	
	public void setConsumable(boolean canBeConsumed) {
		this.canBeConsumed = canBeConsumed;
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
}
