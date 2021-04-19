package junit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import dialogue.Link;
import dialogue.Node;


public class TestNode {
	private int nodeID;
	private String message;
	private ArrayList<Link> options;
	private Node n, p;
	
	@Before
	public void setUp() {
		nodeID = 0;
		message = "Game?";
		n = new Node(nodeID, message);
		p = new Node(1, "Stop");
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
		Link l = new Link(n, p, true, message);
		Link l2 = new Link(n, p, false, message);
		n.addLink(l);
		n.addLink(l2);
		assertEquals(l,n.getAvailableLinks().get(0));
		assertEquals(l2,n.getUnavailableLinks().get(0));
	}
	
	
	
}
