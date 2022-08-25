import javax.swing.*;

public class Square {

    private final JPanel panel;
    private final int position_x;
    private final int position_y;

    Square(int x, int y, JPanel p) {
        this.position_x = x;
        this.position_y = y;
        this.panel = p;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public int getPositionX() {
        return position_x;
    }

    public int getPositionY() {
        return position_y;
    }
}
