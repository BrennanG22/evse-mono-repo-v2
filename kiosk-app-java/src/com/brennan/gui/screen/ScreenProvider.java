package com.brennan.gui.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Flow;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.batik.swing.JSVGCanvas;

import com.brennan.datastate.EVSEDataState;
import com.brennan.gui.components.*;
import com.formdev.flatlaf.FlatLightLaf;

/*
 * Provides the screens for the GUI
 */

public class ScreenProvider {
  private BufferedImage uofaLogo;

  public ScreenProvider() {
    try {
      URL logoURL = ScreenProvider.class.getResource("/assets/university-of-alberta-logo.png");
      if (logoURL == null) {
        throw new IllegalStateException("Image not found: /assets/university-of-alberta-logo.png");
      }
      uofaLogo = ImageIO.read(logoURL);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load university logo", e);
    }
  }

  public Screen getTestScreen(ScreenHost screenHost, EVSEDataState state) {
    Screen panel = new Screen();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JPanel outer = new JPanel();
    outer.setLayout(new BorderLayout());

    JPanel logo = new ImagePanel(uofaLogo);
    outer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    outer.add(logo);
    panel.add(outer);

    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new GridLayout(0, 2));

    JPanel panel1 = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    RoundedButton btn1 = new RoundedButton("Test", true,
        () -> {
          screenHost.swipeTransition(this.getVerifyScreen(state, screenHost));
        },
        new Color(30, 180, 50), new Color(30, 120, 50),
        new Color(70, 70, 70));

    btn1.setOnStateChange(state.verifyComplete, (value) -> {
      if (value) {
        btn1.setPaintColor(new Color(30, 80, 30));
      } else {
        btn1.setPaintColor(new Color(80, 30, 30));
      }
    });

    panel1.add(btn1);
    btnPanel.add(panel1);

    JPanel panel2 = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    RoundedButton btn2 = new RoundedButton("Test", true, () -> {
      state.verifyComplete.set(!state.verifyComplete.get());
    },
        new Color(30, 180, 50), new Color(30, 120, 50), new Color(70, 70, 70));
    panel2.add(btn2);
    btnPanel.add(panel2);

    panel.add(btnPanel);

