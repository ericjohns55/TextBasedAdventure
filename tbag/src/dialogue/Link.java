package dialogue;

public class Link{
	private Node nextNode; 
	private Node previousNode;
	private boolean isAvailable;
	private String option;
	
	public Link(Node next, Node previous, boolean available, String option) {
		this.isAvailable = available;
		this.nextNode = next;
		this.previousNode = previous;
		this.option = option;
	}
	
	public Node getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
	
	public Node getPreviousNode() {
		return previousNode;
	}
	
	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	} 
}


