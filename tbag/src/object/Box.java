package object;

public class Box extends Object {
	
	public Box(String description, boolean movable, boolean interactable) {
		super("Box", description, movable, interactable);
	}
	
	public void move(String i) {
		if(canBeMoved() && isInteractable()) {
			if(i.equals("push box")) {
				setMoved(true);
				setDescription("The box was moved");
			}
		}
	}
}