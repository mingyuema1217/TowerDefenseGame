package WizardTD;

import processing.core.PImage;

import java.util.List;

import static WizardTD.App.CELLSIZE;

/**
 * Represents a monster in the game, with its movement path, appearance, health, and special abilities.
 * This class extends the MovePath class to inherit movement-related functionalities.
 */
public class Monster extends MovePath {

    private final MonstersConfig monstersConfig;
    private int pathIndex;
    private final List<int[]> path;
    private final PImage sprite;
    private final App app;
    private boolean isVanish;
    private int x;
    private int y;
    private final Wave wave;
    private int monsterInWaveStartFrame;
    private int offset = (CELLSIZE - 20) / 2;
    private int maxHp;
    private double armour;
    private int currHp;

    private boolean active;

    private int vanishCount = 1;

    /**
     * Constructs a new Monster instance with specified properties and settings.
     *
     * @param app            The main App instance where the game is run.
     * @param wave           The Wave instance indicating the current wave in the game.
     * @param monstersConfig The configuration settings for the monster, including type, hp, and armor.
     */
    public Monster(App app, Wave wave, MonstersConfig monstersConfig) {
        super(app.getGameBoard());
        this.path = findShortestPath();
        this.monstersConfig = monstersConfig;
        this.app = app;
        this.wave = wave;
        this.sprite = app.getImageByType(monstersConfig.getType());
        this.maxHp = monstersConfig.getHp();
        this.currHp = this.maxHp;
        this.armour = monstersConfig.getArmour();
    }


    public void drawMonster() {
        // The image() method is used to draw PImages onto the screen.
        // Since monster is 20*20, cellsize is 32*32, we need to set offset to make the monster centered at road
        app.image(sprite, x + offset, y + offset);
    }

    /**
     * Decreases the monster's current hp based on the damage received.
     * @param dmg The damage points to be deducted from the monster's current hit points.
     * Note: Actual hit points lost might be less due to the influence of armor.
     */
    public void hpLose(int dmg) {
        if (isVanish) {
            return;
        }
        double realLose = armour * dmg;
        currHp -= (int) realLose;
        if (currHp <= 0) {
            isVanish = true;
            app.setDieMonster(app.getDieMonster() + 1);
            app.manaGainedByKilling(monstersConfig.getManaGainedOnKill());
            if(app.getDieMonster()==app.getTotalMonster()){
                app.isGameWin = true;
            }
        }


    }

    /**
     * Draws the monster's health bar on top of the monster.
     */
    public void drawHp() {
        //Hp display on the top of monster
        int hpWidth = 25;
        float percentOfHp = (float) currHp / maxHp;
        //should be change later, green width should be percentage of health
        //if lose health is 20% then, 20% * hpwidth
        float greenWidth = percentOfHp * 25;
        float redWidth = hpWidth - greenWidth;
        //draw Green HP
        app.fill(0, 255, 0);
        //-3 should be offset for hp since it is longer than monster
        app.rect(x + offset - 3, y + offset - 5 - 3, greenWidth, 2);
        //draw Red Hp
        app.fill(255, 0, 0);
        app.rect(x + offset + greenWidth - 3, y + offset - 5 - 3, redWidth, 2);
    }

