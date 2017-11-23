package br.unifor.cg.np2;

public class RecorteCohenSutherland {

	public static final int INSIDE = 0; //Binario 0000
	public static final int LEFT   = 1; //Binario 0001
	public static final int RIGHT  = 2; //Binario 0010
	public static final int BOTTOM = 4; //Binario 0100
	public static final int TOP    = 8; //Binario 1000
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;


	public RecorteCohenSutherland(int xMinimo, int xMaximo, int yMinimo, int yMaximo){
		this.xMin = xMinimo;
		this.xMax = xMaximo;
		this.yMin = yMinimo;
		this.yMax = yMaximo;
	}

	

	/**
	 * Compute the bit code for a point (x, y) using the clip rectangle
	 * bounded diagonally by (xmin, ymin), and (xmax, ymax)
	 * ASSUME THAT xmax, xmin, ymax and ymin are global constants.
	 * Computes OutCode for given point (x,y)
	 * @param x Horizontal coordinate
	 * @param y Vertical coordinate
	 * @return Computed OutCode
	 */
	private int computeOutCode(double x, double y) {
		int code = INSIDE; // initialised as being inside of [[clip window]]
		System.out.println("xMin"+xMin);
		if (x < xMin) {
			code = code | LEFT; // to the left of clip window
			
		} else if (x > xMax) {
			code = code | RIGHT; // to the right of clip window
		}
		if (y < yMin) {
			code = code | BOTTOM; // below the clip window
		} else if (y > yMax) {
			code = code | TOP; // above the clip window
		}
		System.out.println("Code"+code);
		return code;
	}

	/**
	 * Execute line clipping using Cohen-Sutherland
	 * Taken from: http://en.wikipedia.org/wiki/Cohen-Sutherland
	 * Cohen�Sutherland clipping algorithm clips a line from P0 = (x0, y0) to P1 = (x1, y1) against a rectangle with diagonal from (xmin, ymin) to (xmax, ymax).
	 * @param linha LineSegment to work with
	 * @return Clipped line
	 */
	public SegmentoLinha clip(SegmentoLinha linha) {
		System.out.println("Executando Algoritmo Cohen-Sutherland...");
		int x0 = linha.x0;
		int x1 = linha.x1;
		int y0 = linha.y0;
		int y1 = linha.y1;
		
		// compute outcodes for P0, P1, and whatever point lies outside the clip rectangle
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
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge
				int x, y;

				// At least one endpoint is outside the clip rectangle; pick it.
				int outCodeOut = (outCode0 != 0) ? outCode0 : outCode1;


				// Now find the intersection point;
				// use formulas:
				//   slope = (y1 - y0) / (x1 - x0)
				//   x = x0 + (1 / slope) * (ym - y0), where ym is ymin or ymax
				//   y = y0 + slope * (xm - x0), where xm is xmin or xmax
				if ((outCodeOut & TOP) != 0) { // point is above the clip rectangle
					x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
					y = yMax;
				} else if ((outCodeOut & BOTTOM) != 0) { // point is below the clip rectangle
					x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
					y = yMin;
				} else if ((outCodeOut & RIGHT) != 0) { // point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
					x = xMax;
				} else { // point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
					x = xMin;
				}

				// Now we move outside point to intersection point to clip
				// and get ready for next pass.
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
			return new SegmentoLinha(x0, y0, x1, y1);
		}
		return null;
	}

}
