package com.romanvlasenko.statuschecker;

import com.romanvlasenko.statuschecker.image.ImageUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;

public class TrayInstance implements Observer {

  private HashMap<String, Boolean> urlStatusMap = new HashMap<>();
  private HashMap<String, MenuItem> urlMenuItems = new HashMap<>();

  public static final int IMG_SIZE_FACTOR = 2;
  private Image statusGreenImg = createImage("resources/green.png");
  private Image statusRedImg = createImage("resources/red.png");

  private final static String UP = "[OK] ";
  private final static String DOWN = "[NA] ";

  private TrayIcon trayIcon;

  public TrayInstance(Set<String> urlNames) {

    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
    }

    final PopupMenu menu = new PopupMenu();

    addUrlMenuItems(menu, urlNames);
    menu.addSeparator();
    menu.add(createExitMenu());

    trayIcon = new TrayIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
    resizeImages(trayIcon.getSize());

    trayIcon.setPopupMenu(menu);

    try {
      final SystemTray tray = SystemTray.getSystemTray();
      tray.add(trayIcon);
    } catch (AWTException e) {
      System.out.println("TrayIcon could not be added.");
    }

  }

  private void resizeImages(Dimension trayIconSize) {
    int width = (int) trayIconSize.getWidth();
    int height = (int) trayIconSize.getHeight();

    statusGreenImg = ImageUtil.resizeImage(statusGreenImg, width - IMG_SIZE_FACTOR, height - IMG_SIZE_FACTOR);
    statusRedImg = ImageUtil.resizeImage(statusRedImg, width - IMG_SIZE_FACTOR, height - IMG_SIZE_FACTOR);
  }

  private MenuItem createExitMenu() {
    MenuItem exitItem = new MenuItem("Exit");
    exitItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    return exitItem;
  }

  private void addUrlMenuItems(PopupMenu menu, Set<String> urlNames) {
    for (String urlName : urlNames) {
      MenuItem menuItem = new MenuItem(DOWN + urlName);
      urlMenuItems.put(urlName, menuItem);

      menu.add(menuItem);
    }
  }

  private Image createImage(String fileName) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File(fileName));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return img;
  }

  private void updateTrayIcon() {
    boolean green = true;
    for (boolean status : urlStatusMap.values()) {
      green &= status;
    }
    trayIcon.setImage(green ? statusGreenImg : statusRedImg);
  }

  private void updateMenuItem(String urlName, boolean available) {
    String status = available ? UP : DOWN;
    String menuLabel = status + urlName;
    MenuItem menuItem = urlMenuItems.get(urlName);
    menuItem.setLabel(menuLabel);
  }

  public void update(Observable o, Object urlStatus) {
    UrlStatus uStatus = (UrlStatus) urlStatus;

    String name = uStatus.getUrlName();
    Boolean enabled = uStatus.isAvailable();

    urlStatusMap.put(name, enabled);

    updateMenuItem(name, enabled);
    updateTrayIcon();
  }
}
