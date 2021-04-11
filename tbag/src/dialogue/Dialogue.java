package dialogue;

import java.util.TreeMap;

public class Dialogue{
	private TreeMap<Integer, Node> nodes;

    public Dialogue() {
        nodes = new TreeMap<>();
    }

    public void addNode(Node n) {
        nodes.put(n.getNodeID(), n);
    }
    
    public void removeNode(Node n) {
    	nodes.remove(n.getNodeID(), n);
    }
}


