package junit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import dialogue.Link;
import dialogue.Node;


public class TestNode {
	private int nodeID;
	private String message;
	private Node n, p;
	
	@Before
	public void setUp() {
		nodeID = 0;
		message = "Game?";
		n = new Node(0, nodeID, message, "y/n");
		p = new Node(0, 1, "Stop", "y/n");
	}
	
	@Test
	public void testNodeID() {
		assertEquals(0, n.getNodeID());
	}
	
	@Test
	public void testMessage() {
		assertEquals("Game?", n.getMessage());
	}
	
	@Test
	public void testOptions() {
		Link l = new Link(0, n, p, true, message);
		Link l2 = new Link(1, n, p, false, message);
		n.addLink(l);
		n.addLink(l2);
		assertEquals(l,n.getAvailableLinks().get(0));
		assertEquals(l2,n.getUnavailableLinks().get(0));
	}
	
	
	
}
