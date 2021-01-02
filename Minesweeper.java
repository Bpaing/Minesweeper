import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Minesweeper {
		private JFrame frame;
		private Board panel;
		
	public Minesweeper() {
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Board(8, 8, 10);
		panel.setPreferredSize(new Dimension(panel.getBoard()[0].length*panel.size, panel.getBoard().length*panel.size+panel.menuSpace+panel.spacing));
		JMenuBar menubar = new JMenuBar();
		JMenu size = new JMenu("size");
		JMenu cheat = new JMenu("cheat");
		JMenu theme = new JMenu("themes");
		JMenuItem easy = new JMenuItem("Beginner (8x8, 10 mines)");
		JMenuItem medium = new JMenuItem("Intermediate (16x16, 40 mines)");
		JMenuItem hard = new JMenuItem("Expert (16x30, 99 mines)");
		JMenuItem xray = new JMenuItem("x-ray vision");
		JMenuItem nostalgia = new JMenuItem("nostalgia");
		JMenuItem white = new JMenuItem("white"); 
		JMenuItem yellow = new JMenuItem("yellow");
		JMenuItem orange = new JMenuItem("orange");
		JMenuItem green = new JMenuItem("green");
		JMenuItem blue = new JMenuItem("blue");
		size.add(easy);
		size.add(medium);
		size.add(hard);
		cheat.add(xray);
		theme.add(nostalgia);
		theme.add(white);
		theme.add(yellow);
		theme.add(orange);
		theme.add(green);
		theme.add(blue);
		menubar.add(size);
		menubar.add(cheat);
		menubar.add(theme);
		frame.setJMenuBar(menubar);
		MenuListener menu = new MenuListener();
		easy.addActionListener(menu);
		medium.addActionListener(menu);
		hard.addActionListener(menu);
		xray.addActionListener(menu);
		panel.addMouseListener(new Clicker());
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void resize(int row, int col, int mines) {
		frame.setResizable(true);
		panel = new Board(row, col, mines);
		panel.setPreferredSize(new Dimension(panel.getBoard()[0].length*panel.size, panel.getBoard().length*panel.size+panel.menuSpace+panel.spacing));
		panel.addMouseListener(new Clicker());
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);
	}
	
	class MenuListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Beginner (8x8, 10 mines)")) {
				resize(8, 8, 10);
			} else if (e.getActionCommand().equals("Intermediate (16x16, 40 mines)")) {
				resize(16, 16 , 40);
			} else if (e.getActionCommand().equals("Expert (16x30, 99 mines)")) {
				resize(16, 30, 99);
			}
			if (e.getActionCommand().equals("x-ray vision")) {
				panel.toggleCheat();
			}
			if (e.getActionCommand().equals("nostalgia")) {
				panel.changeTheme(1);
			} else if (e.getActionCommand().equals("white")) {
				panel.changeTheme(2);
			} else if (e.getActionCommand().equals("yellow")) {
				panel.changeTheme(3);
			} else if (e.getActionCommand().equals("orange")) {
				panel.changeTheme(4);
			} else if (e.getActionCommand().equals("green")) {
				panel.changeTheme(5);
			} else if (e.getActionCommand().equals("blue")) {
				panel.changeTheme(6);
			}
		}
	}
	
	class Clicker implements MouseListener{
		public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) 
	            panel.checkClick(e.getX(), e.getY());
	        if(e.getButton() == MouseEvent.BUTTON3) 
	            panel.checkFlag(e.getX(), e.getY());
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
}
