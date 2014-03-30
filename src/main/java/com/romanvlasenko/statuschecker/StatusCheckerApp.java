package com.romanvlasenko.statuschecker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class StatusCheckerApp {

    public static void main(String... args) {
        if (args.length > 0) {
            String confPath = args[0];
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(new File(confPath)));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to load 'settings.conf'. Check provided path");
            }

            start(properties);
        } else {
            System.out.println("Path to 'settings.conf' is not specified");
        }
    }

    private static void start(Properties properties) {
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