package com.example.stepcount.model;

public class StepData {

  private int userId;
  private int recordDate;
  private int timeInterval;
  private int stepCount;

  public StepData() {
  }

  public StepData(int userId, int recordDate, int timeInterval, int stepCount) {
    super();
    this.userId = userId;
    this.recordDate = recordDate;
    this.timeInterval = timeInterval;
    this.stepCount = stepCount;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userid) {
    this.userId = userid;
  }

  public int getRecordDate() {
    return recordDate;
  }

  public void setRecordDate(int recordDate) {
    this.recordDate = recordDate;
  }

  public int getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(int timeInterval) {
    this.timeInterval = timeInterval;
  }

  public int getStepCount() {
    return stepCount;
  }

  public void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  @Override
  public String toString() {
    return "StepData{" +
        "userId=" + userId +
        ", recordDate=" + recordDate +
        ", timeInterval=" + timeInterval +
        ", stepCount=" + stepCount +
        '}';
  }
}
