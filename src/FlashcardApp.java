import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FlashcardApp {
    private ArrayList<Flash> flashcards;
    private JFrame frame;
    private JTextField questionField;
    private JTextField answerField;
    private JTextArea resultArea;
    private JTextField userAnswerField;
    private JLabel scoreLabel;
    private JButton addButton;
    private JButton startQuizButton;
    private JButton submitButton;
    private int currentQuizIndex;
    private int score;
    private int streak;

    public FlashcardApp() {
        flashcards = new ArrayList<>();
        score = 0;
        currentQuizIndex = -1;
        streak = 0;

        frame = new JFrame("Flashcard App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(500, 400));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel addCardPanel = createAddCardPanel();
        frame.add(addCardPanel, BorderLayout.NORTH);

        JPanel quizPanel = createQuizPanel();
        frame.add(quizPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createAddCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), "Add Flashcard", 0, 0, new Font("Arial", Font.BOLD, 16), Color.BLUE));
        panel.setBackground(Color.WHITE);

        questionField = new JTextField();
        answerField = new JTextField();
        addButton = createButton("Add Flashcard", new Color(135, 206, 250)); // Sky Blue
        startQuizButton = createButton("Start Quiz", new Color(144, 238, 144)); // Light Green

        panel.add(new JLabel("Question:"));
        panel.add(questionField);
        panel.add(new JLabel("Answer:"));
        panel.add(answerField);
        panel.add(addButton);
        panel.add(startQuizButton);

        addButton.addActionListener(e -> addFlashcard());
        startQuizButton.addActionListener(e -> startQuiz());

        return panel;
    }

    private JPanel createQuizPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        userAnswerField = new JTextField();
        submitButton = createButton("Submit Answer", new Color(144, 238, 144)); // Light Green
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        panel.add(new JLabel("Your Answer:"), BorderLayout.NORTH);
        panel.add(userAnswerField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.EAST);
        panel.add(scoreLabel, BorderLayout.WEST);
        panel.add(resultArea, BorderLayout.SOUTH);
        panel.setBackground(Color.WHITE);

        submitButton.addActionListener(e -> checkAnswer());

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void addFlashcard() {
        String question = questionField.getText().trim();
        String answer = answerField.getText().trim();
        if (validateInput(question, answer)) {
            flashcards.add(new Flash(question, answer));
            System.out.println("Adding Flashcard - Question: '" + question + "', Answer: '" + answer + "'");
            questionField.setText("");
            answerField.setText("");
            JOptionPane.showMessageDialog(frame, "Flashcard added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean validateInput(String question, String answer) {
        if (question.isEmpty() || answer.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both question and answer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void startQuiz() {
        if (flashcards.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No flashcards available. Please add some flashcards first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentQuizIndex = -1; // Reset current index
        score = 0; // Reset score
        streak = 0; // Reset streak
        scoreLabel.setText("Score: 0"); // Update score display
        askNextQuestion(); // Ask the first question
    }

    private void askNextQuestion() {
        currentQuizIndex = (int) (Math.random() * flashcards.size());
        questionField.setText(flashcards.get(currentQuizIndex).getQuestion());
        userAnswerField.setText(""); // Clear the answer field
        resultArea.setText(""); // Clear the result area
    }

    private void checkAnswer() {
        if (currentQuizIndex < 0 || currentQuizIndex >= flashcards.size()) {
            return; // No valid question to check
        }

        String userAnswer = userAnswerField.getText().trim();
        String correctAnswer = flashcards.get(currentQuizIndex).getAnswer().trim();

        // Debug output
        System.out.println("Correct Answer: '" + correctAnswer + "' (Length: " + correctAnswer.length() + ")");

        // Always increase score and reset streak for correct answers
        streak++;
        score += streak;
        scoreLabel.setText("Score: " + score);
        JOptionPane.showMessageDialog(frame, "Correct! Streak: " + streak, "Result", JOptionPane.INFORMATION_MESSAGE);

        // Display both question and user's answer in the result area
        resultArea.append("Question: " + flashcards.get(currentQuizIndex).getQuestion() + "\n");
        resultArea.append("Your Answer: " + userAnswer + "\n");

        askNextQuestion(); // Ask the next question after checking the answer
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlashcardApp::new);
    }
}

class Flash {
    private String question;
    private String answer;

    public Flash(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
