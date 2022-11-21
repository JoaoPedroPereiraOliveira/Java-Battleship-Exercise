package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int playerTurn = 0;

    static class Player {
        private String name;
        private int points;
        private Board board;
        private Board boardFog;
        private Ship[] ships;

        private int wins;
        private int opponent;

        public Player(String name, int points, Board board, Board boardFog, Ship[] ships) {
            this.name = name;
            this.points = points;
            this.board = board;
            this.boardFog = boardFog;
            this.ships = ships;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public boolean isWinner() {
            int maxPoints = 0;
            for (Ship shipCount: ships) {
                maxPoints += shipCount.size;
            }
            return maxPoints == points;
        }

        public String getName() {
            return name;
        }

        public int getPoints() {
            return points;
        }

        public Board getBoard() {
            return board;
        }

        public Board getBoardFog() {
            return boardFog;
        }

        public Ship[] getShips() {
            return ships;
        }

        public Ship getShips(int id) {
            return ships[id];
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getWins() {
            return wins;
        }

        public void setOpponent(int opponent) {
            this.opponent = opponent;
        }

        public int getOpponent() {
            return opponent;
        }
    }

    static class Board {
        private final int boardHeight;
        private final int boardWidth;
        private char[][] board;

        public Board(){
            this.boardHeight = 10;
            this.boardWidth = 10;
            board = new char[this.boardHeight][this.boardWidth];
        }

        public Board(int boardHeight, int boardWidth){
            this.boardHeight = boardHeight;
            this.boardWidth = boardWidth;
            board = new char[this.boardHeight][this.boardWidth];
            for (int y = 0; y < boardHeight; y++) {
                for (int x = 0; x < boardWidth; x++) {
                    setBoard(y, x, '~');
                }
            }
        }

        public int getBoardHeight() {
            return boardHeight;
        }

        public int getBoardWidth() {
            return boardWidth;
        }

        public char[][] getBoard() {
            return board;
        }

        public void setBoard(int y, int x, char change) {
            this.board[y][x] = change;
        }
    }

    static class Ship {
        private final String name;
        private final int size;
        private int[][] positions;
        private boolean isPositioned;
        private int hits;

        public Ship(String name, int size) {
            this.name = name;
            this.size = size;
            this.isPositioned = false;
            this.positions = new int[size][2];
            this.hits = 0;
        }

        public Ship(String name, int size, String position, Board board) {
            this.name = name;
            this.size = size;
            this.positions = new int[size][2];
            this.hits = 0;

            if (!placeShipes(board, this, position)) {
                System.out.println("\nShip cannot be positioned here\n");
                this.isPositioned = false;
            }
        }

        public boolean setPosition(int[] letterPosition, int[] numberPosition, Board board) {
            char[][] currentBoard = board.getBoard();

            int smallLetter = Math.min(letterPosition[0], letterPosition[1]);
            int smallNumber = Math.min(numberPosition[0], numberPosition[1]);
            int bigLetter = Math.max(letterPosition[0], letterPosition[1]);
            int bigNumber = Math.max(numberPosition[0], numberPosition[1]);

            if (letterPosition[0] >= board.getBoardHeight() && numberPosition[0] >= board.getBoardWidth() &&
                    letterPosition[1] >= board.getBoardHeight() && numberPosition[1] >= board.getBoardWidth()) {

                return false;
            }

            if (letterPosition[0] == numberPosition[0] && letterPosition[1] == numberPosition[1]) {
                System.out.println("\nError! Wrong ship location! Try again:\n");
                return false;
            }

            if (letterPosition[0] == letterPosition[1]){
                if (Math.abs(numberPosition[0] - numberPosition[1]) != size - 1){
                    System.out.println("\nError! Wrong length of the " + this.name + "! Try again:\n");
                    return false;
                }

                if (currentBoard[letterPosition[0]][smallNumber == 0 ? smallNumber : smallNumber - 1] != '~' ||
                        currentBoard[letterPosition[0]][bigNumber <= board.boardWidth ? bigNumber - 1 : bigNumber + 1] != '~') {
                    System.out.println("\nError! You placed it too close to another one. Try again:\n");
                    return false;
                }
            } else if (numberPosition[0] == numberPosition[1]) {
                if (Math.abs(letterPosition[0] - letterPosition[1]) != size - 1){
                    System.out.println("\nError! Wrong length of the " + this.name + "! Try again:\n");
                    return false;
                }

                if (currentBoard[smallLetter == 0 ? smallLetter : smallLetter - 1][numberPosition[0]] != '~' ||
                        currentBoard[bigLetter == (board.boardHeight-1) ? bigLetter : bigLetter + 1][numberPosition[0]] != '~') {
                    System.out.println("\nError! You placed it too close to another one. Try again:\n");
                    return false;
                }

            } else {
                System.out.println("\nError! Wrong ship location! Try again:\n");
                return false;
            }

            int place = 0;

            for (int y = smallLetter; y <= bigLetter; y++) {
                for (int x = smallNumber; x <= bigNumber; x++) {
                    if(board.getBoard()[y][x] != '~'){
                        System.out.println("\nError! You placed it too close to another one. Try again:\n");
                        return false;
                    }
                }
            }

            for (int y = smallLetter; y <= bigLetter; y++) {
                for (int x = smallNumber; x <= bigNumber; x++) {
                    board.setBoard(y, x, 'O');

                    this.positions[place][0] = y;
                    this.positions[place][1] = x;
                    place++;
                }
            }

            this.isPositioned = true;
            return true;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        public int[][] getPosition() {
            return positions;
        }

        public boolean isSank() {
            return hits == size;
        }

        public void setHit() {
            this.hits += 1;
        }

        public int getHit() {
            return hits;
        }
    }

    public static void field(Board board){
        char[][] currentBoard = board.getBoard();

        System.out.print(" ");
        for (int i = 1; i <= board.boardWidth; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int y = 0; y < board.boardHeight; y++) {
            System.out.print((char) ('A' + y));
            for (int x = 0; x < board.boardWidth; x++) {
                System.out.print(" " + currentBoard[y][x]);
            }
            System.out.println();
        }

    }

    public static boolean placeShipes(Board board, Ship ship, String coords) {
        String[] coordsArray = coords.split(" ");

        if (coordsArray.length == 2) {

            int[] correctLetters = new int[2];
            Arrays.fill(correctLetters, -1);

            for (int i = 0; i < board.boardHeight; i++) {
                if (coordsArray[0].toUpperCase().charAt(0) == (char) ('A' + i)) {
                    correctLetters[0] = i;
                }
                if (coordsArray[1].toUpperCase().charAt(0) == (char) ('A' + i)) {
                    correctLetters[1] = i;
                }
            }

            if (correctLetters[0] == -1 || correctLetters[1] == -1) {
                return false;
            }

            int[] coordX = new int[2];

            try {
                coordX[0] = Integer.parseInt(coordsArray[0].substring(1));
                coordX[1] = Integer.parseInt(coordsArray[1].substring(1));
            } catch (NumberFormatException e) {
                return false;
            }

            coordX[0]--;
            coordX[1]--;

            if(coordX[0] < 0 || coordX[0] > board.boardWidth ||
                    coordX[1] < 0 || coordX[1] > board.boardWidth) {
                return false;
            }

            return ship.setPosition(correctLetters, coordX, board);
        }
        return false;
    }

    public static boolean play(Player currentPlayer, Player opponentPlayer, String coord) {
        Scanner scanner = new Scanner(System.in);
        int currectLetter = -1;

        if(coord.length() < 2)
            return false;

        for (int i = 0; i < opponentPlayer.getBoard().getBoardHeight(); i++) {
            if (coord.toUpperCase().charAt(0) == (char) ('A' + i)) {
                currectLetter = i;
            }
        }

        if (currectLetter == -1) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            return false;
        }

        int currectNumber = 0;

        try {
            currectNumber = Integer.parseInt(coord.substring(1));
        } catch (NumberFormatException e) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            return false;
        }

        currectNumber--;

        if(currectNumber < 0 || currectNumber > opponentPlayer.getBoard().getBoardWidth()) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            return false;
        }

        if (opponentPlayer.getBoard().getBoard()[currectLetter][currectNumber] == 'O') {

            opponentPlayer.getBoard().setBoard(currectLetter, currectNumber, 'X');
            opponentPlayer.getBoardFog().setBoard(currectLetter, currectNumber, 'X');

            System.out.println();
            field(opponentPlayer.getBoardFog());
            System.out.println("---------------------");
            field(currentPlayer.getBoard());

            for (Ship s: opponentPlayer.getShips()) {
                for (int i = 0; i < s.size; i++) {
                    if (s.getPosition()[i][0] == currectLetter && s.getPosition()[i][1] == currectNumber) {
                        s.setHit();
                        currentPlayer.setPoints(currentPlayer.getPoints() + 1);
                        if(currentPlayer.isWinner() && s.isSank()){
                            System.out.println("\nYou sank the last ship. You won. Congratulations!\n");
                            return true;
                        } else if(s.isSank()) {
                            System.out.println("\nYou sank a ship!");
                            System.out.println("Press Enter and pass the move to another player\n");
                            scanner.nextLine();
                            playerTurn = currentPlayer.getOpponent();
                            return false;
                        } else {
                            System.out.println("\nYou hit a ship");
                            System.out.println("Press Enter and pass the move to another player\n");
                            scanner.nextLine();
                            playerTurn = currentPlayer.getOpponent();
                            return false;
                        }
                    }
                }
            }
        } else if(opponentPlayer.getBoard().getBoard()[currectLetter][currectNumber] != 'X') {

            opponentPlayer.getBoard().setBoard(currectLetter, currectNumber, 'M');
            opponentPlayer.getBoardFog().setBoard(currectLetter, currectNumber, 'M');
            System.out.println();
            System.out.println("\nYou missed!\n");
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            playerTurn = currentPlayer.getOpponent();
        } else {
            System.out.println();
            System.out.println("\nYou missed!\n");
            System.out.println("Press Enter and pass the move to another player\n");
            scanner.nextLine();
            playerTurn = currentPlayer.getOpponent();
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Number of players(min 2): ");
        int playersnum = 1;
        do {
            playersnum = scanner.nextInt();
        } while (playersnum < 2);

        Player[] player = new Player[playersnum];

        for (int i = 0; i < playersnum; i++) {
            player[i] = new Player(
                    "Player " + (i+1), 0,
                    new Board(10, 10),
                    new Board(10, 10),
                    new Ship[] {
                            new Ship ("Aircraft Carrier", 5),
                            new Ship ("Battleship", 4),
                            new Ship ("Submarine", 3),
                            new Ship ("Cruiser", 3),
                            new Ship ("Destroyer", 2)
            });
        }


        for (int i = 0; i < player.length; i++) {
            int placeTurns = 0;
            boolean isplaced = true;

            System.out.println(player[i].getName() + ", place your ships on the game field\n");

            field(player[i].getBoard());

            do{
                if (isplaced) {
                    System.out.println("\nEnter the coordinates of the " + player[i].getShips(placeTurns).getName() + " (" + player[i].getShips(placeTurns).getSize() + " cells):\n");
                }

                isplaced = placeShipes(player[i].getBoard(), player[i].getShips(placeTurns), scanner.nextLine());

                if (isplaced){
                    field(player[i].getBoard());
                    placeTurns++;
                }

            }while (player[i].getShips().length > placeTurns);

            System.out.println("\nPress Enter and pass the move to another player");
            scanner.nextLine();
            System.out.println();
        }

        if (player.length == 2) {
            player[0].setOpponent(1);
            player[1].setOpponent(0);
        } else {
            for (Player value : player) {
                System.out.println("Set opponent for " + value.name);

                for (int i = 0; i < player.length; i++) {
                    if(player[i] != value)
                        System.out.println(i + " - " + player[i].name);
                }

                int op = 0;

                do {
                    try {
                        op = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                } while (op < 0 && op > player.length);

                value.setOpponent(op);
            }
        }

        boolean gameEnd = false;
        playerTurn = 0;
        scanner.reset();

        while (!gameEnd){
            System.out.println();
            field(player[player[playerTurn].getOpponent()].getBoardFog());
            System.out.println("---------------------");
            field(player[playerTurn].getBoard());
            System.out.println("\n" + player[playerTurn].name + ", it's your turn:");
            gameEnd = play(player[playerTurn], player[player[playerTurn].getOpponent()], scanner.nextLine());
        }
    }
}
