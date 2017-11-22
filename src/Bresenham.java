
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Bresenham extends JFrame {

	private static final long serialVersionUID = 7716286508117538157L;

	public static void main(String[] args) {

		new Bresenham();
	}

	Bresenham() {

		// initialize the window
		super("Bresenham");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		
		
		
		
		setSize(500, 500);
		getContentPane().add("Center", new CanvasBresenham());
		setVisible(true);
	}
}

class CanvasBresenham extends Canvas {


	int centerX, centerY, pixelSize;

	// initialize the graphics
	void initgr() {

		pixelSize = 10;
		centerX = pixelSize * ((getWidth() / pixelSize) / 2);
		centerY = pixelSize * ((getHeight() / pixelSize) / 2);

	}

	// draw one pixel with center (x, y)
	void putPixel(Graphics g, int x, int y) {
		g.setColor(Color.BLUE);
		g.fillRect((x * pixelSize) - (pixelSize / 2), (y * pixelSize)
				- (pixelSize / 2), pixelSize, pixelSize);
	}

	

	void drawBresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {

		int x, y, dx, dy;
		double gradient, f;

		dy = y2 - y1;
		dx = x2 - x1;

		x = x1;
		y = y1;

		f = 0.0;
		gradient = (double) dy / (double) dx;

		while (x <= x2) {
			putPixel(g, x, y);
			x++;
			f += gradient;
			if (f > 0.5) {
				y++;
				f--;
			}
		}

		// the line to approximate
		g.setColor(Color.black);
		g.drawLine(x1 * pixelSize, y1 * pixelSize, x2 * pixelSize, y2
				* pixelSize);

	}


	
	public void paint(Graphics g) {

		initgr();

		paintCoordinateSystem(g);
		
		String xinicial1 = JOptionPane.showInputDialog("Insira valor do ponto inicial do X : ");
		int xinicial = Integer.parseInt(xinicial1);
		
		String yinicial1 = JOptionPane.showInputDialog("Insira valor do ponto inicial do Y : ");
		int yinicial = Integer.parseInt(yinicial1);   
		
		String xfinal1 = JOptionPane.showInputDialog("Insira valor do ponto Final do X : ");
		int xfinal = Integer.parseInt(xfinal1);
		
		String yfinal1 = JOptionPane.showInputDialog("Insira valor do ponto Final Y : ");
		int yfinal = Integer.parseInt(yfinal1);  
		
		
		drawBresenhamLine(g, xinicial, xinicial, xfinal,yfinal);
		

	}

	private void paintCoordinateSystem(Graphics g) {

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		// coordinate grid
		g.setColor(Color.WHITE);

		for (int i = 0; i <= getWidth(); i += pixelSize) {
			g.drawLine(i, 0, i, getHeight());
			g.drawLine(0, i, getWidth(), i);
		}

		g.setColor(Color.RED);
		g.drawLine(centerX, 0, centerX, getHeight());
		g.drawLine(0, centerY, getWidth(), centerY);
		g.drawLine(centerX - 5, 10, centerX, 0);
		g.drawLine(centerX + 5, 10, centerX, 0);
		g.drawLine(getWidth() - 10, centerY - 5, getWidth(), centerY);
		g.drawLine(getWidth() - 10, centerY + 5, getWidth(), centerY);
		g.drawString("x", getWidth() - 20, centerY - 10);
		g.drawString("y", centerX + 10, 20);
		for (int i = -170; i <= 190; i += 10) {
			g.drawLine(centerX - 4, centerY + i, centerX + 4, centerY + i);
		}
		for (int i = -240; i <= 220; i += 10) {
			g.drawLine(centerX + i, centerY - 4, centerX + i, centerY + 4);
		}

		for (int i = -150; i <= 150; i += 50) {
			g.drawLine(centerX - 10, centerY + i, centerX + 10, centerY + i);
		}
		for (int i = -200; i <= 200; i += 50) {
			g.drawLine(centerX + i, centerY - 10, centerX + i, centerY + 10);
		}
		g.drawString("5", centerX - 25, centerY - 45);
		g.drawString("5", centerX + 45, centerY - 15);

		// translate origin and orientation
		g.translate(centerX, centerY);
		((Graphics2D) g).scale(1.0, -1.0);

		// center
		g.fillOval(-pixelSize / 2, -pixelSize / 2, pixelSize, pixelSize);

	}

}
