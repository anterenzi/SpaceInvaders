package spaceinvaders;

import java.util.Random;
import javalib.worldimages.*;

public class PowerUp {
    Posn pos;
    int width = 5;
    int height = 5;
    Random rand;
    String type;
    WorldImage powImage;
    
    /**
     * Constructor for the power up
     * 
     * @param t a String representing which power up it is
     */
    public PowerUp(String t) {
        this.rand = new Random();
        int posX = 50 + this.rand.nextInt(300);
        int posY = 25 + this.rand.nextInt(175);
        this.pos = new Posn(posX, posY);
        this.type = t;
        switch(this.type) {
            case "Health": this.powImage = new FromFileImage(this.pos, "images/health.png");;
                break;
            case "Invulnerability": this.powImage = new FromFileImage(this.pos, "images/invulnerable.png");;
                break;
            case "Rapidshot": this.powImage = new FromFileImage(this.pos, "images/rapidshot.png");;
                break;  
        }
    }
    
    /**
     * Generates image of a power up
     * 
     * @return a WorldImage object of the power up
     */
    public WorldImage powerUpImage() {
        return powImage;
    }
}
