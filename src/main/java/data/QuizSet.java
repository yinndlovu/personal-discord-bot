package data;

public class QuizSet {

    private final String question;
    private final String answer;

    public QuizSet(String question, String answer) {
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
