import java.awt.*;

public class Tree {

    private int x;
    private int y;

    public Tree(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawMe(Graphics g, int x, int y) {
        g.setColor(new Color(43, 63, 35, 125));
        drawBunch(g, x-32, y-20);
        drawBunch(g, x-32, y+20);
        drawBunch(g, x+32, y-20);
        drawBunch(g, x+32, y+20);
        drawBunch(g, x, y+40);
        drawBunch(g, x, y-40);
        g.setColor(new Color(100, 60, 20));
        g.fillOval(x-25, y-25, 50, 50);
    }

    public void drawBunch(Graphics g, int x, int y) {
        g.fillOval(x-50, y-50, 100, 100);
    }

    public int getTrunkRadius() {
        return 25;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