    /**
     * Updates the state and position of the monster in every game frame.
     * This method handles the logic for the monster's movement along a pre-defined path,
     * checks if the monster should vanish or continue moving, and updates the monster's
     * position based on its speed and the next path index.
     * Additionally, it renders the monster sprite and its HP bar on the game board.
     */
    @Override
    public void tick() {
        if (isVanish) {
            this.playingVanish();
            return;
        }
        if (pathIndex >= path.size() - 1) {
            pathIndex = 0;
            app.banishedManaLose(currHp);
        }
        if (wave.getFrameCount() < monsterInWaveStartFrame) {
            return;
        }
        this.active = true;

        int[] spawnPos = null;
        int[] currPos = null;
        int[] nextPos = null;

        float speed = monstersConfig.getSpeed() * app.getFastForward();
        //Find the spawn point
        if (pathIndex == 0) {
            currPos = path.get(pathIndex);
            spawnPos = new int[2];
            //spawn point at different location will expand 1 index
            if (currPos[1] == 0) {
                spawnPos[0] = currPos[0];
                spawnPos[1] = -1;
            } else if (currPos[1] == app.getGameBoard().length - 1) {
                spawnPos[1] = app.getGameBoard().length;
                spawnPos[0] = currPos[0];
            } else if (currPos[0] == app.getGameBoard().length - 1) {
                spawnPos[0] = app.getGameBoard().length;
                spawnPos[1] = currPos[1];
            } else if (currPos[0] == 0) {
                spawnPos[0] = -1;
                spawnPos[1] = currPos[1];
            }
        } else {
            currPos = path.get(pathIndex);
            nextPos = path.get(pathIndex + 1);
        }


        //Find the start position
        if (pathIndex == 0) {
            x = spawnPos[1] * App.CELLSIZE;
            y = spawnPos[0] * App.CELLSIZE + App.TOPBAR;
            pathIndex++;
        }
        //find the next position for monster
        else {
            //Calculate x-coordinates and speed for next movement
            if (nextPos[1] > currPos[1]) {
                x += speed;
                // Check if match the next position
                if (x >= nextPos[1] * App.CELLSIZE) {
                    x = nextPos[1] * App.CELLSIZE;
                    pathIndex++;
                }
            } else if (nextPos[1] < currPos[1]) {
                x -= speed;
                if (x <= nextPos[1] * App.CELLSIZE) {
                    x = nextPos[1] * App.CELLSIZE;
                    pathIndex++;
                }
            }

            //Calculate y-coordinates and speed for next movement
            if (nextPos[0] > currPos[0]) {
                y += speed;
                if (y >= (nextPos[0] * App.CELLSIZE) + App.TOPBAR) {
                    y = nextPos[0] * App.CELLSIZE + App.TOPBAR;
                    pathIndex++;
                }
            } else if (nextPos[0] < currPos[0]) {
                y -= speed;
                if (y <= (nextPos[0] * App.CELLSIZE) + App.TOPBAR) {
                    y = nextPos[0] * App.CELLSIZE + App.TOPBAR;
                    pathIndex++;
                }
            }
        }

        this.drawMonster();
        this.drawHp();
    }

    /**
     * Play the vanishing animation for a gremlin monster.
     */
    public void playingVanish() {
        if (!active || !isVanish || vanishCount > 20) {
            return;
        }
        if (!"gremlin".equals(monstersConfig.getType())) {
            return;
        }
        if (vanishCount <= 4) {
            app.image(app.gremlin1, x, y);
        } else if (vanishCount <= 8) {
            app.image(app.gremlin2, x, y);
        } else if (vanishCount <= 12) {
            app.image(app.gremlin3, x, y);
        } else if (vanishCount <= 16) {
            app.image(app.gremlin4, x, y);
        } else if (vanishCount <= 20) {
            app.image(app.gremlin5, x, y);
        }
        vanishCount++;
    }


    public int getMonsterInWaveStartFrame() {
        return monsterInWaveStartFrame;
    }

    public void setMonsterInWaveStartFrame(int monsterInWaveStartFrame) {
        this.monsterInWaveStartFrame = monsterInWaveStartFrame;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXCenter() {
        return x + offset + 10;
    }

    public int getYCenter() {
        return y + offset + 10;
    }

    public boolean isVanish() {
        return isVanish;
    }

    public void setVanish(boolean vanish) {
        isVanish = vanish;
    }

    public boolean isActive() {
        return active;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public double getArmour() {
        return armour;
    }

    public void setArmour(double armour) {
        this.armour = armour;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}