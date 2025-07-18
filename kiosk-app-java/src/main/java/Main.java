import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Screen Transition");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            SlidePanel slidePanel = new SlidePanel();
            frame.add(slidePanel);

            JButton switchBtn = new JButton("Switch Screen");
            switchBtn.addActionListener(e -> slidePanel.startSlide());

            frame.add(switchBtn, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}

class SlidePanel extends JPanel {
    private final JPanel screen1 = new JPanel();
    private final JPanel screen2 = new JPanel();
    private int offsetX = 0;
    private final Timer timer;
    private boolean sliding = false;

    public SlidePanel() {
        setLayout(null); // Manual positioning

        screen1.setBackground(Color.CYAN);
        screen2.setBackground(Color.ORANGE);

        screen1.setBounds(0, 0, 400, 300);
        screen2.setBounds(400, 0, 400, 300); // Start off-screen

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

            // Optional: swap panels
            screen1.setBounds(0, 0, 400, 300);
            screen2.setBounds(400, 0, 400, 300);
        }
        repaint();
    }
}
