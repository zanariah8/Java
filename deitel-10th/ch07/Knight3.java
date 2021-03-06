// Ex 7.22)d: Knight's tour around a chess board.
//			  I could inline a bunch of stuff but...
//				a) readability
//				b) indentation levels
//				c) compiler does it better
//
//			  Added a lookAhead function.
	
import java.util.Arrays;

public class Knight3 {
	private static final int DIM = 8;
	private static final int SIZE = DIM*DIM;

	private static final int[][] board  = new int[DIM][DIM];
	private static int[][] access = new int[DIM][DIM];
	private static final int[][] initAccs = {{2,3,4,4,4,4,3,2},
										     {3,4,6,6,6,6,4,3},
										     {4,6,8,8,8,8,6,4},
										     {4,6,8,8,8,8,6,4},
										     {4,6,8,8,8,8,6,4},
										     {4,6,8,8,8,8,6,4},
										     {3,4,6,6,6,6,4,3},
										     {2,3,4,4,4,4,3,2}};

	private static final int[] horz = { 2,  1, -1, -2, -2, -1,  1,  2};
	private static final int[] vert = {-1, -2, -2, -1,  1,  2,  2,  1};

	private static int row = 0;
	private static int col = 0;

	public static void main(String[] args) {
		int completed = 0;
		for (int n = 0; n < SIZE; n++) {
			int c = n % DIM;
			int r = (n-c) / DIM;
			startTourAt(r, c);
			int moves = runTour();
			completed += moves == SIZE-1 ? 1 : 0;
			printBoard(moves, r, c);
		}
	}

	private static void startTourAt(int r, int c) {
		for (int row = 0; row < DIM; row++)
			for (int col = 0; col < DIM; col++) {
				board[row][col]  = 0;
				access[row][col] = initAccs[row][col];
			}
		row = r;
		col = c;
		board[r][c] = 1;
	}

	private static int runTour() {
		int moves = 0;
		for (int i = 0; i < SIZE; i++) {
				int move = scoreMoves();

				if (move == DIM)
					break; // No moves

				updateAccess(-1);

				row += vert[move];
				col += horz[move];

				moves++;
				board[row][col]++;
		}
		return moves;
	}

	private static int scoreMoves() {
		int res_move  = DIM;
		int min_score = DIM+1;

		for (int move = 0; move < DIM; move++) {

			int r = row + vert[move];
			int c = col + horz[move];

			if (inRange(r,c) && notVisited(r,c))
				if(access[r][c] < min_score) {
					min_score = access[r][c];
					res_move = move;
				} else if (access[r][c] == min_score) {
					int res_next = lookAhead(res_move);
					int mov_next = lookAhead(move);
					res_move = mov_next < res_next ? move : res_move;
				}
		}
		return res_move;
	}

	private static int lookAhead(int mv) {
		// Update board
		updateAccess(-1);
		row += vert[mv];
		col += horz[mv];
		board[row][col]++;

		// Find min for this move
		int min_score = DIM+1;

		for (int move = 0; move < DIM; move++) {
			int r = row + vert[move];
			int c = col + horz[move];

			if (inRange(r,c) && notVisited(r,c)
					&& access[r][c] < min_score)
				min_score = access[r][c];
		}

		//Undo all updates
		board[row][col]--;
		col -= horz[mv];
		row -= vert[mv];
		updateAccess(1);

		return min_score;
	}

	private static void updateAccess(int inc) {
		for (int move = 0; move < DIM; move++) {
			int r = row + vert[move];
			int c = col + horz[move];
			if (inRange(r,c))
				access[r][c] += inc;
		}
	}

	private static void printBoard(int moves, int r, int c) {
		for (int[] row : board)
			System.out.println(Arrays.toString(row));
		System.out.printf ("Moves: %d. Start: (%d,%d)%s%n",
				moves, r, c, moves == SIZE-1 ? "\tComplete.": "");
	}

	private static boolean notVisited(int r, int c) {
		// pre: inRange(r,c)
		return board[r][c] == 0;
	}

	private static boolean inRange (int r, int c) {
		return 0 <= r && r < DIM && 0 <= c && c < DIM;
	}
}
