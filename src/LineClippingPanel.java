import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LineClippingPanel extends JPanel {

    public static final int INSIDE = 0;
    public static final int LEFT   = 1;
    public static final int RIGHT  = 2;
    public static final int BOTTOM = 4;
    public static final int TOP    = 8;

    public static final int COHEN_SUTHERLAND = 0;
  

    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    private LineClipper clipper;

    private class LineSegment {
        public int x0;
        public int y0;
        public int x1;
        public int y1;

        public LineSegment(int x0, int y0, int x1, int y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }

        @Override
        public String toString() {
            return "LineSegment(x0: " + x0 + ", y0: " + y0 + "; x1: " + x1 + ", y1: " + y1 + ")";
        }
    }

    public interface LineClipper {
        public LineSegment clip(LineSegment clip);
    }

    public class CohenSutherland implements LineClipper {

        /**
         * Computes OutCode for given point (x,y)
         * @param x Horizontal coordinate
         * @param y Vertical coordinate
         * @return Computed OutCode
         */
        private int computeOutCode(double x, double y) {
            int code = INSIDE;

            if (x < xMin) {
                code |= LEFT;
            } else if (x > xMax) {
                code |= RIGHT;
            }
            if (y < yMin) {
                code |= BOTTOM;
            } else if (y > yMax) {
                code |= TOP;
            }

            return code;
        }

        /**
         * Execute line clipping using Cohen-Sutherland
         * Taken from: http://en.wikipedia.org/wiki/Cohen-Sutherland
         * @param line LineSegment to work with
         * @return Clipped line
         */
        public LineSegment clip(LineSegment line) {
            System.out.println("\nExecutando Cohen-Sutherland...");
            int x0 = line.x0, x1 = line.x1, y0 = line.y0, y1 = line.y1;
            int outCode0 = computeOutCode(x0, y0);
            int outCode1 = computeOutCode(x1, y1);
            System.out.println("OutCode0: " + outCode0 + ", OutCode1: " + outCode1);
            boolean accept = false;

            while (true) {
                if ((outCode0 | outCode1) == 0) { // Bitwise OR is 0. Trivially accept
                    accept = true;
                    break;
                } else if ((outCode0 & outCode1) != 0) { // Bitwise AND is not 0. Trivially reject
                    break;
                } else {
                    int x, y;

                    // Pick at least one point outside rectangle
                    int outCodeOut = (outCode0 != 0) ? outCode0 : outCode1;

                   
                    if ((outCodeOut & TOP) != 0) {
                        x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
                        y = yMax;
                    } else if ((outCodeOut & BOTTOM) != 0) {
                        x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
                        y = yMin;
                    } else if ((outCodeOut & RIGHT) != 0) {
                        y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
                        x = xMax;
                    } else {
                        y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
                        x = xMin;
                    }

                    // Now we move outside point to intersection point to clip
                    if (outCodeOut == outCode0) {
                        x0 = x;
                        y0 = y;
                        outCode0 = computeOutCode(x0, y0);
                    } else {
                        x1 = x;
                        y1 = y;
                        outCode1 = computeOutCode(x1, y1);
                    }
                }
            }
            if (accept) {
                return new LineSegment(x0, y0, x1, y1);
            }
            return null;
        }
        
    }

    
    public LineClippingPanel(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        
           
                clipper = new CohenSutherland();
            
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.blue);
        drawLine(g2d, xMin, 0, xMin, getHeight());
        drawLine(g2d, xMax, 0, xMax, getHeight());
        drawLine(g2d, 0, yMin, getWidth(), yMin);
        drawLine(g2d, 0, yMax, getWidth(), yMax);

        int x0, y0, x1, y1;
        LineSegment line, clippedLine;
        for (int i = 0; i < 10; i++) {
            x0 = (int)(Math.random() * getWidth());
            x1 = (int)(Math.random() * getWidth());
            y0 = (int)(Math.random() * getHeight());
            y1 = (int)(Math.random() * getHeight());
            line = new LineSegment(x0, y0, x1, y1);
            clippedLine = clipper.clip(line);

            System.out.println("Original: " + line);
            System.out.println("Clipped: " + clippedLine);

            if (clippedLine == null) {
                g2d.setColor(Color.red);
                drawLine(g2d, line.x0, line.y0, line.x1, line.y1);
            } else {
            	System.err.println(clippedLine);
                g2d.setColor(Color.red);
                drawLine(g2d, line.x0, line.y0, clippedLine.x0, clippedLine.y0);
                drawLine(g2d, clippedLine.x1, clippedLine.y1, line.x1, line.y1);
                g2d.setColor(Color.green);
                drawLine(g2d, clippedLine.x0, clippedLine.y0, clippedLine.x1, clippedLine.y1);
            }
        }
    }

    
    
    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, getHeight() - y1, x2, getHeight() - y2);
    }

    public static void main(String[] args) {

        

        JFrame mainFrame = new JFrame("Line Clipping");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x0, y0, x1, y1;
        x0 = y0 = x1 = y1 = -1;

        do {
            String response = JOptionPane.showInputDialog(mainFrame, "Digite o tamanho do recorte.\n"
                    + "(0 <= x <= 800 and 0 <= y <= 600)",
                                                          "100,100,700,500");
            if (response == null) System.exit(0);
            String[] coordinates = response.split(",");
            try {
                x0 = Integer.parseInt(coordinates[0]);
                y0 = Integer.parseInt(coordinates[1]);
                x1 = Integer.parseInt(coordinates[2]);
                y1 = Integer.parseInt(coordinates[3]);
            } catch(NumberFormatException ne) {
                JOptionPane.showMessageDialog(mainFrame, "Todos os valores devem ser inteiros");
            } finally {}
        } while (0 > x0 || x1 > 800 || 0 > y0 || y1 > 600 || x0 >= x1 || y0 >= y1);



        
        
        //tamannho do recorte
        mainFrame.add(new LineClippingPanel(x0, y0, x1, y1));
        
        
        
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}