package spaceinvaders;

import javalib.worldimages.*;

public class Player {
    
    Posn pos;
    int width;
    int height;
    int health;
    Posn velocity;
    Posn right;
    Posn left;
    Posn stop;
    
    /**
     * Constructs a Player object
     * 
     * @param playerLoc the starting position
     * @param playerWidth the width
     * @param playerHeight the height
     * @param playerSpeed the initial speed
     * @param playerHealth the initial health
     */
    public Player(Posn playerLoc, int playerWidth, int playerHeight, int playerSpeed, int playerHealth)
    {
        this.pos = playerLoc;
        this.width = playerWidth;
        this.height = playerHeight;
        this.health = playerHealth;
        this.velocity = new Posn(0, 0);
        this.right = new Posn(playerSpeed, 0);
        this.left = new Posn(-playerSpeed, 0);
        this.stop = new Posn(0,0);
    }
    
    /**
     * Generates the image of the player
     * 
     * @return a WorldImage object of the player
     */
    public WorldImage playerImage() {
        return new FromFileImage(this.pos, "images/player.png");
    }
    
    /**
     * Changes the direction of the player
     * 
     * @param ke a String from onKeyEvent in SpaceWorld
     */
    public void changeDir(String ke) {
        switch (ke) {
            case "right":
                this.velocity = this.right;
                break;
            case "left":
                this.velocity = this.left;
                break;
            case "down":
                //this.velocity = this.stop;
                break;
        }
    }
    
    /**
     * Moves the player by modifying it's Posn, pos
     */
    public void move()
    {
        this.pos = SpaceWorld.posnAdd(this.pos, this.velocity);
    }
    
    /**
     * Generates a new player Projectile
     * 
     * @return a new Projectile object
     */
    public Projectile shoot() {
        Projectile pro = new Projectile(SpaceWorld.posnAdd(this.pos, new Posn(0, -10)), -5, this.width/3, this.height/3, "player");
        return pro;
    }
}
