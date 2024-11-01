package com.chessengine.intellij_chessengine.model;

public class ChessModel {
    private final Board board;
    private boolean whiteToMove;

    public ChessModel() {
        board = new Board();
        whiteToMove = true;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public Board getBoard() {
        return board;
    }

    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPieceAt(startX, startY);
        if (piece != null && piece.isWhite() == whiteToMove) {
            if (piece.getValidMoves().contains(new javafx.util.Pair<>(endX, endY))) {
                board.movePiece(startX, startY, endX, endY);
                whiteToMove = !whiteToMove;
                return true;
            }
        }
        return false;
    }
}
