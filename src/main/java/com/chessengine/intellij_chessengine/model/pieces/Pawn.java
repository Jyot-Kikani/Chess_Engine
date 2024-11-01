package com.chessengine.intellij_chessengine.model.pieces;

import com.chessengine.intellij_chessengine.model.Board;
import com.chessengine.intellij_chessengine.model.Piece;
import com.chessengine.intellij_chessengine.model.PieceType;
import javafx.util.Pair;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(PieceType.PAWN, isWhite);
    }

    @Override
    public void calculateValidMoves(int x, int y, Board board, boolean checkKingSafety) {
        clearValidMoves();
        int direction = isWhite() ? -1 : 1;  // White moves up, black moves down

        // Move forward one square
        if (board.isInBounds(x + direction, y) && board.isSquareEmpty(x + direction, y)) {
            Piece originalPiece = board.getPieceAt(x + direction, y);
            board.setPieceAt(x + direction, y, this);
            board.setPieceAt(x, y, null);


            if (!checkKingSafety && !board.isKingInCheck(isWhite())) {
                validMoves.add(new Pair<>(x + direction, y));
            }

            board.setPieceAt(x + direction, y, originalPiece);  // Restore original piece
            board.setPieceAt(x, y, this);  // Restore Pawn to original position

            // Move forward two squares from the starting position
            if ((isWhite() && x == 6) || (!isWhite() && x == 1)) {
                if (board.isSquareEmpty(x + 2 * direction, y) && board.isSquareEmpty(x + direction, y)) {
                    originalPiece = board.getPieceAt(x + 2 * direction, y);
                    board.setPieceAt(x + 2 * direction, y, this);
                    board.setPieceAt(x, y, null);

                    if (!checkKingSafety && !board.isKingInCheck(isWhite())) {
                        validMoves.add(new Pair<>(x + 2 * direction, y));
                    }

                    board.setPieceAt(x + 2 * direction, y, originalPiece);  // Restore original piece
                    board.setPieceAt(x, y, this);  // Restore Pawn to original position
                }
            }
        }

        // Capture diagonally
        for (int dy = -1; dy <= 1; dy += 2) {
            if (board.isInBounds(x + direction, y + dy) && board.isOpponentPiece(x + direction, y + dy, isWhite())) {
                Piece originalPiece = board.getPieceAt(x + direction, y + dy);
                board.setPieceAt(x + direction, y + dy, this);
                board.setPieceAt(x, y, null);

                if (checkKingSafety) {
                    validMoves.add(new Pair<>(x + direction, y + dy));
                } else if (!board.isKingInCheck(isWhite())) {
                    validMoves.add(new Pair<>(x + direction, y + dy));
                }

                board.setPieceAt(x + direction, y + dy, originalPiece);  // Restore original piece
                board.setPieceAt(x, y, this);  // Restore Pawn to original position
            }
        }
    }
}
