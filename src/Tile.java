import java.util.*;
import java.awt.*;
import java.awt.Color;

public class Tile implements Comparable {
    public int id, team, distance, component, value, difficulty;
    public boolean visited;
    public double x, y, radius;
    public Polygon polygon;
    public Tile parent, up, upright, downright, down, downleft, upleft;
    public Player p;
    public Color color;

    public Tile(int id, int team, int difficulty, double x, double y, double r) {
        this.id = id;
        this.team = team;
        this.component = -1;
        this.value = 1;
        this.difficulty = difficulty;
        this.x = x;
        this.y = y;
        this.radius = r;
        this.polygon = new Polygon();
        this.p = new Player(team, 0, radius);
        switch (team) {
            case 1:
                color = new Color(106, 197, 244);
                break;
            case 2:
                color = new Color(252, 81, 11);
                break;
            default:
                color = new Color(150, 150, 150);
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
    public boolean isConnected(Tile other) {
        return (this.up!=null&&this.up.id==other.id || this.upright!=null&&this.upright.id==other.id || this.downright!=null&&this.downright.id==other.id || this.down!=null&&this.down.id==other.id || this.downleft!=null&&this.downleft.id==other.id || this.upleft!=null&&this.upleft.id==other.id);
    }
    public void addPlayer(Player newPlayer) {
        if (newPlayer.move(this, 0)) {
            p = newPlayer;
        }
    }

    public boolean movePlayer(Tile dest, int dist) {
        if (!p.move(dest, dist))
            return false;
        Player temp = p;
        p = new Player(team, 0, radius);
        dest.p = temp;
        dest.team = team;
        dest.color = color;
        return true;
    }
    private Polygon makePolygon(double[] c) {
        Polygon p = new Polygon();
        for (int i = 0; i < c.length; i++) {
            p.addPoint((int)(polygon.xpoints[(int)c[i]]*(c[i]%1) + polygon.xpoints[(int)c[i]+1]*(1- (c[i]%1))), (int)(polygon.ypoints[(int)c[i]]*(c[i]%1) + polygon.ypoints[(int)c[i]+1]*(1- (c[i]%1))));
        }
        return p;
    }
    public void drawMe(Graphics2D g, boolean isFocus, boolean showMove) {
        g.setColor(color);
        g.fillPolygon(polygon);
        switch(difficulty) {
            case 0: {

            }
            case 2: {
                //3, 4.5, 0.5, 1, 2
                //g.setColor(new Color(50, 50, 50));
                //g.fillPolygon(makePolygon(new double[] {2.5, 5.5, 0, 1, 2}));
                //g.setColor(new Color(100, 100, 100));
                //g.fillPolygon(makePolygon(new double[] {3, 4.5, 0.5, 1, 2}));
            }
            case 3: {

            }
        }
        g.setColor(new Color(255, 255, 255, (showMove ? 70 : 0) + (isFocus ? 100 : 0)));
        g.fillPolygon(polygon);
        g.setColor(new Color(100, 100, 100));
        g.drawPolygon(polygon);
        p.drawMe(g, x, y);
        g.setColor(new Color(0, 0, 0, 130));
        Polygon p = new Polygon();
        for (int i = 0; i < 6; i++) {
            p.addPoint((int) (Math.round(Math.cos(Math.PI * i / 3) * this.radius*(this.difficulty-1)/5 + this.x)),
                    (int) (Math.round(Math.sin(Math.PI * i / 3) * this.radius*(this.difficulty-1)/5 + this.y)));
        }
        g.fillPolygon(p);
        g.setColor(new Color(0, 0, 0));

        //g.drawString(String.valueOf(distance), (int) (x - g.getFontMetrics().stringWidth(String.valueOf(distance))/2), (int) (y + g.getFontMetrics().getMaxAscent()/2 - g.getFontMetrics().getMaxDescent()/2));
        
        //g.drawString(String.valueOf(id), (int) (x - g.getFontMetrics().stringWidth(String.valueOf(id))/2), (int) (y + g.getFontMetrics().getMaxAscent()/2 - g.getFontMetrics().getMaxDescent()/2));
    }

    public void drawMe(Graphics2D g, int a) {
        g.setColor(new Color(255, 255, 255, a));
        g.fillPolygon(polygon);
        p.drawMe(g, x, y);
    }

    @Override
    public int compareTo(Object other) {
        return this.distance - ((Tile) other).distance;
    }
}