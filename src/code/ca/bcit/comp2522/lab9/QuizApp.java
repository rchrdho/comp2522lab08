package ca.bcit.comp2522.lab9;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp extends Application
{

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex;
    private int score = 0;

    private final int QUESTION_LIMIT = 10;
    private final int RESET_NUMBER   = 0;

    private Label questionLabel   = new Label();
    private TextField answerField = new TextField();
    private Label scoreLabel      = new Label();
    private Button submitButton   = new Button("Submit");
    private Button startButton    = new Button("Start Quiz");
    private Button endButton      = new Button("End Quiz");

    @Override
    public void start(final Stage stage)
    {
        loadQuestions();
        setupUI(stage);
    }

    /*
    Loads questions from a text file (quiz.txt)
     */
    private void loadQuestions()
    {
        final Path source;
        final String fileName;

        source = Paths.get("src", "resources", "quiz.txt");

        try(final BufferedReader br = Files.newBufferedReader(source))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split("\\|");
                questions.add(new Question(parts[0], parts[1]));
            }
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }

        Collections.shuffle(questions); // Shuffle questions for random order
    }

    /*
    Sets up the user interface
     */
    private void setupUI(Stage stage)
    {
        final HBox hbox = new HBox(5);
        hbox.getChildren().addAll(startButton, endButton);
        hbox.setAlignment(Pos.CENTER);

        // Set up the UI components
        final VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hbox, questionLabel, answerField, submitButton, scoreLabel);

        final Scene scene = new Scene(vbox, 400, 300);

        // Load and apply the CSS file
        final URL cssURL = getClass().getResource("/style.css");

        if(cssURL != null)
        {
            scene.getStylesheets().add(cssURL.toExternalForm());
        }
        else
        {
            System.out.println("Error: style.css file not found!");
        }

        stage.setScene(scene);
        stage.setTitle("Quiz App");

        submitButton.setDisable(true); // Disable submit button so users won't click before starting the quiz
        endButton.setDisable(true);    // Disable end button before the start of the quiz

        // Event handling for buttons and text field
        startButton.setOnAction(e -> startQuiz());
        endButton.setOnAction(e -> endQuiz());
        submitButton.setOnAction(e -> checkAnswer());
        answerField.setOnAction(e -> checkAnswer());

        stage.show();
    }

    /*
    Initializes the quiz by resetting score and question index, then showing the first question
     */
    private void startQuiz()
    {
        currentQuestionIndex = RESET_NUMBER;          // Reset question index to start from the beginning
        score = RESET_NUMBER;                         // Reset score
        scoreLabel.setText("Score: " + RESET_NUMBER); // Initilaize score display
        startButton.setDisable(true);                 // Disable Start Button during quiz
        submitButton.setDisable(false);               // Enable the submit button once the quiz is started
        endButton.setDisable(false);                  // Enable the end button during the quiz
        showNextQuestion();                           // Show the first question
    }

    /*
    Displays the next question or ends the quiz if the question limit is reached
     */
    private void showNextQuestion()
    {
        if(currentQuestionIndex < QUESTION_LIMIT && currentQuestionIndex < questions.size())
        {
            questionLabel.setText(questions.get(currentQuestionIndex).getQuestion());
            answerField.clear();
        }
        else
        {
            endQuiz();
        }
    }

    /*
    Checks the user's answer, updates the score if correct, and moves to the next questions
     */
    private void checkAnswer()
    {
        final String userAnswer;
        userAnswer = answerField.getText();

        if(userAnswer.equalsIgnoreCase(questions.get(currentQuestionIndex).getAnswer()))
        {
            score++;
            scoreLabel.setText("Score: " + score);
        }
        currentQuestionIndex++;
        showNextQuestion();
    }

    /*
    Ends the quiz by showing the final score and enabling the Start Button for a restart
     */
    private void endQuiz()
    {
        questionLabel.setText("Quiz Over! Thank you for playing");
        scoreLabel.setText("Final Score: " + score);
        startButton.setDisable(false); // Enable the start button to restart the quiz
    }

    public static void main(final String[] args)
    {
        launch(args);
    }

    /*
    Inner class to represet a quiz question
     */
    private static class Question
    {
        private final String question;
        private final String answer;

        public Question(String question, String answer)
        {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion()
        {
            return question;
        }

        public String getAnswer()
        {
            return answer;
        }
    }
}