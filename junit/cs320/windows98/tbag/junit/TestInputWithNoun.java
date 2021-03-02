package cs320.windows98.tbag.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cs320.windows98.tbag.input.Input;

public class TestInputWithNoun {
	private Input input;
	
	@Before
	public void setUp() {
		input = new Input("pick up the book");
	}
	
	@Test
	public void testContainsNoun() {
		assertTrue(input.containsNoun());
	}
	
	@Test
	public void testRemoveArticles() {
		input.removeArticles(input.getWords());
		
		assertFalse(input.getWords().contains("the") ||
				input.getWords().contains("an") ||
				input.getWords().contains("a"));
	}
	
	@Test
	public void testGetAction() {
		assertEquals("pick up", input.getAction());
	}
	
	@Test
	public void testGetSubject() {
		assertEquals("book", input.getSubject());
	}
}
