import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Board extends JFrame implements ActionListener{
    ArrayList<Square> list = new ArrayList<>();
    boolean clicked = false, castled = false, wCheck = false, bCheck = false;
    Square current = null, wKing, bKing;
    final int size = 8;
    int turn = 1;
    JPanel panel = new JPanel();

    public Board(){
        panel.setLocation(70, -1);
        panel.setSize(800, 700);
        JLabel l1 = new JLabel("<html> 8 7 6 5 4 3 2 1</html>");
        l1.setFont(l1.getFont().deriveFont(Font.PLAIN, 70));
        l1.setForeground(Color.black);
        l1.setBounds(10, 0, 70, 700);

        JLabel l2 = new JLabel("A   B   C   D   E   F   G   H");
        l2.setFont(l2.getFont().deriveFont(Font.PLAIN, 67));
        l2.setForeground(Color.BLACK);
        l2.setBounds(95, 695, 800, 70);
        l2.setBackground(Color.red);
        this.add(l1);
        this.add(l2);
        this.setSize(885, 910);

        
        list = makeButtons(size);
        setupPieces(7, 0, 1, "white");
        setupPieces(0, 7, -1, "black");
        labelPieces();
        
        this.setLayout(null);
        this.setBackground(Color.white);
        panel.setLayout(new GridLayout(8, 8, 2, 2));
        this.add(panel);
        panel.setVisible(true);
        this.setTitle("CHESS");
        this.setLocation(350, 30);
        this.setIconImage(new ImageIcon("images/bknight.PNG").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        // System.out.println("constructor over");
    }
    
    
    public void actionPerformed(ActionEvent e){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square s = list.get(i*8+j);
                if (e.getSource() == s){
                    // String color = (turn%2==1) ? "white" : "black";
                    // Square king = (turn%2==1) ? wKing : bKing;
                    // long t = System.nanoTime();
                    // boolean check = inCheck(color, king);
                    // System.out.println("Time: " + (System.nanoTime()-t));
                    // if (check) System.out.println("CHECK!!!");
                    //! instead of doing pulsing from the king, do it based on the last piece moved 
                    //! check where the piece went and where the piece left (discovery check)
                    //determine here if in check or not
                    //create two instance variables keeping track of each king

                    //take two

                    if (!clicked && s.getPiece() != null && !s.getPiece().isPinned() && matchTurn(s.getPiece().getColor())){
                        // System.out.println("clicked");
                        current = s;
                        clicked = true;
                        System.out.println("Piece clicked: " + numberToLetter(s.getCol()) + (8-s.getRow()));
                        //change image to highlighted
                        s.setBackground(new Color(245, 247, 129));
                    } else if (clicked) {
                        if (s == current) {
                            clicked = false;
                            current = null;
                            if (((i/size) % 2 ==  0 && (i%size) % 2 == 0) || ((i/size) % 2 ==  1 && (i%size) % 2 == 1)){
                                s.setForeground(Color.black);
                                s.setBackground(Color.black);
                            } else {
                                s.setForeground(new Color( 105, 49, 19)); //brown
                                s.setBackground(new Color (105, 49, 19));
                            }
                            //review image to non highlighted
                            return;
                        }
    
                        boolean valid = validMove(current, s);
                        if (valid){
                            //! implement here code for check
                            if (!simulateMove(current, s)) return;


                            ++turn;
                            if (castled) {
                                int increment;
                                Square newKing, newRook;
                                // System.out.println("Castle");
                                String side = (Math.abs(s.getCol()-current.getCol()) > 3) ? "Queen" : "King"; 
                                System.out.println(side + " side castle");
                                if (side.equals("King")) {
                                    increment = (Math.abs(s.getCol()-current.getCol()))/(s.getCol()-current.getCol());
                                    newRook = list.get(s.getRow()*8+s.getCol()-increment);
                                    newKing = list.get(current.getRow()*8+current.getCol()+increment);
                                } else {
                                    increment = (Math.abs(s.getCol()-current.getCol()))/(s.getCol()-current.getCol());
                                    newRook = list.get(s.getRow()*8+s.getCol()-3*increment);
                                    newKing = list.get(current.getRow()*8+current.getCol()+2*increment);
                                }
                                newRook.setPiece(s.getPiece());
                                newRook.setIcon(s.getIcon());
                                newKing.setPiece(current.getPiece());
                                newKing.setIcon(current.getIcon());
                                s.setIcon(null);
                                s.setPiece(null);
                                current.setIcon(null);
                                current.setPiece(null);
                                newKing.setVerticalTextPosition(JButton.CENTER);
                                newKing.setHorizontalTextPosition(JButton.CENTER);
                                newRook.setVerticalTextPosition(JButton.CENTER);
                                newRook.setHorizontalTextPosition(JButton.CENTER);
                                castled = false;

                                if (newKing.getPiece().getColor().equals("white")) wKing = newKing;
                                else bKing = newKing;
                            } else {
                                System.out.println("Valid move: " + current.getPiece().getName() + " to " + numberToLetter(s.getCol()) + (8-s.getRow()));
                                s.setIcon(current.getIcon());
                                s.setPiece(current.getPiece());
                                current.setPiece(null);
                                current.setIcon(null);
                                s.setVerticalTextPosition(JButton.CENTER);
                                s.setHorizontalTextPosition(JButton.CENTER);
                            }

                            if ((current.getRow()%2==0 && current.getCol()%2==0) || (current.getCol()%2==1 && current.getRow()%2==1)) {
                                current.setForeground(Color.black);
                                current.setBackground(Color.black);
                            } else { 
                                current.setForeground(new Color( 105, 49, 19)); //brown
                                current.setBackground(new Color (105, 49, 19));
                            }

                            //take two 
                            //TODO functionality for check
                            // Square king = (turn%2==1) ? wKing : bKing;
                            // boolean checked = inCheck(king, s, false) || inCheck(king, current, true);
                            // if (checked) {
                            //     System.out.println("CHECKKK");
                            //     if (turn%2==1) wCheck = true;
                            //     else bCheck = true;
                            //     //! in valid move check if still in check and if so return false
                            // }
                            clicked = false;
                            current = null;
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Square> makeButtons(int size){
        ArrayList<Square> list = new ArrayList<>();
        for (int i = 0; i < size * size; i++){
            Square button = new Square(String.valueOf(i/size) + String.valueOf(i%size));
            button.setText(" ");
            button.addActionListener(this);
            if (((i/size) % 2 ==  0 && (i%size) % 2 == 0) || ((i/size) % 2 ==  1 && (i%size) % 2 == 1)){
                button.setForeground(Color.black);
                button.setBackground(Color.black);
            } else {
                button.setForeground(new Color( 105, 49, 19)); //brown
                button.setBackground(new Color (105, 49, 19));
            }
            //button.setContentAreaFilled(false);
            button.setFocusable(false);
            //button.setOpaque(true);
            // this.add(button);
            panel.add(button);
            list.add(button);
            // button.addActionListener(this);
            button.setRow(i/size);
            button.setCol(i%size);
        }
        return list;
    }
  
    public void setupPieces(int x, int y, int increment, String color){
        //white first, get +1, on squares 48-63, x = 7 y = 0
        //black second, inc -1, squares 15-0, x = 0, y = 7
        list.get(x*8).setPiece(new Piece("Rook", color, null, -1*increment));
        list.get(x*8+7).setPiece(new Piece("Rook", color, null, -1*increment));
        list.get(x*8+1).setPiece(new Piece("Knight", color, null, -1*increment));
        list.get(x*8+6).setPiece(new Piece("Knight", color, null, -1*increment));
        list.get(x*8+2).setPiece(new Piece("Bishop", color, null, -1*increment));
        list.get(x*8+5).setPiece(new Piece("Bishop", color, null, -1*increment));

        if (color.equals("white")){
            list.get(x*8+3).setPiece(new Piece("Queen", color, null, -1*increment));
            list.get(x*8+4).setPiece(new Piece("King", color, null, -1*increment));
            wKing = list.get(x*8+4);
        } else {
            list.get(x*8+3).setPiece(new Piece("Queen", color, null, -1*increment));
            list.get(x*8+4).setPiece(new Piece("King", color, null, -1*increment));
            bKing = list.get(x*8+4);
        }

        int row = x-increment; //either 1 or 6
        for (int i = 0; i < 8; i++){
            list.get(row*8 + i).setPiece(new Piece("Pawn", color, null, -1*increment));
        }  
    }

    public void labelPieces(){
        for (Square b : list){
            if (b.getPiece() == null) continue;
            b.setForeground(Color.BLACK);
            b.setVerticalTextPosition(JButton.CENTER);
            b.setHorizontalTextPosition(JButton.CENTER);
            Piece p = b.getPiece();
            // b.setText(b.getPiece().getColor().substring(0, 1) + b.getPiece().getName());
            // System.out.println(p.getColor().substring(0,1).toLowerCase() + p.getName().toLowerCase());
            ImageIcon i = new ImageIcon("images/" + p.getColor().substring(0,1).toLowerCase() + p.getName().toLowerCase() + ".PNG");
            ImageIcon i3 = new ImageIcon(i.getImage().getScaledInstance(75, 65, Image.SCALE_SMOOTH));
            b.setIcon(i3);
            // TODO make images grey and white when transitioning to transparent
        }
    }

    public boolean validMove(Square from, Square to){
        System.out.println("trying to go to " + numberToLetter(to.getCol()) + (8-to.getRow()));
        String name = from.getPiece().getName();
        int[] toPair = {to.getRow(), to.getCol()};
        int[] fromPair = {from.getRow(), from.getCol()};
        String direction = moveType(fromPair, toPair);
        if (name.equals("King") && from.getPiece().getMoves()==0 && to.getPiece() != null && to.getPiece().getName().equals("Rook") && to.getPiece().getMoves()==0) direction = "castle";
        if (direction == null || (to.getPiece() != null && from.getPiece().getColor().equals(to.getPiece().getColor()) && !direction.equals("castle"))) return false;
        if ((name.equals("Bishop")&&!direction.equals("diagonal")) || (name.equals("rook")&&!direction.equals("lateral")) || name.equals("Knight")&&!direction.equals("L") || (!name.equals("Knight")&&direction.equals("L"))) return false;

        if (name.equals("Pawn")) {
            if ((to.getRow()-from.getRow() != from.getPiece().getDirection()) && !(to.getRow()-from.getRow() == from.getPiece().getDirection()*2 && from.getPiece().getMoves()==0)) return false;
            int over = Math.abs(to.getCol()-from.getCol());
            if (Math.abs(to.getRow()-from.getRow()) != 1 && over ==1) return false;
            if (over == 0 && to.getPiece() == null) return true;
            if (over == 1 && to.getPiece() != null) return true;
            return false;
        } else if (name.equals("Bishop") || (name.equals("Queen") && direction.equals("diagonal"))) {
            int vert = (to.getRow() > from.getRow()) ? 1 : -1; //row
            int horz = (to.getCol() > from.getCol()) ? 1 : -1; //col
            int found = 0, newX = from.getRow(), newY = from.getCol();

            for (int i = 1; from.getRow() + i*vert != to.getRow() && from.getCol() + i*horz != to.getCol(); i++) {
                newX += vert;
                newY += horz;
                Piece p = list.get(newX*8+newY).getPiece();
                if (p != null) ++found;
            }

            if (found == 0) return true;
            return false;
        } else if (name.equals("Rook") || (name.equals("Queen") && direction.equals("lateral"))) {
            boolean vertical = (from.getRow() == to.getRow()) ? false : true;
            int incX, incY, newX = from.getRow(), newY = from.getCol(), found = 0;
            if (vertical){
                incY = 0;
                incX = Math.abs(to.getRow()-from.getRow())/(to.getRow()-from.getRow());
            } else {
                incX = 0;
                incY = Math.abs(to.getCol()-from.getCol())/(to.getCol()-from.getCol());
            }
            

            for (int i = 1; from.getRow() + i*incX != to.getRow() || from.getCol() + i*incY != to.getCol(); i++) {
                newX += incX;
                newY += incY;
                Piece p = list.get(newX*8+newY).getPiece();
                if (p != null) ++found;
            }

            if (found == 0) return true;
            return false;
        } else if (name.equals("Knight")) {
            return true;
        } else if (name.equals("King")) {
            if (direction.equals("castle")) {
                if (from.getPiece().getMoves() != 0 || to.getPiece().getMoves() != 0) return false;
                int side = Math.abs(to.getCol()-from.getCol())/(to.getCol()-from.getCol());
                int newY = from.getCol(), found = 0;

                do {
                    newY += side;
                    Square s = list.get(from.getRow()*8+newY);
                    if (s.getPiece() != null) ++found;
                } while (newY != to.getCol());

                if (found == 1){
                    castled = true;
                    return true;
                } return false;
            }
            if (Math.abs(to.getRow()-from.getRow()) > 1) return false;
            if (Math.abs(to.getCol()-from.getCol()) > 1) return false;
            return true;
        }


        return false;
    }

    public String moveType(int[] from, int[] to){
        if ((from[0]==to[0] && from[1]!=to[1]) || (from[1]==to[1] && from[0] != to[0])) return "lateral";
        if (Math.abs(from[0]-to[0])==Math.abs(from[1]-to[1])) return "diagonal";
        if ((Math.abs(from[0]-to[0])==2  && Math.abs(from[1]-to[1])==1) ^ (Math.abs(from[0]-to[0])==1  && Math.abs(from[1]-to[1])==2)) return "L";
        return null;
    }

    public String numberToLetter(int num){
        switch (num){
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
        }
        return "Z";
    }

    public boolean matchTurn(String color){
        if (color.equals("white")) return turn%2==1;
        return turn%2==0;
    }
    
    public boolean inCheck(String allyColor, Square king) {
        String direction;
        for (int xVal : new int[] {-1, 0, 1}) {
            for (int yVal : new int[] {-1, 0, 1}) {
                if (xVal == 0 && yVal == 0) continue;
                if (Math.abs(xVal + yVal) == 1) direction = "lateral";
                else direction = "diagonal";
                boolean checked = inCheck(allyColor, king.getRow(), king.getCol(), xVal, yVal, direction);
                if (checked) return true;
            }
        }

        for (int xVal : new int[] {-2, -1, 0, 1, 2}) {
            for (int yVal : new int[] {-2, -1, 0, 1, 2}) {
                if (Math.abs(xVal) == Math.abs(yVal)) continue;
                int currentX = king.getRow() + xVal;
                int currentY = king.getCol() + yVal;
                if (!inRange(currentX, currentY)) continue;
                Square s = list.get(currentX*8 + currentY);
                if (s.getPiece() != null && s.getPiece().getName().equals("Knight") && !s.getPiece().getColor().equals(allyColor)) return true;
            }
        }

        return false;
    }

    public boolean inCheck(String allyColor, int currentX, int currentY, int incX, int incY, String direction) {
        Piece pinned = null;
        int allies = 0, enemies = 0, squares = 0;
        currentX += incX;
        currentY += incY;
        while (inRange(currentX, currentY)){
            ++squares;
            // System.out.println(currentX + " " + currentY);
            Square s  = list.get(currentX*8+currentY);
            if (s.getPiece() != null){
                if (s.getPiece().getColor().equals(allyColor)) {
                    ++allies;
                    pinned = s.getPiece();
                } else ++enemies;
            }
            if (allies == 2) break;
            if (enemies == 1){
                String name = s.getPiece().getName();
                boolean b1 = direction.equals("lateral") && (name.equals("Rook") || name.equals("Queen"));
                boolean b2 = direction.equals("diagonal") && (name.equals("Bishop") || name.equals("Queen"));
                boolean b3 = direction.equals("diagaonal") && (name.equals("Pawn") && squares == 1);
                
                if (b1 || b2 || b3){
                    if (pinned != null) {
                        pinned.setPinned(true);
                        return false;
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public boolean inCheck(Square king, Square threat, boolean discovery, ArrayList<Square> board){
        // System.out.println(king);
        // System.out.println(threat);
        String direction = moveType(new int[] {king.getRow(), king.getCol()}, new int[] {threat.getRow(), threat.getCol()});
        if (direction == null) return false;
        if (!direction.equals("L") || (direction.equals("L") && discovery)) {
            int dX, dY;
            if (threat.getRow() == king.getRow()) dX = 0;
            else if (threat.getRow() > king.getRow()) dX = 1;
            else dX = -1;
            if (threat.getCol() == king.getCol()) dY = 0;
            else if (threat.getCol() > king.getCol()) dY = 1;
            else dY = -1;
            // System.out.println(direction + " " + dX + " " + dY);
            int currentX = king.getRow() + dX, currentY = king.getCol() + dY, squares = 0, allies = 0, enemies = 0;
            Piece pinned = null;

            while (inRange(currentX, currentY)){
                // System.out.println("currently at: " + currentX + " " + currentY);
                if (!discovery && threat.getRow()-king.getRow() == -dX && threat.getCol()-king.getCol() == -dY) {
                    // System.out.println("out of bounds at" + currentX + " " + currentY);
                    return false;
                }
                ++squares;
                // System.out.println(currentX + " " + currentY);
                Square s  = board.get(currentX*8+currentY);

                if (s.getPiece() != null){
                    if (s.getPiece().getColor().equals(king.getPiece().getColor())) {
                        ++allies;
                        pinned = s.getPiece();
                    } else ++enemies;
                    // System.out.println("found one: " + currentX + " " + currentY);
                } else {
                    currentX += dX;
                    currentY += dY;
                    continue;
                }
                if (allies == 2) break;
                if (enemies == 1){
                    // System.out.println(currentX + " " + currentY);
                    String name = s.getPiece().getName();
                    boolean b1 = direction.equals("lateral") && (name.equals("Rook") || name.equals("Queen"));
                    boolean b2 = direction.equals("diagonal") && (name.equals("Bishop") || name.equals("Queen"));
                    boolean b3 = direction.equals("diagonal") && (name.equals("Pawn") && squares == 1);
                    
                    if (b1 || b2 || b3){
                        if (pinned != null) {
                            pinned.setPinned(true);
                            return false;
                        }
                        return true;
                    }
                }
                currentX += dX;
                currentY += dY;
            }
        } else {
            return threat.getPiece().getName().equals("Knight");
        }

        return false;
    }
    
    public boolean simulateMove(Square from, Square to) {
        ArrayList<Square> copy = new ArrayList<>();
        for (Square q : list){
            Square newSquare = new Square("");
            newSquare.setRow(q.getRow());
            newSquare.setCol(q.getCol());
            newSquare.setPiece(q.getPiece());
            copy.add(newSquare);
        }
        from = copy.get(from.getRow()*8+from.getCol());
        to = copy.get(to.getRow()*8+to.getCol());
        
        if (castled) {
            int increment;
            Square newKing, newRook;
            // System.out.println("Castle");
            //! current is the square being moved from
            //!s is the square being moved to
            String side = (Math.abs(to.getCol()-from.getCol()) > 3) ? "Queen" : "King"; 
            // System.out.println(side + " side castle");
            if (side.equals("King")) {
                increment = (Math.abs(to.getCol()-from.getCol()))/(to.getCol()-from.getCol());
                newRook = list.get(to.getRow()*8+to.getCol()-increment);
                newKing = list.get(from.getRow()*8+from.getCol()+increment);
            } else {
                increment = (Math.abs(to.getCol()-from.getCol()))/(to.getCol()-from.getCol());
                newRook = list.get(to.getRow()*8+to.getCol()-3*increment);
                newKing = list.get(from.getRow()*8+from.getCol()+2*increment);
            }
            newRook.setPiece(to.getPiece());
            newRook.setIcon(to.getIcon());
            newKing.setPiece(from.getPiece());
            newKing.setIcon(from.getIcon());
            to.setIcon(null);
            to.setPiece(null);
            from.setIcon(null);
            from.setPiece(null);
            newKing.setVerticalTextPosition(JButton.CENTER);
            newKing.setHorizontalTextPosition(JButton.CENTER);
            newRook.setVerticalTextPosition(JButton.CENTER);
            newRook.setHorizontalTextPosition(JButton.CENTER);
            
    
        } else {
            // System.out.println("Valid move: " + from.getPiece().getName() + " to " + numberToLetter(to.getCol()) + (8-to.getRow()));
            to.setIcon(from.getIcon());
            to.setPiece(from.getPiece());
            from.setPiece(null);
            from.setIcon(null);
            to.setVerticalTextPosition(JButton.CENTER);
            to.setHorizontalTextPosition(JButton.CENTER);
        }
        Square king = (turn%2==1) ? wKing : bKing;
        boolean checked = inCheck(king, to, false, copy) || inCheck(king, from, true, copy);
        if (checked) {
            System.out.println("YOU ARE IN CHECK");
        }
        return !checked;
    }
    
    public boolean inRange(int x, int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
    
    public static void main(String[] args) {
        // for (int i = 0; i< 10; i++){
        //     System.out.print("break");
        // }
        new Board();
        System.out.println("started");
    }
}
