package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(PieceType.BISHOP, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        // Clear the list of valid moves
        clearValidMoves();

        // Define the directions a Bishop can move (diagonally)
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Iterate over each direction
        for (int[] dir : directions) {
            // Move in the current direction up to 7 steps (max board size)
            for (int step = 1; step < 8; step++) {
                int newX = x + dir[0] * step;
                int newY = y + dir[1] * step;

                // Check if the new position is within the board boundaries
                if (!board.isInBounds(newX, newY)) break;

                Piece targetPiece = board.getPieceAt(newX, newY);
                if (targetPiece == null) {
                    // Temporarily move the piece to the new position
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);

                    // Check if the move is valid considering king safety
                    if(checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }

                    // Revert the temporary move
                    board.setPieceAt(newX, newY, null);
                    board.setPieceAt(x, y, this);
                } else if (board.isOpponentPiece(newX, newY, isWhite())) {
                    // Temporarily move the piece to the new position
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);

                    // Check if the move is valid considering king safety
                    if(checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }

                    // Revert the temporary move and break as the path is blocked
                    board.setPieceAt(newX, newY, targetPiece);
                    board.setPieceAt(x, y, this);
                    break;
                } else {
                    // Stop if the path is blocked by own piece
                    break;
                }
            }
        }
    }
}
