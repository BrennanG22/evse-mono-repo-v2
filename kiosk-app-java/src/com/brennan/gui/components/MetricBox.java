package com.brennan.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.brennan.utils.SwingObservableState;

public class MetricBox<T> extends JPanel {
  public MetricBox(String name, SwingObservableState<T> value, String unit) {

    setLayout(new BorderLayout());
    setMaximumSize(new Dimension(200, 40));
    setBackground(new Color(245, 245, 245));
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        new EmptyBorder(5, 10, 5, 10)));

    JLabel label = new JLabel(name + ":");
    JLabel val = new JLabel( " " + unit);
    val.setFont(new Font("Segoe UI", Font.BOLD, 14));

    value.addListener((valueIn) -> {
      val.setText(valueIn.toString() + " " + unit);
      val.repaint();
    });

    add(label, BorderLayout.WEST);
    add(val, BorderLayout.EAST);
  }

public MetricBox(String name, String value, String unit) {

    setLayout(new BorderLayout());
    setMaximumSize(new Dimension(200, 40));
    setBackground(new Color(245, 245, 245));
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        new EmptyBorder(5, 10, 5, 10)));

    JLabel label = new JLabel(name + ":");
    JLabel val = new JLabel(value + " " + unit);
    val.setFont(new Font("Segoe UI", Font.BOLD, 14));

    add(label, BorderLayout.WEST);
    add(val, BorderLayout.EAST);
  }
}