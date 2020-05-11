import java.awt.*;

public class BOXES {

    public Rectangle rec;
    public int xpos, ypos;
    public int xrec, yrec;
    public int centerX, centerY;
    public int color;
    public int resistance;
    public int bestPath = 1000000000;
    public int a;
    public int bestPathResistance2 = 1000000000;
    public String bestPath2;
    public int currentResistance;
    public boolean record;
    public int from = 0;
    public int resistanceFrom;
    public boolean on = false;
    public boolean done = false;

    public BOXES(int txpos, int typos, int ta){
        xpos = txpos-1;
        ypos = typos-1;
        a = ta;

        xrec = 800/a-1;
        yrec = 800/a-1;

        centerX = xpos+xrec/10;
        centerY = ypos+yrec/10;

        color = (int) (Math.random()*50)*5;
        resistance = 50-color/5;

        rec = new Rectangle(xpos,ypos,xrec,yrec);
    }
}
