package spaceinvaders;

import java.awt.Color;
import java.util.*;
import javalib.colors.*;
import javalib.impworld.*;
import javalib.worldimages.*;

/**
 *
 * ~~~~~SPACE INVADERS~~~~~
 * A modern take on the classic Space Invaders game.
 * Alien spaceships are invading Earth!
 * Don't let them pass, or you'll lose health.
 * You'll also lose health if they shoot you.
 * Every 15 (default) kills, you advance to the next level!
 * And your health becomes 10 + whatever level you're on!
 * There are three different power ups to shoot!
 * Healing, temporary invulnerability, and temporary rapid shot.
 * Every 5 levels, invaders gain +1 to their health.
 * The spawn rate of the invaders also increases every level in cycles of 5.
 * This means that every time the health increases, the spawn rate reduces.
 * Try to see how far you can get!
 * 
 * -- Andrew Terenzi & Kavan McEachern
 * 
 * 
 * Image sources:
 * 
 * http://jootix.com/upload/DesktopWallpapers/storage/planet-earth-in-space-planet-earth-space.jpg
 * http://images3.wikia.nocookie.net/__cb20111125233603/cso/images/thumb/e/ec/Oberon_bomb.png/640px-Oberon_bomb.png
 * 
 * Sound sources:
 * 
 * Animation sources:
 * 
 * http://gifsoup.com/webroot/animatedgifs/582581_o.gif
 * 
 * 
 */
public class SpaceWorld extends World {
    int width;
    int height;
    static int defaultShootInterval = 65;
    int shootInterval = defaultShootInterval;
    int totalPowerUps = 0;
    int invulnerabilityAge = 0;
    int rapidshotAge = 0;
    int killCount = 0;
    int quota = 20;
    int shotCount = 0;
    int shotCountLevel = 0;
    int shotsMade = 0;
    int shotsMadeLevel = 0;
    int gameAcc = 0;
    int levelAcc = 0;
    int level = 0;
    int spawnChance = ((this.level % 5) * 3) + 5;
    int invShootChance = 100;
    int pauseTick = 100;
    boolean pause = true;
    boolean playerInvulnerable = false;
    boolean bossAlive = false;
    public Player player;
    public ArrayList<Invader> invaders;
    public ArrayList<Projectile> invPros;
    public ArrayList<Projectile> playerPros;
    public ArrayList<PowerUp> powerUps;
    public ArrayList<Explosion> explosions;
    Random rand;
    SoundPlayer sp;
    
    /**
     * Constructor for the Space World
     * 
     * @param thePlayer
     * @param w the width of the game
     * @param h the height of the game
     */
    public SpaceWorld(Player thePlayer, int w, int h)
    {
        super();
        width = w;
        height = h;
        this.player = thePlayer;
        this.invaders = new ArrayList<>();
        this.invPros = new ArrayList<>();
        this.playerPros = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.explosions = new ArrayList<>();
        rand = new Random();
    }
    
    /**
     * Generates the WorldImage
     * 
     * @return WorldImage an object representing the image of the whole world
     */
    @Override
    public WorldImage makeImage() {
        String playerHealth = "The player's health is: " + this.player.health;
        String theKillCount = "The kill count is: " + (this.killCount);
        if (pause) {
            if (level == 0) {
                return new OverlayImages(
                            new RectangleImage(new Posn(this.width / 2, this.height / 2), this.width, this.height, new Black()),
                            new OverlayImages(new FromFileImage(new Posn(this.width/2, this.height/2 + 100), "images/level" + level + ".png"),
                                            new FromFileImage(new Posn(this.width/2, this.height/2 - 100), "images/intro2.png")));
            } else {
                return new OverlayImages(
                            new RectangleImage(new Posn(this.width / 2, this.height / 2), this.width, this.height, new Black()),
                            new OverlayImages(
                                new TextImage(new Posn(this.width/2, this.height/2 + 77), "Shot accuracy, game : " + this.gameAcc + "%", 13, 1, Color.gray),
                                new OverlayImages(
                                    new TextImage(new Posn(this.width/2, this.height/2 + 60), "Shot accuracy, level : " + this.levelAcc + "%", 13, 1, Color.gray),
                                    new FromFileImage(new Posn(this.width/2, this.height/2 - 60), "images/level" + level + ".png"))));
            }
        } else {
            return new OverlayImages(
                        new FromFileImage(new Posn(this.width/2, this.height/2), "images/background.png"),
                        new OverlayImages(
                            powerUpImages(),
                            new OverlayImages(
                                invaderImages(),
                                new OverlayImages(
                                    this.player.playerImage(),
                                    new OverlayImages(
                                        new TextImage(new Posn(this.width/2, 10), playerHealth, 13, 1, Color.gray),
                                        new OverlayImages(
                                            new TextImage(new Posn(this.width/2, 27), theKillCount, 13, 1, Color.gray),
                                            new OverlayImages(
                                                projectileImages(),
                                                expImages())))))));
        }
    }
    
