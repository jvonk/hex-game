import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener, MouseListener, MouseWheelListener {
    private List<Tile> tiles;
    public int wid, hei, focus;
    public double radius, zoomFactor;

    public Board(double r) {
        radius = r;
        zoomFactor = 1;
        System.out.println(r);
        System.out.println(radius);
        System.out.println(this.radius);
        Toolkit tk = Toolkit.getDefaultToolkit();
        wid = ((int) tk.getScreenSize().getWidth());
        hei = ((int) tk.getScreenSize().getHeight());
        level1();
        focus = 0;
        addMouseWheelListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }

    public void level1() {
        tiles = new ArrayList<Tile>();
        tiles.add(new Tile(tiles.size(), 0, 0, 0, radius));
        addTileUp(0, 0);
        addTileUpRight(0, 2);
        addTileDownRight(0, 0);
        addTileDown(0, 1);
        addTileDownLeft(0, 1);
        addTileUpLeft(0, 1);
        addTileUpRight(2, 2);
        addTileUp(2, 2);
        addTileDownRight(2, 2);
        addTileDown(3, 0);
        addTileDownLeft(4, 1);
        tiles.get(4).addPlayer(new Player(1, 1, radius));
        connect();
        connect();
        connect();
    }

    public void connect() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).radius = radius;
            if (tiles.get(i).up != null && tiles.get(i).up.downright != null) {
                tiles.get(i).upright = tiles.get(i).up.downright;
            }
            if (tiles.get(i).upright != null && tiles.get(i).upright.down != null) {
                tiles.get(i).downright = tiles.get(i).upright.down;
            }
            if (tiles.get(i).downright != null && tiles.get(i).downright.downleft != null) {
                tiles.get(i).down = tiles.get(i).downright.downleft;
            }
            if (tiles.get(i).down != null && tiles.get(i).down.upleft != null) {
                tiles.get(i).downleft = tiles.get(i).down.upleft;
            }
            if (tiles.get(i).downleft != null && tiles.get(i).downleft.up != null) {
                tiles.get(i).upleft = tiles.get(i).downleft.up;
            }
            if (tiles.get(i).upleft != null && tiles.get(i).upleft.upright != null) {
                tiles.get(i).up = tiles.get(i).upleft.upright;
            }
        }
    }

    public void addTileDown(int previd, int team) {
        tiles.add(
                new Tile(tiles.size(), team, tiles.get(previd).x, tiles.get(previd).y + radius * Math.sqrt(3), radius));
        tiles.get(tiles.size() - 1).up = tiles.get(previd);
        tiles.get(previd).down = tiles.get(tiles.size() - 1);
    }

    public void addTileUp(int previd, int team) {
        tiles.add(
                new Tile(tiles.size(), team, tiles.get(previd).x, tiles.get(previd).y - radius * Math.sqrt(3), radius));
        tiles.get(tiles.size() - 1).down = tiles.get(previd);
        tiles.get(previd).up = tiles.get(tiles.size() - 1);
    }

    public void addTileDownRight(int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x + radius * 3 / 2,
                tiles.get(previd).y + radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).upleft = tiles.get(previd);
        tiles.get(previd).downright = tiles.get(tiles.size() - 1);
    }

    public void addTileUpRight(int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x + radius * 3 / 2,
                tiles.get(previd).y - radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).downleft = tiles.get(previd);
        tiles.get(previd).upright = tiles.get(tiles.size() - 1);
    }

    public void addTileDownLeft(int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x - radius * 3 / 2,
                tiles.get(previd).y + radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).upright = tiles.get(previd);
        tiles.get(previd).downleft = tiles.get(tiles.size() - 1);
    }

    public void addTileUpLeft(int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x - radius * 3 / 2,
                tiles.get(previd).y - radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).downright = tiles.get(previd);
        tiles.get(previd).upleft = tiles.get(tiles.size() - 1);
    }

    public void animate() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            repaint();
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.translate(-(tiles.get(focus).x*zoomFactor-wid/2), -(tiles.get(focus).y*zoomFactor-hei/2));
        g.scale(zoomFactor, zoomFactor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).drawMe(g, i==focus);
        }
    }

    public Dimension getPreferredSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        wid = ((int) tk.getScreenSize().getWidth());
        hei = ((int) tk.getScreenSize().getHeight());
        return new Dimension(wid, hei); // Sets the size of the panel
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
        } else {
            zoomFactor /= 1.1;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getLocationOnScreen();
        double newx = (tiles.get(focus).x*zoomFactor-wid/2)+p.getX()-wid/2;
        double newy = (tiles.get(focus).y*zoomFactor-hei/2)+p.getY()-hei/2;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).polygon.contains((newx+wid/2)/zoomFactor, (newy+hei/2)/zoomFactor)) {
                if (e.isControlDown() && tiles.get(focus).p.type>0) {
                    tiles.get(focus).movePlayer(tiles.get(i));
                }
                focus = i;
                repaint();
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}