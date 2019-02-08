package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.QuestionAttempt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionAttemptService {

    public static String selectAllInto(List<QuestionAttempt> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT attemptID, questionID, correct FROM QuestionAttempts"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new QuestionAttempt(results.getInt("attemptID"), results.getInt("questionID"), results.getBoolean("correct")));
                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'QuestionAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static QuestionAttempt selectById(int id) {
        QuestionAttempt result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT attemptID, questionID, correct FROM QuestionAttempts WHERE attemptID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new QuestionAttempt(results.getInt("attemptID"), results.getInt("questionID"), results.getBoolean("correct"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'QuestionAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(QuestionAttempt itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO QuestionAttempts (attemptID, questionID, correct) VALUES (?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getAttemptID());
            statement.setInt(2, itemToSave.getQuestionID());
            statement.setBoolean(3, itemToSave.getCorrect());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'QuestionAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(QuestionAttempt itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE QuestionAttempts SET questionID = ?, correct = ? WHERE attemptID = ?"
            );
            statement.setInt(1, itemToSave.getQuestionID());
            statement.setBoolean(2, itemToSave.getCorrect());
            statement.setInt(3, itemToSave.getAttemptID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'QuestionAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM QuestionAttempts WHERE attemptID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'QuestionAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}