package com.romanvlasenko.statuschecker;


public class UrlStatus {

  private String urlName;
  private boolean available;

  public UrlStatus(String urlName, boolean available) {
    this.urlName = urlName;
    this.available = available;
  }

  public boolean isAvailable() {
    return available;
  }

  public String getUrlName() {
    return urlName;
  }

}
