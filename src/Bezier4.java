import java.awt.Color;
import java.awt.Graphics;

public class Bezier4 implements Bezier {

    private Point p1, p2, p3, p4;
    
    private int nPoints = 50;
    private double pace = 1.0/nPoints;
    private int dir = -1;

    private int[] xList;
    private int[] yList;

    private double t = 0.0;
    private int currIdx = 0;

    private Point[] tPoints;
    private Point tPoint;

    public Bezier4(Point p1, Point p2, Point p3, Point p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        this.xList = new int[this.nPoints];
        this.yList = new int[this.nPoints];

        generatePoints(xList, yList);

        tPoint = new Point();

        tPoints = new Point[5];
        for(int i = 0; i < tPoints.length; i++) {
            tPoints[i] = new Point();
        }

    }

    public void tick() {

        tPoint.setX(xList[currIdx]);
        tPoint.setY(yList[currIdx]);
        currIdx-=dir;

        tPoints[0].setX((int)((1 - t) * p1.getX() + t * p2.getX()));
        tPoints[0].setY((int)((1 - t) * p1.getY() + t * p2.getY()));
        tPoints[1].setX((int)((1 - t) * p2.getX() + t * p3.getX()));
        tPoints[1].setY((int)((1 - t) * p2.getY() + t * p3.getY()));
        tPoints[2].setX((int)((1 - t) * p3.getX() + t * p4.getX()));
        tPoints[2].setY((int)((1 - t) * p3.getY() + t * p4.getY()));

        tPoints[3].setX((int)((1 - t) * tPoints[0].getX() + t * tPoints[1].getX()));
		tPoints[3].setY((int)((1 - t) * tPoints[0].getY() + t * tPoints[1].getY()));
		tPoints[4].setX((int)((1 - t) * tPoints[1].getX() + t * tPoints[2].getX()));
		tPoints[4].setY((int)((1 - t) * tPoints[1].getY() + t * tPoints[2].getY()));

        t += pace;

        if(currIdx >= nPoints-1 || currIdx <= 0) {
            dir *= -1;
            pace *= -1;
        }
    }

    @Override
    public void render(Graphics g) {
        
        if(Main.exibMode >= 1) {
            for(int i = 0; i < 3; i++) {
                tPoints[i].render(g);
            }
        }

        if(Main.exibMode >= 2) {
            g.drawLine(tPoints[0].getX(), tPoints[0].getY(), tPoints[1].getX(), tPoints[1].getY());
            g.drawLine(tPoints[1].getX(), tPoints[1].getY(), tPoints[2].getX(), tPoints[2].getY());

            for(int i = 2; i < tPoints.length; i++) {
                tPoints[i].render(g);
            }
        }

        if(Main.exibMode >= 3) {
            g.drawLine(tPoints[3].getX(), tPoints[3].getY(), tPoints[4].getX(), tPoints[4].getY());
            tPoint.render(g);
        }
        
        if(Main.exibMode >= 4) {
            g.setColor(Color.red);
            g.drawPolyline(xList, yList, nPoints);
        }
        
        
    }

    private void generatePoints(int[] xList, int[] yList) {;
        int i = 0;
        for (t = 0.0; t <= 1.0; t += pace) { 
			xList[i] = (int) (Math.pow(1 - t, 3) * p1.getX() + 3 * Math.pow(1 - t, 2) * t * p2.getX() + 3 * (1 - t) * Math.pow(t, 2) * p3.getX() + Math.pow(t, 3) * p4.getX());
			yList[i] = (int) (Math.pow(1 - t, 3) * p1.getY() + 3 * Math.pow(1 - t, 2) * t * p2.getY() + 3 * (1 - t) * Math.pow(t, 2) * p3.getY() + Math.pow(t, 3) * p4.getY());
			i++; 
		}
        t = 0.0;
    }
}
