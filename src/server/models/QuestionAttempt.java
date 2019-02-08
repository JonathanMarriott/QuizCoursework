package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class QuestionAttempt {
    private int attemptID;
    private int questionID;
    private boolean correct;

    // Get IntelliJ to auto-generate a constructor, getter and setters here:

    public int getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public QuestionAttempt(int attemptID, int questionID, boolean correct) {
        this.attemptID = attemptID;
        this.questionID = questionID;
        this.correct = correct;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static ArrayList<QuestionAttempt> questionattempts = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (QuestionAttempt q: questionattempts) {
            if (q.getAttemptID() > id) {
                id = q.getAttemptID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("attemptID", getAttemptID());
        j.put("questionID", getQuestionID());
        j.put("correct", getCorrect());

        return j;
    }
}