import java.awt.Color;
import java.awt.Graphics;

public class Point {
    
    private int x, y;
    private Color color = Color.blue;
    private int radius = 15;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.setColor(this.color);
        g.fillOval(x-(radius/2), y-(radius/2), radius, radius);
    }


    /* Getters */
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public Color getColor() {return this.color;}
    public int getRadius() {return this.radius;}
    /***/

    /* Setters */
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setColor(Color color) {this.color = color;}
    public void setRadius(int radius) {this.radius = radius;}
    /***/
}
