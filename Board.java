import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JComponent;

public class Board extends JComponent{
	private Block[][] grid;
	private int numMines;
	protected final int size = 35;
	protected final int spacing = 1;
	protected final int menuSpace = size*2;
	private final int smiley = 40;
	private final int smileyBox = (int) (smiley*1.2);
	private final int scoreBox = smileyBox*2;
	private boolean cheat;
	private int numBlocks;
	private int gameState;
	/*
	 -1 = lose 
	 0 = playing
	 1 = win
	 */
	
	private static int theme = 1;
	//color customization.
	private Color color1;
	private Color color2;
	private Color color3;
	private Color color4;
	//nostalgia
	private Color nostalgia1 = new Color(188, 188, 188);
	private Color nostalgia2 = new Color(126, 126, 126);
	private Color nostalgia3 = new Color(155, 155, 155);
	private Color nostalgia4 = new Color(10, 1, 1);
	//white
	private Color white1 = new Color(248, 246, 233);
	private Color white2 = new Color(40, 40, 38);
	private Color white3 = new Color(10, 10, 9);
	private Color white4 = new Color(96, 91, 80);
	//yellow
	private Color yellow1 = new Color(211,185,90);
	private Color yellow2 = new Color(9,12,21);
	private Color yellow3 = new Color(148, 130, 64);
	private Color yellow4 = new Color(84, 79, 61);
	//orange
	private Color orange1 = new Color (230, 160, 64);
	private Color orange2 = new Color(6, 9, 14);
	private Color orange3 = new Color(28, 23, 34);
	private Color orange4 = new Color(103, 108, 124);
	//green
	private Color green1 = new Color(90, 183, 29);
	private Color green2 = new Color(3, 6, 8);
	private Color green3 = new Color(19, 71, 47);
	private Color green4 = new Color(75, 96, 89);
	//blue
	private Color blue1 = new Color(112, 186, 196);
	private Color blue2 = new Color(3, 4, 6);
	private Color blue3 = new Color(201, 189, 175);
	private Color blue4 = new Color(58, 66, 87);
	
