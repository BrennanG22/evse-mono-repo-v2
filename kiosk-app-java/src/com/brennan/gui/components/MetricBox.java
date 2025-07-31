package com.brennan.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.brennan.utils.SwingObservableState;

public class MetricBox<T> extends JPanel {
  public MetricBox(String name, SwingObservableState<T> value, String unit) {

    setLayout(new BorderLayout());
    // setMaximumSize(new Dimension(200, 40));
    setBackground(new Color(245, 245, 245));
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        new EmptyBorder(5, 10, 5, 10)));

    JLabel label = new JLabel(name + ":", SwingConstants.CENTER);
    JLabel val = new JLabel(String.format("%-2.2s %s", (value.get()!=null)?"--":value.get(), unit), SwingConstants.CENTER);
    val.setFont(new Font("Segoe UI", Font.BOLD, 14));

    value.addListener((valueIn) -> {
      val.setText(String.format("%-7.7s %s", valueIn, unit));
      val.repaint();
    });

    add(label, BorderLayout.NORTH);
    add(val, BorderLayout.CENTER);
  }

  public MetricBox(String name, String value, String unit) {

    setLayout(new BorderLayout());
    // setMaximumSize(new Dimension(200, 40));
    setBackground(new Color(245, 245, 245));
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        new EmptyBorder(5, 10, 5, 10)));

    JLabel label = new JLabel(name + ":", SwingConstants.CENTER);
    JLabel val = new JLabel(String.format("%5.5s %s", !value.isBlank()?value:"--", unit), SwingConstants.CENTER);
    val.setFont(new Font("Segoe UI", Font.BOLD, 14));

    add(label, BorderLayout.NORTH);
    add(val, BorderLayout.CENTER);
  }
}