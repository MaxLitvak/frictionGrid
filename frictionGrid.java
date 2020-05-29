// Use pathFinder for a fast 3 directional move method on line 435
// USe pathFinder2 for a slower 8 directional move method on line 435 (sometimes encounters error)
// Change grid size on line 82


import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class frictionGrid implements Runnable, MouseListener, MouseMotionListener {

    JFrame frame;
    Canvas canvas;
    BufferStrategy bufferStrategy;

    final int WIDTH = 800;
    final int HEIGHT = 800;

    public Image mario;
    public HashMap<Integer, Integer> placeX;
    public HashMap<Integer, Integer> placeY;
    public int random[];
    public MARIO all[];
    public MOUSE mouse;
    public int origonalMouseX;
    public int origonalMouseY;
    public boolean mouseHighlight = false, mouseRelease = false;
    public int mouseHX, mouseHY;
    public BOXES[][] allBoxes;
    public RESET reset;
    public int boxX, boxY;
    public int bestResistance = 10000;
    public resetColors RESETCOLORS;
    public int count;
    public boolean u = true;

    public static void main(String[] args) {
        frictionGrid ex = new frictionGrid();
        new Thread(ex).start();
    }

    public frictionGrid() {
        frame = new JFrame("grid");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();

        mario = Toolkit.getDefaultToolkit().getImage("mario.png");
        placeX = new HashMap<Integer, Integer>();
        placeY = new HashMap<Integer, Integer>();

        //Change the size of the array to change the size of the grid
        allBoxes = new BOXES[20][20];
        for (int a = 0; a < allBoxes.length; a++) {
            for (int b = 0; b < allBoxes.length; b++) {
                allBoxes[a][b] = new BOXES(800 / allBoxes.length * a, 800 / allBoxes.length * b, allBoxes.length);
            }
        }

        for (int x = 0; x < allBoxes.length; x++) {
            for (int a = 0; a < allBoxes.length; a++) {
                placeX.put(x, allBoxes[x][a].centerX);
                placeY.put(a, allBoxes[x][a].centerY);
            }
        }

        random = new int[12];
        for (int x = 0; x < random.length; x++) {
            random[x] = (int) (Math.random() * allBoxes.length);
            for (int a = 0; a < random.length; a++) {
                while (random[x] == random[a] && a != x) {
                    random[x] = (int) (Math.random() * allBoxes.length);
                }
            }
        }
        mouse = new MOUSE();

        all = new MARIO[6];
        for (int x = 0; x < all.length; x++) {
            all[x] = new MARIO(placeX.get(random[x]), placeY.get(random[x + 1]), random[x], allBoxes[1][1].xrec - allBoxes[1][1].xrec / 7);
        }

        reset = new RESET();
        RESETCOLORS = new resetColors();
    }

    public void run() {

        while (true) {
            render();
            moveEverything();

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {

            }
        }
    }

    public void moveEverything() {
        for (int a = 0; a < all.length; a++) {
            if (all[a].move == true) {
                ArrayList<Integer> hi = new ArrayList<Integer>();
                int lastA = 0;
                int i = 0;
                for (int x = 0; x < all[a].path.length(); x++) {
                    if (all[a].path.substring(x, x + 1).equals("a") && lastA == 0) {
                        hi.add(0, Integer.parseInt(all[a].path.substring(0, x)));
                        i++;
                        lastA = x;
                    } else {
                        if (all[a].path.substring(x, x + 1).equals("a")) {
                            hi.add(i, Integer.parseInt(all[a].path.substring(lastA + 1, x)));
                            i++;
                            lastA = x;
                        }
                    }
                }
                for (int x = 0; x < hi.size(); x += 2) {
                    while (all[a].xpos != allBoxes[hi.get(x)][hi.get(x + 1)].centerX || all[a].ypos != allBoxes[hi.get(x)][hi.get(x + 1)].centerY) {
                        count++;
                        if (count == 2) {//(int) allBoxes.length / 25 + 1) {
                            all[a].move(allBoxes[hi.get(x)][hi.get(x + 1)].centerX, allBoxes[hi.get(x)][hi.get(x + 1)].centerY);
                            count = 0;
                        }
                        render();
                    }
                }
                all[a].move = false;
            }
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        if (u == true) {
            // hi();
            u = false;
        }
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 800, 800);
        for (int x = 0; x < all.length; x++) {
            all[x].x = all[x].xpos / (800 / allBoxes.length);
            all[x].y = all[x].ypos / (800 / allBoxes.length);
        }

        for (int x = 0; x < allBoxes.length; x++) {
            for (int y = 0; y < allBoxes.length; y++) {
                Color h = new Color(allBoxes[x][y].color, allBoxes[x][y].color, allBoxes[x][y].color);
                g.setColor(h);
                g.fillRect(allBoxes[x][y].xpos, allBoxes[x][y].ypos, allBoxes[x][y].xrec, allBoxes[x][y].yrec);
            }

        }
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.setColor(Color.RED);
        g.drawString("RESET", 650, 35);
        g.drawString("ResetColors", 375, 35);

        Color bLue = new Color(.202f, .58f, .92f, 0.5F);
        g.setColor(bLue);

        if (mouseHighlight == true) {
            if (origonalMouseX > mouse.xpos && origonalMouseY > mouse.ypos) {
                g.fillRect(mouse.xpos, mouse.ypos, origonalMouseX - mouse.xpos, origonalMouseY - mouse.ypos);
            }
            if (origonalMouseX < mouse.xpos && origonalMouseY > mouse.ypos) {
                g.fillRect(origonalMouseX, mouse.ypos, mouse.xpos - origonalMouseX, origonalMouseY - mouse.ypos);
            }
            if (origonalMouseX < mouse.xpos && origonalMouseY < mouse.ypos) {
                g.fillRect(origonalMouseX, origonalMouseY, mouse.xpos - origonalMouseX, mouse.ypos - origonalMouseY);
            }
            if (origonalMouseX > mouse.xpos && origonalMouseY < mouse.ypos) {
                g.fillRect(mouse.xpos, origonalMouseY, origonalMouseX - mouse.xpos, mouse.ypos - origonalMouseY);
            }
        }
        if (mouseRelease == true) {
            if (origonalMouseX > mouseHX && origonalMouseY > mouseHY) {
                g.fillRect(mouseHX, mouseHY, origonalMouseX - mouseHX, origonalMouseY - mouseHY);
            }
            if (origonalMouseX < mouseHX && origonalMouseY > mouseHY) {
                g.fillRect(origonalMouseX, mouseHY, mouseHX - origonalMouseX, origonalMouseY - mouseHY);
            }
            if (origonalMouseX < mouseHX && origonalMouseY < mouseHY) {
                g.fillRect(origonalMouseX, origonalMouseY, mouseHX - origonalMouseX, mouseHY - origonalMouseY);
            }
            if (origonalMouseX > mouseHX && origonalMouseY < mouseHY) {
                g.fillRect(mouseHX, origonalMouseY, origonalMouseX - mouseHX, mouseHY - origonalMouseY);
            }
        }
        for (int x = 0; x < all.length; x++) {
            g.drawImage(mario, all[x].xpos, all[x].ypos, all[x].xrec, all[x].yrec, null);
            all[x].rec = new Rectangle(all[x].xpos, all[x].ypos, all[x].xrec, all[x].yrec);
        }

        for (int x = 0; x < all.length; x++) {
            if (mouse.rec.intersects(all[x].rec)) {
                all[x].mouseOn = true;
            } else {
                all[x].mouseOn = false;
            }
        }

        g.dispose();
        bufferStrategy.show();
    }

    public void pathFinder(int x, int y, String path, int resistance, int a) {
        path = path + Integer.toString(x) + "a" + Integer.toString(y) + "a";
        resistance = resistance + allBoxes[x][y].resistance;
        allBoxes[x][y].currentResistance = resistance;
        ArrayList<Integer> hi = new ArrayList<Integer>();
        boolean go = false;
        if (resistance < allBoxes[x][y].bestPath) {
            allBoxes[x][y].bestPath = resistance;
            go = true;
        }
        if (x == boxX && y == boxY) {
            if (resistance < bestResistance) {
                bestResistance = resistance;
                all[a].path = path;
            }
        } else {
            if (x == boxX || y == boxY) {
                if (x == boxX) {
                    if (go == true && resistance <= bestResistance) {
                        pathFinder(x, y + 1, path, resistance, a);
                        allBoxes[x][y].bestPath = resistance;
                    }
                }
                if (y == boxY) {
                    if (go == true && resistance <= bestResistance) {
                        pathFinder(x + 1, y, path, resistance, a);
                        allBoxes[x][y].bestPath = resistance;
                    }
                }
            } else {
                if (go == true && resistance <= bestResistance) {
                    pathFinder(x + 1, y + 1, path, resistance, a);
                    allBoxes[x][y].bestPath = resistance;
                }
                if (go == true && resistance <= bestResistance) {
                    pathFinder(x, y + 1, path, resistance, a);
                    allBoxes[x][y].bestPath = resistance;
                }
                if (go == true && resistance <= bestResistance) {
                    pathFinder(x + 1, y, path, resistance, a);
                    allBoxes[x][y].bestPath = resistance;
                }
            }
        }
    }

    public void pathFinder2(int x, int y, String path, int resistance, int a) {
        path = path + Integer.toString(x) + "a" + Integer.toString(y) + "a";
        resistance = resistance + allBoxes[x][y].resistance;
        for (int j = 0; j < allBoxes.length; j++) {
            for (int k = 0; k < allBoxes.length; k++) {
                if (allBoxes[j][k].record == true && allBoxes[j][k].on == false) {
                    if (resistance - allBoxes[j][k].resistanceFrom < allBoxes[j][k].bestPathResistance2 && x == boxX && y == boxY) {
                        allBoxes[j][k].bestPathResistance2 = resistance - allBoxes[j][k].resistanceFrom;
                        // System.out.println(path.substring(allBoxes[j][k].from,path.length()-2));
                        allBoxes[j][k].bestPath2 = path.substring(allBoxes[j][k].from,path.length()-2);
                    }
                    if (allBoxes[j][k].done == true){
                        allBoxes[j][k].on = true;
                        allBoxes[j][k].record = false;
                    }
                }
            }
        }
        if (allBoxes[x][y].record == false && allBoxes[x][y].on == false && x != boxX && y != boxY) {
            allBoxes[x][y].record = true;
            allBoxes[x][y].from = path.length();
            allBoxes[x][y].resistanceFrom = resistance;
        }
        boolean diagonalDownRight = true;
        boolean diagonalDownLeft = true;
        boolean down = true;
        boolean right = true;
        boolean diagonalUpLeft = true;
        boolean diagonalUpRight = true;
        boolean up = true;
        boolean left = true;
        boolean go = false;
        if (resistance < allBoxes[x][y].bestPath) {
            allBoxes[x][y].bestPath = resistance;
            go = true;
        }
        if (x == boxX && y == boxY) {
            if (resistance < bestResistance) {
                bestResistance = resistance;
                all[a].path = path;
            }
        } else {
                for (int z = 0; z < path.length() - 2; z += 2) {
                    if (path.substring(z, z + 2).equals(Integer.toString(x + 1) + Integer.toString(y + 1))) {
                        diagonalDownRight = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x) + Integer.toString(y + 1))) {
                        down = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x + 1) + Integer.toString(y))) {
                        right = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x - 1) + Integer.toString(y - 1))) {
                        diagonalUpLeft = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x) + Integer.toString(y - 1))) {
                        up = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x - 1) + Integer.toString(y))) {
                        left = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x - 1) + Integer.toString(y + 1))) {
                        diagonalDownLeft = false;
                    }
                    if (path.substring(z, z + 2).equals(Integer.toString(x + 1) + Integer.toString(y - 1))) {
                        diagonalUpRight = false;
                    }
                }
                if (diagonalDownRight == true && y != allBoxes.length - 1 && x != allBoxes.length - 1 && go == true && resistance <= bestResistance) {
                    pathFinder2(x + 1, y + 1, path, resistance, a);
                }
                if (down == true && y != allBoxes.length - 1 && go == true && resistance <= bestResistance) {
                    pathFinder2(x, y + 1, path, resistance, a);
                }
                if (right == true && x != allBoxes.length - 1 && go == true && resistance <= bestResistance) {
                    pathFinder2(x + 1, y, path, resistance, a);
                }
                if (diagonalUpLeft == true && y != 0 && x != 0 && go == true && resistance <= bestResistance) {
                    pathFinder2(x - 1, y - 1, path, resistance, a);
                }
                if (diagonalDownLeft == true && x != 0 && y != allBoxes.length - 1 && go == true && resistance <= bestResistance) {
                    pathFinder2(x - 1, y + 1, path, resistance, a);
                }
                if (diagonalUpRight == true && x != allBoxes.length - 1 && y != 0 && go == true && resistance <= bestResistance) {
                    pathFinder2(x + 1, y - 1, path, resistance, a);
                }
                if (up == true && y != 0 && go == true && resistance <= bestResistance) {
                    pathFinder2(x, y - 1, path, resistance, a);
                }
                if (left == true && x != 0 && go == true && resistance <= bestResistance) {
                    pathFinder2(x - 1, y, path, resistance, a);
                }
                allBoxes[x][y].done = true;
            }
    }

    void resetColors() {
        for (int x = 0; x < allBoxes.length; x++) {
            for (int a = 0; a < allBoxes.length; a++) {
                allBoxes[x][a].color = (int) (Math.random() * 50) * 5;
                allBoxes[x][a].resistance = 50 - allBoxes[x][a].color / 5;
            }
        }
    }

    void Reset() {
        for (int x = 0; x < random.length; x++) {
            random[x] = (int) (Math.random() * allBoxes.length);
            for (int a = 0; a < random.length; a++) {
                while (random[x] == random[a] && a != x) {
                    random[x] = (int) (Math.random() * allBoxes.length);
                }
            }
        }
        for (int x = 0; x < all.length; x++) {
            all[x].xpos = placeX.get(random[x]);
            if (x == all.length) {
                all[x].ypos = placeY.get(random[x - 1]);
            } else {
                all[x].ypos = placeY.get(random[x + 1]);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println(e.getX());
        //System.out.println(e.getY());
        mouse.xpos = e.getX();
        mouse.ypos = e.getY();
        mouse.rec = new Rectangle(mouse.xpos, mouse.ypos, mouse.xrec, mouse.yrec);
        for (int x = 0; x < all.length; x++) {
            if (all[x].clicked == 1) {
                all[x].clicked++;
            }
        }
        for (int x = 0; x < all.length; x++) {
            if (all[x].mouseOn == true) {
                all[x].clicked++;
            }
        }

        for (int a = 0; a < all.length; a++) {
            if (all[a].clicked >= 2) {
                for (int x = 0; x < allBoxes.length; x++) {
                    for (int y = 0; y < allBoxes.length; y++) {
                        if (mouse.rec.intersects(allBoxes[x][y].rec)) {
                            boxX = x;
                            boxY = y;
                            if (boxX >= all[a].x && boxY >= all[a].y) { // comment out for 8 directional
                            pathFinder(all[a].x, all[a].y, "", 0, a);
                            // pathFinder2(all[a].x, all[a].y, "", 0, a);
                            System.out.println("final  " + all[a].path + "    " + bestResistance);
                            for (int u = 0; u < allBoxes.length; u++) {
                                for (int h = 0; h < allBoxes.length; h++) {
                                    allBoxes[u][h].bestPath = 1000000;
                                }
                            }
                            all[a].move = true;
                            bestResistance = 1000000;
                            } // comment out for 8 directional
                            all[a].clicked = 0;
                        }
                    }
                }
            }
        }

        if (mouse.rec.intersects(reset.rec)) {
            Reset();
        }

        if (mouse.rec.intersects(RESETCOLORS.rec)) {
            resetColors();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        origonalMouseX = mouse.xpos;
        origonalMouseY = mouse.ypos;
        if (mouseRelease == true) {
            mouseRelease = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseHX = mouse.xpos;
        mouseHY = mouse.ypos;
        mouseRelease = true;
        mouseHighlight = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseHighlight = true;
        mouse.xpos = e.getX();
        mouse.ypos = e.getY();
        if (origonalMouseX > mouse.xpos && origonalMouseY > mouse.ypos) {
            mouse.rec = new Rectangle(mouse.xpos, mouse.ypos, origonalMouseX - mouse.xpos, origonalMouseY - mouse.ypos);
        }
        if (origonalMouseX < mouse.xpos && origonalMouseY > mouse.ypos) {
            mouse.rec = new Rectangle(origonalMouseX, mouse.ypos, mouse.xpos - origonalMouseX, origonalMouseY - mouse.ypos);
        }
        if (origonalMouseX < mouse.xpos && origonalMouseY < mouse.ypos) {
            mouse.rec = new Rectangle(origonalMouseX, origonalMouseY, mouse.xpos - origonalMouseX, mouse.ypos - origonalMouseY);
        }
        if (origonalMouseX > mouse.xpos && origonalMouseY < mouse.ypos) {
            mouse.rec = new Rectangle(mouse.xpos, origonalMouseY, origonalMouseX - mouse.xpos, mouse.ypos - origonalMouseY);
        }
        for (int x = 0; x < all.length; x++) {
            if (all[x].mouseOn == true) {
                all[x].clicked++;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.xpos = e.getX();
        mouse.ypos = e.getY();
        mouse.rec = new Rectangle(mouse.xpos, mouse.ypos, mouse.xrec, mouse.yrec);
    }
}

