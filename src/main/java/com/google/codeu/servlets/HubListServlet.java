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
import java.util.HashSet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Scanner;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

@WebServlet("/hub-list")
public class HubListServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
    List<Hub> hubs = datastore.getAllHubs();
    Set<String> hubNames = new HashSet<>();
    for (Hub hub : hubs){
      hubNames.add(hub.getName());
    }
    try{
      InputStreamReader streamReader = new InputStreamReader(getServletContext().getResourceAsStream("/WEB-INF/Hubs-data.csv"));
      CSVReader reader = new CSVReader(streamReader);
      String[] cells;
      while((cells = reader.readNext()) != null){
        double lat = Double.parseDouble(cells[1]);
        double lng = Double.parseDouble(cells[2]);
        String address = cells[3];
        String name = cells[4];
        Hub hub = new Hub(name, address, lat, lng);
        if(!hubNames.contains(hub.getName())){
          datastore.storeHub(hub);
        }
      }
    } catch (IOException e){
       e.printStackTrace();
    }
  }


  /** Responds with a JSON representation of all Hub data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    List<Hub> hubs = datastore.getAllHubs();
    Gson gson = new Gson();
    String json = gson.toJson(hubs);
    response.getOutputStream().println(json);
  }
}
