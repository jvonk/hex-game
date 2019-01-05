import java.util.*;
import java.awt.*;
import java.awt.Color;

public class Player {
    public int team, type;
    public double radius;
    public Color color;
    public Color complement;

    public Player(int team, int type, double r) {
        this.team = team;
        this.type = type;
        this.radius = r;
        color = new Color(150, 150, 150);
        complement = new Color(255, 255, 255);
        switch (team) {
            case 1: {
                color = new Color(106, 197, 244);
            }
            break;
            case 2: {
                color = new Color(252, 81, 11);
            }
            break;
        }
    }

    public boolean move(Tile dest, int dist) {
        if (dist > type) return false;
        if (dest.p != null)
            return dest.p.type <= type;
        return true;
    }

    public void drawMe(Graphics2D g, double x, double y) {
        switch (type) {
            case 1: {
                g.setColor(color);
                g.fillOval((int) (x - radius / 2), (int) (y - radius / 2), (int) (radius), (int) (radius));
                g.setColor(new Color(0, 0, 0, 120));
                g.fillOval((int) (x - radius / 2), (int) (y - radius / 2), (int) (radius), (int) (radius));
            }
                break;
            case 2: {
                g.setColor(color);
                g.fillOval((int) (x - radius / 2), (int) (y - radius / 2), (int) (radius), (int) (radius));
                g.setColor(complement);
                g.fillOval((int) (x - radius / 4), (int) (y - radius / 4), (int) (radius / 2), (int) (radius / 2));
                g.setColor(new Color(0, 0, 0, 120));
                g.fillOval((int) (x - radius / 2), (int) (y - radius / 2), (int) (radius), (int) (radius));
            }
            break;
        }
    }
}