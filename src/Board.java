import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private List<Tile> tiles;
    public int focus, hover, turn, max;
    public double wid, hei, radius, zoomFactor;
    public MouseEvent dragStart;

    public Board(double r) {
        radius = r;
        zoomFactor = 1;
        Toolkit tk = Toolkit.getDefaultToolkit();
        wid = tk.getScreenSize().getWidth();
        hei = tk.getScreenSize().getHeight();
        level1();
        turn = 1;
        focus = 0;
        hover = 0;
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
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
        tiles.get(2).addPlayer(new Player(2, 2, radius));
        max = 2;
        connect();
    }

    public void dijkstra() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).id = i;
            tiles.get(i).distance = Integer.MAX_VALUE;
            tiles.get(i).parent = null;
            tiles.get(i).visited = false;
        }
        PriorityQueue<Tile> heap = new PriorityQueue<Tile>();
        tiles.get(focus).distance = 0;
        tiles.get(focus).visited = true;
        heap.addAll(tiles);
        while (!heap.isEmpty()) {
            Tile tile = heap.poll();
            if (tile.distance == Integer.MAX_VALUE)
                break;
            tile.visited = true;
            if (tile.up != null && tile.up.distance > tile.distance + 1) {
                tile.up.distance = tile.distance + 1;
                if (!tile.up.visited) {
                    heap.remove(tile.up);
                    heap.add(tile.up);
                }
                tile.up.parent = tile;
            }
            if (tile.upright != null && tile.upright.distance > tile.distance + 1) {
                tile.upright.distance = tile.distance + 1;
                if (!tile.upright.visited) {
                    heap.remove(tile.upright);
                    heap.add(tile.upright);
                }
                tile.upright.parent = tile;
            }
            if (tile.downright != null && tile.downright.distance > tile.distance + 1) {
                tile.downright.distance = tile.distance + 1;
                if (!tile.downright.visited) {
                    heap.remove(tile.downright);
                    heap.add(tile.downright);
                }
                tile.downright.parent = tile;
            }
            if (tile.down != null && tile.down.distance > tile.distance + 1) {
                tile.down.distance = tile.distance + 1;
                if (!tile.down.visited) {
                    heap.remove(tile.down);
                    heap.add(tile.down);
                }
                tile.down.parent = tile;
            }
            if (tile.downleft != null && tile.downleft.distance > tile.distance + 1) {
                tile.downleft.distance = tile.distance + 1;
                if (!tile.downleft.visited) {
                    heap.remove(tile.downleft);
                    heap.add(tile.downleft);
                }
                tile.downleft.parent = tile;
            }
            if (tile.upleft != null && tile.upleft.distance > tile.distance + 1) {
                tile.upleft.distance = tile.distance + 1;
                if (!tile.upleft.visited) {
                    heap.remove(tile.upleft);
                    heap.add(tile.upleft);
                }
                tile.upleft.parent = tile;
            }
        }
    }

    public void connect() {
        for (int n = 0; n < 100; n++) // ensure that things 100 blocks away are connected
            for (int i = 0; i < tiles.size(); i++) {
                tiles.get(i).radius = radius;
                // clockwise
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
                // counterclockwise
                if (tiles.get(i).downright != null && tiles.get(i).downright.up != null) {
                    tiles.get(i).upright = tiles.get(i).downright.up;
                }
                if (tiles.get(i).down != null && tiles.get(i).down.upright != null) {
                    tiles.get(i).downright = tiles.get(i).down.upright;
                }
                if (tiles.get(i).downleft != null && tiles.get(i).downleft.downright != null) {
                    tiles.get(i).down = tiles.get(i).downleft.downright;
                }
                if (tiles.get(i).upleft != null && tiles.get(i).upleft.down != null) {
                    tiles.get(i).downleft = tiles.get(i).upleft.down;
                }
                if (tiles.get(i).up != null && tiles.get(i).up.downleft != null) {
                    tiles.get(i).upleft = tiles.get(i).up.downleft;
                }
                if (tiles.get(i).upright != null && tiles.get(i).upright.upleft != null) {
                    tiles.get(i).up = tiles.get(i).upright.upleft;
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
        g.translate(-(tiles.get(focus).x * zoomFactor - wid / 2), -(tiles.get(focus).y * zoomFactor - hei / 2));
        g.scale(zoomFactor, zoomFactor);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        dijkstra();
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).drawMe(g, i == focus,
                    tiles.get(focus).p == null ? false : (tiles.get(focus).p.move(tiles.get(i), tiles.get(i).distance)));
        }
        g.setColor(new Color(100, 0, 0));
        
        if (turn==tiles.get(focus).team) {
            for (int i = 0; i < tiles.size(); i++)
                if (tiles.get(i).parent != null)
                if (tiles.get(focus).p.move(tiles.get(i), tiles.get(i).distance))
                    g.drawLine((int) (tiles.get(i).x), (int) (tiles.get(i).y), (int) (tiles.get(i).parent.x),
                            (int) (tiles.get(i).parent.y));
            
            Set<Integer> visited = new HashSet<Integer>();
            Tile parent = tiles.get(hover);
            g.setColor(new Color(255, 0, 0));
            while (parent.parent != null) {
                if (visited.contains(parent.id))
                    break;
                visited.add(parent.id);
                if (tiles.get(focus).p.move(parent, parent.distance))
                    g.drawLine((int) (parent.x), (int) (parent.y), (int) (parent.parent.x), (int) (parent.parent.y));
                parent = parent.parent;
            }
        }
    }

    public Dimension getPreferredSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        wid = tk.getScreenSize().getWidth();
        hei = tk.getScreenSize().getHeight();
        return new Dimension((int) (wid), (int) (hei)); // Sets the size of the panel
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

    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragStart != null) {
            Point p = e.getLocationOnScreen();
            double newx = (tiles.get(focus).x * zoomFactor - wid / 2) + p.getX() - wid / 2;
            double newy = (tiles.get(focus).y * zoomFactor - hei / 2) + p.getY() - hei / 2;
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).polygon.contains((newx + wid / 2) / zoomFactor, (newy + hei / 2) / zoomFactor)) {
                    if (tiles.get(focus).p.type > 0) {
                        dijkstra();
                        if (turn==tiles.get(focus).team && tiles.get(focus).movePlayer(tiles.get(i), tiles.get(i).distance)) {
                            boolean works = false;
                            while(!works) {
                                turn%=max;
                                turn++;
                                for (int j = 0; j < tiles.size(); j++) {
                                    if (tiles.get(j).team==turn && tiles.get(j).p!=null) {
                                        works = true;
                                        break;
                                    }
                                }
                            }
                            focus = i;
                            hover = i;
                            repaint();
                        }
                    }
                    break;
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getLocationOnScreen();
        double newx = (tiles.get(focus).x * zoomFactor - wid / 2) + p.getX() - wid / 2;
        double newy = (tiles.get(focus).y * zoomFactor - hei / 2) + p.getY() - hei / 2;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).polygon.contains((newx + wid / 2) / zoomFactor, (newy + hei / 2) / zoomFactor)) {
                dijkstra();
                focus = i;
                hover = i;
                repaint();
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        dragStart = null;
        Point p = e.getLocationOnScreen();
        double newx = (tiles.get(focus).x * zoomFactor - wid / 2) + p.getX() - wid / 2;
        double newy = (tiles.get(focus).y * zoomFactor - hei / 2) + p.getY() - hei / 2;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).polygon.contains((newx + wid / 2) / zoomFactor, (newy + hei / 2) / zoomFactor)) {
                if (tiles.get(focus).p.type > 0) {
                    dijkstra();
                    hover = i;
                    repaint();
                }
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart == null)
            dragStart = e;
        Point p = e.getLocationOnScreen();
        double newx = (tiles.get(focus).x * zoomFactor - wid / 2) + p.getX() - wid / 2;
        double newy = (tiles.get(focus).y * zoomFactor - hei / 2) + p.getY() - hei / 2;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).polygon.contains((newx + wid / 2) / zoomFactor, (newy + hei / 2) / zoomFactor)) {
                if (tiles.get(focus).p.type > 0) {
                    dijkstra();
                    hover = i;
                    repaint();
                }
                break;
            }
        }
    }
}