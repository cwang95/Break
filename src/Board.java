import java.util.LinkedList;

/**
 * Board, a unique representation of the checkers board
 */
public class Board {
    public int[][] board;
    public int winner = 0;
    //the usable squares on the board  0 is open, 1 is player 1, -1 is player 2, K is 1 or -1 * 2

    public Board() {
        board = new int[8][8];
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                if (b < 2) {
                    board[a][b] = 1;
                } else if (b > 5) {
                    board[a][b] = -1;
                } else {
                    board[a][b] = 0;
                }
            }
        }
    }

    /**
     * Creates new Board object from original status,
     * location of the piece to be moved, and code of the move we
     * want to make
     *
     * @param BP   Original board status
     * @param A    a position of piece to be moved
     * @param B    b position of piece
     * @param code Code of the move we want to make
     */
    public Board(Board BP, int A, int B, int code) {
        board = new int[8][8];
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                board[a][b] = BP.board[a][b];  // Copy board exactly as it exists
            }
        }
        // TODO: cases 3,4,8,9 are actually duplicates!

        int value = board[A][B];    // Value of the piece we want to move
        board[A][B] = 0;            // Set the value now on the board to be empty
        switch (code) {
            case (0):
                A--;
                B++;
                break;   // Step to upper right
            case (1): /* A stays the same */
                B++;
                break;   // Step straight to right
            case (2):
                A++;
                B++;
                break;   // Step to lower right
            case (3):
                A--;
                B++;
                break;   // Jump upper right
            case (4):
                A++;
                B++;
                break;   // Jump lower right

            case (5):
                A--;
                B--;
                break;   // Step to upper left
            case (6): /* A stays the same*/
                B--;
                break;  // Step straight to left
            case (7):
                A++;
                B--;
                break;  // Step to lower left
            case (8):
                A--;
                B--;
                break;  // Jump upper left
            case (9):
                A++;
                B--;
                break;  // Jump lower left
            default:
                break;
        }
        board[A][B] = value;
        if ((B == 0 && value == -1)) {
            winner = -1;
        }
        if ((B == 7 && value == 1)) {
            winner = 1;
        }
    }

    /**
     *
     * @return
     */
    public boolean gameOver(boolean player1) {
        if (winner == 1) {
            return true;
        }
        if (winner == -1) {
            return true;
        }
        return getMoves(player1).isEmpty();
    }

    /**
     * Returns a list of all moves for the given player
     *
     * @param player   Whose turn it is and which moves to find for
     * @return         List of moves for the player on the current state of the board
     */
    public LinkedList<Board> getMoves(boolean player) {

        LinkedList<Board> moves = new LinkedList<>();

        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                if (player && board[a][b] > 0) {
                    if (attackOK(a, b, a - 1, b + 1) || open(a - 1, b + 1)) {  //move upper right
                        moves.add(new Board(this, a, b, 0));
                    }
                    if (open(a, b + 1)) {    // move straight right
                        moves.add(new Board(this, a, b, 1));
                    }
                    if (attackOK(a, b, a + 1, b + 1) || open(a + 1, b + 1)) {  //move lower right
                        moves.add(new Board(this, a, b, 2));
                    }
                }
                if (!player && board[a][b] < 0) {
                    if (attackOK(a, b, a - 1, b - 1) || open(a - 1, b - 1)) {  //move upper left is open
                        moves.add(new Board(this, a, b, 5));
                    }
                    if (open(a, b - 1)) {    // move straight left is open
                        moves.add(new Board(this, a, b, 6));
                    }
                    if (attackOK(a, b, a + 1, b - 1) || open(a + 1, b - 1)) {  //move lower left
                        moves.add(new Board(this, a, b, 7));
                    }
                }
            }
        }


        return moves;
    }


    /**
     * Checks to see if there is an attack available from a1,b1 to a2,b2
     *
     * @param a1 Original a position
     * @param b1 Original b position
     * @param a2 New a position
     * @param b2 New b position
     * @return Validity of attack
     */
    public boolean attackOK(int a1, int b1, int a2, int b2) {
        return inBounds(a2, b2) && (Math.signum(board[a2][b2]) == -Math.signum(board[a1][b1]));
    }

    /**
     * Check for if the space is available
     *
     * @param a a position to check
     * @param b b position to check
     * @return Emptiness of position
     */
    public boolean open(int a, int b) {
        return inBounds(a, b) && board[a][b] == 0;
    }

    public boolean inBounds(int a, int b) {
        return a >= 0 && a < board.length && b >= 0 && b < board[0].length;
    }


    /**
     * Heuristic score based on #white pieces - # black pieces
     *
     * @return score of current board
     */
    public double boardValue() {
        double pieceDifference = 0;
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                if (b < 1) {
                    if (board[a][b] == -1) {
                        return Double.MIN_VALUE;
                    }
                } else if (b > 6) {
                    if (board[a][b] == 1) {
                        return Double.MAX_VALUE;
                    }
                } else {
                    pieceDifference += board[a][b];
                }
            }
        }
        return pieceDifference;
    }

    /**
     * Heuristic score based on number of pieces on the board
     * and how close they are to reaching the other side
     *
     * @return score
     */
    public double boardValueOffensive() {
        double pieceDifference = 0;
        for (int a = 0; a < board.length; a++) {
            for (int b = 0; b < board[0].length; b++) {
                if (b < 1) {
                    if (board[a][b] == -1) {
                        return Double.MIN_VALUE;
                    }
                } else if (b > 6) {
                    if (board[a][b] == 1) {
                        return Double.MAX_VALUE;
                    }
                } else {
                    int positionNum = board[a][b];

                    if (positionNum == 1) {
                        pieceDifference += b;
                    } else if (positionNum == -1) {
                        pieceDifference += (-7 + b);
                    }
                }
            }
        }

        return pieceDifference;
    }

    @Override
    public String toString() {
        String s = "board: ";
        for (int a = 0; a < board.length; a++) {
            s += "\n";
            for (int b = 0; b < board[0].length; b++) {
                switch (board[a][b]) {
                    case (-2):
                        s += 'B';
                        break;
                    case (-1):
                        s += 'b';
                        break;
                    case (0):
                        s += '.';
                        break;
                    case (1):
                        s += 'w';
                        break;
                    case (2):
                        s += 'W';
                        break;
                    default:
                        break;
                }
                s += " ";
            }
        }
        return s;
    }
}
