package spaceinvaders;

import java.util.Random;
import javalib.worldimages.*;

public class Invader {
    Posn pos;
    int width;
    int height;
    int health;
    Posn velocity;
    Random rand;
    
    /**
     * Constructor method for an invader
     * 
     * @param invaderLoc
     * @param invWidth
     * @param invHeight
     * @param invSpeed
     * @param invHealth
     */
    public Invader(Posn invaderLoc, int invWidth, int invHeight, int invSpeed, int invHealth)
    {
        this.pos = invaderLoc;
        this.width = invWidth;
        this.height = invHeight;
        this.health = invHealth;
        this.rand = new Random();
        this.velocity = new Posn(0, rand.nextInt(2) + 1);
    }
    
    /**
     * Generates the invader image.
     * 
     * @return a WorldImage object representing the invader
     */
    public WorldImage invImage() {
        return new FromFileImage(this.pos, "images/invader2.png");
    }
    
    /**
     * Moves the invader by modifying it's Posn, pos
     */
    public void move() {
        this.pos = SpaceWorld.posnAdd(this.pos, this.velocity);
    }
    
    /**
     * Creates a new projectile for the SpaceWorld method to generate
     * 
     * @return a Projectile object
     */
    public Projectile shoot() {
        Projectile pro = new Projectile(this.pos, this.velocity.y + 5, this.width/2, this.height/2, "invader");
        return pro;
    }
    
    
}
