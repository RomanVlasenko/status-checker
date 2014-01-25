package com.romanvlasenko.statuschecker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class UrlChecker {

  private final Observer trayInstance;
  private final HashMap<String, String> urlsToCheck;

  public UrlChecker(Observer trayInstance, HashMap<String, String> urlsToCheck) {
    this.trayInstance = trayInstance;
    this.urlsToCheck = urlsToCheck;
  }

  private boolean isUrlAvailable(String urlStr) {
    boolean available = false;

    try {
      URL url = new URL(urlStr);
      HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
      urlConn.connect();

      available = urlConn.getResponseCode() >= 200 && urlConn.getResponseCode() < 400;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return available;
  }

  public void start() {
    do {
      for (Map.Entry<String, String> url : urlsToCheck.entrySet()) {
        String urlName = url.getKey();
        String urlAddress = url.getValue();

        boolean enabled = isUrlAvailable(urlAddress);
        trayInstance.update(null, new UrlStatus(urlName, enabled));
      }
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (true);
  }
}
