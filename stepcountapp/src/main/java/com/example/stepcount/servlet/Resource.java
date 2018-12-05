package com.example.stepcount.servlet;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import com.example.stepcount.model.*;
import com.example.stepcount.dal.*;

/**
 * Root resource (exposed at "/" path)
 */
@Path("/")
public class Resource {

  private static final StepDataDao stepDataDao = StepDataDao.getInstance();

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "APPLICATION_JSON" media type.
   *
   * @return SkierData object that will be returned as a APPLICATION_JSON response.
   */
  @Path("current/{userId}")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.TEXT_PLAIN)
  public int getDatabyUserID(@PathParam("userId") int userId) throws SQLException {
    return stepDataDao.getStepDataByUserID(userId);
  }


  @GET
  @Path("single/{userId}/{day}")
  public int getDataByUserIDAndRecordDate(@PathParam("userId") int userId,
      @PathParam("day") int day) throws SQLException {
    return stepDataDao.getStepDataByUserIDAndRecordDate(userId, day);
  }

  @Path("range/{userId}/{startDay}/{numDays}")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.TEXT_PLAIN)
  public ArrayList<Integer> getDataByUserIDAndRangeOfDays(@PathParam("userId") int userId,
      @PathParam("startDay") int startDay,
      @PathParam("numDays") int numDays) throws SQLException {
    return stepDataDao.getStepDataByUserIDAndRangeOfDays(userId, startDay, numDays);
  }

//  @Path("")
//  @POST
//  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//  public void postData(MultivaluedHashMap<String, String> params) throws SQLException {
//    int userId = Integer.parseInt(params.getFirst("UserId"));
//    int day = Integer.parseInt(params.getFirst("day"));
//    int timeInterval = Integer.parseInt(params.getFirst("timeInterval"));
//    int stepCount = Integer.parseInt(params.getFirst("stepCount"));
//    StepData newStepData = new StepData(userId, day, timeInterval, stepCount);
//    stepDataDao.create(newStepData);

  @Path("")
  @POST
  @Consumes("application/json")
  public boolean postData(StepData stepData) throws SQLException {
    stepDataDao.create(stepData);
    return true;
  }

  @Path("clear")
  @GET
  public boolean clearData() throws SQLException {
    stepDataDao.clear();
    return true;
  }

}
