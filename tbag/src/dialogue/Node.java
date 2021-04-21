package dialogue;

import java.util.ArrayList;

public class Node{
	private int nodeID;
	private String message;
	private ArrayList<Link> options;
	private boolean wrong;
	
	public Node(int ID, String message, boolean wrong) {
		this.nodeID = ID;
		this.message = message;
		options = new ArrayList<Link>();
		this.wrong = wrong;
		
	}
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
	
	public ArrayList<Link> getAvailableLinks() {
		ArrayList<Link> availableLinks = new ArrayList<>();
        for(Link l : options) {
            if (l.isAvailable()) {
                availableLinks.add(l);
            }
        }
        return availableLinks;
	}
	
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
	public boolean isWrong() {
		return wrong;
	}
	public void setWrong(boolean wrong) {
		this.wrong = wrong;
	}
	
}


