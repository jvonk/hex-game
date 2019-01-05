import java.util.*;
import java.awt.*;

public class Player {
    public int team, type;
    public double radius;
    public Player (int team, int type, double r) {
        this.team=team;
        this.type=type;
        this.radius = r;
    }
    public boolean move (int dist) {
        return dist<=2;
    }
    public void drawMe(Graphics2D g, double x, double y) {
        switch(type) {
            case 1: {
                g.setColor(new Color((120+team*201)%255, (100+team*567)%255, (120+team*94)%255));
                g.fillOval((int)(x-radius/2), (int)(y-radius/2), (int)(radius), (int)(radius));
            }
            default: break;
        }
    }
}