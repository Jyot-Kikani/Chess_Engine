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
        clearValidMoves();
        int[][] moves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];

            if (board.isInBounds(newX, newY)) {
                Piece targetPiece = board.getPieceAt(newX, newY);
                if (targetPiece == null || board.isOpponentPiece(newX, newY, isWhite())) {
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);
                    if(checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    board.setPieceAt(newX, newY, targetPiece);  // Restore original piece
                    board.setPieceAt(x, y, this);  // Restore Knight to original position
                }
            }
        }
    }
}
