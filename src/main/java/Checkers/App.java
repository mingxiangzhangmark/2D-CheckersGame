// General Description
// Package: Checkers
// Purpose: The main game application class that extends PApplet from Processing.
// --> It sets up the game's graphical user interface, initializes the game state, and handles user input and game updates.
// Features:
// Static fields for defining game constants like cell size and board dimensions.
// An array for the game board (Cell[][] board) and collections for managing the pieces in play.
// Methods settings(), setup(), and draw() for initializing the window, setting up the game state, 
// --> and drawing the game frame by frame, respectively.
// mousePressed(MouseEvent e) to handle piece selection and moves based on user clicks.
// Utility methods like setFill(int colourCode, int blackOrWhite) for graphical operations.
// Each class is tailored to handle specific aspects of the game: 
// CheckersPiece and Cell manage the game's logical state, while App integrates these components
// -->  with Processing to manage the game's graphical interface and user interactions. Together, they form 
// --> a cohesive structure allowing for the development of a checkers game.

package Checkers;

// Utilizes HashMap for storing collections of objects, where each item has a key and value. 
// Used here for managing pieces in play with their respective colors as keys.
import java.util.HashMap;
// Utilizes HashSet for storing unique elements, ensuring no duplicates. 
// Used for managing cells and pieces where uniqueness is essential, such as tracking selected cells or pieces in play.
import java.util.HashSet;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;

// Importing core PApplet class from the Processing library, which is the basis for creating drawing windows and handling events.
// Processing library imports for graphical interface and interactions
import processing.core.PApplet;
// Import for handling mouse events, enabling interactive components like clicking on pieces or cells.
import processing.event.MouseEvent;

public class App extends PApplet {

    // Constants for game configuration, defining the visual and structural aspects of the checkers board.
    public static final int CELLSIZE = 48; // Size of each cell on the board, affecting the overall scale of the game's visual representation.
    public static final int SIDEBAR = 0; // Width of an unused sidebar, potentially reserved for future features like game stats or controls.
    public static final int BOARD_WIDTH = 8; // The width and height of the checkers board, defining an 8x8 grid.
    public static final int[] BLACK_RGB = {181, 136, 99}; // RGB values for the color of black cells, contributing to the board's visual design.
    public static final int[] WHITE_RGB = {240, 217, 181}; // RGB values for the color of white cells, complementing the board's aesthetics.

