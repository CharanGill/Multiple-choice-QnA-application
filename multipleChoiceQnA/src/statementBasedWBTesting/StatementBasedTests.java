package statementBasedWBTesting;

import org.junit.jupiter.api.Test;

import qnaApp.Create;

import java.io.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class StatementBasedTests{
	@Test

	void testCreateQuizWithValidInput() {
	String input = "General Knowledge\n" +
	"What is 2 + 2?\n" +
	"4\n" +
	"1\n" +
	"2\n" +
	"4\n" +
	"5\n" +
	"3\n" +
	"no\n";
	InputStream in = new ByteArrayInputStream(input.getBytes());
	System.setIn(in);
	Create create = new Create();
	create.createQuiz();
	File quizFile = new File("questionBank/General Knowledge.txt");
	assertTrue(quizFile.exists(), "Quiz file should be created");
	try (BufferedReader reader = new BufferedReader(new FileReader(quizFile))) {
		assertEquals("What is 2 + 2?", reader.readLine());
		assertEquals("4", reader.readLine());
		assertEquals("1", reader.readLine());
		assertEquals("2", reader.readLine());
		assertEquals("4", reader.readLine());
		assertEquals("5", reader.readLine());
		assertEquals("3", reader.readLine());
	} catch (IOException e) {
		fail("File read failed: " + e.getMessage());
	}
	quizFile.delete(); // Clean up after test
	}
	@Test
	void testEmptyQuizName() {
		String input = "\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("Input Error: Quiz must have a name!"),
				"Error message should be displayed for empty quiz name");
	}
	@Test
	void testEmptyQuestionText() {
		String input = "Math Quiz\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("Input Error: Question cannot be empty!"),
				"Error message should be displayed for empty question text");
	}
	@Test
	void testInvalidNumberOfOptions() {
		String input = "History Quiz\nWho was the first president of the USA?\n5\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("The number of answers must be between 2 and 4!"),
				"Error message should be displayed for invalid number of options");
	}
	@Test
	void testMissingOptionText() {
		String input = "Science Quiz\nWhat is H2O?\n2\nWater\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("Input Error: Option cannot be empty!"),
				"Error message should be displayed for empty option text");
	}
	@Test
	void testInvalidCorrectAnswer() {
		String input = "Geography Quiz\nWhat is the largest continent?\n3\nAsia\nAfrica\nEurope\n5\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("Choose a number from the provided options!"),
				"Error message should be displayed for invalid correct answer");
	}
	@Test
	void testFileWriteFailure() {
		String input = "Test Quiz\nWhat is 2 + 2?\n2\n1\n2\n1\nno\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		// Simulate file write failure by setting an invalid directory
		System.setProperty("user.dir", "/invalid/path");
		Create create = new Create();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		create.createQuiz();
		assertTrue(out.toString().contains("File Error: Unable to write the quiz file."),
				"Error message should be displayed for file write failure");
	}
}
