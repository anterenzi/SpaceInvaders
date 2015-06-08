package spaceinvaders;

import javalib.worldimages.*;

public class Boss extends Invader {
    
    /**
     * Constructor method for an invader
     * 
     * @param invaderLoc
     * @param invWidth
     * @param invHeight
     * @param invSpeed
     * @param invHealth
     */
    public Boss(Posn invaderLoc, int invWidth, int invHeight, int invSpeed, int invHealth)
    {
        super(invaderLoc, invWidth, invHeight, invSpeed, invHealth);
        this.velocity = new Posn(0, invSpeed);
    }
    
    /**
     * Generates the invader image.
     * 
     * @return a WorldImage object representing the invader
     */
    @Override
    public WorldImage invImage() {
        return new FromFileImage(this.pos, "images/boss2.png");
    }
    
    /**
     * Moves the invader by modifying it's Posn, pos
     */
    @Override
    public void move() {
        this.pos = SpaceWorld.posnAdd(this.pos, this.velocity);
    }
    
    /**
     * Creates a new projectile for the SpaceWorld method to generate
     * 
     * @return a Projectile object
     */
    @Override
    public Projectile shoot() {
        Projectile pro = new Projectile(this.pos, this.velocity.y + 5, this.width/2, this.height/2, "boss");
        return pro;
    }
    
    
}
