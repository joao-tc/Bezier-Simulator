import java.awt.Color;
import java.awt.Graphics;

public class Bezier3 {

    private Point p1, p2, p3;
    
    private int nPoints = 50;

    private int[] xList;
    private int[] yList;

    private double t = 0.0;
    private int currIdx = 0;

    private Point tPoint;

    public Bezier3(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        this.xList = new int[this.nPoints];
        this.yList = new int[this.nPoints];

        generatePoints(xList, yList);

        this.tPoint = new Point(xList[0], yList[0]);
    }

    public void tick() {
        tPoint.setX(xList[currIdx]);
        tPoint.setY(yList[currIdx]);
        currIdx++;
        if(currIdx >= nPoints-1) {
            currIdx = 0;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.drawPolyline(xList, yList, nPoints);
        tPoint.render(g);
    }

    private void generatePoints(int[] xList, int[] yList) {
        double aux = 1.0/nPoints;
        int i = 0;
        for (t = 0.0; t <= 1.0; t += aux) { 
			xList[i] = (int)(Math.pow(1 - t, 2) * p1.getX() + 2 * (1 - t) * t * p2.getX() + Math.pow(t, 2) * p3.getX());
			yList[i] = (int)(Math.pow(1 - t, 2) * p1.getY() + 2 * (1 - t) * t * p2.getY() + Math.pow(t, 2) * p3.getY());
			i++; 
		}
        t = 0.0;
    }

}
