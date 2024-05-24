import java.awt.*;

public class Island {
    private int x, y, cx, cy, radius;
    private int[] randX;
    private int[] randY;
    private int numTrees;
    private int countSeperatedTrees;

    public Island(int islandX, int islandY, int islandR, int numTrees) {
        x = islandX;
        y = islandY;
        cx = islandX;
        cy = islandY;
        radius = islandR;
        this.numTrees = numTrees;
        randX = new int[numTrees];
        randY = new int[numTrees];
        for(int i = 0; i < numTrees; i++) {
            //Random value between -radius+120 and radius-120
            randX[i] = (int)(Math.random()*(2*radius-240)-radius+120)+cx;
            //Random value within the circle to match x
            randY[i] = (int)(Math.random()*(2*yVal(randX[i]))-yVal(randX[i]))+cy;
            //Seperate trees
            while(countSeperatedTrees < i) {
                randX[i] = (int)(Math.random()*(2*radius-240)-radius+120)+cx;
                randY[i] = (int)(Math.random()*(2*yVal(randX[i]))-yVal(randX[i]))+cy;
                countSeperatedTrees = 0;
                for(int j = 0; j < i; j++) {
                    if(calcDist(randX[i], randY[i], randX[j], randY[j]) >= 150 && calcDist(randX[i], randY[i], islandX, islandY) >= 50) {
                        countSeperatedTrees++;
                    }
                }
            }
        }
    }

    public void drawMe(Graphics g) {
        g.setColor(new Color(15, 140, 100, 80));
        g.fillOval(cx-radius-30, cy-radius-30, 2*radius+60, 2*radius+60);
        g.setColor(new Color(150, 140, 100));
        g.fillOval(cx-radius, cy-radius, 2*radius, 2*radius);
        g.setColor(new Color(86, 125, 70));
        g.fillOval(cx-radius+120, cy-radius+120, 2*radius-240, 2*radius-240);
    }

    public double calcDist(double x1, double y1, double x2, double y2) {       
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public int yVal(int x) {
        return (int)(Math.sqrt(1-Math.pow((double)(x-cx)/(radius-120), 2))*(radius-120));
    }

    public void setPos(int x, int y) {
        cx = x;
        cy = y;
    }

    public int getRadius() {
        return radius;
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

    public int getNumTrees() {
        return numTrees;
    }

    public int[] getRandX() {
        return randX;
    }

    public int[] getRandY() {
        return randY;
    }
}
