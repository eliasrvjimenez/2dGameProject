package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;


import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;
public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 pixels per tile (ppt)
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; // 48x48ppt 
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 720 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 38;
    public final int maxWorldRow = 38;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    // FPS
    int FPS = 60; 

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this,keyH);
    public CollisionChecker cChecker = new CollisionChecker(this);
    
    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS; // 1 billion nanoseconds / FPS, draws every .016 seconds.
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null){
            
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer+= (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // 1 Update : update info about player position.
                update();

                // 2 Draw: draw the screen with new info.
                repaint();
                delta--;
                drawCount ++;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS:" + drawCount + ". Coords:" + player.worldX/tileSize + "," + player.worldY/tileSize);
                timer = 0;
                drawCount = 0;
            }
            
        }
    }

    public void update() { 
        
        player.update();
    }

    public void paintComponent(Graphics g) {


        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        
        
        tileM.draw(g2);
        

        player.draw(g2);
        g2.dispose();
    }
}
