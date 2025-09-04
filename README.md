# Chess Engine

A Java-based Chess game built with JavaFX and structured using the Model-View-Controller (MVC) pattern. This project recreates the classic chess experience with full rule implementation, intuitive UI, and support for saving/loading games.

---

## Features
- Full chess rules (turn-taking, check, checkmate, stalemate)
- Piece movement & validation for all standard chess pieces
- Special moves: Castling, En Passant, Pawn Promotion
- Interactive JavaFX-based GUI with move highlighting
- Move history with undo/redo support
- Save and load games for later continuation
- Sound effects for moves and selections

---

## Directory Structure
```
Chess_Engine/
│── src/
│   ├── main/
│   │   └── ChessApplication.java   # Entry point
│   ├── model/                      # Core game logic
│   │   ├── ChessModel.java
│   │   ├── Board.java
│   │   ├── GameLoader.java
│   │   ├── GameSaver.java
│   │   ├── Move.java
│   │   ├── Piece.java, PieceType.java
│   │   └── pieces/ (Pawn, Rook, Bishop, Knight, Queen, King)
│   ├── view/                       # JavaFX UI
│   │   ├── ChessView.java
│   │   └── MoveRecord.java
│   └── controller/                 # Controller logic
│       └── ChessController.java
│── resources/                      # Images, sounds
│── README.md
```

---

## Getting Started

### Prerequisites
- Java 11+
- JavaFX SDK

### Running the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/Jyot-Kikani/Chess_Engine.git
   cd Chess_Engine
   ```
2. Compile and run:
   ```bash
   javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -d out src/**/*.java
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp out com.chessengine.intellij_chessengine.ChessApplication
   ```
   *(Replace `/path/to/javafx/lib` with your JavaFX SDK path.)*

---

## Screenshots
- Start of the game
![Start of the Game](Images/Start%20of%20the%20Game.jpeg)
- Making a Move
![Making moves with highlights](Images/Making%20a%20Move.jpeg)
- Highlight Valid Moves
![Highlightinh valid moves](Images/Available%20Moves.jpeg)
- Undo/Redo functionality
![Undo Redo Functionality](Images/Undo%20moves.jpeg)
- Save & Load dialogs
![Save Game](Images/Save%20Game.jpeg)
![Load Game](Images/Load%20Game.jpeg)
- Game End
![Game End](Images/Game%20End.jpeg)

---

## Testing
- Unit tests for piece movement and rule validation
- Manual testing for check/checkmate, stalemate, and special moves
- Stress-tested for stability with complex endgames

---

## Future Improvements
- AI Opponent (Minimax with Alpha-Beta pruning)
- Multiplayer support (LAN/Online)
- Game analysis (move evaluation, hints, mistakes)
- Enhanced animations and improved graphics

---

## Authors
- Jyot Kikani (23BCE126)
- Jemil Patel (23BCE117)
