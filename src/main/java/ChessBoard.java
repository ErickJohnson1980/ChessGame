/*To Do : I have an info Label added to the header for displaying information about the game,
but I am Not currently using it for anything, I should make use of this to display feedback to the user
*/

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class ChessBoard extends JFrame {

    ArrayList<Square> squares = new ArrayList<>();
    Square selectedPiece = null;
    Color turn = Color.BLACK;

    ChessBoard() {
        //create a window
        this.setTitle("Erick's Simple Chess Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 450);
        this.getContentPane().setBackground(Color.BLACK);

        this.setLayout(new BorderLayout(0, 0));
        //create a new window icon image and set it on our window
        try {
            ImageIcon windowIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("icon.jpg")));
            this.setIconImage(windowIcon.getImage());
        } catch (Exception e) {
            System.out.println("Error Loading Icon");
        }

        ///header
        JPanel header = new JPanel();
        header.setLayout(new GridLayout(3, 1));
        header.setSize(400, 50);
        JLabel info = new JLabel("");
        JButton reset = new JButton("Reset Board");
        reset.addActionListener(e -> ResetBoard());
        header.add(info);
        header.add(reset);
        this.add(header, BorderLayout.NORTH);
        ConstructBoard();
        addListeners();
        this.setVisible(true);
    }


    public JLabel getLabel(String text, Color color) {
        JLabel label = new JLabel();
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText(text);
        label.setSize(50, 50);
        label.setFont(new Font("Serif", Font.BOLD, 40));
        label.setForeground(color);
        return label;
    }

    public void addListeners() {
        for (Square s : squares) {

            s.getPanel().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me) {
                    if (selectedPiece == null) {
                        JPanel p = (JPanel) me.getComponent();
                        p.setBorder(BorderFactory.createLineBorder(Color.yellow));
                    }
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    if (selectedPiece == null) {
                        JPanel p = (JPanel) me.getComponent();
                        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    }
                }

                @Override
                public void mousePressed(MouseEvent me) {

                    JPanel p = (JPanel) me.getComponent();
                    if (selectedPiece == null) {
                        JLabel l = (JLabel) p.getComponent(0);
                        if (l.getForeground() == turn && !l.getText().equals("")) {
                            selectedPiece = s;
                            getPossibleMoves(l.getText(), s.getPositionX(), s.getPositionY());
                            p.setBorder(BorderFactory.createLineBorder(Color.yellow));
                        }
                    } else if (selectedPiece == s) {
                        selectedPiece = null;
                        setBordersBlack();
                    } else {

                        LineBorder b = (LineBorder) s.getPanel().getBorder();
                        if (b.getLineColor() == Color.RED) {
                            JLabel l = (JLabel) p.getComponent(0);
                            if (turn == Color.BLACK) {
                                turn = Color.WHITE;

                            } else {
                                turn = Color.BLACK;
                            }

                            JPanel sp = selectedPiece.getPanel();
                            JLabel sl = (JLabel) sp.getComponent(0);
                            if (sl.getForeground() != l.getForeground() || l.getText().equals("")) {
                                l.setText(sl.getText());
                                l.setForeground(sl.getForeground());
                                sl.setText("");
                                selectedPiece = null;
                                setBordersBlack();
                            }
                        }
                    }

                }
            });
        }
    }

    public void getPossibleMoves(String piece, int x, int y) {

        switch (piece) {
            case "♔":
                markKingPossibleMoves(x, y);
                break;
            case "♖":
                markRookPossibleMoves();
                break;
            case "♘":
                markKnightPossibleMoves(x, y);
                break;
            case "♗":
                markBishopPossibleMoves();
                break;
            case "♕":
                markRookPossibleMoves();
                markBishopPossibleMoves();
                break;
            case "♙":
                markPawnPossibleMoves(x, y);
                break;
            default:
                System.out.println("Error in ChessBoard.getPossibleMoves() switch statement");
        }
    }


    public void setBordersBlack() {
        for (Square sq : squares) {
            sq.getPanel().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    private void markPawnPossibleMoves(int x, int y) {
        for (Square s : squares) {
            JPanel p = s.getPanel();
            JLabel l = (JLabel) p.getComponent(0);
            if (turn == Color.WHITE) {
                if (s.getPositionX() == x && s.getPositionY() == (y - 1) && l.getText().equals("")) {

                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                if (s.getPositionX() == x && s.getPositionY() == (y - 2) && l.getText().equals("") && selectedPiece.getPositionY() == 6) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                //attack squares
                if (s.getPositionX() == (x - 1) && s.getPositionY() == (y - 1) && !l.getText().equals("")) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                if (s.getPositionX() == (x + 1) && s.getPositionY() == (y - 1) && !l.getText().equals("")) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            } else if (turn == Color.BLACK) {
                if (s.getPositionX() == x && s.getPositionY() == (y + 1) && l.getText().equals("")) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                if (s.getPositionX() == x && s.getPositionY() == (y + 2) && l.getText().equals("") && selectedPiece.getPositionY() == 1) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                //attack squares
                if (s.getPositionX() == (x - 1) && s.getPositionY() == (y + 1) && !l.getText().equals("")) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                if (s.getPositionX() == (x + 1) && s.getPositionY() == (y + 1) && !l.getText().equals("")) {
                    p.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            }
        }
    }

    private void markRookPossibleMoves() {
        int a = squares.indexOf(selectedPiece) - 8;
        //get the up direction squares
        while (a >= 0) {

            JPanel p = squares.get(a).getPanel();
            JLabel l = (JLabel) p.getComponent(0);
            p.setBorder(BorderFactory.createLineBorder(Color.RED));
            if (!l.getText().equals("")) {
                break;
            }
            a -= 8;
        }
        a = squares.indexOf(selectedPiece) + 8;
        //get the down direction squares
        while (a <= 63) {

            JPanel p = squares.get(a).getPanel();
            JLabel l = (JLabel) p.getComponent(0);
            p.setBorder(BorderFactory.createLineBorder(Color.RED));
            if (!l.getText().equals("")) {
                break;
            }
            a += 8;
        }
        a = squares.indexOf(selectedPiece) - 1;
        //get the left direction squares
        while (a >= (squares.indexOf(selectedPiece) - selectedPiece.getPositionX())) {
            JPanel p = squares.get(a).getPanel();
            JLabel l = (JLabel) p.getComponent(0);
            p.setBorder(BorderFactory.createLineBorder(Color.RED));
            a -= 1;
            if (!l.getText().equals("")) {
                a = -1;
            }
        }

        a = selectedPiece.getPositionX() + 1;
        //get the right direction squares
        while (a <= 7) {
            int sq = a + (squares.indexOf(selectedPiece) - selectedPiece.getPositionX());
            JPanel p = squares.get(sq).getPanel();
            JLabel l = (JLabel) p.getComponent(0);
            p.setBorder(BorderFactory.createLineBorder(Color.RED));
            a += 1;
            if (!l.getText().equals("")) {
                a = 1000;
            }
        }

    }

    private void markKnightPossibleMoves(int x, int y) {
        for (Square s : squares) {
            JPanel p = s.getPanel();
            if (s.getPositionX() == (x - 2) && s.getPositionY() == y - 1) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x - 2) && s.getPositionY() == y + 1) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x + 2) && s.getPositionY() == y - 1) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x + 2) && s.getPositionY() == y + 1) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x + 1) && s.getPositionY() == y - 2) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x + 1) && s.getPositionY() == y + 2) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x - 1) && s.getPositionY() == y - 2) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            if (s.getPositionX() == (x - 1) && s.getPositionY() == y + 2) {
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
    }

    private void markBishopPossibleMoves() {
        //get up/left
        if ((squares.indexOf(selectedPiece) - 9) >= 0) {
            Square current = squares.get(squares.indexOf(selectedPiece) - 9);
            while (current.getPositionX() < selectedPiece.getPositionX() && squares.contains(current)) {
                JPanel p = current.getPanel();
                JLabel l = (JLabel) p.getComponent(0);
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!l.getText().equals("") || squares.indexOf(current) - 9 < 0) break;
                current = squares.get(squares.indexOf(current) - 9);

            }
        }
        //get up/right
        if ((squares.indexOf(selectedPiece) - 7) >= 0) {
            Square current = squares.get(squares.indexOf(selectedPiece) - 7);
            while (current.getPositionX() > selectedPiece.getPositionX() && squares.contains(current)) {
                JPanel p = current.getPanel();
                JLabel l = (JLabel) p.getComponent(0);
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!l.getText().equals("") || squares.indexOf(current) - 7 < 0) break;
                current = squares.get(squares.indexOf(current) - 7);

            }
        }
        //get down/left
        if ((squares.indexOf(selectedPiece) + 7) <= 63) {
            Square current = squares.get(squares.indexOf(selectedPiece) + 7);
            while (current.getPositionX() < selectedPiece.getPositionX() && squares.indexOf(current) <= 63) {
                JPanel p = current.getPanel();
                JLabel l = (JLabel) p.getComponent(0);
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!l.getText().equals("") || (squares.indexOf(current) + 7) >= squares.size()) break;
                current = squares.get(squares.indexOf(current) + 7);

            }
        }
        //get down/right
        if ((squares.indexOf(selectedPiece) + 9) < squares.size()) {
            Square current = squares.get(squares.indexOf(selectedPiece) + 9);
            while (current.getPositionX() > selectedPiece.getPositionX() && squares.indexOf(current) <= 63) {
                JPanel p = current.getPanel();
                JLabel l = (JLabel) p.getComponent(0);
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!l.getText().equals("") || (squares.indexOf(current) + 9) >= squares.size()) break;
                current = squares.get(squares.indexOf(current) + 9);

            }
        }
    }

    private void markKingPossibleMoves(int x, int y) {
        for (Square s : squares) {

            int distX = Math.abs(s.getPositionX() - x);
            int distY = Math.abs(s.getPositionY() - y);
            if (distX == 1 && distY == 1 || distX == 0 && distY == 1 || distX == 1 && distY == 0) {
                JPanel p = s.getPanel();
                p.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
    }

    public void ConstructBoard() {
        JPanel SquaresParentContainer = new JPanel();
        SquaresParentContainer.setLayout(new GridLayout(8, 8));
        int rows = 0;
        int columns = 0;
        boolean even = false;
        String color = "";
        while (rows < 8) {
            if (columns < 8) {
                JPanel panel = new JPanel();
                if (even) {
                    if (columns % 2 != 0) {
                        color = "Grey";
                        panel.setBackground(Color.GRAY);
                    } else {
                        color = "DarkGrey";
                        panel.setBackground(Color.DARK_GRAY);
                    }
                }
                if (!even) {
                    if (columns % 2 != 0) {
                        color = "DarkGrey";
                        panel.setBackground(Color.DARK_GRAY);
                    } else {
                        color = "Grey";
                        panel.setBackground(Color.GRAY);
                    }
                }

                System.out.println("setting panel " + "Color:" + color + " : Location: " + rows + " : " + columns);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.setLayout(new BorderLayout());
                panel.setSize(50, 50);
                SquaresParentContainer.add(panel);
                squares.add(new Square(columns, rows, panel));

                columns++;
            } else {
                rows++;
                columns = 0;
                even = !even;
            }
        }
        // initial placement of images on squares
        //black pieces
        //first row
        squares.get(0).getPanel().add(getLabel("♖", Color.BLACK));
        squares.get(1).getPanel().add(getLabel("♘", Color.BLACK));
        squares.get(2).getPanel().add(getLabel("♗", Color.BLACK));
        squares.get(3).getPanel().add(getLabel("♕", Color.BLACK));
        squares.get(4).getPanel().add(getLabel("♔", Color.BLACK));
        squares.get(5).getPanel().add(getLabel("♗", Color.BLACK));
        squares.get(6).getPanel().add(getLabel("♘", Color.BLACK));
        squares.get(7).getPanel().add(getLabel("♖", Color.BLACK));
        //black pieces
        //second row
        squares.get(8).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(9).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(10).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(11).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(12).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(13).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(14).getPanel().add(getLabel("♙", Color.BLACK));
        squares.get(15).getPanel().add(getLabel("♙", Color.BLACK));
        //empty squares
        squares.get(16).getPanel().add(getLabel("", Color.WHITE));
        squares.get(17).getPanel().add(getLabel("", Color.WHITE));
        squares.get(18).getPanel().add(getLabel("", Color.WHITE));
        squares.get(19).getPanel().add(getLabel("", Color.WHITE));
        squares.get(20).getPanel().add(getLabel("", Color.WHITE));
        squares.get(21).getPanel().add(getLabel("", Color.WHITE));
        squares.get(22).getPanel().add(getLabel("", Color.WHITE));
        squares.get(23).getPanel().add(getLabel("", Color.WHITE));
        squares.get(24).getPanel().add(getLabel("", Color.WHITE));
        squares.get(25).getPanel().add(getLabel("", Color.WHITE));
        squares.get(26).getPanel().add(getLabel("", Color.WHITE));
        squares.get(27).getPanel().add(getLabel("", Color.WHITE));
        squares.get(28).getPanel().add(getLabel("", Color.WHITE));
        squares.get(29).getPanel().add(getLabel("", Color.WHITE));
        squares.get(30).getPanel().add(getLabel("", Color.WHITE));
        squares.get(31).getPanel().add(getLabel("", Color.WHITE));
        squares.get(32).getPanel().add(getLabel("", Color.WHITE));
        squares.get(33).getPanel().add(getLabel("", Color.WHITE));
        squares.get(34).getPanel().add(getLabel("", Color.WHITE));
        squares.get(35).getPanel().add(getLabel("", Color.WHITE));
        squares.get(36).getPanel().add(getLabel("", Color.WHITE));
        squares.get(37).getPanel().add(getLabel("", Color.WHITE));
        squares.get(38).getPanel().add(getLabel("", Color.WHITE));
        squares.get(39).getPanel().add(getLabel("", Color.WHITE));
        squares.get(40).getPanel().add(getLabel("", Color.WHITE));
        squares.get(41).getPanel().add(getLabel("", Color.WHITE));
        squares.get(42).getPanel().add(getLabel("", Color.WHITE));
        squares.get(43).getPanel().add(getLabel("", Color.WHITE));
        squares.get(44).getPanel().add(getLabel("", Color.WHITE));
        squares.get(45).getPanel().add(getLabel("", Color.WHITE));
        squares.get(46).getPanel().add(getLabel("", Color.WHITE));
        squares.get(47).getPanel().add(getLabel("", Color.WHITE));
        // white pieces
        //second row
        squares.get(48).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(49).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(50).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(51).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(52).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(53).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(54).getPanel().add(getLabel("♙", Color.WHITE));
        squares.get(55).getPanel().add(getLabel("♙", Color.WHITE));
        // white pieces
        //first row
        squares.get(56).getPanel().add(getLabel("♖", Color.WHITE));
        squares.get(57).getPanel().add(getLabel("♘", Color.WHITE));
        squares.get(58).getPanel().add(getLabel("♗", Color.WHITE));
        squares.get(59).getPanel().add(getLabel("♕", Color.WHITE));
        squares.get(60).getPanel().add(getLabel("♔", Color.WHITE));
        squares.get(61).getPanel().add(getLabel("♗", Color.WHITE));
        squares.get(62).getPanel().add(getLabel("♘", Color.WHITE));
        squares.get(63).getPanel().add(getLabel("♖", Color.WHITE));
        this.add(SquaresParentContainer, BorderLayout.CENTER);


    }

    public void ResetBoard() {

        for (Square s : squares) {
            JLabel l = (JLabel) s.getPanel().getComponent(0);
            if (squares.indexOf(s) >= 16 && squares.indexOf(s) <= 47) {
                l.setText("");
            } else if (squares.indexOf(s) >= 8 && squares.indexOf(s) <= 15 || squares.indexOf(s) >= 48 && squares.indexOf(s) <= 55) {
                l.setText("♙");
            } else if (squares.indexOf(s) == 0 || squares.indexOf(s) == 7 || squares.indexOf(s) == 56 || squares.indexOf(s) == 63) {
                l.setText("♖");
            } else if (squares.indexOf(s) == 1 || squares.indexOf(s) == 6 || squares.indexOf(s) == 57 || squares.indexOf(s) == 62) {
                l.setText("♘");
            } else if (squares.indexOf(s) == 2 || squares.indexOf(s) == 5 || squares.indexOf(s) == 58 || squares.indexOf(s) == 61) {
                l.setText("♗");
            } else if (squares.indexOf(s) == 3 || squares.indexOf(s) == 59) {
                l.setText("♕");
            } else if (squares.indexOf(s) == 4 || squares.indexOf(s) == 60) {
                l.setText("♔");
            }

            turn = Color.BLACK;
            this.getComponent(0).repaint();

        }
    }
}



