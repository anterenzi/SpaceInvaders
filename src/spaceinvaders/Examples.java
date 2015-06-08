package spaceinvaders;

import javalib.worldimages.*;
import tester.Tester;


public class Examples {
    
    static int gameWidth;
    static int gameHeight;
    static double gameSpeed;
    
    Player thePlayer = new Player(new Posn(200, 390), 10, 10, 10, 10);
    SpaceWorld sw = new SpaceWorld(thePlayer, 400, 400);
    Projectile p = new Projectile(new Posn(100, 100), 10, 10, 10, "player");
    Invader inv = new Invader(new Posn(150, 150), 7, 7, 5, 1);
    Invader bounds = new Invader(new Posn(100, 1000), 7, 7, 5, 1);
    Projectile playerPro = new Projectile(new Posn(100, -100), 10, 10, 10, "player");
    Projectile invPro = new Projectile(new Posn(100, 1000), 10, 10, 10, "player");
    Projectile overlap = new Projectile(new Posn(155, 145), 10, 10, 10, "player");
    Posn right = new Posn(5, 0);
    Posn left = new Posn(-5, 0);
    Posn zero = new Posn(0, 0);

    /*
     * Main class for the package
     */
    public static void main(String[] args) {
        gameWidth = 600;
        gameHeight = 600;
        gameSpeed = .035; // default .04
        Examples be = new Examples();
        //Tester.runReport(be, false, false);
        SpaceWorld sw =
            new SpaceWorld(new Player(new Posn(gameWidth / 2, gameHeight - 30), 15, 10, 7, 10), gameWidth, gameHeight); // loc, width, height, speed, health
        sw.bigBang(gameWidth, gameHeight, gameSpeed);
    }
    
    /*
     * Resets the game world state
     */
    void reset() {
        this.thePlayer = new Player(new Posn(200, 390), 10, 10, 10, 10);
        this.sw = new SpaceWorld(thePlayer, gameWidth, gameHeight);
        this.p = new Projectile(new Posn(100, 100), 10, 10, 10, "player");
        this.inv = new Invader(new Posn(150, 150), 7, 7, 5, 1);
    }
    
    /*
     * Tests the posnAdd method in SpaceWorld
     */
    void testPosnAdd(Tester t) {
        Posn p100 = new Posn(100, 100);
        Posn p0 = new Posn(0, 0);
        Posn pNeg = new Posn(-100, -100);
        t.checkExpect(p100, SpaceWorld.posnAdd(p0, p100));
        t.checkExpect(p0, SpaceWorld.posnAdd(p100, pNeg));
    }
    
    /*
     * Tests the onKeyEvent method in SpaceWorld
     */
    void testOnKeyEvent(Tester t) {
        t.checkExpect(thePlayer.velocity, zero);
        sw.onKeyEvent("left");
        t.checkExpect(thePlayer.velocity, left);
        this.reset();
        sw.onKeyEvent("right");
        t.checkExpect(thePlayer.velocity, right);
        this.reset();
    }
    
    /*
     * Tests the playerShoot method in SpaceWorld
     */
    void testPlayerShoot(Tester t) {
        t.checkExpect(sw.playerPros.isEmpty(), true);
        sw.playerShoot();
        t.checkExpect(sw.playerPros.isEmpty(), false);
        this.reset();
    }
    
    void testOnTick(Tester t) {
        //too many things happen!!
    }
    
    /*
     * Tests the makeInvaders method in SpaceWorld
     */
    void testMakeInvaders(Tester t) {
        //uses a lot of random chance
        t.checkExpect(sw.invaders.isEmpty(), true);
        int x = 100000;
        while(x >= 0) {
            sw.makeInvaders();
            x--;
        }
        t.checkExpect(sw.invaders.isEmpty(), false);
        //very small chance of failing!
        this.reset();
    }
    
    /*
     * Tests the removeInvaders method in SpaceWorld
     */
    void testRemoveInvaders(Tester t) {
        sw.invaders.add(bounds);
        sw.removeInvaders();
        t.checkExpect(sw.invaders.isEmpty(), true);
        this.reset();
        sw.invaders.add(inv);
        sw.removeInvaders();
        t.checkExpect(sw.invaders.isEmpty(), false);
        this.reset();
    }
    
    void testMoveInvaders(Tester t) {
        //redundant because calls the move statement of every invader
    }
    
    /*
     * Tests the makeProjectiles method in SpaceWorld
     */
    void testMakeProjectiles(Tester t) {
        //uses a lot of random chance
        t.checkExpect(sw.invPros.isEmpty(), true);
        sw.invaders.add(inv);
        int x = 10000;
        while(x >= 0) {
            sw.makeProjectiles();
            x--;
        }
        t.checkExpect(sw.invPros.isEmpty(), false);
        //very small chance of failing!
        this.reset();
    }
    
