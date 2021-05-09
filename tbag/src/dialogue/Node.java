package dialogue;

import java.util.ArrayList;

public class Node{
	private int npcID;
	private int nodeID;
	private String message;
	private ArrayList<Link> options;
	private String type;
	
	public Node(int npcID, int nodeID, String message, String type) {
		this.setNpcID(npcID);
		this.nodeID = nodeID;
		this.message = message;
		options = new ArrayList<Link>();
		this.type = type;
	}
	
	// getters and setters
	
	public int getNodeID() {
		return nodeID;
	}
	
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addLink(Link link) {
        options.add(link);
    }

    public void removeLinks(Link link) {
        options.remove(link);
    }

	public ArrayList<Link> getOptions() {
		return options;
	}
	
	// get all the links that this node has
	public ArrayList<Link> getAvailableLinks() {
		ArrayList<Link> availableLinks = new ArrayList<>();
        for(Link l : options) {
            if (l.isAvailable()) {
                availableLinks.add(l);
            }
        }
        return availableLinks;
	}
	
	// grab all the links that are not connected
	public ArrayList<Link> getUnavailableLinks() {
		ArrayList<Link> unavailableLinks = new ArrayList<>();
        for(Link l : options) {
            if (!l.isAvailable()) {
                unavailableLinks.add(l);
            }
        }
        return unavailableLinks;
	}

	public void setOptions(ArrayList<Link> options) {
		this.options = options;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getNpcID() {
		return npcID;
	}
	
	public void setNpcID(int npcID) {
		this.npcID = npcID;
	}
}