    /**
     * Creates the image of all the invaders
     * 
     * @return an OverlayImage that represents all the invaders
     */
    public OverlayImages invaderImages() {
        IColor col = new Red();
        OverlayImages invImages = new OverlayImages(new DiskImage(new Posn(0,0), 0, col), new DiskImage(new Posn(0,0), 0, col));
        for(Invader i : this.invaders){
            invImages = new OverlayImages(invImages, i.invImage());
        }
        return invImages;
    }
    
    /**
     * Creates the image of all the invaders
     * 
     * @return an Overlay image that represents all the projectiles
     */
    public OverlayImages projectileImages() {
        IColor col = new Blue();
        OverlayImages proImages = new OverlayImages(new DiskImage(new Posn(0,0), 0, col), new DiskImage(new Posn(0,0), 0, col));
        for(Projectile p : this.playerPros){
            proImages = new OverlayImages(proImages, p.proImage());
        }
        for(Projectile p : this.invPros){
            proImages = new OverlayImages(proImages, p.proImage());
        }
        return proImages;
    }
    
    /**
     * Creates the image of all the explosions
     * 
     * @return an Overlay image that represents all the explosions
     */
    public OverlayImages expImages() {
        IColor col = new Blue();
        OverlayImages expImages = new OverlayImages(new DiskImage(new Posn(0,0), 0, col), new DiskImage(new Posn(0,0), 0, col));
        for(Explosion exp : this.explosions){
            expImages = new OverlayImages(expImages, exp.expImage());
        }
        return expImages;
    }
    
    /**
     * Creates the image of all the power ups
     * 
     * @return an Overlay image that represents all the power ups
     */
    public OverlayImages powerUpImages() {
        IColor col = new Blue();
        OverlayImages powImages = new OverlayImages(new DiskImage(new Posn(0,0), 0, col), new DiskImage(new Posn(0,0), 0, col));
        for(PowerUp p : this.powerUps){
            powImages = new OverlayImages(powImages, p.powerUpImage());
        }
        return powImages;
    }
    
    /*
     * OnKeyEvent method for the Game
     */
    @Override
    public void onKeyEvent(String ke) {
        switch (ke) {
            case "left":
            case "right":
            case "down":
                this.player.changeDir(ke);
                break;
            case " ":
                playerShoot();
                break;
            case "t":
                this.killCount = this.quota;
                checkLevelEnd();
                break;
        }
    }
    
    /**
     * Checks if the player can shoot
     * if so, calls the player's shoot method
     * && adds that to the array of projectiles
     */
    public void playerShoot() {
        Projectile pro;
        Projectile lastPro;
        if (!this.playerPros.isEmpty()) {
            lastPro = this.playerPros.get(this.playerPros.size() - 1);
            if (lastPro.pos.y < (this.height - this.shootInterval)) {
                this.shotCountLevel++;
                pro = this.player.shoot();
                this.playerPros.add(pro);
                sp = new SoundPlayer("sounds/missile.wav");
                sp.play();
            }
        } else {
            this.shotCountLevel++;
            pro = this.player.shoot();
            this.playerPros.add(pro);
            sp = new SoundPlayer("sounds/missile.wav");
            sp.play();
        }
    }
    
