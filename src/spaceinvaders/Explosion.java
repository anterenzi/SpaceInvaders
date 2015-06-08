/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import javalib.colors.IColor;
import javalib.colors.Red;
import javalib.worldimages.*;

/**
 *
 * @author kav
 */
public class Explosion {
    String filePath;
    int lastFrame;
    int currFrame;
    int frameLife;
    int tick;
    Posn pos;
    
    public Explosion(String f, Posn p) {
        this.filePath = f;
        this.lastFrame = 90;
        this.currFrame = 0;
        this.frameLife = 2;
        this.tick = 0;
        this.pos = p;
    }
    
    // supports up to 999 animation frames
    public WorldImage expImage() {
        if(this.tick % this.frameLife == 0) {
            this.currFrame++;
        }
        this.tick++;
        if(this.currFrame <= this.lastFrame) {
            if(this.currFrame < 10) {
                return new FromFileImage(this.pos, filePath + "00" + this.currFrame + ".png");
            } else if (this.currFrame < 100) {
                return new FromFileImage(this.pos, filePath + "0" + this.currFrame + ".png");
            } else {
                return new FromFileImage(this.pos, filePath + this.currFrame + ".png");
            }
        } else {
            IColor col = new Red();
            return new DiskImage(new Posn(0,0), 0, col);
        }
    }
}