    // Color schemes for the board and pieces, allowing for visual customization beyond the traditional black and white.
    // Array to hold custom color schemes for the board (white & black, green, blue)
    public static final float[][][] coloursRGB = new float[][][] {
            // Default scheme with white and black cells.
            {
                    {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                    {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
            },
            // Green scheme, potentially for highlighting or alternative themes.
            {
                    {105, 138, 76}, // Green for white cells
                    {105, 138, 76}  // Green for black cells
            },
            // Blue scheme, offering another alternative visual theme.
            {
                    {196,224,232}, // Light blue
                    {170,210,221}  // Slightly darker blue
            }
    };


    // Static variables to determine the window size
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;


    public static final int FPS = 60 ; // Frames per second for the animation

    /* --------------------------------------- */
    // DATA STORAGE
    /* --------------------------------------- */

    // Storing the game's state, including the layout of the board, current and selected pieces, and which pieces are in play.

    private Cell[][] board; // Represents the game board as a grid of cells.
    private CheckersPiece currentSelected; // The currently selected piece, if any.
    private HashSet<Cell> selectedCells; // Tracks cells highlighted for potential moves.
    private HashMap<Character, HashSet<CheckersPiece>> piecesInPlay = new HashMap<>(); // Active pieces, differentiated by color ('w' for white, 'b' for black).
    private char currentPlayer = 'w'; // Tracks the current turn, alternating between 'w' (white) and 'b' (black).

    ////
    private boolean isAnimating = false; // if playing animating
    private CheckersPiece movingPiece = null; // ismoving piece
    private float startX, startY; // start position
    private float endX, endY; // end position
    private float currentX, currentY; // current position

    private boolean isMoving = false; // no moving at the beginning
    public App() {

    }

    // Setup method to initialize window settings
    @Override
    public void settings() {
        size(WIDTH, HEIGHT); // Set the size of the application window based on the board dimensions.
    }

    // Initial setup for the game, executed once at the beginning
    @Override
    public void setup() {
        frameRate(FPS); // Set the frame rate

        //Set up the data structures used for storing data in the game
        this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
        HashSet<CheckersPiece> w = new HashSet<>();
        HashSet<CheckersPiece> b = new HashSet<>();
        piecesInPlay.put('w', w);
        piecesInPlay.put('b', b);

        // Populate the board with pieces in initial positions
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2,i);
                // Place white and black pieces on the board in their initial positions
                if ((i2+i) % 2 == 1) {
                    if (i < 3) {
                        // Initialize white pieces in the first three rows
                        board[i][i2].setPiece(new CheckersPiece('w'));
                        w.add(board[i][i2].getPiece());
                    } else if (i >= 5) {
                        // Initialize black pieces in the last three rows
                        board[i][i2].setPiece(new CheckersPiece('b'));
                        b.add(board[i][i2].getPiece());
                    }
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(){

    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Check if the user clicked on a piece which is theirs - make sure only whoever's current turn it is, can click on pieces
        int x = e.getX();
        int y = e.getY();
        if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT) return;

        Cell clicked = board[y/App.CELLSIZE][x/App.CELLSIZE];
        if (clicked.getPiece() != null && clicked.getPiece().getColour() == currentPlayer) {

            // If other pieces were previously selected, clear the possible move positions of the previous pieces

            if (currentSelected != null) {
                selectedCells.clear();
            }

            // Deselect if the piece clicked is already the currently selected piece
            if (clicked.getPiece() == currentSelected) {
                currentSelected = null;
            } else {
                // Otherwise, select the piece to click and find out all the possible move positions
                currentSelected = clicked.getPiece();
                // Gets and highlights all possible move locations
                selectedCells = currentSelected.getAvailableMoves(board);
            }
            redraw();
        }

        int mouseX = e.getX();
        int mouseY = e.getY();
        int cellX = mouseX / CELLSIZE;
        int cellY = mouseY / CELLSIZE;

        // check if the click is within the board bounds
        if (cellX < 0 || cellX >= BOARD_WIDTH || cellY < 0 || cellY >= BOARD_WIDTH) {
            return;
        }
//
        // get the clicked cell
        Cell clickedCell = board[cellY][cellX];

        // check if the clicked cell is one of the available move positions
        if (currentSelected != null && selectedCells.contains(clickedCell)) {
            // check if the move is a jump move
            boolean isJumpMove = Math.abs(currentSelected.getPosition().getX() - clickedCell.getX()) == 2
                    || Math.abs(currentSelected.getPosition().getY() - clickedCell.getY()) == 2;

            if (isJumpMove) {
                // get the mid cell between the current position and the clicked position
                int midX = (currentSelected.getPosition().getX() + clickedCell.getX()) / 2;
                int midY = (currentSelected.getPosition().getY() + clickedCell.getY()) / 2;
                Cell capturedCell = board[midY][midX];
                // check if there's a piece to capture
                if (capturedCell.getPiece() != null && capturedCell.getPiece().getColour() != currentPlayer) {
                    piecesInPlay.get(capturedCell.getPiece().getColour()).remove(capturedCell.getPiece()); // 从活动棋子集合中移除
                    capturedCell.setPiece(null); // 移除棋子
                }
            }
            // Do any animations or special effects for the move
             animateMove(currentSelected, clickedCell);

            // move the piece to the new position
            movePiece(currentSelected, clickedCell);

            // delete the old position of the piece
            currentSelected = null;
            selectedCells.clear();

            // change player
            switchTurn();

            redraw();
        }

    }

    // piece moving animation
    private void animateMove(CheckersPiece piece, Cell destination) {
        isAnimating = true;
        movingPiece = piece;
        startX = piece.getPosition().getX() * CELLSIZE + CELLSIZE / 2f;
        startY = piece.getPosition().getY() * CELLSIZE + CELLSIZE / 2f;
        endX = destination.getX() * CELLSIZE + CELLSIZE / 2f;
        endY = destination.getY() * CELLSIZE + CELLSIZE / 2f;
        currentX = startX;
        currentY = startY;
    }

    private void movePiece(CheckersPiece piece, Cell destination) {
        // remove the piece from the old cell
        piece.getPosition().setPiece(null);
        // set the piece to the new cell
        destination.setPiece(piece);
        // update the piece's position
        piece.setPosition(destination);


        // After moving the piece, check if it has reached the opposite end to be promoted to a king
//        if (!piece.isKing() && ((piece.getColour() == 'w' && destination.getY() == BOARD_WIDTH - 1) ||
//                (piece.getColour() == 'b' && destination.getY() == 0))) {
//            piece.makeKing();
//            // Additional code can be added here to change the piece's appearance, like adding a crown symbol
//        }
        if (!piece.isWhiteKing() && (piece.getColour() == 'w' && destination.getY() == BOARD_WIDTH - 1) ) {
            piece.makeWhiteKing();

        } else if (!piece.isBlackKing() && (piece.getColour() == 'b' && destination.getY() == 0)) {

            piece.makeBlackKing();
        }
    }
    private void switchTurn() {

        currentPlayer = (currentPlayer == 'w') ? 'b' : 'w';

        currentSelected = null;

        selectedCells.clear();

        redraw();
    }



    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame. 
     */
    public void draw() {
        this.noStroke(); // Disable drawing outlines to prepare for drawing filled shapes.
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]); // Set the background color of the board.

        // Draw the board and the pieces.
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                ///
                if (isAnimating && board[i][i2].getPiece() == movingPiece) {
                    continue;
                }
                ///
                if (currentSelected != null && board[i][i2].getPiece() == currentSelected) {
                    // Highlight the selected cell if it contains the current selected piece.
                    this.setFill(1, (i2+i) % 2);
                    this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                } else if ((i2+i) % 2 == 1) {
                    // Regular drawing for unselected cells.
                    this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
                    this.rect(i2*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }
                board[i][i2].draw(this); // Draw the piece in the cell, if any.
            }
        }

