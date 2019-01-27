package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Question {
    private int questionID;
    private int quizID;
    private String questionTitle;
    private String image;
    private String explanation;



    // Get IntelliJ to auto-generate a constructor, getter and setters here:

    public Question(int questionID, int quizID, String questionTitle, String image, String explanation) {
        this.questionID = questionID;
        this.quizID = quizID;
        this.questionTitle = questionTitle;
        this.image = image;
        this.explanation = explanation;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static ArrayList<Question> questions = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Question q: questions) {
            if (q.getQuestionID() > id) {
                id = q.getQuestionID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("questionID", getQuestionID());
        j.put("quizID", getQuizID());
        j.put("questionTitle", getQuestionTitle());
        j.put("image", getImage());
        j.put("explanation", getExplanation());

        return j;
    }
}