	public Board(int rIn, int cIn, int mIn) {
		grid = new Block[rIn][cIn];
		numMines = mIn;
		numBlocks = rIn*cIn;
		cheat = false;
		for(int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = new Block();
			}
		}
		this.changeTheme(theme);
		newGame();
	}
	
	//resets the board
	public void newGame() {
		gameState = 0;
		numBlocks = grid.length*grid[0].length;
		for (Block[] row: grid) {
			for (Block block: row) {
				block.reset();
			}
		}
		plantMines();
		scanMines();
	}
	
	
	//plants mines randomly on the board
	public void plantMines(){
	    Random r = new Random();
	    for (int count = 0; count < numMines; count++){
	      int row = r.nextInt(grid.length);
	      int col = r.nextInt(grid[0].length);
	      if (!(grid[row][col].hasMine()))
	        grid[row][col].arm();
	      else
	        count--;
	    }
	  }
	
	// Scans the board. When it finds a mine it increments all adjacent blocks that do not have mines.
	public void scanMines(){
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col].hasMine()) {
					for (int r = row-1; r <= row+1 && r < grid.length; r++) {
						for (int c = col-1; c <= col+1 && c < grid[row].length; c++) {
							if (r > -1 && c > -1)
								if(!grid[r][c].hasMine()) 
									grid[r][c].increment();
						}
					}
				}
			}
		}
	}
	
	//Opens a block and recurses when an empty box with no adjacent mines is found
	public void openBlock(int row, int col){
		 if (row < 0 || row > grid.length - 1 || col < 0 || col > grid[0].length - 1)
			 return;
		 if (grid[row][col].hasMine())
			 return;
		 else if (!grid[row][col].isOpen() && !grid[row][col].hasFlag()){
			 grid[row][col].reveal();
			 numBlocks--;
			 if(grid[row][col].getNum() == 0) {
				 for(int r = row-1; r <= row+1; r++) {
					 for(int c = col-1; c <= col+1; c++) {
						 openBlock(r, c);
					 }
				 }
			 }
		 }
		 repaint();
	}
	
	
	public void checkClick(int x, int y) {
		//checks blocks for clicks
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				int posX = size*col;
				int posY = size*row+ menuSpace;
				   if (x >= posX && x < posX+size && y >= posY && y < posY+size) {
					   if(!(grid[row][col].isOpen()) && gameState == 0 && !(grid[row][col].hasFlag())) {
						   openBlock(row, col);
						   if(grid[row][col].hasMine())
							   gameState = -1;
						   if(numBlocks == numMines)
							   gameState = 1;
					   }
				}
			}		
		}
		//resets game if smiley box is clicked
		if (x >= (grid[0].length*size-smileyBox)/2 && x <= (grid[0].length*size-smileyBox)/2+smileyBox && y >= (menuSpace-smileyBox)/2 && y <= (menuSpace-smileyBox)/2+smileyBox){
			newGame();
		}
		repaint();
	}
	
	public void checkFlag(int x, int y) {
		//checks for block clicks to flag
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				int posX = size*col;
				int posY = size*row+ menuSpace;
				   if (x >= posX && x < posX+size && y >= posY && y < posY+size && gameState == 0) {
					grid[row][col].flag();
				}
			}		
		}
		repaint();
	}
	
	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		//menuSpace and background
		g.setColor(color2);
		g.fillRect(0, 0, grid[0].length*size, grid.length*size+menuSpace);
		g.setColor(color3);
		g.fillRect(0, 0, grid[0].length*size, menuSpace-spacing);
		//menu boxes
		if(color1.equals(nostalgia1))
			g.setColor(nostalgia2);
		else
			g.setColor(color4);
		g.fillRect((grid[0].length*size-smileyBox)/2, (menuSpace-smileyBox)/2, smileyBox, smileyBox);
		g.setColor(color4);
		g.fillRect((menuSpace-scoreBox/2)/2, (menuSpace-scoreBox/2)/2, scoreBox, scoreBox/2);
		if(color1.equals(nostalgia1))
			g.setColor(Color.RED);
		else
			g.setColor(color1);
		g.setFont(new Font("Roboto Mono", Font.BOLD, scoreBox/2-5));
		g.drawString(String.valueOf(numMines), scoreBox/5, menuSpace-scoreBox/5);
		//smiley face
		if(color1.equals(nostalgia1))
			g.setColor(Color.YELLOW);
		else
			g.setColor(color1);
		g.fillOval((grid[0].length*size-smiley)/2, (menuSpace-smiley)/2, smiley, smiley);
		if(color1.equals(nostalgia1))
			g.setColor(color4);
		else
			g.setColor(color2);
		if(gameState == 0) {
			//eyes
			g.fillRect(grid[0].length*size/2-smiley/4, menuSpace-smiley, smiley/8, smiley/8);
			g.fillRect(grid[0].length*size/2+smiley/8, menuSpace-smiley, smiley/8, smiley/8);
			//mouth
			g.fillRect(grid[0].length*size/2-smiley/4, (int)(menuSpace-smiley/1.5), smiley/2, smiley/15);
		} else {
			if(gameState == -1) {
				//game over eyes
				g.drawLine(grid[0].length*size/2-smiley/5-smiley/10, menuSpace-smiley, grid[0].length*size/2-smiley/10, menuSpace-smiley+smiley/5);
				g.drawLine(grid[0].length*size/2-smiley/10, menuSpace-smiley, grid[0].length*size/2-smiley/5-smiley/10, menuSpace-smiley+smiley/5);
				g.drawLine(grid[0].length*size/2+smiley/5+smiley/10, menuSpace-smiley, grid[0].length*size/2+smiley/10, menuSpace-smiley+smiley/5);
				g.drawLine(grid[0].length*size/2+smiley/10, menuSpace-smiley, grid[0].length*size/2+smiley/5+smiley/10, menuSpace-smiley+smiley/5);
				//game over mouth
				g.fillArc(grid[0].length*size/2-smiley/4, (int)(menuSpace-smiley/1.5), smiley/2, smiley/4, 0, 180);	
			} else if (gameState == 1) {
				g.fillRect(grid[0].length*size/2-smiley/4, menuSpace-smiley, smiley/8, smiley/8);
				g.fillRect(grid[0].length*size/2+smiley/8, menuSpace-smiley, smiley/8, smiley/8);
				//win mouth
				g.fillArc(grid[0].length*size/2-smiley/4, (int)(menuSpace-smiley/1.25), smiley/2, smiley/4, 30, -240);	
			}
		}
		//blocks
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if(grid[row][col].isOpen() && !(grid[row][col].hasMine())) {
					//open blocks
					if(color1.equals(nostalgia1))
						g.setColor(color3);
					else
						g.setColor(color4);
					g.fillRect(size*col+spacing, size*row+spacing + menuSpace, size-(2*spacing), size-(2*spacing));
					//sets color based on number of adjacent mines and draws it
					if(grid[row][col].getNum() == 1)
						if(color1.equals(nostalgia1))
							g.setColor(Color.BLUE);
						else
							g.setColor(Color.WHITE);
					else if (grid[row][col].getNum() == 2)
						g.setColor(Color.GREEN);
					else if (grid[row][col].getNum() == 3)
						g.setColor(Color.RED);
					else if (grid[row][col].getNum() == 4)
						g.setColor(Color.YELLOW);
					else if (grid[row][col].getNum() == 5)
						g.setColor(Color.PINK);
					else if (grid[row][col].getNum() == 6)
						g.setColor(Color.ORANGE);
					else if (grid[row][col].getNum() == 7)
						g.setColor(Color.MAGENTA);
					else if (grid[row][col].getNum() == 8)
						g.setColor(Color.CYAN);
					g.setFont(new Font("Verdana", Font.BOLD, size-8));
					if(grid[row][col].getNum() > 0)
						g.drawString(String.valueOf(grid[row][col].getNum()), size*col+size/4, size*row+menuSpace+27);
					} else {
						//paints mine locations if cheats are on
						if(cheat && grid[row][col].hasMine()) {
							g.setColor(Color.RED);
						} else
						//paints unopened boxes
						g.setColor(color1);
						g.fillRect(size*col+spacing, size*row+spacing+menuSpace, size-(2*spacing), size-(2*spacing));
						//paints flags. If the game is over and an open block is flagged, it will be replaced with an X.
						if(grid[row][col].hasFlag()) {
							g.setColor(Color.BLACK);
							if(gameState != 0 && !grid[row][col].hasMine()) {
								g.drawLine(size*col+spacing, size*row+spacing+menuSpace, size*col+spacing+size-(2*spacing), size*row+spacing+menuSpace+size-(2*spacing));
								g.drawLine(size*col+spacing+size-(2*spacing), size*row+spacing+menuSpace, size*col+spacing, size*row+spacing+menuSpace+size-(2*spacing));
							} else {
								g.fillRect(size*col+spacing+size/4, size*row+spacing+size/2+size/4+menuSpace,size/2, size/8);
								g.fillRect(size*col+spacing+size/2-size/20, size*row+spacing+menuSpace+size/4,size/10, size/2);
								g.setColor(Color.RED);
								g.fillRect(size*col+spacing+size/2-size/3+size/20, size*row+spacing+menuSpace+size/4, size/3, size/4);
							}
						}
					}
					//reveals all mine locations when game is lost
					if(grid[row][col].hasMine() && gameState == -1 && !grid[row][col].hasFlag()) {
						g.setColor(Color.BLACK); 
						g.fillRect(size*col+spacing+size/4, size*row+spacing+size/4+menuSpace,size/2, size/2);
					}
			}
		}
	}	
	
	//cheat method for menu bar
	public void toggleCheat() {
		if (!cheat)
			cheat = true;
		else
			cheat = false;
		repaint();
	}
	
	//changes color theme according to their order in the menu
	public void changeTheme(int num) {
		theme = num;
		//nostalgia
		if (num == 1) {
			color1 = nostalgia1;
			color2 = nostalgia2;
			color3 = nostalgia3;
			color4 = nostalgia4;
		} //white
		else if (num == 2) {
			color1 = white1;
			color2 = white2;
			color3 = white3;
			color4 = white4;
		} //yellow
		else if (num == 3) {
			color1 = yellow1;
			color2 = yellow2;
			color3 = yellow3;
			color4 = yellow4;
		} //orange
		else if (num == 4) {
			color1 = orange1;
			color2 = orange2;
			color3 = orange3;
			color4 = orange4;
		} //green
		else if (num == 5) {
			color1 = green1;
			color2 = green2;
			color3 = green3;
			color4 = green4;
		} //blue
		else if (num == 6) {
			color1 = blue1;
			color2 = blue2;
			color3 = blue3;
			color4 = blue4;
		}
		repaint();
	}
	
	//returns 2D array
	public Block[][] getBoard(){
		return grid;
	}
	
}
