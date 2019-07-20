package com.google.codeu.data;

import java.util.UUID;

public class Hub{

  private UUID id;
  private String name;
  private String address;
  private Double gpsLat;
  private Double gpsLong;
  private String photo;
  private String desc;


  public Hub(String name, String address, Double gpsLat, Double gpsLong, String photo, String desc){
    this(UUID.randomUUID(), name, address, gpsLat, gpsLong, photo, desc);
  }

  public Hub(UUID id, String name, String address, Double gpsLat, Double gpsLong, String photo, String desc){
    this.id = id;
    this.name = name;
    this.address = address;
    this.gpsLat = gpsLat;
    this.gpsLong = gpsLong;
    this.photo = photo;
    this.desc = desc;
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

  public String getPhoto(){
    return photo;
  }

  public String getDesc() {
    return desc;
  }

}
