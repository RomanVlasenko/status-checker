package com.romanvlasenko.statuschecker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class StatusCheckerApp {

  public static void main(String... args) {

    Properties properties = new Properties();
    try {
      properties.load(new FileReader(new File("resources/settings.properties")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    HashMap<String, String> urlsToCheck = new HashMap<>();

    for (Entry<Object, Object> urlEntry : properties.entrySet()) {
      String key = (String) urlEntry.getKey();
      String value = (String) urlEntry.getValue();

      urlsToCheck.put(key, value);
    }

    TrayInstance trayInstance = new TrayInstance(urlsToCheck.keySet());

    UrlChecker urlChecker = new UrlChecker(trayInstance, urlsToCheck);
    urlChecker.start();
  }

}