        if (currentSelected != null) {
            // Highlight the selected piece's available moves.
            for (Cell cell : selectedCells) {
                highlightCell(cell.getX(), cell.getY(), coloursRGB[2]); // Highlight the cell in blue.
            }
        }
        //

        // Draw the moving piece if there is one
        if (isAnimating) {
            // 更新当前位置
            currentX = lerp(currentX, endX, 0.5f);
            currentY = lerp(currentY, endY, 0.5f);

            // draw the piece at the current position
            if (movingPiece.getColour() == 'w') {
                fill(255); //white
            } else {
                fill(0); // black
            }

            ellipse(currentX, currentY, CELLSIZE * 0.8f, CELLSIZE * 0.8f);

            // chack if it is completed
            if (abs(currentX - endX) < 1 && abs(currentY - endY) < 1) {
                isAnimating = false; // OK
            }
        }


        // Check for end game condition where one player has no more pieces.
        if (piecesInPlay.get('w').size() == 0 || piecesInPlay.get('b').size() == 0) {
            // Display the winner.
            fill(255);
            stroke(0);
            strokeWeight(4.0f);
            rect(App.WIDTH*0.2f-5, App.HEIGHT*0.4f-25, 150, 40); // Draw a rectangle for the text background.
            fill(200,0,200);
            textSize(24.0f); // Set text size
            if (piecesInPlay.get('w').size() == 0) {
                text("Black wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            } else if (piecesInPlay.get('b').size() == 0) {
                text("White wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            }
        }


        ////king

        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                // Draw the piece in the cell, if any, with special handling for kings
                if (board[i][i2].getPiece() != null) {
                    CheckersPiece piece = board[i][i2].getPiece();

                    // Call a custom drawing method if the piece is a king
                    if (piece.isWhiteKing() || piece.isBlackKing()) {
                        piece.drawKing(this); // This method needs to be implemented in the CheckersPiece class
                    } else {
                        // If it's not a king, draw it normally
                        piece.draw(this);
                    }
                }
            }
        }

    }
    private void highlightCell(int x, int y, float[][] colour) {
        fill(colour[1][0], colour[1][1], colour[1][2]); // set it blue
        rect(x * CELLSIZE, y * CELLSIZE, CELLSIZE, CELLSIZE); // draw the rectangle
    }

    /**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
    public void setFill(int colourCode, int blackOrWhite) {
        // Set the fill color for drawing cells, allowing for different themes or highlights.
        this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
    }



    public static void main(String[] args) {
        PApplet.main("Checkers.App"); // Launch the Processing application.
    }


}