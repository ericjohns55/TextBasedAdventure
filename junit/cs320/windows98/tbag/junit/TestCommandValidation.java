package cs320.windows98.tbag.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cs320.windows98.tbag.input.Command;
import cs320.windows98.tbag.input.Input;

public class TestCommandValidation {
	private Input input;
	
	@Before
	public void setUp() {
		input = new Input("walk south");
	}
	
	@Test
	public void testValidate() {
		input = new Input("look");
		Command command = new Command(input);
		
		assertTrue(command.validate());
	}
	
	@Test
	public void testValidate2() {
		input = new Input("grab the book");
		Command command = new Command(input);
		
		assertTrue(command.validate());
	}
	
	@Test
	public void testValidate3() {
		input = new Input("");
		Command command = new Command(input);
		
		assertFalse(command.validate());
	}
}
