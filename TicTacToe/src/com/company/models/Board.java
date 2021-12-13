package com.company.models;

public class Board {
    private Cell[][] board;

    public Board() {
        InitializeBoard();
    }

    public Cell[][] getBoard() {
        return board;
    }

    private void InitializeBoard()
    {
        board = new Cell[3][];

        for (int row = 0; row < board.length; row++)
        {
            board[row] = new Cell[3];
            for (int col = 0; col < board[row].length; col++)
            {
                board[row][col] = new Cell(" ");
            }
        }
    }

    public void drawBoard()
    {
        int counter = 1;
        System.out.println("\nBoard: ");
        System.out.println(new String("|---|---|---|"));
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if(cell.getSymbol().equals(" "))
                    System.out.print("| " + counter + " ");
                else
                    System.out.print("| " + cell.getSymbol() + " ");
                counter++;
            }
            System.out.println("|");
        }
        System.out.println(new String("|---|---|---|\n"));
    }
}
