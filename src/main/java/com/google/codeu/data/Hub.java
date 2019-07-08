package com.google.codeu.data;

import java.util.UUID;
import javafx.util.Pair;

public class Hub{

  private UUID id;
  private String name;
  private String address;
  private Pair<Double, Double> gpsLocation;

  public Hub(String name, String address, Pair<Double, Double> gpsLocation){
    this(UUID.randomUUID(), name, address, gpsLocation);
  }

  public Hub(UUID id, String name, String address, Pair<Double, Double> gpsLocation){
    this.id = id;
    this.name = name;
    this.address = address;
    this.gpsLocation = gpsLocation;
  }

  public UUID getId() {
    return id;
  }

  public String getName(){
    return name;
  }

  public String getAddress(){
    return address;
  }

  public Pair<Double, Double> getLocation(){
    return gpsLocation;
  }

}
