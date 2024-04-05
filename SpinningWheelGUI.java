import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpinningWheelGUI extends JPanel implements ActionListener {
    Timer timer;
    private double angle = 0;
    private double angleIncrement = 3 + Math.random() * 2;
    private double slowDownRate = 0.999; // slow down rate
    private boolean spinning = true;

    public SpinningWheelGUI() {
        // Dimensions of the wheel
        setPreferredSize(new Dimension(400, 400));
        // Change the delay to change the speed
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int radius = 150;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int numSegments = 24;
        double degreesPerSegment = 360.0 / numSegments;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Rotates the entire graphics context
        g2d.rotate(Math.toRadians(angle), centerX, centerY);

        // Draws the wheel segments
        for (int i = 0; i < numSegments; i++) {
            if (i % 2 == 0) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }
            double startAngle = i * degreesPerSegment;
            g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, (int)startAngle, (int)degreesPerSegment);
        }

        // Draws the numbers on the segments
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        FontMetrics metrics = g2d.getFontMetrics();
        for (int i = 0; i < numSegments; i++) {
            String numberText = Integer.toString(i + 1);
            double textAngleRadians = Math.toRadians((i + 0.5) * degreesPerSegment);
            int textRadius = radius - 20;
            int textX = centerX + (int) (textRadius * Math.sin(textAngleRadians));
            int textY = centerY - (int) (textRadius * Math.cos(textAngleRadians));
            int textWidth = metrics.stringWidth(numberText);
            int textHeight = metrics.getHeight();
            g2d.drawString(numberText, textX - textWidth / 2, textY + textHeight / 4);
        }
        Graphics2D g2 = (Graphics2D) g;
        int arrowHeight = 40;
        int arrowWidth = 15;
        int arrowX = centerX - arrowWidth / 2; // Center of the arrow base aligned with the center of the wheel
        int arrowY = centerY - radius - arrowHeight / 2; // Positioned just above the wheel
        Polygon arrow = new Polygon();
        arrow.addPoint(arrowX, arrowY); // Left base
        arrow.addPoint(arrowX + arrowWidth, arrowY); // Right base
        arrow.addPoint(arrowX + arrowWidth / 2, arrowY + arrowHeight); // Tip pointing downwards
        g2.setColor(Color.BLACK);
        g2.fillPolygon(arrow);
        // Disposes of the graphics copy
        g2d.dispose();



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (spinning) {
            angle += angleIncrement;
            angleIncrement *= slowDownRate; // applies the slowdown rate
            if (angleIncrement < 0.03) {    // stops the wheel when increment is small enough
                spinning = false;
                angleIncrement = 0;
            }
        }
        repaint();
    }

    //starts slowing down the wheel
    public void startSlowingDown() {
        spinning = true;
        slowDownRate = 0.99; //  quicker or slower stop
    }

    //  starts spinning the wheel again
    public void startSpinning() {
        spinning = true;

        slowDownRate = 0.999;   // resets the slow down rate
    }

    // Added main here to test with
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Victim Picker Wheel");
            SpinningWheelGUI spinningWheelGUI = new SpinningWheelGUI();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(spinningWheelGUI);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Example usage, adjust delay accodingly, depends on how long we want the wheel to spin
            new Timer(10000, e -> {
                spinningWheelGUI.startSlowingDown();
            }).start();
        });

    }
}
