import java.awt.*;

public class Ship {
    private int x, y, cx, cy;
    
    public Ship(int shipX, int shipY) {
        x = shipX;
        y = shipY;
        cx = shipX;
        cy = shipY;
    }

    public void drawMe(Graphics g) {
        g.setColor(new Color(168, 130, 76));
        g.fillPolygon(new int[] {cx-40, cx-50, cx-45, cx-000, cx+45, cx+50, cx+40}, 
                      new int[] {cy+80, cy-20, cy-50, cy-100, cy-50, cy-20, cy+80}, 7);
        g.setColor(new Color(143, 107, 56));
        g.fillPolygon(new int[] {cx-32, cx-40, cx-36, cx-00, cx+36, cx+40, cx+32}, 
                      new int[] {cy+64, cy-16, cy-40, cy-80, cy-40, cy-16, cy+64}, 7);
        g.setColor(new Color(168, 130, 76));
        g.fillRect(cx-35, cy+35, 70, 20);
        g.fillRect(cx-45, cy-50, 90, 20);
    }

    public void setPos(int x, int y) {
        cx = x;
        cy = y;
    }

    public void setOriPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCX() {
        return cx;
    }

    public int getCY() {
        return cy;
    }
}