package com.programmingpals.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class StudentViewTest {
	
	StudentView studentView = new StudentView();
	
	@BeforeEach
	public void setUp() 
	{
		studentView = new StudentView();
	}
	
	@Test
	public void validSpacedInputTest()
	{
		String input = "Joe Pitt";
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
	
	@Test
	public void validNospaceInputTest()
	{
		String input = "JoePitt";
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
	
	@Test
	public void validNumberInputTest()
	{
		String input = "5";
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
	
	@Test
	public void validSymbolInputTest()
	{
		String input = "_!@#$%^&*()_+";
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
	
	@Test
	public void validMixedInputTest()
	{
		String input = "5hf&";
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
	
	@Test
	public void blankInputTest()
	{
		String input = "";
    	studentView.setCurrentStudentName(input);
        assertEquals("anonymous", studentView.getCurrentStudentName());
	}
	
	@Test
	public void spaceInputTest()
	{
		String input = "       ";
    	studentView.setCurrentStudentName(input);
        assertEquals("anonymous", studentView.getCurrentStudentName());
	}
	
	@Test
	public void nullInputTest()
	{
		String input = null;
    	studentView.setCurrentStudentName(input);
        assertEquals("anonymous", studentView.getCurrentStudentName());
	}
	
	@Test
	public void longInputTest()
	{
		String input = "hi".repeat(10000);
    	studentView.setCurrentStudentName(input);
        assertEquals(input, studentView.getCurrentStudentName());
	}
}
