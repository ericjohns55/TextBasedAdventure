package object;

import java.util.HashMap;
import items.Item;

public class Object {
	private String name;
	private String description;
	private boolean isInteractable;
	private boolean movable;
	private boolean moved;
	private HashMap<String, Item> items;
	
	public Object(String name, String description, boolean movable, boolean interactable) {
		this.name = name;
		this.isInteractable = interactable;
		this.movable = movable;
		this.moved = false;
		this.description = description;
		items = new HashMap<String, Item>();
		
	}
	
	public boolean isMoved() {
		return moved && movable;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
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
	
	public boolean isInteractable() {
		return isInteractable;
	}
	
	public void setInteractable(boolean isInteractable) {
		this.isInteractable = isInteractable;
	}
	
	public boolean canBeMoved() {
		return movable;
	}
	
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	public void addItem(String identifier, Item item)
	{
		items.put(identifier, item);
	}
	
	public Item getItem(String identifier) {
		if (contains(identifier)) {
			return items.get(identifier);
		}  else {
			return null;
		}
	}
	
	public String listItems() {
		String objString = "\n";
		
		if (!items.isEmpty()) {
			for (Item item : items.values()) {
				objString += "The " + name + " has a" + item.getName() + "on it. ";
			}
			
			objString = objString.substring(0, objString.length() - 2) + "\n";
		} else {
			objString = "\nThere is nothing on the " + name + ".";
		}
		
		return objString;
	}
	
	public void removeItem(String identifier) {
		if (contains(identifier)) {
			items.remove(identifier);
		}
	}
	
	public boolean contains(String identifier) {
		return items.containsKey(identifier);
	}
	
	
}
