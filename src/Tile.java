import java.util.*;
import java.awt.*;

public class Tile {
    public int id, team;
    public double x, y, radius;
    public Polygon polygon;
    public Tile up, upright, downright, down, downleft, upleft;

    public Tile(int id, int team, double x, double y, double r) {
        this.id = id;
        this.team = team;
        this.x = x;
        this.y = y;
        this.radius = r;
        this.polygon = new Polygon();
        this.up = null;
        this.upright = null;
        this.downright = null;
        this.down = null;
        this.downleft = null;
        this.upleft = null;
        for (int i = 0; i < 6; i++) {
            polygon.addPoint((int) (Math.round(Math.cos(Math.PI * i / 3) * this.radius + this.x)),
                    (int) (Math.round(Math.sin(Math.PI * i / 3) * this.radius + this.y)));
        }
    }

    public void drawMe(Graphics2D g) {
        g.setColor(new Color((150+team*201)%255, (150+team*567)%255, (150+team*94)%255));
        g.fillPolygon(polygon);
        g.setColor(new Color(100, 100, 100));
        g.drawPolygon(polygon);
    }
}