    return panel;
  }

  public Screen getVerifyScreen(EVSEDataState state, ScreenHost screenHost) {
    Screen panel = new Screen();
    panel.setLayout(new BorderLayout());

    // ----- Center: Image Indicator Panel -----
    JPanel indicatorPanel = new JPanel(new GridLayout(1, 2, 40, 0)); // horizontal gap between images

    JSVGCanvas leftImage = new JSVGCanvas();
    leftImage.setURI(getClass().getResource("/assets/contactless.svg").toString());

    ImagePanel rightImage = new ImagePanel("/assets/electric_car.svg");

    indicatorPanel.add(leftImage);
    indicatorPanel.add(rightImage);

    panel.add(indicatorPanel, BorderLayout.CENTER);

    // ----- Bottom: Buttons -----
    RoundedButton nextButton = new RoundedButton(
        "Next", true, null,
        new Color(30, 180, 50), new Color(30, 120, 50), new Color(70, 70, 70));
    nextButton.setPreferredSize(new Dimension(150, 60));

    RoundedButton cancelButton = new RoundedButton(
        "Cancel", true,
        () -> screenHost.swipeTransition(this.getTestScreen(screenHost, state)),
        new Color(30, 180, 50), new Color(30, 120, 50), new Color(70, 70, 70));
    cancelButton.setPreferredSize(new Dimension(150, 60));

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10)); // spacing between buttons
    btnPanel.add(cancelButton);
    btnPanel.add(nextButton);

    JPanel btnWrapper = new JPanel(new BorderLayout());
    btnWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // bottom margin
    btnWrapper.add(btnPanel, BorderLayout.CENTER);

    panel.add(btnWrapper, BorderLayout.SOUTH);

    return panel;
  }

  public Screen getChargeStateScreen(EVSEDataState state) {

    try {
      UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (Exception ex) {
      System.err.println("Failed to initialize LaF");
    }
    Screen panel = new Screen();
    JPanel root = new JPanel();
    root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
    root.setBackground(Color.WHITE);
    root.setBorder(new EmptyBorder(20, 20, 20, 20));

    // SOC Progress Circle Area
    root.add(RoundProgressBar.createSOCSection());

    // Three-column metrics section
    root.add(createMetricsRow(state));

    // Bottom buttons
    root.add(Box.createVerticalStrut(20));
    root.add(createButtonRow());

    panel.add(root);
    return panel;
  }

  public Screen getChargeStateA() {
    Screen mainPanel = new Screen();
    mainPanel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = 1;
    constraints.insets = new Insets(10, 10, 10, 10);


    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weighty = 1;
    constraints.anchor = GridBagConstraints.CENTER;
    constraints.fill = GridBagConstraints.HORIZONTAL;

    mainPanel.add(RoundProgressBar.createSOCSection(), constraints);

    JPanel statsPanel = new JPanel();
    statsPanel.add(new MetricBox<>("Test", "Test", "Test"));

    statsPanel.setOpaque(true);
    statsPanel.setBackground(Color.LIGHT_GRAY);

    constraints.gridy = 1;
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.CENTER;
    constraints.weighty = 1;


    mainPanel.add(statsPanel, constraints);

    constraints.fill = GridBagConstraints.HORIZONTAL;

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
    btnPanel.add(new RoundedButton("Test1", true, null,
        new Color(20, 180, 50), new Color(20, 150, 50),
        new Color(110, 110, 110)));
    btnPanel.setOpaque(true); // To help visually debug
    btnPanel.setBackground(Color.LIGHT_GRAY); // See if spacing shows up
    btnPanel.add(new JButton("Test2"));
    
    constraints.gridy = 2;
    constraints.weighty = 0.1;

    mainPanel.add(btnPanel, constraints);

    return mainPanel;
  }

  static class ReadoutBox extends JPanel {
    private final JTextArea textArea;
    private final Color paintColor;
    private BufferedImage backgroundCache;
    private Dimension lastSize;
    private Color lastColor;

    public ReadoutBox(String text, Color paintColor) {
      this.paintColor = paintColor;
      setLayout(new BorderLayout());
      setOpaque(false);

      textArea = new JTextArea(text);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setEditable(false);
      textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      textArea.setOpaque(false); // Let background show through
      textArea.setForeground(Color.DARK_GRAY);
      textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

      add(textArea, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
      Dimension size = getSize();
      if (backgroundCache == null || !size.equals(lastSize) || !paintColor.equals(lastColor)) {
        backgroundCache = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = backgroundCache.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(paintColor);
        g2.fillRoundRect(0, 0, size.width, size.height, 20, 20);
        g2.dispose();

        lastSize = size;
        lastColor = paintColor;
      }

      g.drawImage(backgroundCache, 0, 0, null);
      super.paintComponent(g);
    }
  }

  private JPanel createMetricsRow(EVSEDataState state) {
    JPanel row = new JPanel(new GridLayout(1, 3, 15, 0));
    row.setMaximumSize(new Dimension(800, 150));
    row.setOpaque(false);

    row.add(buildColumn("Connection", new MetricBox("Status", "Online", "")));
    row.add(buildColumn("Voltage / Current", new MetricBox("Voltage", "410", "V"),
        new MetricBox("Current", "32", "A"),
        new MetricBox<Float>("Power", state.power, "kW")));
    row.add(buildColumn("Timing", new MetricBox("Time to Bulk", "00:40:30", ""),
        new MetricBox("Estimated TOD", "12:30", "")));
    return row;
  }

  private JPanel buildColumn(String title, JComponent... children) {
    JPanel col = new JPanel();
    col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
    col.setOpaque(false);
    col.add(new JLabel("<html><h2>" + title + "</h2></html>"));
    for (JComponent c : children) {
      col.add(Box.createVerticalStrut(5));
      col.add(c);
    }
    return col;
  }

  private JPanel createButtonRow() {
    JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
    row.setMaximumSize(new Dimension(800, 80));

    JButton startBtn = new JButton("Start Charging");
    startBtn.setBackground(new Color(0x4CAF50)); // Green
    startBtn.setForeground(Color.WHITE);

    JButton stopBtn = new JButton("Stop Charging");
    stopBtn.setBackground(new Color(0xF44336)); // Red
    stopBtn.setForeground(Color.WHITE);

    row.add(startBtn);
    row.add(stopBtn);
    return row;
  }

}
