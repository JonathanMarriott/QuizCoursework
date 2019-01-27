package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Quiz {
    private int id;
    private String quizTitle;
    private int userID;
    private int noOfPlays;

    // Get IntelliJ to auto-generate a constructor, getter and setters here:

    public Quiz(int id, String quizTitle, int userID, int noOfPlays) {
        this.id = id;
        this.quizTitle = quizTitle;
        this.userID = userID;
        this.noOfPlays = noOfPlays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getNoOfPlays() {
        return noOfPlays;
    }

    public void setNoOfPlays(int noOfPlays) {
        this.noOfPlays = noOfPlays;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static ArrayList<Quiz> quizs = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Quiz q: quizs) {
            if (q.getId() > id) {
                id = q.getId();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id", getId());
        j.put("quizTitle", getQuizTitle());
        j.put("userID", getUserID());
        j.put("noOfPlays", getNoOfPlays());
        return j;
    }
}