import java.util.ArrayList;

public class Board {
    //variables for board values
    public static final int X = 1;  // Player human
    public static final int O = -1;  // Computer
    public static final int EMPTY = 0;
    private int oScore;
    private int xScore;
    private int count;
    private int stop;
    private int neighborhoodvalue;
    //    public static int childrencount = 0;
    public static final int EvaluationBoard[][] = {
            {20, 7, 5, 5, 5, 5, 7, 20},
            {7, 7, 2, 2, 2, 2, 7, 7},
            {5, 2, 3, 1, 1, 3, 2, 5},
            {5, 2, 1, 0, 0, 1, 2, 5},
            {5, 2, 1, 0, 0, 1, 2, 5},
            {5, 2, 3, 1, 1, 3, 2, 5},
            {7, 7, 2, 2, 2, 2, 7, 7},
            {20, 7, 5, 5, 5, 5, 7, 20}};

    //immediate last move that lead to this board
    private Move lastMove;

    //variable containing who played last;whose turn resulted in this board
    private int lastLetterPlayed;

    private int[][] gameBoard;

    public Board() {
        lastMove = new Move();
        lastLetterPlayed = O;
        oScore = 2;
        xScore = 2;
        count = 4;
        stop = 0;
        neighborhoodvalue = -1;
        gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }

