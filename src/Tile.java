import java.util.*;
import java.awt.*;
import java.awt.Color;

public class Tile implements Comparable {
    public int id, team, distance;
    public boolean visited;
    public double x, y, radius;
    public Polygon polygon;
    public Tile parent, up, upright, downright, down, downleft, upleft;
    public Player p;
    public Color color;

    public Tile(int id, int team, double x, double y, double r) {
        this.id = id;
        this.team = team;
        this.x = x;
        this.y = y;
        this.radius = r;
        this.polygon = new Polygon();
        this.p = new Player(team, 0, radius);
        switch (team) {
        case 1: {
            color = new Color(106, 197, 244);
        }
            break;
        case 2: {
            color = new Color(252, 81, 11);
        }
            break;
        default: {
            color = new Color(150, 150, 150);
        }
            break;
        }
        this.parent = null;
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

    public void addPlayer(Player newPlayer) {
        if (newPlayer.move(0)) {
            p = newPlayer;
        }
    }

    public boolean movePlayer(Tile dest, int dist) {
        if (!p.move(dist))
            return false;
        Player temp = p;
        p = new Player(team, 0, radius);
        dest.p = temp;
        return true;
    }

    public void drawMe(Graphics2D g, boolean isFocus, boolean showMove) {
        g.setColor(color);
        g.fillPolygon(polygon);
        g.setColor(new Color(255, 255, 255, (showMove ? 70 : 0) + (isFocus ? 100 : 0)));
        g.fillPolygon(polygon);
        g.setColor(new Color(100, 100, 100));
        g.drawPolygon(polygon);
        p.drawMe(g, x, y);
        g.setColor(new Color(0, 0, 0));
        g.drawString(String.valueOf(distance), (int) (x - 4), (int) (y + 6));
    }
    public void drawMe(Graphics2D g, int a) {
        g.setColor(new Color(255, 255, 255, a));
        g.fillPolygon(polygon);
    }

    @Override
    public int compareTo(Object other) {
        return this.distance - ((Tile) other).distance;
    }
}