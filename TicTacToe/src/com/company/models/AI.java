package com.company.models;

//// CREDIT:
//// https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/

import com.company.Main;

public class AI {

    static char player = 'O', opponent = 'X';

    private Boolean isMovesLeft(char[][] board)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ')
                    return true;
        return false;
    }

    private int evaluate(char[][] b)
    {
        for (int row = 0; row < 3; row++)
        {
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2])
            {
                if (b[row][0] == player)
                    return +10;
                else if (b[row][0] == opponent)
                    return -10;
            }
        }

        for (int col = 0; col < 3; col++)
        {
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col])
            {
                if (b[0][col] == player)
                    return +10;

                else if (b[0][col] == opponent)
                    return -10;
            }
        }

        if (b[0][0] == b[1][1] && b[1][1] == b[2][2])
        {
            if (b[0][0] == player)
                return +10;
            else if (b[0][0] == opponent)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0])
        {
            if (b[0][2] == player)
                return +10;
            else if (b[0][2] == opponent)
                return -10;
        }
        return 0;
    }

    private int minimax(char[][] board, int depth, boolean isMax)
    {
        int score = evaluate(board);

        if (score == 10)
            return score;

        if (score == -10)
            return score;

        if (!isMovesLeft(board))
            return 0;

        int best;

        if (isMax)
        {
            best = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j]==' ')
                    {
                        board[i][j] = player;

                        best = Math.max(best, minimax(board, depth + 1, false));

                        board[i][j] = ' ';
                    }
                }
            }
        }

        else
        {
            best = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == ' ')
                    {
                        board[i][j] = opponent;

                        best = Math.min(best, minimax(board, depth + 1, true));

                        board[i][j] = ' ';
                    }
                }
            }
        }
        return best;
    }

    public Move findBestMove(char[][] board)
    {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = new Move();
        bestMove.setRow(-1);
        bestMove.setCol(-1);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (board[i][j] == ' ')
                {
                    board[i][j] = player;

                    int moveVal = minimax(board, 0, false);

                    board[i][j] = ' ';

                    if (moveVal > bestVal)
                    {
                        bestMove.setRow(i);
                        bestMove.setCol(j);
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }
}
