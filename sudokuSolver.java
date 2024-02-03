
/**
 * Solves sudoku boards
 * 
 * @author Basil Howard Ok
 */
import java.time.LocalTime;
import java.util.Random;

class sudokuSolver {

    private static char[] numSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    static int numIndex = 0;
    static int boardIndex = 0;
    static char[] availNumbers;
    static int[] boardIndexSet;
    static int boardX;
    static int boardY;

    public static boolean isValidSudoku(char[][] board) {
        if (!checkDupesInRows(board))
            return false;

        char[][] transpose = transpose(board);
        if (!checkDupesInRows(transpose))
            return false;

        char[][] blocksToRows = blocksToRows(board);
        if (!checkDupesInRows(blocksToRows))
            return false;

        return true;
    }

    public static boolean checkDupesInRows(char[][] board) {
        int dupeIsTwo = 0;
        int count = 0;
        while (count < board.length) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (numSequence[i] == board[count][j]) {
                        dupeIsTwo++;
                        // System.out.println(dupeIsTwo); debug print
                    }
                    if (dupeIsTwo == 2) {
                        return false;
                    }
                }
                dupeIsTwo = 0;
            }
            count++;
        }
        return true;
    }

    public static void solveSudoku(char[][] board) {
        // solveSudoku will first fill in an initial guess char[][]
        // then it will call checkSudoku to ensure if it fulfills the 3 rules
        // iterate until checkSodoku returns true
        int count = 0;

        char[][] answerBoard = null;
        while (!checkFinalSudoku(answerBoard)) {
            // first algorithm based off RNG
            answerBoard = fillRandomBoard(board);

            System.out.println(count + "...");
            count++;
        }
        System.out.println("Final Answer:");
        printBoard(answerBoard);

    }

    public static int[] getBoardIndexSet(char[] row, int length) {
        int ret[] = new int[length];
        int count = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] == '.') {
                ret[count] = i;
                count++;
            }
        }

        return ret;
    }

    public static char[][] backTrackSolution(char[][] board) {

        char[][] answerBoard = copyBoard(board);
        availNumbers = getAvailNums(board[0]);
        boardIndexSet = getBoardIndexSet(board[0], availNumbers.length);
        boardX = 0;
        boardY = 0;

        while (!checkFinalSudoku(board)) {

            if (isValidSudoku(board)) {
                addToBoard(board, answerBoard);
            } else {
                backTrackBoard(board, answerBoard);
            }
            System.out.println(boardX);
        }
        return board; // solved board with final answer
    }

    public static char[][] copyBoard(char[][] board) {
        char[][] ret = new char[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                ret[i][j] = board[i][j];
            }
        }
        return ret;
    }

    public static char[][] addToBoard(char[][] board, char[][] answerBoard) {

        // if index equals the length of availnumbers
        // reset the index
        // increment rows by 1
        // update availNumbers with new array set
        if (numIndex >= availNumbers.length) {
            // System.out.println("yay");

            // System.out.println(" here is num " + numIndex + " and avail: " +
            // availNumbers.length);
            backTrackBoard(board, answerBoard);
        }
        // System.out.println("length is " + boardIndexSet.length + ", index is: " +
        // boardIndex);
        if (boardIndex >= boardIndexSet.length) {
            // System.out.println("rest");
            boardIndex = 0;
            numIndex = 0;
            boardX++;
            // System.out.println("board x" + boardX);
            availNumbers = getAvailNums(board[boardX]);
            boardIndexSet = getBoardIndexSet(board[boardX], availNumbers.length);
        }
        // if row equal 10 just return board as is
        if (boardX == 10) {
            return board;
        }

        // case 1
        // add next number to the board at the indicated index position
        // increase index position for both avail numbers and the board by 1
        int tempX = boardX;
        int tempY = boardIndexSet[boardIndex];
        // System.out.println(tempX);
        // System.out.println(tempY);
        answerBoard[tempX][tempY] = availNumbers[numIndex];
        boardIndex++;
        numIndex++;

        return answerBoard;
    }

    public static char[][] backTrackBoard(char[][] board, char[][] answerBoard) {

        // case 1
        // decrease index position for the board by 1
        // remove the number on the board at this index

        boardIndex--;
        // System.out.println(boardIndex);
        answerBoard[boardX][boardIndexSet[boardIndex]] = '.';

        // case 2
        // if the index position for available numbers is equal to its length
        // decrease index position for the board by 1
        // remove the number on the board at this index
        if (numIndex >= availNumbers.length) {
            boardIndex--;

            // if the 2nd decrement makes the position less than 0
            // decrease the row by 1
            // update the avail numbers with function
            // set and remove the last indexed element
            // find the removed element and update the index in the avail numbers
            if (boardIndex < 0) {
                boardX--;

                boardIndex = answerBoard.length - 1;
                // System.out.println(boardX);
                availNumbers = getAvailNums(board[boardX]);
                boardIndexSet = getBoardIndexSet(board[boardX], availNumbers.length);
                char tempChar = answerBoard[boardX][boardIndex];
                answerBoard[boardX][boardIndex] = '.';

                for (int i = 0; i < availNumbers.length; i++) {
                    if (availNumbers[i] == tempChar) {
                        numIndex = i + 1;
                        break;
                    }
                }

            } else {
                // decrease the index position by 1 again
                // remove and record the number on the board at the updated index
                // with the record, find the index position on the available numbers array
                // update the index position for available numbers when found and add 1
                // System.out.println(boardX);

                char tempChar = answerBoard[boardX][boardIndexSet[boardIndex]];
                answerBoard[boardX][boardIndexSet[boardIndex]] = '.';

                for (int i = 0; i < availNumbers.length; i++) {
                    if (availNumbers[i] == tempChar) {
                        numIndex = i + 1;
                        break;
                    }
                }
            }

        }

        return answerBoard;
    }

    // NOTICE, THIS FUNCTION TAKES TOO MUCH TIME TO FINISH. IMPLEMENT BACKTRACKING
    // randomly fills in all numbers
    public static char[][] fillRandomBoard(char[][] board) {
        char[][] ret = new char[9][9];

        for (int i = 0; i < board.length; i++) {
            char[] availNums = getAvailNums(board[i]);
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == '.') {
                    int rand = getRandInLength(availNums);
                    ret[i][j] = availNums[rand];
                    availNums = removeChar(rand, availNums);
                } else {
                    ret[i][j] = board[i][j];
                }
            }
        }
        return ret;
    }

    public static char[] removeChar(int index, char[] charArray) {
        char[] ret = new char[charArray.length - 1];
        int count = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (i != index) {
                ret[count] = charArray[i];
                count++;
            }
        }

        return ret;
    }

    public static int getRandInLength(char[] line) {
        int length = line.length;
        String time = LocalTime.now().toString().replaceAll("[\\:\\.]", "");
        Random generator = new Random(Long.parseLong(time));
        int ret = generator.nextInt(length);
        return ret;
    }

    public static char[] getAvailNums(char[] line) {
        int count = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == '.') {
                count++;
            }
        }

        char[] indexArray = new char[line.length - count];
        count = 0;
        for (int i = 0; i < line.length; i++) {
            if (line[i] != '.') {
                indexArray[count] = line[i];
                count++;
            }
        }

        char[] numCopy = new char[numSequence.length];
        for (int i = 0; i < numSequence.length; i++) {
            numCopy[i] = numSequence[i];
        }

        for (int i = 0; i < indexArray.length; i++) {
            for (int j = 0; j < numCopy.length; j++) {
                if (indexArray[i] == numCopy[j]) {
                    numCopy[j] = '.';
                    break;
                }
            }
        }

        char[] ret = new char[line.length - count];
        count = 0;
        for (int i = 0; i < numCopy.length; i++) {
            if (numCopy[i] != '.') {
                ret[count] = numCopy[i];
                count++;
            }
        }
        return ret;
    }

    public static boolean checkFinalSudoku(char[][] board) {
        if (board == null) {
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++)
                if (board[i][j] == '.')
                    return false;
        }

        // check if rows are 1-9
        if (!checkRowsHasNums(board))
            return false;

        // check if columns are 1-9
        char[][] transpose = transpose(board);
        if (!checkRowsHasNums(transpose))
            return false;

        // check if blocks are 1-9
        char[][] blocksToRows = blocksToRows(board);
        if (!checkRowsHasNums(blocksToRows))
            return false;

        return true;
    }

    public static boolean checkRowsHasNums(char[][] board) {
        boolean hasNum = false;
        int count = 0;
        while (count < board.length) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (numSequence[i] == board[count][j]) {
                        hasNum = true;
                        j = board.length;
                    }
                }
                if (!hasNum)
                    return false;
                hasNum = false;
            }
            count++;
        }
        return true;
    }

    // useful for checking columns
    public static char[][] transpose(char[][] board) {
        char[][] ret = new char[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                ret[i][j] = board[j][i];
            }
        }
        return ret;
    }

    // for when checking blocks
    public static char[][] blocksToRows(char[][] board) {
        // converts 3x3 blocks into 1x9 rows until 9x9
        char[][] ret = new char[9][9];
        int inCount = 0; // is used to indicate row index, it raises on the 3rd, resets on the ninth, and
                         // uses outCount to change base range after 3 rows are filled. or 27 chars
        int outCount = 0;
        int indexMult = 0; // used to change base range in columns after every 9th, resets via mod
                           // calculation

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                // selects board normally and puts it into ret into 3 different rows until each
                // rows are filled, when filled, the base range in rows will change from...
                // 0-2, 3-5, 6-8
                ret[inCount + (outCount * 3)][(j % 3) + (3 * (indexMult % 3))] = board[i][j];

                if ((j + 1) % 3 == 0)
                    inCount++;

            }
            inCount = 0;
            indexMult++;
            if ((i + 1) % 3 == 0)
                outCount++;
        }

        return ret;

    }

    public static char[][] createBoard() {
        char[][] ret = {
                { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
                { '6', '.', '.', '1', '9', '5', '.', '.', '.' },
                { '.', '9', '8', '.', '.', '.', '.', '6', '.' },
                { '8', '.', '.', '.', '6', '.', '.', '.', '3' },
                { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
                { '7', '.', '.', '.', '2', '.', '.', '.', '6' },
                { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
                { '.', '.', '.', '4', '1', '9', '.', '.', '5' },
                { '.', '.', '.', '.', '8', '.', '.', '7', '9' } };
        return ret;
    }

    public static char[][] createTestBoard() {
        char[][] ret = {
                { '5', '3', '4', '6', '7', '8', '9', '1', '2' },
                { '6', '7', '2', '1', '9', '5', '3', '4', '8' },
                { '1', '9', '8', '3', '4', '2', '5', '6', '7' },
                { '8', '5', '9', '7', '6', '1', '4', '2', '3' },
                { '4', '2', '6', '8', '5', '3', '7', '9', '1' },
                { '7', '1', '3', '9', '2', '4', '8', '5', '6' },
                { '9', '6', '1', '5', '3', '7', '2', '8', '4' },
                { '2', '8', '7', '4', '1', '9', '6', '3', '5' },
                { '3', '4', '5', '2', '8', '6', '1', '7', '9' }
        };
        return ret;
    }

    public static void printBoard(char[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]);
                if ((j + 1) % 3 == 0)
                    System.out.print(' ');
            }
            System.out.println();
            if ((i + 1) % 3 == 0)
                System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] testBoard = createBoard();
        // char[][] newBoard = createTestBoard();
        // char[][] tpose = transpose(testBoard);
        // char[][] btr = blocksToRows(testBoard);
        // System.out.println("Sudoku Board:");
        // printBoard(testBoard);
        // System.out.println("====================================================");
        // System.out.println("Transpose:");
        // printBoard(tpose);
        // System.out.println("====================================================");
        // System.out.println("Blocks To Rows:");
        // printBoard(btr);

        // System.out.println(checkFinalSudoku(newBoard));
        // // solveSudoku(newBoard);

        // System.out.println(isValidSudoku(testBoard));

        // char[] availNums = getAvailNums(testBoard[0]);

        // for (int i = 0; i < availNums.length; i++) {
        // System.out.print(availNums[i]);
        // }
        // System.out.println();
        // System.out.println(availNums.length);
        char[] availNums = getAvailNums(testBoard[0]);
        int[] indexSet = getBoardIndexSet(testBoard[0], availNums.length);
        // availNums = removeChar(2, availNums);
        System.out.println(availNums.length);

        for (int i = 0; i < availNums.length; i++) {
            System.out.print(availNums[i]);
        }
        System.out.println();
        System.out.println('h');
        for (int i = 0; i < indexSet.length; i++) {
            System.out.print(indexSet[i]);
        }

        char finalAnswer[][] = backTrackSolution(testBoard);

    }

}