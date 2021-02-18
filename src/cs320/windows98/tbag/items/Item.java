package cs320.windows98.tbag.items;

public class Item {
	private String name;
	private double weight;
	private boolean isInteractable;
	private boolean canBePickedUp;
	
	public Item(String name, double weight) {
		this.name = name;
		this.weight = weight;
		this.isInteractable = false;
		this.canBePickedUp = true;
	}
	
	public Item(String name) {
		this(name, 0);
	}
	
	public Item() {
		this("Item");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public boolean canBePickedUp() {
		return canBePickedUp;
	}
	
	public void setCanBePickedUp(boolean canBePickedUp) {
		this.canBePickedUp = canBePickedUp;
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
