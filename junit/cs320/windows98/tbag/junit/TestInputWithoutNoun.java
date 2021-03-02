package cs320.windows98.tbag.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs320.windows98.tbag.input.Input;

public class TestInputWithoutNoun {
	private Input input;
	
	@Before
	public void setUp() {
		input = new Input("walk south");
	}
	
	@Test
	public void testContainsNoun() {
		assertFalse(input.containsNoun());
	}
	
	@Test
	public void testGetAction() {
		assertEquals("walk", input.getAction());
	}
	
	@Test
	public void testGetSubject() {
		assertEquals("south", input.getSubject());
	}
}
