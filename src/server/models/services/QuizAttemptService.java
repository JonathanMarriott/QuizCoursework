package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.QuizAttempt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuizAttemptService {

    public static String selectAllInto(List<QuizAttempt> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT attemptID, userID, quizID, date FROM QuizAttempts"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new QuizAttempt(results.getInt("attemptID"), results.getInt("userID"), results.getInt("quizID"), results.getString("date")));
                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'QuizAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static QuizAttempt selectById(int id) {
        QuizAttempt result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT attemptID, userID, quizID, date FROM QuizAttempts WHERE attemptID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new QuizAttempt(results.getInt("attemptID"), results.getInt("userID"), results.getInt("quizID"), results.getString("date"));

                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'QuizAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(QuizAttempt itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO QuizAttempts (attemptID, userID, quizID, date) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getAttemptID());
            statement.setInt(2, itemToSave.getUserID());
            statement.setInt(3, itemToSave.getQuizID());
            statement.setString(4, itemToSave.getDate());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'QuizAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(QuizAttempt itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE QuizAttempts SET userID = ?, quizID = ?, date = ? WHERE attemptID = ?"
            );
            statement.setInt(1, itemToSave.getUserID());
            statement.setInt(2, itemToSave.getQuizID());
            statement.setString(3, itemToSave.getDate());
            statement.setInt(4, itemToSave.getAttemptID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'QuizAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM QuizAttempts WHERE attemptID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'QuizAttempts' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}