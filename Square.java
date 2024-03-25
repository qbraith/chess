import javax.swing.JButton;
//chess
public class Square extends JButton{
    private int row;
    private int col;
    private Piece piece;

    public Square(String thang){
        super(thang);
        this.piece = null;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }
    
    public void setRow(int row){
        this.row = row;
    }
    
    public void setCol(int col){
        this.col = col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!o.getClass().equals(Square.class)) return false;
        Square q = (Square) o;
        return row==q.getRow() && col==q.getCol() && piece.equals(q.getPiece());
    }

    public String toString(){
        String p;
        if (getPiece() == null) p = "blank";
        else p = getPiece().getColor() + " " + getPiece().getName();
        return p + ", " + (8-getRow()) + ":" + getCol();
    }
    
}
