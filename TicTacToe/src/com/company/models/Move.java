package com.company.models;

public class Move {

    public Move() {
    }

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }



    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int row;
    private int col;
}
