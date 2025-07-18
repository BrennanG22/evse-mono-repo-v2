import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modern Swing UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null); // Center on screen

            SlidePanel slidePanel = new SlidePanel();
            frame.add(slidePanel);

            RoundedButton switchBtn = new RoundedButton("Switch Screen");
            switchBtn.addActionListener(e -> slidePanel.startSlide());

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(new Color(245, 245, 245));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            btnPanel.add(switchBtn);

            frame.add(btnPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
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
            JPanel temp = screen1;
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

class RoundedButton extends JButton {
    private Color normalColor = new Color(33, 150, 243);
    private Color hoverColor = new Color(30, 136, 229);
    private boolean hovered = false;

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false); // Disable default background painting
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setUI(new BasicButtonUI());

    }

    @Override
    protected void paintComponent(Graphics g) {
        int arc = 20;
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hovered ? hoverColor : normalColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Draw text
        FontMetrics fm = g2.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - 3;

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(140, 40);
    }
}