    /*
     * OnTick method for the Game
     * --> all the actions at every clock tick
     */
    @Override
    public void onTick() {
        if (pause) {
            levelScreen();
        } else {
            projectileHit();
            removeInvaders();
            removeProjectiles();
            removeExplosions();
            makeInvaders();
            moveInvaders();
            makeProjectiles();
            moveProjectiles();
            makePowerUps();
            this.player.move();
            agePowerUps();
        }
    }
    
    /**
     * Generates the screen that comes in between levels
     */
    public void levelScreen() {
        if (pauseTick <= 0) {
            this.bossAlive = false;
            this.invaders.clear();
            this.invPros.clear();
            this.playerPros.clear();
            this.explosions.clear();
            removePowerUps();
            this.player.pos = new Posn(this.width / 2, this.player.pos.y);
            this.pause = false;
            this.shotCountLevel = 0;
            this.shotsMadeLevel = 0;
        }
        this.pauseTick -= 1;
    }
    
    /**
     * Random chance of spawning a new invader
     */
    public void makeInvaders() {
        if(this.level % 5 == 0 && this.level > 0 && !this.bossAlive) {
            Boss bos = new Boss(new Posn(width/2, 0), 50, 100, 1, 50);
            this.invaders.add(bos);
            this.bossAlive = true;
            this.player.velocity = this.player.right;
        } else if (this.level % 5 != 0 || this.level == 0){
            int n = rand.nextInt(100);
            int health = (level / 10) + 1;
            if(n < this.spawnChance) {
                Posn appearLoc = new Posn (rand.nextInt(this.width), 0);
                Invader inv = new Invader(appearLoc, 18, 7, 5, health); // loc, width, height, speed, health
                this.invaders.add(inv);
            }
        }
    }
    
    /**
     * Removes an invader if it goes out of the screen
     */
    public void removeInvaders() {
        Iterator<Invader> it = invaders.iterator();
        Invader inv;
        while(it.hasNext()) {
            inv = it.next();
            if(inv.pos.y > this.height + 20) {
                if (!(inv instanceof Boss)) {
                    this.player.health--;
                } else {
                    this.player.health = 0;
                }
                it.remove();
            }
        }
    }
    
    /**
     * Calls the move method of every invader
     */
    public void moveInvaders() {
        Iterator<Invader> it = invaders.iterator();
        while(it.hasNext()) {
            it.next().move();
        }
    }
    
    /**
     * Randomly generates projectiles from all the invaders
     */
    public void makeProjectiles() {
        int x;
        Projectile p;
        Iterator<Invader> it = invaders.iterator();
        Invader inv;
        while(it.hasNext()) {
            inv = it.next();
            if (!(inv instanceof Boss)) {
                x = rand.nextInt(this.invShootChance);
            } else {
                x = rand.nextInt(this.invShootChance - (2 * this.level));
            }
            if(x < 1) {
                p = inv.shoot();
                this.invPros.add(p);
            }
        }
    }
    
    /**
     * Removes projectiles that go out of bounds
     */
    public void removeProjectiles() {
        Iterator<Projectile> it1 = invPros.iterator();
        Iterator<Projectile> it2 = playerPros.iterator();
        Projectile pro;
        while(it1.hasNext()) {
            pro = it1.next();
            if(pro.pos.y > this.height + 10) {
                it1.remove();
            }
        }
        while(it2.hasNext()) {
            pro = it2.next();
            if(pro.pos.y < -10) {
                it2.remove();
            }
        }
    }
    
    /**
     * Calls the move method of every projectile
     */
    public void moveProjectiles() {
        Iterator<Projectile> it1 = this.invPros.iterator();
        Iterator<Projectile> it2 = this.playerPros.iterator();
        while(it1.hasNext()) {
            it1.next().move();
        }
        while(it2.hasNext()) {
            it2.next().move();
        }
    }
    
    public void removeExplosions() {
        Iterator<Explosion> it = this.explosions.iterator();
        while(it.hasNext()) {
            Explosion exp = it.next();
            if(exp.currFrame > exp.lastFrame) {
                it.remove();
            }
        }
    }
    
