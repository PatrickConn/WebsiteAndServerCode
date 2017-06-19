import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CheckersBoard extends JPanel implements MouseListener {

	CheckersDriver main;
	Board board;
	public boolean gameInProgess;
	public ArrayList<Board> legalMoves;
	int selectedRow, selectedCol;
	int currentPlayer;
	AIplayer AI;
	public ArrayList<Board> AIlegalMoves;


	public CheckersBoard() {

	}
	public CheckersBoard(CheckersDriver main) {


		addMouseListener(this);

		board = new Board();
		AI = new AIplayer(board,this);
		AI.computerPlay();

		this.main = main;
		newGame();
	}



	public void newGame() {
		board.setPieces(); 

		gameInProgess = true;
		currentPlayer = Board.RED;
		legalMoves = board.getLegalMoves(Board.RED);
		selectedRow = -1;

		repaint();
	}

	public void paintComponent(Graphics g) {
		g.drawRect(29,20,getSize().width-79,getSize().height-79);  
		Image crown = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream crownInput = classLoader.getResourceAsStream("crown.png");
		try {
			crown = ImageIO.read(crownInput);

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {   
				if ( row % 2 == col % 2 ) {
					g.setColor(Color.white);
					g.fillRect(30+col*80, 21+row*80, 80,80);
				}
				else {
					g.setColor(Color.black);
					g.fillRect(30+col*80, 21+row*80, 80,80);
				}
				switch(board.pieceAt(row, col)) {
				case Board.WHITE:	
					g.setColor(Color.white);
					g.fillOval(32 + col*80, 23 + row*80, 75, 75);
					break;
				case Board.RED:
					g.setColor(Color.red);
					g.fillOval(32 + col*80, 23 + row*80, 75, 75);
					break;	
				case Board.WHITEKING:
					g.setColor(Color.white);
					g.fillOval(32 + col*80, 23 + row*80, 75, 75);
					g.setColor(Color.red);
					g.drawImage(crown,32 + col*80, 23 + row*80, 75, 75,null);
					break;
				case Board.REDKING:
					g.setColor(Color.red);
					g.fillOval(32 + col*80, 23 + row*80, 75, 75);
					g.drawImage(crown,32 + col*80, 23 + row*80,75,75,null);
					break;

				}
			}
		}

		// draws a rectangle arund pieces that can be selected and then legal moves
		if(currentPlayer == Board.RED) {
			g.setColor(Color.blue);
			try{
				for(int i = 0; i < legalMoves.size(); i++) {
					g.drawRect(30+ legalMoves.get(i).fromCol*80,20+legalMoves.get(i).fromRow*80, 79, 79);
					g.drawRect(30+ legalMoves.get(i).fromCol*80,20+legalMoves.get(i).fromRow*80, 78, 78);
				}
				if (selectedRow >= 0) {
					g.drawRect(30 + selectedCol*80, 20 + selectedRow*80, 77, 77);
					g.setColor(Color.green);
					for (int i = 0; i < legalMoves.size(); i++) {
						if (legalMoves.get(i).fromCol == selectedCol && legalMoves.get(i).fromRow == selectedRow) {
							g.drawRect(30 + legalMoves.get(i).toCol*80, 20 + legalMoves.get(i).toRow*80, 79, 79);
							g.drawRect(30 + legalMoves.get(i).toCol*80, 20 + legalMoves.get(i).toRow*80, 78, 78);
						}
					}
				}
			}catch(NullPointerException e){
				System.out.println("Game Over");
				newGame();
			}
		}	     
	}

	void moveTosquare(int toRow, int toCol) {
		for (int i = 0; i < legalMoves.size(); i++) {
			if (legalMoves.get(i).fromRow == toRow && legalMoves.get(i).fromCol == toCol) {
				selectedRow = toRow;
				selectedCol = toCol;
				repaint();
				return;
			}
		}
		//this is where the humans moves are evaluated.
		if (selectedRow <0) {
			return;
		}

		for (int i = 0; i < legalMoves.size(); i++) {
			if (legalMoves.get(i).fromRow == selectedRow && legalMoves.get(i).fromCol == selectedCol
					&& legalMoves.get(i).toRow == toRow && legalMoves.get(i).toCol == toCol) {
				makeMove(legalMoves.get(i));
				return;
			}
		}
		repaint();
	}



	void makeMove(Board move) {

		if(currentPlayer == Board.RED)
			board.move(move.fromRow, move.fromCol, move.toRow, move.toCol);


		if (move.isJump()) {
			legalMoves = board.getLegalJumps(currentPlayer,move.toRow,move.toCol);
			if (legalMoves != null) {
				selectedRow = move.toRow;  
				selectedCol = move.toCol;      
				return;
			}
		}      
		/* Switch to the other player now since the current player has taken their move */
		if (currentPlayer == Board.WHITE) {
			currentPlayer = Board.RED;
			legalMoves = board.getLegalMoves(currentPlayer);
			if (legalMoves == null) {
				repaint();
				JOptionPane.showMessageDialog(this, "Red has no moves", "You lose", JOptionPane.INFORMATION_MESSAGE);
				gameInProgess = false;
			}
		}
		else {
			currentPlayer = Board.WHITE;
			try{
				AI.computerPlay();
			}catch(NullPointerException ex){
				System.out.println("Game Over");
			}

			makeMove(AI.boardData);
			AIlegalMoves = AI.successors;
			if(AIlegalMoves == null) {
				gameInProgess = false;
			}
		}


		selectedRow = -1;


		repaint();

	}  


	public void mousePressed(MouseEvent evt) {
		int col = (evt.getX() - 20) / 80;
		int row = (evt.getY() - 20) / 80;
		if ((col >= 0 && col < 8 && row >= 0 && row < 8)) {
			moveTosquare(row,col);
		}       
	}

	public void mouseReleased(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }

}
