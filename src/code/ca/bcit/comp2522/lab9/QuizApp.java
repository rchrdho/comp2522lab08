package ca.bcit.comp2522.lab9;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp extends Application
{

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel = new Label();
    private TextField answerField = new TextField();
    private Label scoreLabel = new Label();
    private Button submitButton = new Button("Submit");
    private Button startButton = new Button("Start Quiz");

    @Override
    public void start(final Stage stage)
    {
        loadQuestions();
        setupUI(stage);
    }

    private void loadQuestions()
    {
        // Load questions from quiz.txt
        try(BufferedReader br = new BufferedReader(new FileReader("quiz.txt")))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split("\\|");
                questions.add(new Question(parts[0], parts[1]));
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        Collections.shuffle(questions);
    }

    private void setupUI(Stage stage)
    {
        // Set up the UI components
        VBox root = new VBox(10, startButton, questionLabel, answerField, submitButton, scoreLabel);
        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Quiz App");

        startButton.setOnAction(e -> startQuiz());
        submitButton.setOnAction(e -> checkAnswer());
        answerField.setOnAction(e -> checkAnswer());

        stage.show();
    }

    private void startQuiz()
    {
        currentQuestionIndex = 0;
        score = 0;
        scoreLabel.setText("Score: 0");
        showNextQuestion();
    }

    private void showNextQuestion()
    {
        if(currentQuestionIndex < questions.size())
        {
            questionLabel.setText(questions.get(currentQuestionIndex).getQuestion());
            answerField.clear();
        } else
        {
            endQuiz();
        }
    }

    private void checkAnswer()
    {
        String userAnswer = answerField.getText();
        if(userAnswer.equalsIgnoreCase(questions.get(currentQuestionIndex).getAnswer()))
        {
            score++;
        }
        currentQuestionIndex++;
        showNextQuestion();
    }

    private void endQuiz()
    {
        questionLabel.setText("Quiz Over!");
        scoreLabel.setText("Final Score: " + score);
        startButton.setDisable(false); // Enable restart
    }

    public static void main(final String[] args)
    {
        launch(args);
    }

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