package com.brennan.gui.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.batik.swing.JSVGCanvas;

import com.brennan.datastate.EVSEDataState;
import com.brennan.gui.components.ImagePanel;
import com.brennan.gui.components.RoundedButton;

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

}
