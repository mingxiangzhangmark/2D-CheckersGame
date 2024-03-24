// General Description
// Package: Checkers
// Purpose: Represents a single piece in the checkers game, holding information about the piece's color ('w' for white, 'b' for black) and its position on the board.
// Methods:
// Constructors to set the piece's color.
// getColour() and setPosition(Cell p) for accessing and updating the piece's color and position.
// getPosition() for retrieving the current position.
// getAvailableMoves(Cell[][] board) (not implemented) intended to calculate possible moves.
// capture() and promote() methods (not fully implemented) for handling captures and promotions of pieces.
// draw(App app) for drawing the piece on the board using Processing methods.

package Checkers;

import java.util.HashSet;

public class CheckersPiece {

    // The color of the checkers piece ('w' for white, 'b' for black)
    private char colour;

    // The current position of the piece on the board
    private Cell position;

    // Constructor: Initializes a new piece with a given color
    public CheckersPiece(char c) {
        this.colour = c;
    }

    // Returns the color of the piece
    public char getColour() {
        return this.colour;
    }

    // Sets the position of the piece to a given cell
    public void setPosition(Cell p) {
        this.position = p;
    }

    // Returns the current position of the piece
    public Cell getPosition() {
        return this.position;
    }

    // In the CheckersPiece class, add a boolean attribute to determine if it is a king
//    private boolean isKing = false;

    private boolean isBlackKing = false;
    private boolean isWhiteKing = false;

    // Method to set the piece as a king
//    public void makeKing() {
//        this.isKing = true;
//        // Additional logic to change the piece's appearance to indicate it's a king can go here

    public void makeBlackKing() {
        this.isBlackKing = true;
    }
    public void makeWhiteKing() {
        this.isWhiteKing = true;
    }
    // Getter method to check if the piece is a king
    public boolean isBlackKing() {
        return isBlackKing;
    }
    public boolean isWhiteKing() {
        return isWhiteKing;
    }


    public static final int BOARD_WIDTH = 8; // The width and height of the checkers board, defining an 8x8 grid.
    public HashSet<Cell> getAvailableMoves(Cell[][] board) {
        // Implement the logic to calculate the available moves for the piece
        int direction = this.colour == 'w' ? 1 : -1;

        HashSet<Cell> availableMoves = new HashSet<>();

        int startX = this.position.getX();
        int startY = this.position.getY();

        int[] dx = {-1, 1};
        for (int d : dx) {
            int newX = startX + d;
            int newY = startY + direction;


            if (newX >= 0 && newX < BOARD_WIDTH && newY >= 0 && newY < BOARD_WIDTH) {
                Cell potentialCell = board[newY][newX];

                if (potentialCell.getPiece() == null) {
                    availableMoves.add(potentialCell);
                } else if (potentialCell.getPiece().getColour() != this.colour) {
                    int jumpX = newX + d;
                    int jumpY = newY + direction;

                    // if is in the board and the landing cell is empty
                    if (jumpX >= 0 && jumpX < BOARD_WIDTH && jumpY >= 0 && jumpY < BOARD_WIDTH && board[jumpY][jumpX].getPiece() == null) {
                        availableMoves.add(board[jumpY][jumpX]);
                    }
                }else if (potentialCell.getPiece().getColour() == this.colour) {
                    // If there is an opponent's piece in the new position, check if you can skip the opponent's piece
                    int jumpX = newX + d;
                    int jumpY = newY + direction;

                    // if it is null
                    if (jumpX >= 0 && jumpX < BOARD_WIDTH && jumpY >= 0 && jumpY < BOARD_WIDTH && board[jumpY][jumpX].getPiece() == null) {
                        availableMoves.add(board[jumpY][jumpX]);
                    }
                }

            }
        }


        ///// Check if the piece is a king to add backward moves
            if (this.isWhiteKing || this.isBlackKing){
                // Add logic to include moves in the opposite direction, considering the piece's color
                int backwardDirection = (this.colour == 'w') ? -1 : 1; // Reverse direction for white and black

                // Check backward moves
               // int[] dx = {-1, 1}; // Check diagonals
                for (int d : dx) {
                    int newX = this.position.getX() + d;
                    int newY = this.position.getY() + backwardDirection;

                    // Check if the new position is within bounds
                    if (newX >= 0 && newX < BOARD_WIDTH && newY >= 0 && newY < BOARD_WIDTH) {
                        Cell potentialCell = board[newY][newX];

                        // If the cell is empty, add it to available moves
                        if (potentialCell.getPiece() == null) {
                            availableMoves.add(potentialCell);
                        } else if (potentialCell.getPiece().getColour() != this.colour) {
                            // Check for jumps over the opponent's pieces
                            int jumpX = newX + d;
                            int jumpY = newY + backwardDirection;

                            // If the jump is within bounds and the landing cell is empty
                            if (jumpX >= 0 && jumpX < BOARD_WIDTH && jumpY >= 0 && jumpY < BOARD_WIDTH &&
                                board[jumpY][jumpX].getPiece() == null) {
                                availableMoves.add(board[jumpY][jumpX]);
                            }
                        }
                    }
                }
            }
        return availableMoves; // Returns all possible moving positions
    }
        //
        //return null;
//    }
    public void capture() {
        //capture this piece
    }