        gameBoard[3][3] = X;
        gameBoard[3][4] = O;
        gameBoard[4][3] = O;
        gameBoard[4][4] = X;
    }

    public Board(Board board)            //duplicate board for getchildren
    {
        lastMove = board.lastMove;
        oScore = board.oScore;
        xScore = board.xScore;
        count = board.count;
        stop = board.stop;
        neighborhoodvalue = board.neighborhoodvalue;
        lastLetterPlayed = board.lastLetterPlayed;
        gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }

    //evaluate board
    public int evaluate() {
        int[][] board = this.getGameBoard();
        int eval = oScore - xScore;                        //pieces on the board
        ArrayList<Board> oChildren = getChildren(O);        //possible moves on the board
        ArrayList<Board> xChildren = getChildren(X);
        eval = eval + oChildren.size();
        eval = eval - xChildren.size();
        //stable pieces on the corners

        for (int col = 0; col <= 7; col++) {
            for (int row = 0; row <= 7; row++) {
                if (board[col][row] == O) {
                    eval = eval + EvaluationBoard[col][row];
                }
                if (board[col][row] == X) {
                    eval = eval - EvaluationBoard[col][row];
                }
            }
        }
/*        if (gameBoard[0][0] == O) {                        //upleft
            eval++;
        } else if (gameBoard[0][0] == X) {
            eval--;
        }
        if (gameBoard[0][7] == O) {                        //upright
            eval++;
        } else if (gameBoard[0][7] == X) {
            eval--;
        }
        if (gameBoard[7][0] == O) {                        //downleft
            eval++;
        } else if (gameBoard[7][0] == X) {
            eval--;
        }
        if (gameBoard[7][7] == O) {                        //downright
            eval++;
        } else if (gameBoard[7][7] == X) {
            eval--;
        }*/
//        childrencount++;
        //System.out.println("row "+lastMove.getRow()+" col "+lastMove.getCol()+" with an evaluation of "+eval);
        return eval;
    }

    /* Generates the children of the state
     * Any square in the board that is empty results to a child
     */
    public ArrayList<Board> getChildren(int letter)       //how to input row in the method
    {
        ArrayList<Board> children = new ArrayList<Board>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, letter)) {
                    Board child = new Board(this);
                    child.makeMove(i, j, letter);
                    child.setNeighborhoodValue(EvaluationBoard[i][j]);
                    children.add(child);

                }
            }
        }
        return children;
    }

    /*Validators*/
    //Checks whether a move is valid; whether a square is empty
    public boolean isValidMove(int row, int col, int letter) {
        // Initialize boolean legal as false
        boolean legal = false;

        // If the cell is empty, begin the search
        // If the cell is not empty there is no need to check anything
        // so the algorithm returns boolean legal as is
        if (gameBoard[row][col] == EMPTY) {
            // Initialize variables
            int posX;
            int posY;
            boolean found;
            int current;

            // Searches in each direction
            // x and y describe a given direction in 9 directions
            // 0, 0 is redundant and will break in the first check
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    // Variables to keep track of where the algorithm is and
                    // whether it has found a valid move
                    posX = col + x;
                    posY = row + y;
                    found = false;
                    //current = gameBoard[posY][posX];

                    // Check the first cell in the direction specified by x and y
                    // If the cell is empty, out of bounds or contains the same player
                    // skip the rest of the algorithm to begin checking another direction
                    if (posY < 0 || posY > 7 || posX < 0 || posX > 7) {
                        continue;
                    } else {
                        current = gameBoard[posY][posX];
                        if (current == EMPTY || current == letter) {
                            continue;
                        }
                    }

                    // if  posY<0 || posY>7 || posX<0 || posX>7 || current == EMPTY || current == letter       previous check (changed conditions)


                    // Otherwise, check along that direction
                    while (!found) {
                        posX += x;
                        posY += y;
                        //current = gameBoard[posY][posX];

                        if (posY < 0 || posY > 7 || posX < 0 || posX > 7) {   //out of bounds - end loop check new direction
                            found = true;
                        } else {
                            current = gameBoard[posY][posX];
                            if (current == letter) {
                                found = true;
                                legal = true;
                            } else if (current == EMPTY) {
                                found = true;
                            }
                        }

                        // If the algorithm finds another piece of the same player along a direction
                        // end the loop to check a new direction, and set legal to true
//								if (current == letter)
//								{
//									found = true;
//									legal = true;
//								}
                        // If the algorithm reaches an out of bounds area or an empty space
                        // end the loop to check a new direction, but do not set legal to true yet
//								else if (current == -1 || current == 0)
//								{
//									found = true;
//								}
                    }
                }
            }
        }

        return legal;
    }

    public boolean availableMoves() {
        boolean available = false;
        int i = 0;
        int j = 0;
        while (i < 8 && !available) {
            while (j < 8 && !available) {
                available = isValidMove(i, j, (lastLetterPlayed != O ? O : X));
                j++;
            }
            j = 0;
            i++;
        }
        return available;
    }

    /*Movement Checkers*/
    //Make a move; it places a letter in the board
    public void makeMove(int row, int col, int letter) {
        gameBoard[row][col] = letter;
        lastMove = new Move(row, col);
        lastLetterPlayed = letter;
        if (lastLetterPlayed == O) {
            oScore++;
        } else {
            xScore++;
        }
        checkUp(row, col, letter);
        checkDown(row, col, letter);
        checkRight(row, col, letter);
        checkLeft(row, col, letter);
        checkDownRight(row, col, letter);
        checkUpLeft(row, col, letter);
        checkDownLeft(row, col, letter);
        checkUpRight(row, col, letter);
    }

    //reverse pieces from X to O or the other way around
    public void reversePieces(int startRow, int startCol, int endRow, int endCol) {
        if (startCol == endCol) {                                        //same column
            for (int i = startRow + 1; i < endRow; i++) {
                gameBoard[i][startCol] = lastLetterPlayed;
                if (lastLetterPlayed == O) {
                    oScore++;
                    xScore--;
                } else {
                    xScore++;
                    oScore--;
                }
            }
        } else if (startRow == endRow) {                                //same row
            for (int i = startCol + 1; i < endCol; i++) {
                gameBoard[startRow][i] = lastLetterPlayed;
                if (lastLetterPlayed == O) {
                    oScore++;
                    xScore--;
                } else {
                    xScore++;
                    oScore--;
                }
            }
        } else if (startRow < endRow && startCol < endCol) {                //diagonal upleft to downright
            int j = startCol + 1;
            for (int i = startRow + 1; i < endRow; i++) {
                gameBoard[i][j] = lastLetterPlayed;
                if (lastLetterPlayed == O) {
                    oScore++;
                    xScore--;
                } else {
                    xScore++;
                    oScore--;
                }
                j = j + 1;
            }
        } else {                                                        //diagonal upright to downleft
            int j = startCol - 1;
            for (int i = startRow + 1; i < endRow; i++) {
                gameBoard[i][j] = lastLetterPlayed;
                if (lastLetterPlayed == O) {
                    oScore++;
                    xScore--;
                } else {
                    xScore++;
                    oScore--;
                }
                j = j - 1;
            }
        }

    }

    public void checkUp(int row, int col, int letter) {
        int r = row - 1;
        while (r > -1 && gameBoard[r][col] != lastLetterPlayed && gameBoard[r][col] != EMPTY) {
            r = r - 1;
        }
        if (r > -1 && gameBoard[r][col] == lastLetterPlayed) {
            reversePieces(r, col, lastMove.getRow(), lastMove.getCol());
        } else {
            return;
        }

    }

    public void checkDown(int row, int col, int letter) {
        int r = row + 1;
        while (r < 8 && gameBoard[r][col] != lastLetterPlayed && gameBoard[r][col] != EMPTY) {
            r = r + 1;
        }
        if (r < 8 && gameBoard[r][col] == lastLetterPlayed) {
            reversePieces(lastMove.getRow(), lastMove.getCol(), r, col);
        } else {
            return;
        }
    }

    public void checkRight(int row, int col, int letter) {
        int c = col + 1;
        while (c < 8 && gameBoard[row][c] != lastLetterPlayed && gameBoard[row][c] != EMPTY) {
            c = c + 1;
        }
        if (c < 8 && gameBoard[row][c] == lastLetterPlayed) {
            reversePieces(lastMove.getRow(), lastMove.getCol(), row, c);
        } else {
            return;
        }
    }

    public void checkLeft(int row, int col, int letter) {
        int c = col - 1;
        while (c > -1 && gameBoard[row][c] != lastLetterPlayed && gameBoard[row][c] != EMPTY) {
            c = c - 1;
        }
        if (c > -1 && gameBoard[row][c] == lastLetterPlayed) {
            reversePieces(row, c, lastMove.getRow(), lastMove.getCol());
        } else {
            return;
        }
    }

    public void checkDownRight(int row, int col, int letter) {
        int c = col + 1;
        int r = row + 1;
        while (c < 8 && r < 8 && gameBoard[r][c] != lastLetterPlayed && gameBoard[r][c] != EMPTY) {
            c = c + 1;
            r = r + 1;
        }
        if (c < 8 && r < 8 && gameBoard[r][c] == lastLetterPlayed) {
            reversePieces(lastMove.getRow(), lastMove.getCol(), r, c);
        } else {
            return;
        }
    }

    public void checkUpLeft(int row, int col, int letter) {
        int c = col - 1;
        int r = row - 1;
        while (c > -1 && r > -1 && gameBoard[r][c] != lastLetterPlayed && gameBoard[r][c] != EMPTY) {
            c = c - 1;
            r = r - 1;
        }
        if (c > -1 && r > -1 && gameBoard[r][c] == lastLetterPlayed) {
            reversePieces(r, c, lastMove.getRow(), lastMove.getCol());
        } else {
            return;
        }
    }

    public void checkDownLeft(int row, int col, int letter) {
        int c = col - 1;
        int r = row + 1;
        while (c > -1 && r < 8 && gameBoard[r][c] != lastLetterPlayed && gameBoard[r][c] != EMPTY) {
            c = c - 1;
            r = r + 1;
        }
        if (c > -1 && r < 8 && gameBoard[r][c] == lastLetterPlayed) {
            reversePieces(lastMove.getRow(), lastMove.getCol(), r, c);
        } else {
            return;
        }
    }

    public void checkUpRight(int row, int col, int letter) {
        int c = col + 1;
        int r = row - 1;
        while (c < 8 && r > -1 && gameBoard[r][c] != lastLetterPlayed && gameBoard[r][c] != EMPTY) {
            c = c + 1;
            r = r - 1;
        }
        if (c < 8 && r > -1 && gameBoard[r][c] == lastLetterPlayed) {
            reversePieces(r, c, lastMove.getRow(), lastMove.getCol());
        } else {
            return;
        }
    }

    /*Get + Set*/
    public Move getLastMove() {
        return lastMove;
    }

    public int getLastLetterPlayed() {
        return lastLetterPlayed;
    }

    public int getOScore() {
        return oScore;
    }

    public int getXScore() {
        return xScore;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public int getCount() {
        return count;
    }

    public int getStop() {
        return stop;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    public void setLastLetterPlayed(int lastLetterPlayed) {
        this.lastLetterPlayed = lastLetterPlayed;
    }

    public int setCount(int count) {
        return this.count = count;
    }

    public int setStop(int stop) {
        return this.stop = stop;
    }

    public void setGameBoard(int[][] gameBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.gameBoard[i][j] = gameBoard[i][j];
            }
        }
    }

    public int getNeighborhoodValue() {
        return neighborhoodvalue;
    }

    public void setNeighborhoodValue(int neighborhoodvalue) {
        this.neighborhoodvalue = neighborhoodvalue;
    }

    //Prints the board
    public void print() {
        System.out.println(" _____________________________________");
        System.out.println("|                                     |");
        System.out.println("|    A   B   C   D   E   F   G   H    |");
        for (int row = 0; row < 8; row++) {
            System.out.print("|  " + (1 + row) + " ");

            for (int col = 0; col < 8; col++) {
                if (col < 7) {
                    switch (gameBoard[row][col]) {
                        case X:
                            System.out.print("X | ");
                            break;
                        case O:
                            System.out.print("O | ");
                            break;
                        case EMPTY:
                            System.out.print("_ | ");
                            break;
                        default:
                            break;
                    }
                }
                if (col == 7) {
                    switch (gameBoard[row][7]) {
                        case X:
                            System.out.print("X " + (row + 1) + " ");
                            break;
                        case O:
                            System.out.print("O " + (row + 1) + " ");
                            break;
                        case EMPTY:
                            System.out.print("_ " + (row + 1) + " ");
                            break;
                        default:
                            break;
                    }
                }
            }
            System.out.println(" |");
        }
        System.out.println("|    A   B   C   D   E   F   G   H    |");
        System.out.println("|_____________________________________|");
    }
}