package com.example.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.io.FileWriter;

/**
 * ClientTest class.
 */
public class ClientTest {

  public static void runPhase (Client client, int numberOfThreads, int numberOfIterations, String BASE_URI,
      int startTime, int endTime) {

    ExecutorService phase = Executors.newFixedThreadPool(numberOfThreads);
    List<ArrayList<Integer>> latenciesCollection = Collections.synchronizedList(new ArrayList<>());
    List<ArrayList<Long>> responseTimesCollection = Collections.synchronizedList(new ArrayList<>(numberOfThreads));
//    ArrayList<Long> responseTimesCollection = new ArrayList<>(numberOfIterations * numberOfIterations * 5 * 16);

    long initialTime = System.currentTimeMillis();
    for (int i = 0; i < numberOfThreads; i++) {
      ArrayList<Integer> latencies = new ArrayList<>();
      ArrayList<Long> responseTimes = new ArrayList<>();
      phase.submit(new UnitTask(BASE_URI, numberOfIterations, latencies,
          latenciesCollection, client, startTime, endTime, responseTimes, responseTimesCollection));
    }


    try {
      phase.shutdown();
      phase.awaitTermination(1L, TimeUnit.DAYS);

//      List<Future<Integer>> timePerThread = phase.invokeAll(threads);
      int totalTime = (int) (System.currentTimeMillis() - initialTime);

      List<Integer> allLatencies = latenciesCollection.stream().flatMap(List::stream).collect(Collectors.toList());
      List<Long> responseTimes = responseTimesCollection.stream().flatMap(List::stream).collect(Collectors.toList());
      DescriptiveStatistics latencies = new DescriptiveStatistics();
      for (Integer latency: allLatencies) {
        latencies.addValue(latency);
      }

      System.out.println("number of latency catch: "+ allLatencies.size());
      System.out.println("Phase use time:" + totalTime + " ms");
      System.out.println(
          "Total Number of requests sent: " + numberOfIterations * numberOfThreads * 5);
      System.out.println("Total Number of successful requests: " + latencies.getN());
      System.out.println("Mean latency: " + latencies.getMean() + " ms");
      System.out.println("Median latency: " + latencies.getPercentile(50) + " ms");
      System.out.println("95 percentile latency: " + latencies.getPercentile(95) + " ms");
      System.out.println("99 percentile latency: " + latencies.getPercentile(99) + " ms");
      System.out.println("<<<<<<<<<<<<<<<<<<<<<<< Phase End ");

      try {
        FileWriter writer = new FileWriter("" + numberOfThreads + "threads.txt");
        for( long rt: responseTimes) {
          writer.write(Long.toString(rt) + "\n");
        }
        writer.close();
      } catch (IOException e) {
        System.out.println(e.getCause());
      }

    } catch (InterruptedException e) {
      System.out.println(e.getCause());
    }
//    catch (ExecutionException e) {
//      System.out.println(e.getCause());
//    }
  }

  public static void main(String[] args) throws IOException {
    int maxNumberOfThreads = 300;
    int numberOfTestPerPhase = 300;
//    String BASE_URI = "http://stepcountapp-env.aadp53rwfi.us-west-2.elasticbeanstalk.com/webapi";

//    String BASE_URI =  "http://35.230.118.54:8080/step-count-app/webapi"; //GCP single VM
//    String BASE_URI = "http://applb-343214273.us-west-2.elb.amazonaws.com/step-count-app/webapi";
//    String BASE_URI = "http://ec2-34-221-182-90.us-west-2.compute.amazonaws.com:8080/step-count-app/webapi";
//    String BASE_URI = "http://localhost:8080/webapi";
    String BASE_URI = "http://35.244.218.16/step-count-app/webapi"; // GCP load balancer

    Double[] factors = {0.1, 0.5, 1.0, 0.25};
    int[] phaseLength = {3, 5, 11, 5};
    int[] startTimes = {0, 3, 8, 19};
    int[] endTimes = {2, 7, 18, 23};
    String[] phaseNames = {"Warm-up", "Loading", "Peak", "Cooldown"};
    Client c = ClientBuilder.newClient();
    WebTarget target = c.target(BASE_URI);
    target.path("clear").request().get(Boolean.class);
    for (int i = 0; i < 4; i++) {
      System.out.println(phaseNames[i] + " phase Start >>>>>>>>>>>>>>>>>");
      runPhase(c, (int) (maxNumberOfThreads * factors[i]), numberOfTestPerPhase * phaseLength[i],
          BASE_URI, startTimes[i], endTimes[i]);
    }

  }


}

