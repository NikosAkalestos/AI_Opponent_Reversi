import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Board> children;
        Move cmove;
        Scanner in = new Scanner(System.in);
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        Board board = new Board();
        String res;
        char ch1;
        int pos1;
        int pos2;
        GamePlayer XPlayer;
        GamePlayer OPlayer;

        System.out.println("Would you like to play first? (y/n)");
        res = in.next();
        while (!res.toUpperCase().equals("Y") && !res.toUpperCase().equals("N")) {
            System.out.println("Wrong input. Would you like to play first? (y/n)");
            res = in.next();
        }

        if (res.toUpperCase().equals("Y")) {
            board.setLastLetterPlayed(Board.X);
        }

        System.out.println("What difficalty would you like? \na. Easy \nb. Medium \nc. Hard");
        res = in.next();
        while (!res.toUpperCase().equals("A") && !res.toUpperCase().equals("B") && !res.toUpperCase().equals("C")) {
            System.out.println("Wrong input. What difficalty would you like? \na. Easy \nb. Medium \nc. Hard");
            res = in.next();
        }

        if (res.toUpperCase().equals("A")) {
            XPlayer = new GamePlayer(2, Board.X);
            OPlayer = new GamePlayer(2, Board.O);
        } else if (res.toUpperCase().equals("B")) {
            XPlayer = new GamePlayer(4, Board.X);
            OPlayer = new GamePlayer(4, Board.O);
        } else {
            XPlayer = new GamePlayer(6, Board.X);
            OPlayer = new GamePlayer(6, Board.O);
        }


        board.print();
        //while loop until game is finished
        while (board.getCount() < 64 && board.getStop() < 2) //while board has still playable positions count < 64 and either of the players have available moves
            switch (board.getLastLetterPlayed()) {
                //If X played last, then O plays now
                case Board.X:
                    System.out.println("Now playing: " + (board.getLastLetterPlayed() != board.O ? "O " : "X "));
                    System.out.println("X:" + board.getXScore() + " O:" + board.getOScore());
                    if (board.availableMoves()) {
                        System.out.println("Choose next move (First write the column with small letters and then the row i.e. b3, c2, h1 etc.):");
                        res = in.next();
                        if (res.length() == 2) {
                            ch1 = res.charAt(0);
                            pos1 = (res.charAt(1) - '0') - 1;
                            pos2 = ch1 - 'a';
                        } else {
                            pos1 = -1;
                            pos2 = -1;
                        }
                        while (pos1 < 0 || pos1 > 7 || pos2 < 0 || pos2 > 7 || !board.isValidMove(pos1, pos2, Board.O)) {
                            System.out.println("Wrong input");
                            System.out.println("Choose next move (First write the column with small letters and then the row i.e. b3, c2, h1 etc.):");
                            res = in.next();
                            if (res.length() == 2) {
                                ch1 = res.charAt(0);
                                pos1 = (res.charAt(1) - '0') - 1;
                                pos2 = ch1 - 'a';
                            } else {
                                pos1 = -1;
                                pos2 = -1;
                            }

                        }
                        board.makeMove(pos1, pos2, Board.O);
                        board.print();
                        board.setCount(board.getCount() + 1);
                        board.setStop(0);
                    } else {
                        System.out.println("No available moves");
                        board.setLastLetterPlayed(Board.O); //change lastLetterPlayed so X will play next
                        board.setStop(board.getStop() + 1);
                    }
                    break;
                //If O played last, then X plays now - player pc
                case Board.O:
                    System.out.println("Now playing: " + (board.getLastLetterPlayed() != board.O ? "O " : "X "));
                    System.out.println("X:" + board.getXScore() + " O:" + board.getOScore());
                    if (board.availableMoves()) {
                        Move XMove = XPlayer.MiniMax(board);
                        board.makeMove(XMove.getRow(), XMove.getCol(), Board.X);
//						System.out.println(board.childrencount+" CHILDREN");
//						board.childrencount=0;
                        System.out.println("\n" + GamePlayer.getGenocide() + " children killed.\n" + GamePlayer.getChecked() + " children checked.");
                        board.print();
                        board.setCount(board.getCount() + 1);
                        board.setStop(0);
                    } else {
                        System.out.println("No available moves");
                        board.setLastLetterPlayed(Board.X);
                        board.setStop(board.getStop() + 1);
                    }
                    break;
                default:
                    break;
            }

        //board.makeMove(XMove.getRow(), XMove.getCol(),Board.X);
        if (board.getOScore() > board.getXScore()) {
            System.out.println("Player O wins!!! " + board.getOScore() + " - " + board.getXScore());
        } else if (board.getOScore() < board.getXScore()) {
            System.out.println("Player X wins!!! " + board.getXScore() + " - " + board.getOScore());
        } else {
            System.out.println("It's a draw!! " + board.getXScore() + " - " + board.getOScore());
        }
    }
}