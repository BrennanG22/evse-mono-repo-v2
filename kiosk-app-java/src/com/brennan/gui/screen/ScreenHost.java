package com.brennan.gui.screen;

import java.awt.BorderLayout;

import javax.swing.*;

public class ScreenHost extends JPanel {
  private Screen activeScreen = null;


  public ScreenHost() {
    setLayout(new BorderLayout());
    setBounds(0, 0, getWidth(), getHeight());
  }

  public Screen getActiveScreen() {
    return activeScreen;
  }

  public void setActiveScreen(Screen newScreen) {
    if (activeScreen != null) {
      this.remove(activeScreen);
    }

    activeScreen = newScreen;
    activeScreen.setBounds(0, 0, getWidth(), getHeight());
    this.add(activeScreen);
    this.revalidate();
    this.repaint();
  }

  public void swipeTransition(Screen newScreen) {
    final int totalSteps = 10;
    final int panelWidth = getWidth();
    final int panelHeight = getHeight();
    final int stepSize = panelWidth / totalSteps;

    newScreen.setBounds(panelWidth, 0, panelWidth, panelHeight);
    this.add(newScreen);
    this.setComponentZOrder(newScreen, 0); // Bring to front

    newScreen.validate();
    newScreen.repaint();

    Timer timer = new Timer(15, null); 
    final int[] step = { 0 };

    timer.addActionListener(e -> {
      int newX = panelWidth - (step[0] + 1) * stepSize;
      newX = Math.max(newX, 0); 

      newScreen.setLocation(newX, 0);
      this.repaint();

      step[0]++;
      if (step[0] >= totalSteps) {
        timer.stop();
        setActiveScreen(newScreen); 
      }
    });

    timer.start();
  }
}
