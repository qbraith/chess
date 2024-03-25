import javax.swing.ImageIcon;

public class Piece {
    private String name;
    private ImageIcon image;
    private String color;
    private boolean pinned;
    private int direction;
    private int moves = 0;

    public Piece(String name, String color, ImageIcon image, int direction){
        this.name = name;
        this.color = color;
        this.image = image;
        this.pinned = false;
        this.moves = 0;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!o.getClass().equals(Piece.class)) return false;
        Piece p = (Piece) o;
        return name.equals(p.name) && color.equals(p.color) && moves==p.moves;
        //name color moves
    }
    

    

    
}
