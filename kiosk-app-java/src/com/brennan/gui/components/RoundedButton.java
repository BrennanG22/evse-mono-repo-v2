package com.brennan.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import com.brennan.utils.SwingObservableState;

public class RoundedButton extends JButton {
  private Color paintColor;
  private Color activeColor;
  private Color activePressedColor;
  private Color disabledColor;

  private BufferedImage backgroundCache;
  private Dimension lastSize;
  private Color lastColor;

  private boolean btnActive;
  private Runnable onActiveCallBack = null;

  private int listenerId = -1;
  private SwingObservableState<?> boundObservable = null;

  public RoundedButton(String text, boolean isActive, Runnable onActive, Color activeColor, Color activePressedColor,
      Color disabledColor) {
    super(text);
    this.btnActive = isActive;
    this.onActiveCallBack = onActive;
    this.activeColor = activeColor;
    this.activePressedColor = activePressedColor;
    this.disabledColor = disabledColor;

    setFocusPainted(false);
    setContentAreaFilled(false); // Disable default background painting
    setBorderPainted(false);
    setForeground(Color.WHITE);
    // setFont(new Font("Segoe UI", Font.BOLD, 14));
    setUI(new BasicButtonUI());

    addListeners();

    setPaintColor(btnActive ? activeColor : disabledColor);
  }

  private void addListeners() {
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (btnActive) {
          setPaintColor(activePressedColor);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (btnActive) {
          setPaintColor(activeColor);
        }
      }
    });

    this.addActionListener(e -> {
      if (btnActive && (onActiveCallBack != null)) {
        onActiveCallBack.run();
      }
    });

  }

  // This should be avoided as it just is a temporary override of colour
  public void setPaintColor(Color colorToSet) {
    SwingUtilities.invokeLater(() -> {
      this.paintColor = colorToSet;
      this.repaint();
    });
  }

  public void setIsActive(boolean isActive) {
    this.btnActive = isActive;
    if (this.btnActive) {
      setPaintColor(activeColor);
    } else {
      setPaintColor(disabledColor);
    }
  }

  public <T> void setOnStateChange(SwingObservableState<T> observable, Consumer<T> task) {
    if (boundObservable != null && listenerId != -1) {
      boundObservable.removeListener(listenerId);
    }

    boundObservable = observable;
    listenerId = observable.addListener(task);
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

    // Draw text (always live in case it changes)
    Graphics2D g2 = (Graphics2D) g.create();
    FontMetrics fm = g2.getFontMetrics();
    int stringWidth = fm.stringWidth(getText());
    int stringHeight = fm.getAscent();
    int x = (getWidth() - stringWidth) / 2;
    int y = (getHeight() + stringHeight) / 2 - 3;

    g2.setColor(getForeground());
    g2.drawString(getText(), x, y);
    g2.dispose();
  }

}