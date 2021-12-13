package com.company.engines;

import com.company.models.AI;
import com.company.models.Board;
import com.company.models.Move;

import java.util.Scanner;
import java.util.stream.Stream;

public class EnginePlayerBot {

    private final Scanner consoleReader;

    private final Board board;

    private final String player = "X";
    private final String enemy = "O";

    // Player -> 1
    // Bot    -> 2
    private int currentPlayer;

    public EnginePlayerBot(Scanner reader) {
        this.board = new Board();
        currentPlayer = -1;
        consoleReader = reader;
    }

    public void Run()
    {
        AI bot = new AI();

        for (int turn = 0; turn < 9; turn++)
        {
            currentPlayer = turn % 2 + 1;

            board.drawBoard();

            if(turn == 8) {
                fillLastCell(player);
                break;
            }

            if(turn % 2 == 0)
            {
                Move move = playerTurn(turn);

                if(turn >= 4)
                {
                    if(checkEndGame(move.getRow(), move.getCol(), player))
                    {
                        break;
                    }
                }
            }
            else
            {
                char[][] charBoard  = new char[3][3];

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        charBoard[i][j] = board.getBoard()[i][j].getSymbol().charAt(0);
                    }
                }

                Move move = bot.findBestMove(charBoard);
                board.getBoard()[move.getRow()][move.getCol()].setSymbol(enemy);

                if(turn >= 4)
                {
                    if(checkEndGame(move.getRow(), move.getCol(), enemy))
                    {
                        break;
                    }
                }
            }
        }
    }

    private boolean checkEndGame(int lastRowInserted, int lastColumnInserted, String lastSymbolInserted)
    {
        boolean gameFinished = false;

        gameFinished = hasWinner(lastRowInserted, lastColumnInserted, lastSymbolInserted);

        if(gameFinished) {
            gameEnd(currentPlayer);
        }

        return gameFinished;
    }

    private Move playerTurn(int turn)
    {
        int choice = -1;
        int[] coordinates = new int[2];
        do {
            do {
                System.out.print("Insert coordinates indexes[row,col]: ");
                choice = consoleReader.nextInt();
                choice--;
                coordinates[0] = choice / 3;
                coordinates[1] = choice % 3;

            }while(!checkValidCoordinatesInput(coordinates[0], coordinates[1]));
        } while (!checkACellIsFree(coordinates[0], coordinates[1]));

        board.getBoard()[coordinates[0]][coordinates[1]].setSymbol(player);

        return new Move(coordinates[0], coordinates[1]);
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
        tp:for (int row = 0; row < 3; row++) {
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
                    break tp;
                }
            }
        }
    }

}
