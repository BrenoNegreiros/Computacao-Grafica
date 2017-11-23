package br.unifor.cg.np2;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Aplicacao extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplicacao frame = new Aplicacao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Aplicacao() {
		setTitle("Joao Vidal e Breno Negreiros - Compuatacao Grafica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		int x0,y0,x1,y1;
		
		JFrame mainFrame = new JFrame("Definindo tamanho do recorte");
        String response = JOptionPane.showInputDialog(mainFrame, "Digite o tamanho do recorte.\n", "100,100,700,500");
        if (response == null) System.exit(0);
        String[] coordinates = response.split(",");
        x0 = Integer.parseInt(coordinates[0]);
      	y0 = Integer.parseInt(coordinates[1]);
        x1 = Integer.parseInt(coordinates[2]);
        y1 = Integer.parseInt(coordinates[3]);
        
        
		getContentPane().add(new PanelRecorte(x0, y0, x1, y1));//Adicionando Componente JPanel
	}

}
