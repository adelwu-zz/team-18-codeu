package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Hub;
import com.google.codeu.render.JSoupCleanReviewTransformer;
import com.google.codeu.render.ReviewTransformer;
import com.google.codeu.render.SequentialReviewTransformer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Scanner;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

@WebServlet("/hub-list")
public class HubListServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
    Set<Hub> hubs = datastore.getAllHubs();
    //Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/Hubs-data.csv"));
    try{
      //URL url = getClass().getResource("Hubs-data.csv");
      File directory = new File(getClass().getResource("/WEB-INF/Hubs-data.csv").getFile());
      System.out.println("hello there" + directory.getAbsolutePath());
      FileReader fileReader = new FileReader(getServletContext().getResource("/WEB-INF/Hubs-data.csv").getFile());
      CSVReader reader = new CSVReader(fileReader);
      int x = 0;
      String [] cells;
      while((cells = reader.readNext()) != null && x == 0){
      //String line = scanner.nextLine();
        //ells = line.split(";");
        double lat = Double.parseDouble(cells[0]);
        double lng = Double.parseDouble(cells[1]);
        String address = cells[3];
        String name = cells[4];
        Hub hub = new Hub(name, address, lat, lng);
        if(!hubs.contains(hub)){
          datastore.storeHub(hub);
        }
        x = 1;
      }
    } catch (IOException e){
       e.printStackTrace();
    }
  }


  /** Responds with a JSON representation of all Hub data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    Set<Hub> hubs = datastore.getAllHubs();
    Gson gson = new Gson();
    String json = gson.toJson(hubs);
    response.getOutputStream().println(json);
  }
}
