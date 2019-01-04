import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener {
    private List<Tile> tiles;
    public int wid, hei;
    public double radius;
    public Board (double r) {
        radius = r;
        System.out.println(r);
        System.out.println(radius);
        System.out.println(this.radius);
        Toolkit tk = Toolkit.getDefaultToolkit();
        wid = ((int) tk.getScreenSize().getWidth());
        hei = ((int) tk.getScreenSize().getHeight());
        level1();
    }
    public void level1 () {
        tiles = new ArrayList<Tile>();
        tiles.add(new Tile(tiles.size(), 0, 100, 100, radius));
        addTileUp(0, 0);
        addTileUpRight(0, 2);
        addTileDownRight(0, 0);
        addTileDown(0, 1);
        addTileDownLeft(0, 1);
        addTileUpLeft(0, 1);
        connect();
    }
    public void connect() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).radius=this.radius;
            if (tiles.get(i).up!=null && tiles.get(i).up.downright!=null ) {
                tiles.get(i).upright = tiles.get(i).up.downright;
            }
            if (tiles.get(i).upright!=null && tiles.get(i).upright.down!=null ) {
                tiles.get(i).downright = tiles.get(i).upright.down;
            }
            if (tiles.get(i).downright!=null && tiles.get(i).downright.downleft!=null ) {
                tiles.get(i).down = tiles.get(i).downright.downleft;
            }
            if (tiles.get(i).down!=null && tiles.get(i).down.upleft!=null ) {
                tiles.get(i).downleft = tiles.get(i).down.upleft;
            }
            if (tiles.get(i).downleft!=null && tiles.get(i).downleft.up!=null ) {
                tiles.get(i).upleft = tiles.get(i).downleft.up;
            }
            if (tiles.get(i).upleft!=null && tiles.get(i).upleft.upright!=null ) {
                tiles.get(i).up = tiles.get(i).upleft.upright;
            }
        }
    }
    public void addTileDown (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x, tiles.get(previd).y+radius*Math.sqrt(3), radius));
        tiles.get(tiles.size()-1).up=tiles.get(previd);
        tiles.get(previd).down=tiles.get(tiles.size()-1);
    }
    public void addTileUp (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x, tiles.get(previd).y-radius*Math.sqrt(3), radius));
        tiles.get(tiles.size()-1).down=tiles.get(previd);
        tiles.get(previd).up=tiles.get(tiles.size()-1);
    }
    public void addTileDownRight (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x+radius*3/2, tiles.get(previd).y+radius*Math.sqrt(3)/2, radius));
        tiles.get(tiles.size()-1).upleft=tiles.get(previd);
        tiles.get(previd).downright=tiles.get(tiles.size()-1);
    }
    public void addTileUpRight (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x+radius*3/2, tiles.get(previd).y-radius*Math.sqrt(3)/2, radius));
        tiles.get(tiles.size()-1).downleft=tiles.get(previd);
        tiles.get(previd).upright=tiles.get(tiles.size()-1);
    }
    public void addTileDownLeft (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x-radius*3/2, tiles.get(previd).y+radius*Math.sqrt(3)/2, radius));
        tiles.get(tiles.size()-1).upright=tiles.get(previd);
        tiles.get(previd).downleft=tiles.get(tiles.size()-1);
    }
    public void addTileUpLeft (int previd, int team) {
        tiles.add(new Tile(tiles.size(), team, tiles.get(previd).x-radius*3/2, tiles.get(previd).y-radius*Math.sqrt(3)/2, radius));
        tiles.get(tiles.size()-1).downright=tiles.get(previd);
        tiles.get(previd).upleft=tiles.get(tiles.size()-1);
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
        //g.scale(1.0 * wid / 800, 1.0 * hei / 600);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).drawMe(g);
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

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}