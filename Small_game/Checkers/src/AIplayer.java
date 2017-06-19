import java.awt.Component;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AIplayer extends Component{

	Board boardData;
	CheckersBoard boardGUI;
	
	public ArrayList<Board> successors;
	
	private static final int maxDepth = 5;
	int color; 
	 private static final int tableWeight[] = { 
			 4, 4, 4, 4,//row 1
             4, 3, 3, 3,// row 2
             3, 2, 2, 4,//row 3
             400,6000, 1, 3,//row 4
             3, 1, 2, 4,//row 5
             4, 2, 2, 3,//row 6
             3, 3, 3, 4,
             4, 4, 4, 4};
	 
	 private static final int tableWeightKing[] = { 
			 0, 0, 0, 0,
             0, 0, 0, 0,
             0, 4, 4, 0,
             0, 4, 4, 0,
             0, 4, 4, 0,
             0, 0, 0, 0,
             0, 0, 0, 0,
             0, 0, 0, 0};

	public AIplayer(Board gameBoard, CheckersBoard board) {
		boardData = gameBoard;
		boardGUI=board;
		color = Board.WHITE;
	}
	
	
	public void computerPlay() {
		try {
			Board boardState;

			boardState = minimax(boardData);

			if(!boardState.equals(null)) {
				boardData.move(boardState.fromRow, boardState.fromCol, boardState.toRow, boardState.toCol);

			}
		}
		catch(NullPointerException e) {
			System.out.println("Game Over");
		}
	}

	 public Board minimax (Board board){
		    Board move;
		    Board nextBoard;
		    Board bestMove = null;
		    int value, maxValue = Integer.MIN_VALUE;

		    successors = boardData.getLegalMoves(Board.WHITE);
		    if(successors == null){
				 JOptionPane.showMessageDialog(this, "Red, wins!", "", JOptionPane.INFORMATION_MESSAGE);
				 boardGUI.newGame();
			}
		    
			while (successors.size()>0) {
		      move =  successors.remove(0);
		      nextBoard = board;

		      nextBoard.move(nextBoard.fromRow, nextBoard.fromCol, nextBoard.toRow, nextBoard.toCol);
		      value = minMove (nextBoard, 1, maxValue, Integer.MAX_VALUE);

		      if (value > maxValue) {
		     
		        maxValue = value;
		        bestMove = move;
		      }
		    }

		    System.out.println ("Move value selected : " + maxValue + " at depth : 0");

		    return bestMove;
		  }
	
	
	private int maxMove(Board board, int depth, int alpha, int beta) {
		if(depth > maxDepth || !board.canMove(color, board.fromRow, board.fromCol, board.toRow, board.toCol))
			return evaluate(board);

		ArrayList<Board> successors;
		
		Board nextBoard;
		int value;

		successors = board.getLegalMoves(color);
		while(successors.size() > 0) {
			successors.remove(0);
			nextBoard = board;
			nextBoard.move(nextBoard.fromRow, nextBoard.fromCol, nextBoard.toRow, nextBoard.toCol);
			value =  minMove(nextBoard, depth+1,alpha,beta);

			if(value >alpha) {
				
				alpha = value;
			}
			if(alpha > beta) {
				System.out.println (beta);return beta;
			}

		}
		return alpha;	
	}

	private int minMove(Board board, int depth, int alpha, int beta) {
		if(depth > maxDepth || !board.canMove(color, board.fromRow, board.fromCol, board.toRow, board.toCol))
			return evaluate(board);

		ArrayList<Board> successors;
		Board nextBoard;
		int value;

		successors = board.getLegalMoves(color);
		while(successors.size() > 0) {
			successors.remove(0);
			nextBoard = board;
			nextBoard.move(nextBoard.fromRow, nextBoard.fromCol, nextBoard.toRow, nextBoard.toCol);
			value =  maxMove(nextBoard, depth+1,alpha,beta);

			if(value < beta) {
				beta = value;
			}
			if(beta < alpha) {
				return alpha;
			}

		}
		return beta;	
	}


	private int evaluate(Board board) {
		int colorKing;
		

		if(color == Board.WHITE) {
			colorKing = Board.WHITEKING;
			
		}
		else {
			colorKing = Board.REDKING;
			
		}

		int colorForce = 0;
		int enemyForce = 0;
		int piece;

		try {
			for(int row = 0; row < 8; row++) {
				for(int col =0; col<8; col++) {
					piece = board.pieceAt(row, col);
					if(piece != Board.EMPTY) {
						if(piece == color || piece == colorKing) {
							enemyForce +=calculateValue(piece, row, col);
						}
						else
							colorForce += calculateValue(piece, row, col);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return colorForce - enemyForce;
	}


	private int calculateValue(int piece, int xPos, int yPos) {
		int value;
		int score=0;
		if(piece == Board.WHITE) {
			if((xPos >=4 && xPos <= 7) && (yPos >=4 && yPos <=7)) {
				value = 30;
			}
			else value = 5;
		}
		else if(piece != Board.RED) {
			if((xPos >=4 && xPos <=7)&& (yPos >=4 && yPos <=7)) {
				value = 7;
			}
			else value = 5;
		}
		
		else value = 10;
		int pos = xPos+yPos;
		if(piece==Board.WHITE||piece==Board.RED){
			score= value * tableWeight[pos];
		}else if(piece==Board.WHITEKING||piece==Board.REDKING){
			score= value * tableWeightKing[pos];
		}
		return score;
	}
	
	

}
