package com.example.stepcount.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.example.stepcount.model.*;
import org.apache.commons.dbcp2.BasicDataSource;


public class StepDataDao {

  protected ConnectionManager ConnectionManager;

  private static StepDataDao instance = null;

  protected StepDataDao() {
//    ConnectionManager = new ConnectionManager();
//    BasicDataSource dataSource = DataBaseUtility.getDataSource();

  }

  public static StepDataDao getInstance() {
    if (instance == null) {
      instance = new StepDataDao();
    }
    return instance;
  }
  public void clear() throws SQLException {
    String clearStepData =
        "TRUNCATE TABLE StepData;";
    Connection connection = null;
    PreparedStatement clearStmt = null;
    try {
      connection = ConnectionManager.getConnection();
      // Food has an auto-generated key. So we want to retrieve that key.
      clearStmt = connection.prepareStatement(clearStepData,
          Statement.RETURN_GENERATED_KEYS);
      clearStmt.executeUpdate();

      // Retrieve the auto-generated key and set it, so it can be used by the caller.
      // For more details, see:
      // http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-last-insert-id.html
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
//        System.out.println("close!");
        connection.close();
      }
      if (clearStmt != null) {
        clearStmt.close();
      }
    }
  }
  public StepData create(StepData stepData) throws SQLException {
    String insertStepData =
        "INSERT INTO StepData(UserId, RecordDate, TimeInterval, StepCount) " +
            "VALUES(?,?,?,?)"
            + "ON duplicate KEY UPDATE "
            + "StepCount = VALUES(StepCount);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    try {
      connection = ConnectionManager.getConnection();

      insertStmt = connection.prepareStatement(insertStepData,
          Statement.RETURN_GENERATED_KEYS);
      insertStmt.setInt(1, stepData.getUserId());
      insertStmt.setInt(2, stepData.getRecordDate());
      insertStmt.setInt(3, stepData.getTimeInterval());
      insertStmt.setInt(4, stepData.getStepCount());
      insertStmt.executeUpdate();

      return stepData;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {

        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }

    }
  }


  public int getStepDataByUserID(int userId) throws SQLException {
    int stepCount = 0;

    String selectStepData = "select sum(StepCount) as Step "
        + "from ("
        + "select StepCount "
        + "from StepData "
        + "where UserID=?) as UserStep;";

//    String selectStepData =
//        "select UserID, RecordDate, sum(StepCount) as Step " +
//            "from StepData " +
//            "where UserID = ? " +
//            "group by UserID, RecordDate " +
//            "order by RecordDate DESC " +
//            "limit 1;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = ConnectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectStepData);
      selectStmt.setInt(1, userId);
      results = selectStmt.executeQuery();
      if (results.next()) {
        stepCount = results.getInt("Step");

      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
//        System.out.println("close!");
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return stepCount;
  }


  /**
   * Get the all the stepData for a user and a day.
   */
  public int getStepDataByUserIDAndRecordDate(int userId, int RecordDate) throws SQLException {
    int totalStep = 0;
    String selectStepData =
        "SELECT SUM(StepCount) " +
            "FROM StepData " +
            "WHERE UserId=? " +
            "AND RecordDate=?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = ConnectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectStepData);
      selectStmt.setInt(1, userId);
      selectStmt.setInt(2, RecordDate);
      results = selectStmt.executeQuery();
      if (results.next()) {
        totalStep = results.getInt("sum(StepCount)");

      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
//        System.out.println("close!");
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return totalStep;
  }


  /**
   * Get the all the stepData for a user and a day.
   */
  public ArrayList<Integer> getStepDataByUserIDAndRangeOfDays(int userId, int startDay, int numDays)
      throws SQLException {
    ArrayList<Integer> stepCounts = new ArrayList<>();
    int totalSteps = 0;
    String selectStepData =
        "select RecordDate, Step " +
            "from " +
            "(select userID, RecordDate, sum(StepCount) as Step " +
            "from StepData " +
            "group by userID, RecordDate) as dayData " +
            "where UserID = ? and RecordDate >= ? and RecordDate < ? " +
            "order by RecordDate;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = ConnectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectStepData);
      selectStmt.setInt(1, userId);
      selectStmt.setInt(2, startDay);
      selectStmt.setInt(3, numDays);
      results = selectStmt.executeQuery();
      while (results.next()) {
        int stepCount = results.getInt("Step");
        stepCounts.add(stepCount);
        totalSteps += stepCount;
      }
      stepCounts.add(totalSteps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return stepCounts;
  }
}
