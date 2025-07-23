package com.brennan;

import javax.swing.*;

import com.brennan.datastate.EVSEDataState;
import com.brennan.evse.ECOGInterface;
import com.brennan.evse.EVSEComunaction;
import com.brennan.gui.screen.ScreenHost;
import com.brennan.gui.screen.ScreenProvider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class Main {
  public static void main(String[] args) {
    EVSEDataState evseDataState = new EVSEDataState();
    boolean devModeFlag = false;

    for (String arg : args) {
      if ("--debug".equals(arg)) {
        devModeFlag = true;
        break;
      }
    }

    final boolean isDevelopmentMode = devModeFlag;

    try {
      EVSEComunaction comunactionInterface = new ECOGInterface();
      comunactionInterface.setDataState(evseDataState);
    } catch (Exception e) {

    }

    SwingUtilities.invokeLater(() -> {

      ScreenProvider screenProvider = new ScreenProvider();
      JFrame frame = new JFrame("Modern Swing UI");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      ScreenHost screenHost = new ScreenHost();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      screenHost.setPreferredSize(screenSize);

      // screenHost.setActiveScreen(screenProvider.getTestScreen(
      // screenHost, evseDataState));

      screenHost.setActiveScreen(screenProvider.getChargeStateScreen(evseDataState));

      frame.setContentPane(screenHost);
      if (isDevelopmentMode) {
        // Windowed mode for debugging
        frame.setSize(800, 600); // Or whatever dev size you want
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
      } else {
        // Fullscreen production mode
        frame.setUndecorated(true);
        frame.pack(); // Lay out before fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        // Optional: hide mouse
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "InvisibleCursor");
        frame.setCursor(invisibleCursor);
      }
    });

  }
}

class SlidePanel extends JPanel {
  private final JPanel screen1 = new RoundedPanel(new Color(200, 230, 250));
  private final JPanel screen2 = new RoundedPanel(new Color(255, 204, 153));
  private int offsetX = 0;
  private final Timer timer;
  private boolean sliding = false;

  public SlidePanel() {
    setLayout(null);
    setBackground(new Color(245, 245, 245));

    screen1.setBounds(0, 0, 400, 300);
    screen2.setBounds(400, 0, 400, 300);

    add(screen1);
    add(screen2);

    timer = new Timer(10, (ActionEvent e) -> animate());
  }

  public void startSlide() {
    if (!sliding) {
      sliding = true;
      offsetX = 0;
      timer.start();
    }
  }

  private void animate() {
    offsetX += 10;
    screen1.setLocation(-offsetX, 0);
    screen2.setLocation(400 - offsetX, 0);

    if (offsetX >= 400) {
      sliding = false;
      timer.stop();
      screen1.setBounds(0, 0, 400, 300);
      screen2.setBounds(400, 0, 400, 300);

      // Swap panels (optional)
      removeAll();
      screen1.setBounds(400, 0, 400, 300);
      screen2.setBounds(0, 0, 400, 300);
      add(screen2);
      add(screen1);
      revalidate();
      repaint();
    }
  }
}

class RoundedPanel extends JPanel {
  private final Color bg;

  public RoundedPanel(Color bg) {
    this.bg = bg;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(bg);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

    g2.dispose();
    super.paintComponent(g);
  }
}
