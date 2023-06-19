import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
    private int boardSize;
    private char[][] board;
    private List<int[]> ships;
    private long startTime;
    private List<Long> gameTimes;

    public BattleshipGame(int boardSize, int shipCount) {
        this.boardSize = boardSize;
        this.board = new char[boardSize][boardSize];
        this.ships = new ArrayList<>();
        this.startTime = 0;
        this.gameTimes = new ArrayList<>();

        // Инициализация поля
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = '-';
            }
        }

        // Расстановка кораблей
        Random random = new Random();
        for (int i = 0; i < shipCount; i++) {
            int[] ship = { random.nextInt(boardSize), random.nextInt(boardSize) };
            ships.add(ship);
        }
    }

    public void showMenu() {
        System.out.println("МЕНЮ:");
        System.out.println("1. Новая игра");
        System.out.println("2. Результаты");
        System.out.println("3. Выход");
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            showMenu();
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    showResults();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте ещё раз.");
                    break;
            }
        }
    }

    private void startNewGame() {
        startTime = System.currentTimeMillis();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = '-';
            }
        }

        ships.clear();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int[] ship = { random.nextInt(boardSize), random.nextInt(boardSize) };
            ships.add(ship);
        }

        playSingleGame();
    }

    private void playSingleGame() {
        Scanner scanner = new Scanner(System.in);

        while (!allShipsDestroyed()) {
            printBoard();

            System.out.print("Куда стреляем (введите ячейку в формате A2, D5 и т.д.): ");
            String guess = scanner.next();
            int guessRow = guess.charAt(1) - '1';
            int guessCol = guess.charAt(0) - 'A';

            if (isValidGuess(guessRow, guessCol)) {
                if (checkHit(guessRow, guessCol)) {
                    System.out.println("Попадание!");

                    if (isShipDestroyed(guessRow, guessCol)) {
                        System.out.println("Вы потопили корабль!");
                        markDestroyedShip(guessRow, guessCol);
                        markAdjacentCells(guessRow, guessCol);
                    } else {
                        markHit(guessRow,
                        guessCol);
                    }
                } else {
                    System.out.println("Промах!");
                    markMiss(guessRow, guessCol);
                }
            } else {
                System.out.println("Неверные координаты. Попробуйте ещё раз.");
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime - startTime) / 1000;
        System.out.println("Поздравляем! Вы потопили все корабли!");
        System.out.println("Время игры: " + elapsedTime + " сек.");

        gameTimes.add(elapsedTime);
    }

    private boolean allShipsDestroyed() {
        return ships.isEmpty();
    }

    private boolean isValidGuess(int guessRow, int guessCol) {
        return guessRow >= 0 && guessRow < boardSize && guessCol >= 0 && guessCol < boardSize;
    }

    private boolean checkHit(int guessRow, int guessCol) {
        for (int[] ship : ships) {
            if (guessRow == ship[0] && guessCol == ship[1]) {
                return true;
            }
        }
        return false;
    }

    private boolean isShipDestroyed(int guessRow, int guessCol) {
        for (int[] ship : ships) {
            if (guessRow == ship[0] && guessCol == ship[1]) {
                return false;
            }
        }
        return true;
    }

    private void markHit(int guessRow, int guessCol) {
        board[guessRow][guessCol] = 'U';
    }

    private void markDestroyedShip(int guessRow, int guessCol) {
        board[guessRow][guessCol] = 'X';
    }

    private void markMiss(int guessRow, int guessCol) {
        board[guessRow][guessCol] = 'o';
    }

    private void markAdjacentCells(int guessRow, int guessCol) {
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        for (int[] direction : directions) {
            int newRow = guessRow + direction[0];
            int newCol = guessCol + direction[1];

            if (isValidGuess(newRow, newCol)) {
                if (board[newRow][newCol] == '-') {
                    board[newRow][newCol] = 'o';
                }
            }
        }
    }

    private void printBoard() {
        System.out.print("  ");
        for (int col = 0; col < boardSize; col++) {
            System.out.print((char) ('A' + col) + " ");
        }
        System.out.println();

        for (int row = 0; row < boardSize; row++) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < boardSize; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void showResults() {
        if (gameTimes.isEmpty()) {
            System.out.println("Нет доступных результатов.");
        } else {
            System.out.println("Топ 3 самых быстрых выигранных игр:");

            List<Long> sortedTimes = new ArrayList<>(gameTimes);
            sortedTimes.sort(null);

            int count = Math.min(3, sortedTimes.size());
            for (int i = 0; i < count; i++) {
                long time = sortedTimes.get(i);
                System.out.println((i + 1) + ". " + time + " сек.");
            }
        }
    }

    public static void main(String[] args) {
        int boardSize = 8;
        int shipCount = 5;

        BattleshipGame game = new BattleshipGame(boardSize, shipCount);
        game.playGame();
    }
}