    public void promote() {
        //promote this piece
    }

    // Draws the piece on the board using the Processing library.
    // This method takes an instance of the App class, which extends PApplet from Processing, to access drawing methods.
    public void draw(App app)
    {
        // Set the stroke weight for the outline of the piece
        app.strokeWeight(5.0f);

        if (colour == 'w') {
            // White piece
            app.fill(255); // white fill
            app.stroke(0); // black stroke
        } else if (colour == 'b') {
            // Black piece
            app.fill(0); // black fill
            app.stroke(255);// white stroke
        }

        // Draw the piece as an ellipse (circle) at the piece's position, adjusting the coordinates based on the cell size
        // The method elipse takes 4 parameters
        // Syntax:  ellipse(a, b, c, d)
        // Parameters
        // a	(float)	x-coordinate of the ellipse
        // b	(float)	y-coordinate of the ellipse
        // c	(float)	width of the ellipse by default
        // d	(float)	height of the ellipse by default
        app.ellipse(position.getX()*App.CELLSIZE + App.CELLSIZE/2, position.getY()*App.CELLSIZE + App.CELLSIZE/2, App.CELLSIZE*0.8f, App.CELLSIZE*0.8f);
        // Disable the stroke for subsequent drawings
        app.noStroke();
    }


    // Inside the CheckersPiece class
    public void drawKing(App app) {
        // Draw the base piece as before
        draw(app); // Call the existing draw method to draw the base piece

        // Add the king's design
        float x = position.getX() * App.CELLSIZE + App.CELLSIZE / 2f;
        float y = position.getY() * App.CELLSIZE + App.CELLSIZE / 2f;


        // Customize this section to draw your king's design
        if (isWhiteKing()) {
            // Draw the white king's specific design (an example for a circle with a stroke)
            app.fill(255); // White color
            app.stroke(0); // Black stroke
            app.strokeWeight(5); // Stroke weight
            app.ellipse(x, y, App.CELLSIZE * 0.3f, App.CELLSIZE * 0.3f); // Smaller ellipse for crown
        } else if (isBlackKing()) {
            // Draw the black king's specific design (an example for a circle with a stroke)
            app.fill(0); // Black color
            app.stroke(255); // White stroke
            app.strokeWeight(5); // Stroke weight
            app.ellipse(x, y, App.CELLSIZE * 0.3f, App.CELLSIZE * 0.3f); // Smaller ellipse for crown
        }
        // After drawing the base piece and the king's crown, draw the ring around the king
        app.noFill(); // No fill for the ring
        app.strokeWeight(1); // Stroke weight for the ring
        if (isWhiteKing()) {
            app.stroke(0); // Black ring for the white king
        } else if (isBlackKing()) {
            app.stroke(255); // White ring for the black king
        }
        app.ellipse(x, y, App.CELLSIZE * 0.7f, App.CELLSIZE * 0.7f); // Outer ring
    }


}