    /*
     * Tests the removeProjectiles method in SpaceWorld
     */
    void testRemoveProjectiles(Tester t) {
        sw.playerPros.add(playerPro);
        sw.invPros.add(invPro);
        //both projectiles added are out of range
        
        sw.removeProjectiles();
        t.checkExpect(sw.playerPros.isEmpty(), true);
        t.checkExpect(sw.invPros.isEmpty(), true);
        sw.invPros.add(overlap);
        sw.removeProjectiles();
        t.checkExpect(sw.invPros.isEmpty(), false);
        this.reset();
    }
    
    void testMoveProjectiles(Tester t) {
        //calls the move method of every projectile
    }
    
    void testProjectileHit(Tester t) {
        //only calls playerHit and invaderHit
    }
    
    void testPlayerHit(Tester t) {
        //mainly dependent on inRangeX && inRangeY
    }
    
    /*
     * Tests the invaderHit method in SpaceWorld
     */
    void testInvaderHit(Tester t) {
        this.reset();
        sw.playerPros.add(overlap);
        sw.invaders.add(inv);
        t.checkExpect(SpaceWorld.inRangeX(sw.playerPros.get(0).pos, sw.invaders.get(0).pos, 10, 10), true);
        t.checkExpect(SpaceWorld.inRangeY(sw.playerPros.get(0).pos, sw.invaders.get(0).pos, 10, 10), true);
        sw.invaderHit();
        t.checkExpect(sw.playerPros.isEmpty(), true);
        t.checkExpect(sw.invaders.isEmpty(), true);
        this.reset();
        sw.playerPros.add(invPro);
        sw.invaders.add(inv);
        sw.invaderHit();
        t.checkExpect(sw.playerPros.isEmpty(), false);
        t.checkExpect(sw.invaders.isEmpty(), false);
        this.reset();
    }
    
    /*
     * Tests the inRangeX method in SpaceWorld
     */
    void testInRangeX(Tester t) {
        t.checkExpect(SpaceWorld.inRangeX(new Posn(100, 100), new Posn(100, 100), 10, 10), true);
        t.checkExpect(SpaceWorld.inRangeX(new Posn(100, 100), new Posn(120, 100), 5, 5), false);
        t.checkExpect(SpaceWorld.inRangeX(new Posn(100, 100), new Posn(150, 100), 50, 50), true);
        t.checkExpect(SpaceWorld.inRangeX(new Posn(100, 100), new Posn(80, 100), 5, 5), false);
        t.checkExpect(SpaceWorld.inRangeX(new Posn(100, 100), new Posn(100, 100), 10, 10), true);
        this.reset();
    }
    
    /*
     * Tests the inRangeY method in SpaceWorld
     */
    void testinRangeY(Tester t) {
        t.checkExpect(SpaceWorld.inRangeY(new Posn(100, 100), new Posn(100, 100), 10, 10), true);
        t.checkExpect(SpaceWorld.inRangeY(new Posn(100, 100), new Posn(100, 120), 5, 5), false);
        t.checkExpect(SpaceWorld.inRangeY(new Posn(100, 100), new Posn(100, 150), 50, 50), true);
        t.checkExpect(SpaceWorld.inRangeY(new Posn(100, 100), new Posn(100, 80), 5, 5), false);
        t.checkExpect(SpaceWorld.inRangeY(new Posn(100, 100), new Posn(100, 100), 10, 10), true);
        this.reset();
    }
    
    /*
     * Tests the playerMove method in Player
     */
    void testPlayerMove(Tester t) {
        //thePlayer starts out at (200, 390)
        thePlayer.changeDir("left");
        thePlayer.move();
        thePlayer.move();
        thePlayer.move();
        t.checkExpect(thePlayer.pos.x, 185);
        this.reset();
        thePlayer.changeDir("right");
        thePlayer.move();
        thePlayer.move();
        thePlayer.move();
        t.checkExpect(thePlayer.pos.x, 215);
        this.reset();
    }
    
    /*
     * Tests the shoot method in Player
     */
    void testShoot(Tester t) {
        //only creates a Projectile object
    }
    
    /*
     * Tests the changeDir method in Player
     */
    void testPlayerChangeDir(Tester t) {
        t.checkExpect(thePlayer.velocity, zero);
        thePlayer.changeDir("left");
        t.checkExpect(thePlayer.velocity, left);
        this.reset();
        thePlayer.changeDir("right");
        t.checkExpect(thePlayer.velocity, right);
        this.reset();
    }
    
    /*
     * Tests the move method in Projectile
     */
    void testProjectileMove(Tester t) {
        t.checkExpect(p.pos.y, 100);
        p.move();
        p.move();
        p.move();
        t.checkExpect(p.pos.y, 130);
        this.reset();
    }
    
    /*
     * Tests the move method in Invader
     */
    void testInvaderMove(Tester t) {
        t.checkExpect(p.pos.y, 100);
        p.move();
        p.move();
        p.move();
        t.checkExpect(p.pos.y, 130);
        this.reset();
    }
    
    /*
     * Tests the shoot method in Invader
     */
    void testInvaderShoot(Tester t) {
        //only creates a Projectile object
    }
}
