import java.awt.*;

public class MOUSE {
    int xpos, ypos;
    int xrec, yrec;
    public Rectangle rec;

    public MOUSE(){
        xrec= 10;
        yrec = 10;

        rec = new Rectangle (xpos, ypos, xrec, yrec);
    }
}
