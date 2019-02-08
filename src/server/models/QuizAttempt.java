package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class QuizAttempt {
    private int attemptID;
    private int userID;
    private int quizID;
    private String date;

    // Get IntelliJ to auto-generate a constructor, getter and setters here:

    public int getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public QuizAttempt(int attemptID, int userID, int quizID, String date) {
        this.attemptID = attemptID;
        this.userID = userID;
        this.quizID = quizID;
        this.date = date;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static ArrayList<QuizAttempt> quizattempts = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (QuizAttempt q: quizattempts) {
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
        j.put("userID", getUserID());
        j.put("quizID", getQuizID());
        j.put("date", getDate());












        return j;
    }
}