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
        level2();
        turn = 1;
        focus = 0;
        hover = 0;
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        dijkstra();
        findComponents();
    }

    public int[] count() {
        int[] res = new int[max + 1];
        for (int i = 0; i < tiles.size(); i++) {
            res[tiles.get(i).team]++;
        }
        return res;
    }

    public int[] countPlayers() {
        int[] res = new int[max + 1];
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).p != null)
                res[tiles.get(i).team] += tiles.get(i).p.type;
        }
        return res;
    }

    public void level1() {
        tiles = new ArrayList<Tile>();
        tiles.add(new Tile(tiles.size(), 0, 2, 0, 0, radius));
        addTileUp(0, 0, 1); // 1
        addTileUpRight(0, 2, 1); // 2
        addTileDownRight(0, 0, 1); // 3
        addTileDown(0, 1, 1); // 4
        addTileDownLeft(0, 1, 1); // 5
        addTileUpLeft(0, 1, 1); // 6
        addTileUpRight(2, 2, 1); // 7
        addTileUp(2, 2, 1); // 8
        addTileDownRight(2, 2, 1); // 9
        addTileDown(3, 0, 1); // 10
        addTileDownLeft(4, 1, 1); // 11
        addTileDown(9, 0, 1); // 12
        addTileUpRight(9, 2, 1); // 13
        addTileUpLeft(5, 1, 1); // 14
        tiles.get(4).addPlayer(new Player(1, 1, radius));
        tiles.get(2).addPlayer(new Player(2, 2, radius));
        tiles.get(13).addPlayer(new Player(2, 1, radius));
        tiles.get(14).addPlayer(new Player(1, 2, radius));
        max = 2;
        connect();
    }

    private boolean fixTeam(Tile j) {
        int[] vals = new int[max + 1];
        if (j.up != null) {
            vals[j.up.team]++;
        }
        if (j.upright != null) {
            vals[j.upright.team]++;
        }
        if (j.downright != null) {
            vals[j.downright.team]++;
        }
        if (j.down != null) {
            vals[j.down.team]++;
        }
        if (j.downleft != null) {
            vals[j.downleft.team]++;
        }
        if (j.upleft != null) {
            vals[j.upleft.team]++;
        }
        int[] x = count();
        int m = Arrays.stream(x).max().getAsInt();
        int[] fixes = new int[] { 1, 5, 10, 15, 50, 150, 100000 };
        for (int count = 0; count <= max; count++) {
            vals[count] = fixes[vals[count]] * (m - x[count] + 1);
        }
        m = (int) (Arrays.stream(vals).sum());
        double k = Math.random() * m;
        for (int n = 0; n < vals.length; n++) {
            if (k <= vals[n]) {
                j.team = n;
                if (j.p != null)
                    j.p.team = n;
                return true;
            }
        }
        return false;
    }

    public void level2() {
        long startTime = System.nanoTime();
        long endTime;
        int numTiles = 100;
        max = 2;
        tiles = new ArrayList<Tile>();
        tiles.add(new Tile(tiles.size(), 0, 2, 0, 0, radius));
        for (int i = 1; i <= max; i++) {
            if (i % 2 == 0)
                addTileDown(tiles.size() - 1, i, 1);
            else
                addTileUp(tiles.size() - 1, i, 1);
        }
        while (tiles.size() < numTiles) {
            int i = (int) (Math.random() * tiles.size());
            int j = (int) (Math.random() * 6);
            switch (j) {
            case 0: {
                if (tiles.get(i).up == null) {
                    addTileUp(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            case 1: {
                if (tiles.get(i).upright == null) {
                    addTileUpRight(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            case 2: {
                if (tiles.get(i).downright == null) {
                    addTileDownRight(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            case 3: {
                if (tiles.get(i).down == null) {
                    addTileDown(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            case 4: {
                if (tiles.get(i).downleft == null) {
                    addTileDownLeft(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            case 5: {
                if (tiles.get(i).upleft == null) {
                    addTileUpLeft(i, tiles.get(i).team, (int) (Math.pow(Math.random(), 8) * 5 + 1));
                    fixTeam(tiles.get(tiles.size() - 1));
                }
            }
                break;
            }
            connect();
        }
        endTime = System.nanoTime();
        System.out.println("start: " + (endTime - startTime));
        int total = numTiles;
        for (int i = 0; i < total; i++) {
            int[] vals = countPlayers();
            total = numTiles - Arrays.stream(vals).sum();
            // System.out.println(total-i);
            int[] y = count();
            for (int j = 0; j <= max; j++) {
                vals[j] = vals[j] * 4 + y[j];
            }
            int m = Arrays.stream(vals).max().getAsInt();
            double k = Math.random() * m;
            for (int j = 0; j <= max; j++) {
                vals[j] *= m - vals[j] + 1;
            }

            m = Arrays.stream(vals).sum();
            k = Math.random() * m;
            vals[0] = 0;
            for (int n = 1; n <= max; n++) {
                if (k <= vals[n]) {
                    int j = 0;
                    while (n != tiles.get(j).team) {
                        j = (int) (Math.random() * tiles.size());
                    }
                    tiles.get(j).p = new Player(tiles.get(j).team, (int) (Math.pow(Math.random(), 4) * 4 + 1), radius);
                    break;
                }
            }
        }

        for (int k = 0; k < 10; k++) {
            List<Integer> solution = new ArrayList<>();
            for (int i = 0; i < tiles.size(); i++) {
                solution.add(i);
            }
            Collections.shuffle(solution);
            for (int i = 0; i < solution.size(); i++) {
                fixTeam(tiles.get(solution.get(i)));
            }
        }
        for (int k = 0; k < 10; k++) {
            for (int i = 0; i < tiles.size(); i++) {
                tiles.get(i).check();
            }
        }
        endTime = System.nanoTime();
        System.out.println("end: " + (endTime - startTime));
    }

    public void floodFill(int newComponent) {
        boolean visited = true;
        do {
            visited = false;
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).component != -2)
                    continue;
                visited = true;
                tiles.get(i).component = newComponent;
                for (int j = 0; j < tiles.size(); j++) {
                    if (tiles.get(j).component == -1 && tiles.get(i).isConnected(tiles.get(j))
                            && tiles.get(i).team == tiles.get(j).team) {
                        tiles.get(j).component = -2;
                    }
                }
            }
        } while (visited);
    }

    public void findComponents() {
        int numComponents = 0;
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).component = -1;
        }
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).component == -1) {
                tiles.get(i).component = -2;
                floodFill(numComponents++);
            }
        }
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
            if (tile.up != null && tile.up.distance > tile.distance + tile.up.difficulty) {
                tile.up.distance = tile.distance + tile.up.difficulty;
                if (!tile.up.visited) {
                    heap.remove(tile.up);
                    heap.add(tile.up);
                }
                tile.up.parent = tile;
            }
            if (tile.upright != null && tile.upright.distance > tile.distance + tile.upright.difficulty) {
                tile.upright.distance = tile.distance + tile.upright.difficulty;
                if (!tile.upright.visited) {
                    heap.remove(tile.upright);
                    heap.add(tile.upright);
                }
                tile.upright.parent = tile;
            }
            if (tile.downright != null && tile.downright.distance > tile.distance + tile.downright.difficulty) {
                tile.downright.distance = tile.distance + tile.downright.difficulty;
                if (!tile.downright.visited) {
                    heap.remove(tile.downright);
                    heap.add(tile.downright);
                }
                tile.downright.parent = tile;
            }
            if (tile.down != null && tile.down.distance > tile.distance + tile.down.difficulty) {
                tile.down.distance = tile.distance + tile.down.difficulty;
                if (!tile.down.visited) {
                    heap.remove(tile.down);
                    heap.add(tile.down);
                }
                tile.down.parent = tile;
            }
            if (tile.downleft != null && tile.downleft.distance > tile.distance + tile.downleft.difficulty) {
                tile.downleft.distance = tile.distance + tile.downleft.difficulty;
                if (!tile.downleft.visited) {
                    heap.remove(tile.downleft);
                    heap.add(tile.downleft);
                }
                tile.downleft.parent = tile;
            }
            if (tile.upleft != null && tile.upleft.distance > tile.distance + tile.upleft.difficulty) {
                tile.upleft.distance = tile.distance + tile.upleft.difficulty;
                if (!tile.upleft.visited) {
                    heap.remove(tile.upleft);
                    heap.add(tile.upleft);
                }
                tile.upleft.parent = tile;
            }
        }
    }

    public void connect() {
        // for (int n = 0; n < 1; n++) // ensure that things 10 blocks away are
        // connected
        for (int i = 0; i < tiles.size(); i++) {
            connect(tiles.get(i), new HashSet<Integer>());
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

    private void check(Tile b, Tile c, Tile i, Set<Integer> j) {
        if (c.id != b.id) {
            c = b;
            j.add(i.id);
            if (!j.contains(c.id))
                connect(c, j);
            if (!j.contains(b.id))
                connect(b, j);
        }
    }

    private void connect(Tile i, Set<Integer> j) {
        for (int n = 0; n < 10; n++) {// ensure that things 10 blocks away are connected
            i.radius = radius;
            // clockwise
            if (i.up != null && i.up.downright != null && i.upright != null)
                check(i.up.downright, i.upright, i, j);
            if (i.upright != null && i.upright.down != null && i.downright != null)
                check(i.upright.down, i.downright, i, j);
            if (i.downright != null && i.downright.downleft != null && i.down != null)
                check(i.downright.downleft, i.down, i, j);
            if (i.down != null && i.down.upleft != null && i.downleft != null)
                check(i.down.upleft, i.downleft, i, j);
            if (i.downleft != null && i.downleft.up != null && i.upleft != null)
                check(i.downleft.up, i.upleft, i, j);
            if (i.upleft != null && i.upleft.upright != null && i.up != null)
                check(i.upleft.upright, i.up, i, j);
            // counterclockwise
            if (i.downright != null && i.downright.up != null && i.upright != null)
                check(i.downright.up, i.upright, i, j);
            if (i.down != null && i.down.upright != null && i.downright != null)
                check(i.down.upright, i.downright, i, j);
            if (i.downleft != null && i.downleft.downright != null && i.down != null)
                check(i.downleft.downright, i.down, i, j);
            if (i.upleft != null && i.upleft.down != null && i.downleft != null)
                check(i.upleft.down, i.downleft, i, j);
            if (i.up != null && i.up.downleft != null && i.upleft != null)
                check(i.up.downleft, i.upleft, i, j);
            if (i.upright != null && i.upright.upleft != null && i.up != null)
                check(i.upright.upleft, i.up, i, j);
        }
    }

    public void addTileDown(int previd, int team, int type) {
        if (tiles.get(previd).down != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x, tiles.get(previd).y + radius * Math.sqrt(3),
                radius));
        tiles.get(tiles.size() - 1).up = tiles.get(previd);
        tiles.get(previd).down = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
    }

    public void addTileUp(int previd, int team, int type) {
        if (tiles.get(previd).up != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x, tiles.get(previd).y - radius * Math.sqrt(3),
                radius));
        tiles.get(tiles.size() - 1).down = tiles.get(previd);
        tiles.get(previd).up = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
    }

    public void addTileDownRight(int previd, int team, int type) {
        if (tiles.get(previd).upright != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x + radius * 3 / 2,
                tiles.get(previd).y + radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).upleft = tiles.get(previd);
        tiles.get(previd).downright = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
    }

    public void addTileUpRight(int previd, int team, int type) {
        if (tiles.get(previd).upright != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x + radius * 3 / 2,
                tiles.get(previd).y - radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).downleft = tiles.get(previd);
        tiles.get(previd).upright = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
    }

    public void addTileDownLeft(int previd, int team, int type) {
        if (tiles.get(previd).downleft != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x - radius * 3 / 2,
                tiles.get(previd).y + radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).upright = tiles.get(previd);
        tiles.get(previd).downleft = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
    }

    public void addTileUpLeft(int previd, int team, int type) {
        if (tiles.get(previd).upleft != null)
            return;
        tiles.add(new Tile(tiles.size(), team, type, tiles.get(previd).x - radius * 3 / 2,
                tiles.get(previd).y - radius * Math.sqrt(3) / 2, radius));
        tiles.get(tiles.size() - 1).downright = tiles.get(previd);
        tiles.get(previd).upleft = tiles.get(tiles.size() - 1);
        connect(tiles.get(tiles.size() - 1), new HashSet<Integer>());
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
        g.getClipBounds();
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
       /* if (tiles.size() > 100) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        }*/
        /*
         * findComponents(); int[] components = new int[tiles.size()]; for (int i = 0; i
         * < tiles.size(); i++) { components[tiles.get(i).component] +=
         * tiles.get(i).value; }
         */
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).drawMe(g, i == focus, tiles.get(focus).p == null ? false
                    : (tiles.get(focus).p.move(tiles.get(i), tiles.get(i).distance)));
        }

        g.setColor(new Color(100, 0, 0));

        if (turn == tiles.get(focus).team) {
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
            loop: for (int i = 0; i < tiles.size(); i++) {
                if (i == focus)
                    continue;
                if (tiles.get(i).polygon.contains((newx + wid / 2) / zoomFactor, (newy + hei / 2) / zoomFactor)) {
                    if (tiles.get(focus).p.type > 0) {
                        if (turn == tiles.get(focus).team && tiles.get(focus).p.played == false
                                && tiles.get(focus).movePlayer(tiles.get(i), tiles.get(i).distance)) {
                            boolean works = false;
                            tiles.get(focus).p.played = true;
                            for (int j = 0; j < tiles.size(); j++) {
                                if (tiles.get(j).p != null && tiles.get(j).team == tiles.get(focus).team
                                        && tiles.get(j).p.type > 0 && tiles.get(j).p.played) {
                                    focus = i;
                                    hover = i;
                                    dijkstra();
                                    repaint();
                                    break loop;
                                }
                            }
                            while (!works) {
                                turn %= max;
                                turn++;
                                boolean hasp = false;
                                for (int j = 0; j < tiles.size(); j++) {
                                    if (tiles.get(j).team == turn) {
                                        if (tiles.get(j).p != null) {
                                            works = true;
                                            break;
                                        } else {
                                            hasp = true;
                                        }
                                    }
                                }
                                if (!hasp)
                                    works = true;
                            }
                            focus = i;
                            hover = i;
                            dijkstra();
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
                focus = i;
                hover = i;
                dijkstra();
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
                    hover = i;
                    dijkstra();
                    repaint();
                }
                break;
            }
        }
    }
}