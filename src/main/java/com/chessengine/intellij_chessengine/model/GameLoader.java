package com.chessengine.intellij_chessengine.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Stack;

public class GameLoader {
    public static Stack<Move> loadGameHistory(String fileName) {
        // Construct the path to the saved game file in the 'saved_games' directory
        File file = new File("saved_games", fileName);

        // Check if the file exists before attempting to load
        if (!file.exists()) {
            System.out.println("Error: File not found: " + file.getPath());
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            // Read the game history from the file
            Stack<Move> gameHistory = (Stack<Move>) in.readObject();
            System.out.println("Game history loaded successfully from " + file.getPath());
            return gameHistory;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
