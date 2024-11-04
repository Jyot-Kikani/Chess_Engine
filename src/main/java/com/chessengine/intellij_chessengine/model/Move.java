package com.chessengine.intellij_chessengine.model;

import java.io.Serializable;

public class Move implements Serializable {
    private String moveNotation;
    private String fenString;

    public Move(String moveNotation, String fenString) {
        this.moveNotation = moveNotation;
        this.fenString = fenString;
    }

    public String getMoveNotation() {
        return moveNotation;
    }

    public String getFenString() {
        return fenString;
    }
}
