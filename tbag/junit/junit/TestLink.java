package junit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import dialogue.Link;
import dialogue.Node;


public class TestLink {
	private Node nextNode;
	private Node previousNode;
	private boolean isAvailable;
	private String option;
	private Link l;
	
	@Before
	public void setUp() {
		option = "yes";
		isAvailable = true;
		nextNode = new Node(0, "", false, "y/n"); 
		previousNode = new Node(0, "", false, "y/n"); 
		l = new Link(nextNode, previousNode, isAvailable, option);
	}
	
	@Test
	public void testOption() {
		assertEquals("yes", l.getOption());
	}
	
	@Test
	public void testIsAvailable() {
		assertTrue(l.isAvailable());
	}
	
	@Test
	public void testPreviousNode() {
		assertEquals(previousNode, l.getPreviousNode());
	}
	
	@Test
	public void testNextNode() {
		assertEquals(nextNode, l.getNextNode());
	}
	
	
}
