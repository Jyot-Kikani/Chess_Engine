package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(PieceType.ROOK, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        clearValidMoves();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            for (int step = 1; step < 8; step++) {
                int newX = x + dir[0] * step;
                int newY = y + dir[1] * step;

                if (!board.isInBounds(newX, newY)) break;  // Stop if out of bounds

                Piece targetPiece = board.getPieceAt(newX, newY);

                // If the target square is empty, move there temporarily
                if (targetPiece == null) {
                    board.setPieceAt(newX, newY, this);  // Temporarily move the Rook
                    board.setPieceAt(x, y, null);  // Remove Rook from original position
                    if (checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));  // Add valid move if King is safe
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));  // Add valid move if King is safe
                    }
                    board.setPieceAt(newX, newY, null);  // Restore original square
                    board.setPieceAt(x, y, this);  // Restore Rook to original position
                }
                // If it's an opponent's piece, consider the capture
                else if (board.isOpponentPiece(newX, newY, isWhite())) {
                    board.setPieceAt(newX, newY, this);  // Temporarily move the Rook
                    board.setPieceAt(x, y, null);  // Remove Rook from original position
                    if (checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));  // Add valid move if King is safe
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));  // Add valid move if King is safe
                    }
                    board.setPieceAt(newX, newY, targetPiece);  // Restore opponent's piece
                    board.setPieceAt(x, y, this);  // Restore Rook to original position
                    break;  // Stop after capturing
                }
                // If it's our own piece, stop checking further in that direction
                else {
                    break;  // Stop if blocking own piece
                }
            }
        }
    }
}
