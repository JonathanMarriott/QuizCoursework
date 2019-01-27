package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Answer {
    private int answerID;
    private int questionID;
    private String content;
    private boolean correctAns;

    // Get IntelliJ to auto-generate a constructor, getter and setters here:

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(boolean correctAns) {
        this.correctAns = correctAns;
    }

    public Answer(int answerID, int questionID, String content, boolean correctAns) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.content = content;
        this.correctAns = correctAns;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static ArrayList<Answer> answers = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (Answer a: answers) {
            if (a.getAnswerID() > id) {
                id = a.getAnswerID();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("answerID", getAnswerID());
        j.put("questionID", getQuestionID());
        j.put("content", getContent());
        j.put("correctAns", getCorrectAns());


        return j;
    }
}