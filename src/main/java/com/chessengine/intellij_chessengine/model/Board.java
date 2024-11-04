package com.chessengine.intellij_chessengine.model;
import com.chessengine.intellij_chessengine.model.pieces.*;
import javafx.util.Pair;

public class Board {
    private final Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
//        String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        String startFen = "2bqkbnr/P1ppp2p/5p2/6p1/4P3/8/1PPP1R2/RNBQKB2";
        initializeBoardFromFEN(startFen);
    }

    private void initializeBoardFromFEN(String fen) {
        String[] sections = fen.split(" ");
        String piecePlacement = sections[0];
        String[] rows = piecePlacement.split("/");

        for (int r = 0; r < 8; r++) {
            int col = 0;
            for (char c : rows[r].toCharArray()) {
                if (Character.isDigit(c)) {
                    col += c - '0'; // Move forward for empty squares
                } else {
                    boolean isWhite = Character.isUpperCase(c);
                    switch (Character.toLowerCase(c)) {
                        case 'p' -> board[r][col] = new Pawn(isWhite);
                        case 'r' -> board[r][col] = new Rook(isWhite);
                        case 'n' -> board[r][col] = new Knight(isWhite);
                        case 'b' -> board[r][col] = new Bishop(isWhite);
                        case 'q' -> board[r][col] = new Queen(isWhite);
                        case 'k' -> board[r][col] = new King(isWhite);
                    }
                    col++;
                }
            }
        }
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isSquareEmpty(int x, int y) {
        return board[x][y] == null;
    }

    public boolean isOpponentPiece(int x, int y, boolean isWhite) {
        Piece piece = board[x][y];
        return piece != null && piece.isWhite() != isWhite;
    }

    public Piece getPieceAt(int x, int y) {
        return board[x][y];
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = getPieceAt(startX, startY);
        if (piece != null) {
            // Execute the move
            setPieceAt(endX, endY, piece);
            setPieceAt(startX, startY, null);
        }
    }

    public void setPieceAt(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public boolean isKingInCheck(boolean isWhite) {
        int kingX = -1, kingY = -1;

        // Find the position of the King
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingX = row;
                    kingY = col;
                    break;
                }
            }
            if (kingX != -1) break;
        }

        // Check if any opposing pieces can attack the King
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() != isWhite) {
                    piece.calculateValidMoves(row, col, this, true);
                    if (piece.getValidMoves().contains(new Pair<>(kingX, kingY))) {
                        return true;  // King is in check
                    }
                }
            }
        }
        return false;  // King is safe
    }

    public void displayBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    char pieceChar = getPieceChar(piece);
                    System.out.print(pieceChar + " ");
                }
            }
            System.out.println();
        }
    }

    public char getPieceChar(Piece piece) {
        char pieceChar;
        switch (piece.getType()) {
            case PAWN -> pieceChar = 'P';
            case ROOK -> pieceChar = 'R';
            case KNIGHT -> pieceChar = 'N';
            case BISHOP -> pieceChar = 'B';
            case QUEEN -> pieceChar = 'Q';
            case KING -> pieceChar = 'K';
            default -> throw new IllegalStateException("Unexpected value: " + piece.getType());
        }
        return piece.isWhite() ? Character.toUpperCase(pieceChar) : Character.toLowerCase(pieceChar);
    }
}
