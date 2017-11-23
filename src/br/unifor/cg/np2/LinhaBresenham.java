package br.unifor.cg.np2;

import java.awt.Color;
import java.awt.Graphics;

public class LinhaBresenham {
		
	private int height;
	private int pixelSize;
	public Graphics g;
	
	public LinhaBresenham(Graphics g, int height, int pixelSize) {
		this.g = g;		
		this.height = height;
		this.pixelSize = pixelSize;		
	}
	
    private void plot(int x, int y) {
        g.drawOval(x, y, pixelSize, pixelSize);
    }
	
	
    public void drawLineBresenham(final Color cor, int x1, int y1, int x2, int y2) {
    	g.setColor(cor);
    	y1 = height - y1;
    	y2 = height - y2;

    	// delta de valor exato e valor arredondado da variável dependente
    	int d = 0;

    	int dx = Math.abs(x2 - x1);
    	int dy = Math.abs(y2 - y1);

    	int dx2 = 2 * dx; // slope scaling factors to
    	int dy2 = 2 * dy; // evitar ponto flutuante

    	int ix = x1 < x2 ? 1 : -1; // incrementa direcao
    	int iy = y1 < y2 ? 1 : -1;

    	int x = x1;
    	int y = y1;

    	if (dx >= dy) {
    		while (true) {
    			plot( x, y);
    			if (x == x2)
    				break;
    			x += ix;
    			d += dy2;
    			if (d > dx) {
    				y += iy;
    				d -= dx2;
    			}
    		}
    	} else {
    		while (true) {
    			plot(x, y);
    			if (y == y2)
    				break;
    			y += iy;
    			d += dx2;
    			if (d > dy) {
    				x += ix;
    				d -= dy2;
    			}
    		}
    	}
    }

}
