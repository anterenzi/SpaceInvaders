
package spaceinvaders;

import javalib.worldimages.*;

public class Projectile {
    Posn pos;
    Posn velocity;
    int width;
    int height;
    String source;
    
    /**
     * Constructor for a projectile
     * 
     * @param proPos the initial position of the Projectile
     * @param proSpeed the initial speed
     * @param proWidth the initial width
     * @param proHeight the initial height
     */
    public Projectile(Posn proPos, int proSpeed, int proWidth, int proHeight, String s)
    {
        this.pos = proPos;
        this.velocity = new Posn(0, proSpeed);
        this.width = proWidth;
        this.height = proHeight;
        this.source = s;
    }
    
    /**
     * Generates the image of the projectile
     * 
     * @return a WorldImage representing the projectile
     */
    public WorldImage proImage()
    {
        WorldImage proImage = new FromFileImage(this.pos, "images/projectile.png");
        switch(this.source) {
            case "player": proImage = new FromFileImage(this.pos, "images/projectile2.png");
                break;
            case "boss": proImage = new FromFileImage(SpaceWorld.posnAdd(this.pos, new Posn(0, 15)), "images/plasmaStill.png");
                break;
        }
        return proImage;
    }
    
    /**
     * Moves the projectile by modifying it's Posn, pos
     */
    public void move()
    {
        this.pos = SpaceWorld.posnAdd(this.pos, this.velocity);
    }
    
}
