package br.unifor.cg.np2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelRecorte extends JPanel {

    public static final int INSIDE = 0;
    public static final int LEFT   = 1;
    public static final int RIGHT  = 2;
    public static final int BOTTOM = 4;
    public static final int TOP    = 8;
    
    private Color cor;

    public static final int COHEN_SUTHERLAND = 0;
    
    private final int pixelSize = 5;
    
    private RecorteCohenSutherland corte;
  

    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    public PanelRecorte(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        
           
        corte = new RecorteCohenSutherland(xMin,xMax,yMin,yMax);
            
    }
    
    
 

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
           
        corte = new RecorteCohenSutherland(xMin,xMax,yMin,yMax);
        

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        
        //JANELA - Linhas Desenhadas pelo Java AWT
        g2d.setColor(Color.CYAN);
        g2d.drawLine(xMin, getHeight(), xMin, 0);
        g2d.drawLine(xMax, getHeight(), xMax, 0);
        g2d.drawLine(0, getHeight() - yMin, getWidth(), getHeight() - yMin);
        g2d.drawLine(0, getHeight() - yMax, getWidth(), getHeight() - yMax);
        
        
        //Criando Objeto para Desenhar as Linhas
        LinhaBresenham bresenham = new LinhaBresenham(g2d, getHeight(), pixelSize);
        
        
        int x0, y0, x1, y1;
        SegmentoLinha linha, linhaCortada;
        for (int i = 0; i < 10; i++) { //Determina o Tamanho de LInhas
            x0 = (int)(Math.random() * getWidth());
            x1 = (int)(Math.random() * getWidth());
            y0 = (int)(Math.random() * getHeight());
            y1 = (int)(Math.random() * getHeight());
            linha = new SegmentoLinha(x0, y0, x1, y1);
            linhaCortada = corte.clip(linha);

            System.out.println("Original: " + linha);
            System.out.println("Cortada: " + linhaCortada);

            if (linhaCortada == null) {
            	this.cor = Color.red;
            	System.out.println("If Cor: "+cor);
                bresenham.drawLineBresenham(cor, linha.x0, linha.y0, linha.x1, linha.y1);
            } else {
            	System.err.println(linhaCortada);
            	this.cor = Color.red;
            	bresenham.drawLineBresenham(cor, linha.x0, linha.y0, linhaCortada.x0, linhaCortada.y0);
                bresenham.drawLineBresenham(cor, linhaCortada.x1, linhaCortada.y1, linha.x1, linha.y1);
                this.cor = Color.green;                
                bresenham.drawLineBresenham(cor, linhaCortada.x0, linhaCortada.y0, linhaCortada.x1, linhaCortada.y1);
            }
        }
    }
  



}