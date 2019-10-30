import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Ld3Form {
    private JTextField textFieldX1;
    private JTextField textFieldY1;
    private JTextField textFieldR;
    private JPanel ControlPanel;
    private JPanel drawField;
    private JButton drawButton;
    private JPanel MainField;
    private static Integer previousX = null;
    private static Integer previousY = null;
    private ArrayList<Point> points = new ArrayList<>();
    private Integer[][] combinationCache = new Integer[1000][1000];

    int combinations(int n , int r)
    {
        if (combinationCache[n][r] != null) return combinationCache[n][r];
        if( r== 0 || n == r){
            return 1;
        } else {
            int result = combinations(n-1,r)+combinations(n-1,r-1);
            combinationCache[n][r] = result;
            return result;
        }

    }

    public class LineAnimation extends JPanel {

    }



    public Ld3Form() {
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int x1, y1, R;
                var a = new Point();
                try {
                    points.add(new Point(1, 1));
                    points.add(new Point(300, 300));
                    points.add(new Point(500, 200));
                    drawBesier(points);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Nederīga ievade!", "Kļūda", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        drawField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var position = e.getPoint();

                points.add(new Point(position.x, position.y));
                if (points.size() > 1) {
                    int previousIndex = points.size() - 2;
                    var previousPoint = points.get(previousIndex);
                    int dx = position.x - previousPoint.x, dy = position.y - previousPoint.y;
                    var previousPoints = points;
                   int lastIndex = points.size() - 1;

                    for (double d = 0; d < 1; d += 0.01) {
                        previousPoints.get(lastIndex).x = points.get(previousIndex).x + (int) (dx * d);
                        previousPoints.get(lastIndex).y = points.get(previousIndex).y + (int) (dy * d);
                        drawBesier(previousPoints);


                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    drawBesier(points);
                }

                super.mouseClicked(e);
            }
        });
    }

    private void drawBesier(ArrayList<Point> points) {
        Graphics graphics = drawField.getGraphics();
        var view = drawField.getVisibleRect();
        graphics.clearRect(view.x, view.y, view.height, view.width);
        double x0 = points.get(0).x, y0 = points.get(0).y, x = 0, y = 0;
        double t = 0, ts = 0.01;
        double[] b = new double[points.size()];

        while (t <= 1) {

            for (int index = 0; index < points.size(); index++) {
                b[index] = combinations(points.size() - 1, index)
                        * Math.pow(1 - t, points.size() - 1 - index)
                        * Math.pow(t, index);
                x += b[index] * points.get(index).x;
                y += b[index] * points.get(index).y;
            }

            graphics.drawLine((int) x0, (int) y0, (int) x, (int) y);

            x0 = x;
            y0 = y;
            x = 0;
            y = 0;
            t += ts;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Zīmētājs");
        frame.setContentPane(new Ld3Form().MainField);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
