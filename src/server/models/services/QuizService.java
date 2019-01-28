package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuizService {

    public static String selectAllInto(List<Quiz> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT quizID, quizTitle, userID, noOfPlays FROM Quizzes"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Quiz(results.getInt("quizID"), results.getString("quizTitle"), results.getInt("userID"), results.getInt("noOfPlays")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Quizzes' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Quiz selectById(int id) {
        Quiz result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT quizID, quizTitle, userID, noOfPlays FROM Quizzes WHERE quizID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Quiz(results.getInt("quizID"), results.getString("quizTitle"), results.getInt("userID"), results.getInt("noOfPlays"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Quizzes' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Quiz itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Quizzes (quizID, quizTitle, userID, noOfPlays) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getId());
            statement.setString(2, itemToSave.getQuizTitle());
            statement.setInt(3, itemToSave.getUserID());
            statement.setInt(4, itemToSave.getNoOfPlays());

            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Quizzes' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Quiz itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Quizzes SET quizTitle = ?, userID = ?, noOfPlays = ? WHERE quizID = ?"
            );
            statement.setString(1, itemToSave.getQuizTitle());
            statement.setInt(2, itemToSave.getUserID());
            statement.setInt(3, itemToSave.getNoOfPlays());












            statement.setInt(4, itemToSave.getId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Quizzes' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Quizzes WHERE quizID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Quizzes' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}