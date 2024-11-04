package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(PieceType.KNIGHT, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        clearValidMoves();  // Clear the list of valid moves

        // Define the possible moves for a Knight
        int[][] moves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        // Iterate over each possible move
        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new position is within the board boundaries
            if (board.isInBounds(newX, newY)) {
                Piece targetPiece = board.getPieceAt(newX, newY);

                // Check if the target position is empty or contains an opponent's piece
                if (targetPiece == null || board.isOpponentPiece(newX, newY, isWhite())) {
                    // Temporarily move the Knight to the new position
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);

                    // Check if the move is valid considering king safety
                    if (checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    } else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }

                    // Restore the original position
                    board.setPieceAt(newX, newY, targetPiece);  // Restore original piece
                    board.setPieceAt(x, y, this);  // Restore Knight to original position
                }
            }
        }
    }
}
