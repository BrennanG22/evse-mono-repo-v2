package com.brennan.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
  private final Image originalImage;
  private Image scaledImage;
  private int drawX;
  private int drawY;

  public ImagePanel(String path) {
    // Load the original image from classpath
    originalImage = new ImageIcon(ImagePanel.class.getResource(path)).getImage();
    this.setPreferredSize(new Dimension(400, 200));
  }

  public ImagePanel(BufferedImage image) {
    originalImage = image;
    this.setPreferredSize(new Dimension(400, 200));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (scaledImage == null) {
      precomputeImage();
    }

    if (scaledImage != null) {
      g.drawImage(scaledImage, drawX, drawY, this);
    }
  }

  private void precomputeImage() {
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    int imgWidth = originalImage.getWidth(this);
    int imgHeight = originalImage.getHeight(this);

    if (imgWidth <= 0 || imgHeight <= 0 || panelWidth <= 0 || panelHeight <= 0)
      return;

    // Calculate the scale to fit the panel while preserving aspect ratio
    double panelRatio = (double) panelWidth / panelHeight;
    double imageRatio = (double) imgWidth / imgHeight;

    int drawWidth, drawHeight;

    if (imageRatio > panelRatio) {
      drawWidth = panelWidth;
      drawHeight = (int) (panelWidth / imageRatio);
    } else {
      drawHeight = panelHeight;
      drawWidth = (int) (panelHeight * imageRatio);
    }

    drawX = (panelWidth - drawWidth) / 2;
    drawY = (panelHeight - drawHeight) / 2;

    scaledImage = originalImage.getScaledInstance(drawWidth, drawHeight, Image.SCALE_SMOOTH);
  }
}
