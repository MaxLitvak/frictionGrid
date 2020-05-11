import java.awt.*;

public class MARIO {

    public int xpos;
    public int ypos;
    public int x, y;
    public int xrec, yrec;
    public Rectangle rec;
    public boolean mouseOn = false;
    public int clicked;
    public boolean horizontal = false, vertical = false;
    public int grid;
    public String path;
    public boolean move;

    public MARIO(int txpos, int typos, int tGrid, int xyrec) {
        xpos = txpos;
        ypos = typos;
        xrec = xyrec;
        yrec = xyrec;

        x = (int) ((xpos - 30) / 162);
        y = (int) ((ypos - 40) / 153);

        grid = tGrid;

        rec = new Rectangle(xpos, ypos, xrec, yrec);
    }

    public void move(int XPOS, int YPOS) {
        if (XPOS > xpos) {
            xpos++;
        }
        if (YPOS > ypos) {
            ypos++;
        }
        if (XPOS < xpos) {
            xpos--;
        }
        if (YPOS < ypos) {
            ypos--;
        }
    }
}
