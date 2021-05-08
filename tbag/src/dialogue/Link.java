package dialogue;

public class Link{
	private int linkID;
	private Node nextNode;
	private int nextNodeID;
	private Node previousNode;
	private boolean isAvailable;
	private String option;
	
	public Link(int linkID, Node next, Node previous, boolean available, String option) {
		this.setLinkID(linkID);
		this.isAvailable = available;
		this.nextNode = next;
		this.previousNode = previous;
		this.option = option;
	}
	
	// getters and setters, nodes connect through this class
	
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

	public int getLinkID() {
		return linkID;
	}

	public void setLinkID(int linkID) {
		this.linkID = linkID;
	}

	public int getNextNodeID() {
		return nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}
}


