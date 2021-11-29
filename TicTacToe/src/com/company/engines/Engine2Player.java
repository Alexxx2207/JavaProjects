package com.company.engines;

import com.company.models.Board;

import java.util.Scanner;
import java.util.stream.Stream;

public class Engine2Player {

    private final Scanner consoleReader;

    private final Board board;

    // Player 1 -> играе с X
    // Player 2 -> играе с O
    private int currentPlayer;

    public Engine2Player(Scanner reader) {
        this.board = new Board();
        currentPlayer = -1;
        consoleReader = reader;
    }


    public void Run()
    {
        String currentSymbol = "";

        for (int turn = 0; turn < 9; turn++)
        {
            board.drawBoard();

            if(turn % 2 == 0)
            {
                currentSymbol = "X";
            }
            else
            {
                currentSymbol = "O";
            }
            currentPlayer = turn % 2 + 1;

            if(turn == 8) {
                fillLastCell(currentSymbol);
                break;
            }

            int[] coordinates = null;

            do {
                do {
                    System.out.println("Player " + (currentPlayer == 1 ? "X" : "O"));
                    System.out.print("Insert coordinates indexes[row,col]: ");
                    coordinates = Stream.of(consoleReader.nextLine()
                            .split(",\\s*"))
                            .mapToInt(Integer::parseInt).toArray();
                }while(!checkValidCoordinatesInput(coordinates[0], coordinates[1]));
            } while (!checkACellIsFree(coordinates[0], coordinates[1]));

            board.getBoard()[coordinates[0]][coordinates[1]].setSymbol(currentSymbol);

            boolean gameFinished = false;

            if(turn >= 4)
            {

                gameFinished = hasWinner(coordinates[0], coordinates[1], currentSymbol);

                if(gameFinished) {
                    gameEnd(currentPlayer);
                    break;
                }
            }
        }
    }

    private boolean hasWinner(int lastSymbolInsertedRow,
                              int lastSymbolInsertedCol,
                              String currentSymbolInserted)
    {
        boolean haveWinner = true;
        //check column
        for (int row = 0; row < 3; row++)
        {
            if(!board.getBoard()[row][lastSymbolInsertedCol].getSymbol().equals(currentSymbolInserted))
            {
                haveWinner = false;
                break;
            }
        }

        if(haveWinner)
            return true;
        else
            haveWinner = true;

        //check row
        for (int col = 0; col < 3; col++)
        {
            if(!board.getBoard()[lastSymbolInsertedRow][col].getSymbol().equals(currentSymbolInserted))
            {
                haveWinner = false;
                break;
            }
        }

        if(haveWinner)
            return true;
        else
            haveWinner = true;

        // check main diagonal
        for (int row = 0, col = 0; row < 3 && col < 3; row++, col++) {
            if(!board.getBoard()[row][col].getSymbol().equals(currentSymbolInserted))
            {
                haveWinner = false;
                break;
            }
        }

        if(haveWinner)
            return true;
        else
            haveWinner = true;

        // check secondary diagonal
        for (int row = 0, col = 2; row < 3 && col >= 0; row++, col--) {
            if(!board.getBoard()[row][col].getSymbol().equals(currentSymbolInserted))
            {
                haveWinner = false;
                break;
            }
        }
        
        return haveWinner;
    }

    private void gameEnd(int winner)
    {
        board.drawBoard();

        if(winner == 1)
            System.out.println("Player X wins!");
        else if(winner == 2)
            System.out.println("Player O wins!");
        else
            System.out.println("Draw!");
    }

    private boolean checkACellIsFree(int row, int col)
    {
        boolean result = board.getBoard()[row][col].getSymbol().equals(" ");

        if(!result) {
            System.out.println("\nCell already filled!");
            board.drawBoard();
        }
        return result;
    }

    private boolean checkValidCoordinatesInput(int row, int col)
    {
        boolean result = row >= 0 && row < 3 && col >= 0 && col < 3;

        if(!result) {
            System.out.println("\nInvalid coordinates!");
            board.drawBoard();
        }

        return result;
    }

    private void fillLastCell(String currentSymbol)
    {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if(board.getBoard()[row][col].getSymbol().equals((" ")))
                {
                    board.getBoard()[row][col].setSymbol(currentSymbol);
                    boolean gameFinished = hasWinner(row, col, currentSymbol);

                    if(gameFinished) {
                        gameEnd(currentPlayer);
                        break;
                    }
                    else {
                        gameEnd(-1);
                    }
                    break;
                }
            }
        }


    }
}
