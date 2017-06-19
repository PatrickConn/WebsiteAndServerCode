import java.util.ArrayList;

public class Board {
	static final int WHITE = 1, RED = 3, WHITEKING = 2, REDKING = 4, EMPTY = -1;
	int [][] board;

	int fromRow, fromCol;  
	int toRow, toCol;      
	Board () {
		board = new int[8][8];
		
		setPieces();
	}
	Board(int rowA, int colA, int rowB, int colB) {
       
	   fromRow = rowA;
	   fromCol = colA;
	   toRow = rowB;
	   toCol = colB;
	}
	

	void setPieces() {
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if ( row % 2 != col % 2 ) {
                  if (row < 3) {
                     board[row][col] = WHITE;
                  }
                  else if (row > 4) {
                     board[row][col] = RED;
                  }
                  else
                     board[row][col] = EMPTY;
               }
               else {
                  board[row][col] = EMPTY;
               }
            }
         }
		
	}
	

	int pieceAt(int row, int col) {
        return board[row][col];
     }


	
	boolean isJump() {
		return (fromRow - toRow == 2 || fromRow - toRow == -2);
	}
 
	
	
	
	void move(int fromRow, int fromCol, int toRow, int toCol) {
		board[toRow][toCol] = board[fromRow][fromCol];
		board[fromRow][fromCol] = EMPTY;
		if (fromRow - toRow == 2 || fromRow - toRow == -2) {
			
			int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
			int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
			board[jumpRow][jumpCol] = EMPTY;
		}
		if (toRow == 0 && board[toRow][toCol] == RED)
			board[toRow][toCol] = REDKING;
		if (toRow == 7 && board[toRow][toCol] == WHITE)
			board[toRow][toCol] = WHITEKING;
	}
	
	

	ArrayList<Board> getLegalMoves(int player) {
      
		if (player != WHITE && player != RED)
			return null;
      
		int playerKing; 
		if (player == WHITE)
			playerKing = WHITEKING;
		else
			playerKing = REDKING;
      
		ArrayList<Board> moves = new ArrayList<Board>();
      
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == player || board[row][col] == playerKing) {
					if (isJump(player, row, col, row+1, col+1, row+2, col+2))
						moves.add(new Board(row, col, row+2, col+2));
					if (isJump(player, row, col, row-1, col+1, row-2, col+2))
						moves.add(new Board(row, col, row-2, col+2));
					if (isJump(player, row, col, row+1, col-1, row+2, col-2))
						moves.add(new Board(row, col, row+2, col-2));
					if (isJump(player, row, col, row-1, col-1, row-2, col-2))
						moves.add(new Board(row, col, row-2, col-2));
				}
			}
		}
		
		if(moves.size() == 0) {
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (board[row][col] == player || board[row][col] == playerKing) {
						if (canMove(player,row,col,row+1,col+1))
							moves.add(new Board(row, col, row+1,col+1));
						if (canMove(player,row,col,row-1,col+1))
							moves.add(new Board(row, col, row-1,col+1));
						if (canMove(player,row,col,row+1,col-1))
							moves.add(new Board(row, col, row+1,col-1));
						if (canMove(player,row,col,row-1,col-1))
							moves.add(new Board(row, col, row-1,col-1));
					}
				}
			}
		}
      
		// if there are no legal moves return null and the game is over
		if (moves.size() == 0)
			return null;
		else {
			return moves;
		}
	}  
  
  
	ArrayList<Board> getLegalJumps(int player, int row, int col) {
		if (player != WHITE && player != RED)
			return null;
		int playerKing;  // The constant representing a King belonging to player.
		if (player == WHITE)
			playerKing = WHITEKING;
		else
			playerKing = REDKING;
		ArrayList<Board> moves = new ArrayList<Board>();
		
		if (board[row][col] == player || board[row][col] == playerKing) {
			if (isJump(player, row, col, row+1, col+1, row+2, col+2))
				moves.add(new Board(row,col,row+2, col+2));
			if (isJump(player, row, col, row-1, col+1, row-2, col+2))
				moves.add(new Board(row, col, row-2, col+2));
			if (isJump(player, row, col, row+1, col-1, row+2, col-2))
				moves.add(new Board(row, col, row+2, col-2));
			if (isJump(player, row, col, row-1, col-1, row-2, col-2))
				moves.add(new Board(row, col, row-2, col-2));
		}
		if (moves.size() == 0)
			return null;
		else {
			return moves;
		}
   }  
  
	
	private boolean isJump(int player, int rowA, int colA, int rowB, int colB, int rowC, int colC) {  
		if (rowC < 0 || rowC >= 8 || colC < 0 || colC >= 8)
			return false; 

		if (board[rowC][colC] != EMPTY)
			return false;
		
		if (player == RED) {
			if (board[rowA][colA] == RED && rowC > rowA)
				return false; 
			if (board[rowB][colB] != WHITE && board[rowB][colB] != WHITEKING)
				return false;  
			return true;  // The jump is legal.
		}
		else {
			if (board[rowA][colA] == WHITE && rowC < rowA)
				return false;  
			if (board[rowB][colB] != RED && board[rowB][colB] != REDKING)
				return false;  
			return true; 
		}
  
      
	}  
  
	public boolean canMove(int player, int rowA, int colA, int rowB, int colB) {
      
		//if row number or column number is greater than 8 the space is off board
		if (rowB < 0 || rowB >= 8 || colB < 0 || colB >= 8)
			return false; 
      
		// Can't move into a space containing another piece
		if (board[rowB][colB] != EMPTY)
			return false;  
      //Can't move backwards if a normal piece
		if (player == RED) {
			if (board[rowA][colA] == RED && rowB > rowA)
				return false;  
			return true;  
		}
		else {
			if (board[rowA][colA] == WHITE && rowB < rowA)
				return false;  
			return true;  
		}
      
   }  

}
