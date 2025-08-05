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

import com.brennan.utils.SwingObservableState;

public class RoundProgressBar {

  public static JPanel createSOCSection(SwingObservableState<Integer> value) {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.CENTER_ALIGNMENT);


    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setValue((value.get()==null)?0:value.get());
    progressBar.setForeground(Color.RED);
    progressBar.setBackground(Color.LIGHT_GRAY);
    progressBar.setStringPainted(false);
    progressBar.setPreferredSize(new Dimension(200, 30));

    JLabel socLabel = new JLabel(String.format("SOC: %d%%", (value.get()==null)?0:value.get()));
    socLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    socLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(progressBar);
    panel.add(Box.createVerticalStrut(10));
    panel.add(socLabel);
    panel.add(new MetricBox("Energy", "5.2", "kWh"));
    panel.add(new MetricBox("Time Elapsed", "00:12:34", ""));

    value.addListener((Integer tempVal) -> {
      socLabel.setText(String.format("SOC: %d%%", (value.get()==null)?0:value.get()));
      progressBar.setValue(tempVal);
    });

    return panel;
  }
}
