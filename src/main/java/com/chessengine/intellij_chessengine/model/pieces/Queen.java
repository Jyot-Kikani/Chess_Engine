package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(PieceType.QUEEN, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        clearValidMoves();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};


        for (int[] dir : directions) {
            // Iterate through each direction
            for (int step = 1; step < 8; step++) {
                int newX = x + dir[0] * step;
                int newY = y + dir[1] * step;

                // Check if the new position is within the board bounds
                if (!board.isInBounds(newX, newY)) break;

                Piece targetPiece = board.getPieceAt(newX, newY);
                if (targetPiece == null) {
                    // Temporarily move the Queen to the new position
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y,  null);
                    // Check if the King is in check after the move
                    if (checkKingSafety) {
                        // Debugging output for black Queen
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    // Restore the original positions
                    board.setPieceAt(newX, newY, null);
                    board.setPieceAt(x, y, this);
                }
                else if (board.isOpponentPiece(newX, newY, isWhite())) {
                    // Temporarily move the Queen to the new position
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);
                    if (checkKingSafety) {
                        // Debugging output for black Queen
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    // Restore the original positions
                    board.setPieceAt(newX, newY, targetPiece);
                    board.setPieceAt(x, y, this);
                    break;
                } else {
                    break;  // Stop if blocking own piece
                }
            }
        }
    }
}
