package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class King extends Piece {

    public King(boolean isWhite) {
        super(PieceType.KING, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        clearValidMoves();  // Clear previous moves
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < dx.length; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (board.isInBounds(newX, newY) && (board.isSquareEmpty(newX, newY) || board.isOpponentPiece(newX, newY, isWhite()))) {
                Piece originalPiece = board.getPieceAt(newX, newY);
                if(checkKingSafety) {
                    validMoves.add(new Pair<>(newX, newY));
                }
                else {
                    board.setPieceAt(newX, newY, this);  // Temporarily move the King
                    board.setPieceAt(x, y, null);  // Remove King from original position

                    // Check if the King would be in check after this move
                    boolean kingInCheck = board.isKingInCheck(isWhite());
                    // If the King is not in check, add the move to valid moves
                    if (!kingInCheck) {
                        validMoves.add(new Pair<>(newX, newY));  // Add valid move if King is safe
                    }

                    // Restore the original position
                    board.setPieceAt(newX, newY, originalPiece);  // Restore original piece
                    board.setPieceAt(x, y, this);  // Restore King to original position

                }
            }
        }
    }
}
