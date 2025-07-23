package com.brennan.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class RoundProgressBar {
  public static JPanel createSOCSection() {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Placeholder for circular progressbar (real implementation would use custom
    // paint)
    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue(65);
    progressBar.setForeground(Color.RED);
    progressBar.setBackground(Color.LIGHT_GRAY);
    progressBar.setStringPainted(false);
    progressBar.setPreferredSize(new Dimension(200, 30)); // Not circular, but placeholder

    JLabel socLabel = new JLabel("SOC: 65%");
    socLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    socLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(progressBar);
    panel.add(Box.createVerticalStrut(10));
    panel.add(socLabel);
    panel.add(new MetricBox("Energy", "5.2", "kWh"));
    panel.add(new MetricBox("Time Elapsed", "00:12:34", ""));

    return panel;
  }
}
