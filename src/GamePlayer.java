import java.util.ArrayList;

public class GamePlayer {
    //Variable that holds the maximum depth the MiniMax algorithm will reach for this player
    private int maxDepth;
    //Variable that holds which letter this player controls
    private int playerLetter;
    private double MinNeighborhoodValue = -1;
    private double MaxNeighborhoodValue = -1;
    public static int genocide = 0;
    public static int checked = 0;

    public GamePlayer() {
        maxDepth = 2;
        playerLetter = Board.X;
    }

    public GamePlayer(int maxDepth, int playerLetter) {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    //Initiates the MiniMax algorithm
    public Move MiniMax(Board board) {
        NeighborhoodValueInitializer();
        CounterInitializer();
        //If the X plays then it wants to MINimize the heuristics value  (pc player)
        if (playerLetter == Board.X) {
            return min(new Board(board), 0);
        }
        //If the O plays then it wants to MAXimize the heuristics value   (human player)
        else {
            return max(new Board(board), 0);
        }
    }

    // The max and min functions are called interchangingly, one after another until a max depth is reached
    public Move max(Board board, int depth) {
//        Random r = new Random();

        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
        //check if game-node is terminal     or   if minimax reached maxdepth
        if ((board.getCount() == 64 || board.getStop() == 2) || (depth == maxDepth)) {
            Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
            return lastMove;
        }
        //The children-moves of the state are calculated
        ArrayList<Board> children = new ArrayList<Board>(board.getChildren(Board.O));
        NeighborhoodValueInitializer();
        Move maxMove = new Move(Integer.MIN_VALUE);
        for (Board child : children) {
            if (MaxNeighborhoodValue < child.getNeighborhoodValue()) {
                MaxNeighborhoodValue = child.getNeighborhoodValue();
            }
        }
        for (Board child : children) {
            if (!(child.getNeighborhoodValue() >= (MaxNeighborhoodValue / 4))) {
                genocide++;
                continue;
            }
            checked++;
            //And for each child min is called, on a lower depth
            Move move = min(child, depth + 1);
            //The child-move with the greatest value is selected and returned by max
            if (move.getValue() > maxMove.getValue()) {
/*                if ((move.getValue() == maxMove.getValue()))
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if (r.nextInt(2) == 0)
                    {
                        maxMove.setRow(child.getLastMove().getRow());
                        maxMove.setCol(child.getLastMove().getCol());
                        maxMove.setValue(move.getValue());
                    }
                }
                else
                {*/
                maxMove.setRow(child.getLastMove().getRow());
                maxMove.setCol(child.getLastMove().getCol());
                maxMove.setValue(move.getValue());
//                }
            }
        }
        /*System.out.print("+");*/
        //System.out.println("max row "+maxMove.getRow()+" col "+maxMove.getCol()+" value "+maxMove.getValue());
        return maxMove;
    }

    //Min works similarly to max
    public Move min(Board board, int depth) {
//        Random r = new Random();

        //terminal gamenode maxdepth
        if ((board.getCount() == 64 || board.getStop() == 2) || (depth == maxDepth)) {
            Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
            return lastMove;
        }
        ArrayList<Board> children = new ArrayList<Board>(board.getChildren(Board.X));
        Move minMove = new Move(Integer.MAX_VALUE);
        NeighborhoodValueInitializer();
        for (Board child : children) {
            if (MinNeighborhoodValue < child.getNeighborhoodValue()) {
                MinNeighborhoodValue = child.getNeighborhoodValue();
            }
        }
        for (Board child : children) {
            if (!(child.getNeighborhoodValue() >= (MinNeighborhoodValue / 4))) {
                genocide++;
                continue;
            }
            checked++;
            Move move = max(child, depth + 1);
            if (move.getValue() < minMove.getValue()) {
/*                if ((move.getValue() == minMove.getValue()))
                {
                    if (r.nextInt(2) == 0)
                    {
                        minMove.setRow(child.getLastMove().getRow());
                        minMove.setCol(child.getLastMove().getCol());
                        minMove.setValue(move.getValue());
                    }
                }
                else
                {*/
                minMove.setRow(child.getLastMove().getRow());
                minMove.setCol(child.getLastMove().getCol());
                minMove.setValue(move.getValue());
//                }
            }
        }
        if (depth == 0) {
            System.out.println("min row " + minMove.getRow() + " col " + minMove.getCol() + " value " + minMove.getValue());
        } /*else {
            System.out.println("-");
        }*/

        return minMove;
    }

    private void NeighborhoodValueInitializer() {
        this.MinNeighborhoodValue = -1;
        this.MaxNeighborhoodValue = -1;
    }

    private void CounterInitializer() {
        genocide = 0;
        checked = 0;
    }

    public static int getGenocide() {
        return genocide;
    }

    public static int getChecked() {
        return checked;
    }
}