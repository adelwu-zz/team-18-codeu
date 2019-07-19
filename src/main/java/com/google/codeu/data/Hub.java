package com.google.codeu.data;

import java.util.UUID;

public class Hub{

  private UUID id;
  private String name;
  private String address;
  private Double gpsLat;
  private Double gpsLong;
  public Hub(String name, String address, Double gpsLat, Double gpsLong){
    this(UUID.randomUUID(), name, address, gpsLat, gpsLong);
  }

  public Hub(UUID id, String name, String address, Double gpsLat, Double gpsLong){
    this.id = id;
    this.name = name;
    this.address = address;
    this.gpsLat = gpsLat;
    this.gpsLong = gpsLong;
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

  public Double getLat(){
    return gpsLat;
  }
  public Double getLong(){
    return gpsLong;
  }

}
