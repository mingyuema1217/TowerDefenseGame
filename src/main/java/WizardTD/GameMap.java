package WizardTD;

import static WizardTD.App.*;

import processing.core.PImage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents the game map in the WizardTD game. The map includes the layout of the path,
 * terrain, and any other relevant components of the game.
 */
public class GameMap {
    private String level;
    private Character[][] gameBoard;
    private App app;
    private PImage path0, path1, path2, path3, shrub, wizard_house, grass;
    private PImage house;
    float xHouse = 0;
    float yHouse = 0;

    /**
     * Constructs a GameMap instance based on the provided configuration and application.
     *
     * @param configs The configuration for the game, which contains the map layout and other game settings.
     * @param app     The main application where the game runs.
     */
    public GameMap(Config configs, App app){
        this.level = configs.getLayout();
        this.app = app;
        this.path0 = app.path0;
        this.path1 = app.path1;
        this.path2 = app.path2;
        this.path3 = app.path3;
        this.shrub = app.shrub;
        this.wizard_house = app.wizard_house;
        this.grass = app.grass;
        this.loadMap();
    }

    /**
     * Loads the game map layout from a file.
     * Populates the gameBoard 2D array with the layout information.
     */
    public void loadMap() {
        //get each level into a 2d array
        gameBoard = new Character[BOARD_WIDTH][BOARD_WIDTH];
        try {
            File levelFile = new File(this.level);
            Scanner scan = new Scanner(levelFile);
            for (int i = 0; i < BOARD_WIDTH; i++) {
                String line = scan.nextLine();
                //Add space behind for each line that length less than BOARD_WIDTH
                int lineLength = line.length();
                while (lineLength < BOARD_WIDTH) {
                    line += " ";
                    lineLength++;
                }
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    gameBoard[i][j] = line.charAt(j);
                    //System.out.print(gameBoard[i][j]);//for debuging
                }
                //System.out.println("");//for debuging
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("No such file");
        }
    }

    /**
     * Checks if the specified cell (represented by row and column) is a path.
     *
     * @param r Row index of the cell.
     * @param c Column index of the cell.
     * @return true if the specified cell is a path, false otherwise.
     */
    public boolean isPath(int r, int c) {
        // check the element is not out of boundary
        if (r >= 0 && r < BOARD_WIDTH && c >= 0 && c < BOARD_WIDTH) {
            if (gameBoard[r][c] == 'X') {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines the correct type of path image to use based on surrounding paths.
     *
     * @param r Row index of the cell.
     * @param c Column index of the cell.
     * @return A PImage representing the correct path image for the cell.
     */
    public PImage getPath(int r, int c) {
        boolean isUp = isPath(r - 1, c);
        boolean isDown = isPath(r + 1, c);
        boolean isLeft = isPath(r, c - 1);
        boolean isRight = isPath(r, c + 1);

        //Path exist at all four directions
        if (isUp && isDown && isLeft && isRight) {
            return path3;
        }
        //path exist up, down, left
        else if (isUp && isDown && isLeft) {
            return rotateImageByDegrees(path2, 90);
        }
        //path exist up, down, right
        else if (isUp && isDown && isRight) {
            return rotateImageByDegrees(path2, 270);
        }
        //path exist up, right, left
        else if (isLeft && isRight && isUp) {
            return rotateImageByDegrees(path2, 180);
        }
        //path exist up, down, left
        else if (isLeft && isRight && isDown) {
            return path2;
        }
        //path exist up and left
        else if (isUp && isLeft) {
            return rotateImageByDegrees(path1, 90);
        }
        //path exist up, right
        else if (isUp && isRight) {
            return rotateImageByDegrees(path1, 180);
        }
        //path exist down, left
        else if (isDown && isLeft) {
            return path1;
        }
        //path exist down, right
        else if (isDown && isRight) {
            return rotateImageByDegrees(path1, 270);
        } else if (isDown && isUp) {
            return rotateImageByDegrees(path0, 90);
        } else if (isDown || isUp) {
            return rotateImageByDegrees(path0, 90);
        } else {
            return path0;
        }

    }

    /**
     * Determines the correct orientation for the wizard's house image
     * based on surrounding paths.
     *
     * @param r Row index of the cell.
     * @param c Column index of the cell.
     * @return A PImage representing the oriented wizard's house image for the cell.
     */
    public PImage getHouse(int r, int c) {
        boolean isUp = isPath(r - 1, c);
        boolean isDown = isPath(r + 1, c);
        boolean isLeft = isPath(r, c - 1);
        boolean isRight = isPath(r, c + 1);

        //Path exist up
        if (isUp) {
            return rotateImageByDegrees(wizard_house, 90);
        }
        //path exist down
        else if (isDown) {
            return rotateImageByDegrees(wizard_house, 270);
        }
        //path exist right
        else if (isRight) {
            return rotateImageByDegrees(wizard_house, 180);
        }
        //path exist left
        else {
            return wizard_house;
        }
    }

    /**
     * Draws the game map on the screen.
     * Iterates through the gameBoard and renders each cell appropriately.
     */
    public void drawMap() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                //convert to the actual coordinator
                //xcord means which column it located
                float xcord = j * CELLSIZE;
                //ycord means which row it located
                float ycord = i * CELLSIZE + TOPBAR;

                //Fullfill the grass
                app.image(grass, xcord, ycord, CELLSIZE, CELLSIZE);

                char mapElement = gameBoard[i][j];
                if (mapElement == 'S') {
                    app.image(shrub, xcord, ycord, CELLSIZE, CELLSIZE);
                }
                //for path need to consider specific case.
                else if (mapElement == 'X') {
                    app.image(getPath(i, j), xcord, ycord, CELLSIZE, CELLSIZE);
                } else if (mapElement == 'W') {
                    house = getHouse(i, j);
                    float xOffset = (CELLSIZE - 48) / 2;
                    float yOffset = (CELLSIZE - 48) / 2;
                    xHouse = xcord + xOffset;
                    yHouse = ycord + yOffset;

                }
            }
        }
    }

    /**
     * Draws the wizard's house on the screen.
     * The house is drawn last to overlay other elements.
     */
    public void drawHouse(){
        //Place the house at end to overlay other elements
        app.image(house, xHouse, yHouse, 48, 48);
    }

    public Character[][] getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Determines if a tower can be placed at a given mouse position on the game map.
     *
     * @param mouseX The x-coordinate of the mouse position in pixels.
     * @param mouseY The y-coordinate of the mouse position in pixels.
     * @return true if the tower can be placed at the specified position, false otherwise.
     */
    public boolean canPlaceTower(int mouseX, int mouseY){
        // Check if it is in the board;
        if(mouseX >= 0 && mouseX < 640 && mouseY >= 40 && mouseY < 680) {
            //change the pixels to cells
            int col = (int) mouseX / 32;
            int row = (int) (mouseY - 40) / 32;

            //check if it is grass place
            char item = gameBoard[row][col];
            return item != 'X' && item != 'W' && item != 'S';
        }
        //invalid position
        return false;
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     *
     * @param pimg  The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = app.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
