package blackboxTests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

import qnaApp.Create;
import qnaApp.Main;
import qnaApp.Question;
import qnaApp.Quiz;
import qnaApp.Timer;

public class Specification {

	@Test
	public void testLoadQuestionWithExistingFile() {
		Quiz quiz = new Quiz();
		quiz.loadQuestions("test");
		assertFalse(quiz.getQuestions().isEmpty());
	}
	
	@Test
	public void testLoadQuestionWithNonExistingFile() {
		Quiz quiz = new Quiz();
		quiz.loadQuestions("imaginaryFile");
		assertTrue(quiz.getQuestions().isEmpty());
	}
	
	@Test
	public void testQuestionOptionsExist() {
		Quiz quiz = new Quiz();
		quiz.loadQuestions("test");
		for (Question question : quiz.getQuestions()) {  // Iterate over all questions in the quiz.
	        assertFalse(question.getOptions().isEmpty());
	    }
	}
	
	@Test
	public void testCreateQuiz() {
		String userInput = "testingQuiz\n" +
							"What is 5 + 5? \n" +
							"2\n" +
							"25\n" +
							"10\n" +
							"2\n" +
							"no\n";
		System.setIn(new ByteArrayInputStream(userInput.getBytes()));
		
		Create create = new Create();
	    create.createQuiz();
	    
	    File file = new File("questionBank/testingQuiz.txt");
	    assertTrue(file.exists());
	}
	
	@Test
	public void testAttemptQuiz() {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		String userInput = 	"simpleQuiz\n" +
							"\n" +
							"no";
		
		System.setIn(new ByteArrayInputStream(userInput.getBytes()));
		
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        
		Main app = new Main();
		app.attemptQuiz();
		assertTrue(outputStream.toString().contains("Closing application!"));
	}
	
	@Test
	public void testViewAllQuestions() {
		Quiz quiz = new Quiz();
		quiz.loadQuestions("waffle");
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(outputStream);
		
		System.setOut(ps);
		quiz.viewAllQuestions();
		
		
		String expectedOutput = "List of Questions:\n"
								+ "Question 1:\n"
								+ "What is the capital city of the UK?\n"
								+ "1. Leicester\n"
								+ "2. Birmingham\n"
								+ "3. London\n"
								+ "4. Manchester\n"
								+ "\n"
								+ "Question 2:\n"
								+ "What is the colour of the sky?\n"
								+ "1. blue\n"
								+ "2. purple\n"
								+ "3. orange\n"
								+ "\n";
				
		assertEquals(outputStream.toString(), expectedOutput);	
	}
	
	@Test
	public void testEmptyQuizName() {
	    Create create = new Create();
	    
	    InputStream sysInBackup = System.in;
	    
	    try {
	        ByteArrayInputStream in = new ByteArrayInputStream("\n".getBytes());
	        System.setIn(in);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outputStream));
	        
	        create.createQuiz();
	        
	        String output = outputStream.toString();
	        assertTrue(output.contains("Quiz must have a name!"));
	        
	    } finally {
	        System.setIn(sysInBackup);
	    }
	}
	
	@Test
	public void testCreatingEmptyQuestion() {
		Create create = new Create();
	    
	    InputStream sysInBackup = System.in;
	    
	    try {
	        ByteArrayInputStream in = new ByteArrayInputStream("testingcreation\n\n".getBytes());
	        System.setIn(in);
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outputStream));
	        
	        create.createQuiz();
	        
	        String output = outputStream.toString();
	        assertTrue(output.contains("Question cannot be empty!"));
	        
	    } finally {
	        System.setIn(sysInBackup);
	    }
	}
	
	@Test
    public void testTimerExpiresAfterTimeLimit() throws InterruptedException {
        int timeLimit = 2; // 2 seconds
        Timer timer = new Timer(timeLimit);

        Thread timerThread = new Thread(timer);
        timerThread.start();

        // Wait slightly longer than the time limit to ensure the timer expires
        Thread.sleep((timeLimit + 1) * 1000);

        // Assert that the timer has expired
        assertTrue(timer.isTimeUp());
    }
	
}
