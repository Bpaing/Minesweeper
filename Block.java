public class Block {
	private boolean clicked;
	private boolean flagged;
	private int num;
	/*
	 -1 = mine
	 0 = empty
	 1-8 = empty with adjacent mines
	 */
	
	public Block(){
		num = 0;
		clicked = false;
		flagged = false;
	}
	
	//places a mine inside the block
	public void arm() {
		num = -1;
	}
	
	//flags block if unflagged, unflags if already flagged.
	public void flag() {
		if (!flagged)
			flagged = true;
		else
			flagged = false;
	}
	
	public void reveal() {
		if(!flagged)
			clicked = true;
	}
	
	//checks if the block has been clicked
	public boolean isOpen() {
		return clicked;
	}

	//checks if the block is flagged
	public boolean hasFlag() {
		return flagged;
	}
	
	//checks if the block contains a mine
	public boolean hasMine() {
		return num == -1;
	}
	
	//returns the block's number of adjacent mines
	public int getNum() {
		return num;
	}
	
	//resets block
	public void reset(){
		num = 0;
		clicked = false;
		flagged = false;
	}
	
	//increments num by 1 for nearby mines
	public void increment() {
		num++;
	}
	
}
