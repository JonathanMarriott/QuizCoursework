package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.Answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnswerService {

    public static String selectAllInto(List<Answer> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT answerID, questionID, content, correctAns FROM Answers"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Answer(results.getInt("answerID"), results.getInt("questionID"), results.getString("content"), results.getBoolean("correctAns")));

                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Answers' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Answer selectById(int id) {
        Answer result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT answerID, questionID, content, correctAns FROM Answers WHERE answerID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Answer(results.getInt("answerID"), results.getInt("questionID"), results.getString("content"), results.getBoolean("correctAns"));

                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Answers' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Answer itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Answers (answerID,questionID, content, correctAns) VALUES (?, ?, ?, ?)"
            ); //autoincrement id
            statement.setInt(1, itemToSave.getAnswerID());
            statement.setInt(2, itemToSave.getQuestionID());
            statement.setString(3, itemToSave.getContent());
            statement.setBoolean(4, itemToSave.getCorrectAns());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Answers' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Answer itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Answers SET questionID = ?, content = ?, correctAns = ? WHERE answerID = ?"
            );
            statement.setInt(1, itemToSave.getQuestionID());
            statement.setString(2, itemToSave.getContent());
            statement.setBoolean(3, itemToSave.getCorrectAns());
            statement.setInt(4, itemToSave.getAnswerID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Answers' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Answers WHERE answerID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Answers' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}