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
        clearValidMoves();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            for (int step = 1; step < 8; step++) {
                int newX = x + dir[0] * step;
                int newY = y + dir[1] * step;

                if (!board.isInBounds(newX, newY)) break;

                Piece targetPiece = board.getPieceAt(newX, newY);
                if (targetPiece == null) {
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);
                    if(checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    board.setPieceAt(newX, newY, null);
                    board.setPieceAt(x, y, this);
                } else if (board.isOpponentPiece(newX, newY, isWhite())) {
                    board.setPieceAt(newX, newY, this);
                    board.setPieceAt(x, y, null);
                    if(checkKingSafety) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
                    else if (!board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(newX, newY));
                    }
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