    /**
     * Randomly may create a power up
     */
    public void makePowerUps() {
        int n = this.rand.nextInt(300);
        int i = this.rand.nextInt(3);
        PowerUp pow = new PowerUp("Health");
        if(n < 1) {
            totalPowerUps++;
            switch(i) {
                case 0: pow = new PowerUp("Health");
                    break;
                case 1: pow = new PowerUp("Invulnerability");
                    break;
                case 2: pow = new PowerUp("Rapidshot");
                    break;
            }
            this.powerUps.add(pow);
        }
    }
    
    /**
     * Removes a power up when it's time is up
     */
    public void removePowerUps() {
        this.invulnerabilityAge = 0;
        this.rapidshotAge = 0;
        this.powerUps.clear();
    }
    
    /**
     * Depletes or gets rid of power ups
     */
    public void agePowerUps() {
        if(this.invulnerabilityAge > 0) {
            this.invulnerabilityAge--;
        }
        if(this.rapidshotAge > 0) {
            this.rapidshotAge--;
        }
        if(this.invulnerabilityAge <= 0) {
            this.playerInvulnerable = false;
        }
        if(this.rapidshotAge <= 0) {
            this.shootInterval = defaultShootInterval;
        }
    }
    
    /**
     * Calls all the different hit methods for projectiles
     */
    public void projectileHit() {
        playerHit();
        invaderHit();
        powerUpHit();
    }
    
    /**
     * Removes any projectile that hits the player
     * also lowers the player's health
     */
    public void playerHit() {
        if(!this.playerInvulnerable) {
            Iterator<Projectile> it1 = this.invPros.iterator();
            Projectile pro;
            while(it1.hasNext()) {
                pro = it1.next();
                if (inRangeX(pro.pos, this.player.pos, pro.width, this.player.width)
                        && inRangeY(pro.pos, this.player.pos, pro.width, this.player.width)) {
                    switch (pro.source) {
                        case "invader":
                            this.player.health--;
                            this.explosions.add(new Explosion("images/explosion1/explosion1_0", pro.pos));
                            sp = new SoundPlayer("sounds/explosion.wav");
                            break;
                        case "boss":
                            this.player.health -= this.level;
                            this.explosions.add(new Explosion("images/explosion1/explosion1_0", pro.pos));
                            sp = new SoundPlayer("sounds/explosion.wav");
                            break;
                    }
                    it1.remove();
                    sp.play();
                }
            }
        }
    }
    
    /**
     * Removes any invader that a projectile hits
     * also removes that projectile
     * and checks if the level should change
     */
    public void invaderHit() {
        Iterator<Projectile> it1 = this.playerPros.iterator();
        Iterator<Invader> it2;
        Projectile myPro;
        Invader inv;
        while(it1.hasNext()) {
            it2 = this.invaders.iterator();
            myPro = it1.next();
            while(it2.hasNext()) {
                inv = it2.next();
                if (inRangeX(myPro.pos, inv.pos, myPro.width, inv.width)
                        && inRangeY(myPro.pos, inv.pos, myPro.width, inv.width)) {
                    this.shotsMadeLevel++;
                    inv.health -= 1;
                    if (inv instanceof Boss) {
                        this.explosions.add(new Explosion("images/explosion2/explosion1_0", myPro.pos));
                    }
                    if (inv.health == 0) {
                        if (!(inv instanceof Boss)) {
                            this.explosions.add(new Explosion("images/explosion2/explosion1_0", inv.pos));
                            this.killCount += 1;
                        } else {
                            this.explosions.add(new Explosion("images/explosion1/explosion1_0", inv.pos));
                            this.bossAlive = false;
                            this.killCount = this.level * this.quota;
                        }
                        it2.remove();
                        checkLevelEnd();
                    }
                    it1.remove();
                    sp = new SoundPlayer("sounds/smallexplosion.wav");
                    sp.play();
                    break;
                }
            }
        }
    }
    
