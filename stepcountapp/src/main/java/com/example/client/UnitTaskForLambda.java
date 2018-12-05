package com.example.client;


import com.example.stepcount.model.StepData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.print.attribute.standard.Media;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UnitTaskForLambda implements Runnable {


  private String ip;
  private int numberOfIteration;
  private ArrayList<Integer> latencies;
  private List<ArrayList<Integer>> latenciesCollection;
  private Client client;
  private int startTime;
  private int endTime;
  private ArrayList<Long> responseTimes;
  private List<ArrayList<Long>> responseTimesCollection;
//  private CopyOnWriteArrayList<ArrayList<Integer>> latenciesCollection;


  public UnitTaskForLambda(String ip, int numberOfIteration, ArrayList<Integer> latencies,
      List<ArrayList<Integer>> latenciesCollection, Client client,
      int startTime, int endTime, ArrayList<Long> responseTimes,
      List<ArrayList<Long>> reponseTimesCollection) {
    this.ip = ip;
    this.numberOfIteration = numberOfIteration;
    this.latencies = latencies;
//    System.out.println("Build thread success!");
    this.latenciesCollection = latenciesCollection;
    this.client = client;
    this.startTime = startTime;
    this.endTime = endTime;
    this.responseTimes = responseTimes;
    this.responseTimesCollection =  reponseTimesCollection;
  }

  @Override
  public void run() {
    long start = System.currentTimeMillis();

    WebTarget target = this.client.target(this.ip);
//    System.out.println(this.ip);

    for (int i = 0; i < numberOfIteration; i++) {
//      System.out.println(i);

      int userId1 = ThreadLocalRandom.current().nextInt(0, 100000);
      int timeInterval1 = ThreadLocalRandom.current().nextInt(startTime, endTime + 1);
      int stepCount1 = ThreadLocalRandom.current().nextInt(0,5001);
      int userId2 = ThreadLocalRandom.current().nextInt(0, 100000);
      int timeInterval2 = ThreadLocalRandom.current().nextInt(startTime, endTime + 1);
      int stepCount2 = ThreadLocalRandom.current().nextInt(0,5001);
      int userId3 = ThreadLocalRandom.current().nextInt(0, 100000);
      int timeInterval3 = ThreadLocalRandom.current().nextInt(startTime, endTime + 1);
      int stepCount3 = ThreadLocalRandom.current().nextInt(0,5001);


      long beforeResponse;
      int latency;
      Response response;
      String responseMsg;
//      System.out.println(i);

      String postPath = "" + userId1 + "/1/" + timeInterval1 + "/" + stepCount1;
      beforeResponse = System.currentTimeMillis();
      response = target.path(postPath).request().post(null);
      latency = (int) (System.currentTimeMillis() - beforeResponse);
      this.responseTimes.add(System.currentTimeMillis());
      this.latencies.add(latency); // int -> double

      response.close();


      postPath = "" + userId2 + "/1/" + timeInterval2 + "/" + stepCount2;
      beforeResponse = System.currentTimeMillis();
      response = target.path(postPath).request().post(null);
      latency = (int) (System.currentTimeMillis() - beforeResponse);
      this.responseTimes.add(System.currentTimeMillis());
      this.latencies.add(latency); // int -> double
      response.close();



      beforeResponse = System.currentTimeMillis();
      responseMsg = target.path("current/" + userId1).request().get(String.class);
      latency = (int) (System.currentTimeMillis() - beforeResponse);
      this.responseTimes.add(System.currentTimeMillis());
      this.latencies.add(latency); // int -> double

      beforeResponse = System.currentTimeMillis();
      responseMsg = target.path("single/" + userId2 + "/1").request().get(String.class);
      latency = (int) (System.currentTimeMillis() - beforeResponse);
      this.responseTimes.add(System.currentTimeMillis());
      this.latencies.add(latency); // int -> double

      postPath = "" + userId3 + "/1/" + timeInterval3 + "/" + stepCount3;
      beforeResponse = System.currentTimeMillis();
//      response = target.path(postPath).request().post(Entity.entity(Media))
      response = target.path(postPath).request().post(null);
      latency = (int) (System.currentTimeMillis() - beforeResponse);
      this.responseTimes.add(System.currentTimeMillis());
      this.latencies.add(latency); // int -> double
      response.close();

    }
    this.latenciesCollection.add(latencies);
    this.responseTimesCollection.add(responseTimes);
//    int time = (int) (System.currentTimeMillis() - start);
//    return time;
  }


}
