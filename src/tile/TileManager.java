package tile;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    

    public TileManager(GamePanel gp){
        this.gp = gp;
        
        this.mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        tile = new Tile[15];
        
        getTileImage();
        loadMap("./src/maps/alphaworldmap2.txt");
    }

    
    public void getTileImage(){

        try{
            File f1 = new File("./src/tiles/grass.png");
            File f2 = new File("./src/tiles/stonewall.png");
            File f3 = new File("./src/tiles/water.png");
            File f4 = new File("./src/tiles/sand.png");
            File f5 = new File("./src/tiles/dirt.png");
            File f6 = new File("./src/tiles/tree.png");

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(f1); //grass

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(f2); //stonewall
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(f3); //water
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(f4); //sand

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(f5); //dirt

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(f6); //tree
            tile[5].collision = true;

            
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(String mapPath){
        
        try{
            File file = new File(mapPath); 
            Scanner sc = new Scanner(file);
            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = sc.nextLine();

                while(col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col >= gp.maxWorldCol){
                    col = 0;
                    row++;
                }
                
            }
            sc.close();
        } catch (FileNotFoundException f) {
            f.getStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize * 2 > gp.player.worldX - gp.player.screenX && 
               worldX - gp.tileSize * 2 < gp.player.worldX + gp.player.screenX && 
               worldY + gp.tileSize * 2 > gp.player.worldY - gp.player.screenY && 
               worldY - gp.tileSize * 2 < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;
            
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                
                worldRow++;
                
            }
        }

    }
}