    /**
     * Checks if the player projectile hits a power up
     */
    public void powerUpHit() {
        Iterator<Projectile> it1 = this.playerPros.iterator();
        Iterator<PowerUp> it2;
        Projectile myPro;
        PowerUp pow;
        while(it1.hasNext()) {
            it2 = this.powerUps.iterator();
            myPro = it1.next();
            while(it2.hasNext()) {
                pow = it2.next();
                if (inRangeX(myPro.pos, pow.pos, myPro.width, pow.width)
                        && inRangeY(myPro.pos, pow.pos, myPro.width, pow.width)) {
                    switch(pow.type) {
                        case "Health": {
                            this.player.health = 10 + this.level;
                            sp = new SoundPlayer("sounds/health.wav");
                            sp.play();
                            break;
                        }
                        case "Invulnerability": {
                            this.playerInvulnerable = true;
                            this.invulnerabilityAge = 200;
                            sp = new SoundPlayer("sounds/invulnerability.wav");
                            sp.play();
                            break;
                        }
                        case "Rapidshot": {
                            this.shootInterval = 45;
                            this.rapidshotAge = 200;
                            sp = new SoundPlayer("sounds/rapidshot.wav");
                            sp.play();
                            break;
                        }
                    }
                    it2.remove();
                    break;
                }
            }
        }
    }
    
    /**
     * See if the level has moved on and do the appropriate changes
     */
    public void checkLevelEnd() {
        if (killCount % this.quota == 0) {
            this.level += 1;
            this.shotsMade += this.shotsMadeLevel;
            this.shotCount += this.shotCountLevel;
            if (this.shotCount != 0) {
                this.gameAcc = (this.shotsMade * 100) / this.shotCount;
            } else {
                this.gameAcc = 0;
            }
            if (this.shotCountLevel != 0) {
                this.levelAcc = (this.shotsMadeLevel * 100) / this.shotCountLevel;
            } else {
                this.levelAcc = 0;
            }
            this.spawnChance = ((this.level % 5) * 3) + 5;
            if ((this.level / 5) % 2 == 0) {
                this.invShootChance = 100;
            } else {
                this.invShootChance = 150;
            }
            this.pause = true;
            this.pauseTick = 80;
            this.player.health = 10 + level;
            this.player.velocity = this.player.stop;
        }
    }
    
    
    /**
     * Checks to see if the two objects overlap via their x Posns
     * 
     * @param p1 the Posn of the first object
     * @param p2 the Posn of the second object
     * @param width1 the width of the first object
     * @param width2 the width of the second object
     * @return a boolean if the two Posn's overlap
     */
    public static boolean inRangeX(Posn p1, Posn p2, int width1, int width2) {
        
        int p1LeftEdge = p1.x - width1;
        int p2LeftEdge = p2.x - width2;
        int p1RightEdge = p1.x + width1;
        int p2RightEdge = p2.x + width2;
        if((p1LeftEdge >= p2LeftEdge && p1LeftEdge <= p2RightEdge) ||
                (p1RightEdge <= p2RightEdge && p1RightEdge >= p2LeftEdge)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks to see if two objects overlap via their y Posns
     * 
     * @param p1 the Posn of the first object
     * @param p2 the Posn of the second object
     * @param height1 the height of the first object
     * @param height2 the height of the second object
     * @return a boolean if the two Posn's overlap
     */
    public static boolean inRangeY(Posn p1, Posn p2, int height1, int height2) {
        int p1BottomEdge = p1.y + height1;
        int p2BottomEdge = p2.y + height2;
        int p1TopEdge = p1.y - height1;
        int p2TopEdge = p2.y - height2;
        if((p1BottomEdge >= p2TopEdge && p1BottomEdge <= p2BottomEdge) ||
                (p1TopEdge <= p2BottomEdge && p1TopEdge >= p2BottomEdge)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns a Posn that is the addition of two Posns
     * 
     * @param p1 the Posn of the first object
     * @param p2 the Posn of the second object
     * @return a Posn representing the addition of the two Posns
     */
    public static Posn posnAdd(Posn p1, Posn p2) {
        return new Posn(p1.x + p2.x, p1.y + p2.y);
    }
    
    /*
     * Generates the WorldEnd image of the Game
     */
    @Override
    public WorldEnd worldEnds() {
        if (this.player.health <= 0) {
            return new WorldEnd(true,
                    new OverlayImages(this.makeImage(),
                    new TextImage(new Posn(this.width/2, this.height/2), "YOU GOT TO LEVEL " + level
                                                                    + "!  BUT THEN YOU DIED.", Color.white)));
        } else {
            return new WorldEnd(false, this.makeImage());
        }
    }
}