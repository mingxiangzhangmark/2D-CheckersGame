// General Description
// Package: Checkers
// Purpose: Represents a single cell on the checkers board. It keeps track of its coordinates (x,y) and whether it currently holds a piece.
// Methods:
// Constructor to initialize the cell's coordinates.
// getX(), getY(), setPiece(CheckersPiece p), and getPiece() for managing the cell's coordinates and the piece it contains.
// draw(App app) to draw the piece within this cell using the piece's draw method if it contains a piece.

package Checkers;

public class Cell {

    // The x-coordinate of the cell on the board
    private int x;

    // The y-coordinate of the cell on the board
    private int y;

    // The piece that is currently occupying this cell (null if the cell is empty)
    private CheckersPiece piece;

    // Constructor: Initializes a cell with specified x and y coordinates
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Returns the x-coordinate of the cell
    public int getX() {
        return this.x;
    }

    // Returns the y-coordinate of the cell
    public int getY() {
        return this.y;
    }


    // Assigns a piece to this cell and updates the piece's position to this cell
    // If a piece is already present, it is replaced by the new piece
    public void setPiece(CheckersPiece p) {
        this.piece = p;

        // If the piece is not null, update the piece's position to this cell
        if (p != null) {
            p.setPosition(this);
        }
    }

    // Returns the piece that is currently occupying the cell, or null if the cell is empty
    public CheckersPiece getPiece() {
        return this.piece;
    }

    // Draws the piece on the board using the Processing library, if there is a piece in this cell
    // This method delegates the drawing to the piece's own draw method, providing it with the necessary App instance
    public void draw(App app) {
        if (this.piece != null)
            this.piece.draw(app);
    }
}