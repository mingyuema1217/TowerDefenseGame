package WizardTD;

import processing.core.PImage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static processing.core.PApplet.dist;


/**
 * Represents a tower in the WizardTD game. The tower is responsible for shooting at
 * monsters within its range, and its properties can be upgraded.
 */
public class Tower {
    private int x;
    private int y;
    private Config config;
    private final App app;
    private int towerRange;
    private int towerDmg;
    private ArrayList<Fireball> fireballList;
    private int rankOfTower;
    private List<Monster> monsterList;
    private int frameCountFromLast = 0;

    private boolean isWaitingForShoot = false;
    private int rangeLevel = 0;
    private int damageLevel = 0;
    private int speedLevel = 0;
    private double fireSpeed;


    /**
     * Constructs a new tower at the specified coordinates with a given configuration and application reference.
     *
     * @param x X-coordinate of the tower.
     * @param y Y-coordinate of the tower.
     * @param config Configuration settings for the tower.
     * @param app Reference to the main application.
     * @param rankOfTower Rank or level of the tower.
     */
    public Tower(int x, int y, Config config, App app, int rankOfTower) {
        //change the pixel to grid coordinator
        this.x = (x / 32) * 32;
        this.y = ((y - 40) / 32) * 32 + 40;
        this.config = config;
        this.app = app;
        this.towerRange = config.getInitialTowerRange();
        this.rankOfTower = rankOfTower;
        this.fireballList = new ArrayList<>();
        this.towerDmg = config.getInitialTowerDamage();
        this.fireSpeed = config.getInitialTowerFiringSpeed();
    }

    /**
     * Finds the nearest monster within the tower's range.
     *
     * @param monsterList List of monsters to search within.
     * @return The nearest monster, or null if none are in range.
     */
    public Monster getNearestMonster(List<Monster> monsterList) {
        if (!monsterList.isEmpty()) {
            this.monsterList = monsterList;
            for (Monster monster : monsterList) {
                if (monster.isVanish() || !monster.isActive()) {
                    continue;
                }
                float distanceToMonster = dist(x, y, monster.getXCenter(), monster.getYCenter());
                if (distanceToMonster <= towerRange) {
                    return monster;
                }
            }
        }
        return null;
    }

    /**
     * Makes the tower shoot at monsters.
     *
     * @param monsterList List of monsters to target.
     */
    public void shoot(List<Monster> monsterList) {
        int firingInterval = BigDecimal.valueOf(getFireSpeed()).multiply(new BigDecimal(App.FPS)).intValue();
        if (isWaitingForShoot) {
            frameCountFromLast++;
            // find the interval in frame
            if (frameCountFromLast % firingInterval == 0) {
                Monster target = getNearestMonster(monsterList);
                if (target != null) {
                    Fireball fireball = new Fireball(x + 16, y + 16, target, app, this);
                    fireballList.add(fireball);
                    frameCountFromLast = 0;
                } else {
                    isWaitingForShoot = false;
                }
            }
        } else {
            Monster target = getNearestMonster(monsterList);
            if (target != null) {
                isWaitingForShoot = true;
                Fireball fireball = new Fireball(x + 16, y + 16, target, app, this);
                fireballList.add(fireball);
                frameCountFromLast = 0;
            }
        }
    }

    /**
     * Computes the upgrade cost based on the current level of the tower.
     *
     * @param currentLevel Current upgrade level of the tower.
     * @return Upgrade cost.
     */
    public int getUpgradeCost(int currentLevel) {
        switch (currentLevel) {
            case 0:
                return 20;
            case 1:
            case 2:
                return 10;
        }
        return 0;
    }


    /**
     * Fetches the appropriate image for the tower based on its rank.
     *
     * @return Image for the tower.
     */
    public PImage getImageByRank() {
        this.getTowerRank();
        if (rankOfTower == 1) {
            return app.tower1;
        } else if (rankOfTower == 2) {
            return app.tower2;
        } else if (rankOfTower == 3) {
            return app.tower2;
        } else {
            return app.tower0;
        }
    }

    /**
     * Determines the rank of the tower based on its attributes.
     */
    public void getTowerRank() {
        if (rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) {
            if (rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) {
                if (rangeLevel >= 3 && speedLevel >= 3 && damageLevel >= 3) {
                    rankOfTower = 3;
                    return;
                }
                rankOfTower = 2;
                return;
            }
            rankOfTower = 1;
        }
    }

    /**
     * Draws indicators to show the rank of the tower.
     */
    public void drawRankIndicator() {

        if (rangeLevel > rankOfTower || rangeLevel == 3) {
            for (int i = 1; i <= rangeLevel; i++) {
                app.fill(204, 0, 102);
                app.textSize(10);
                app.text("O", x + (i - 1) * (7), y + 8);
            }
        }

        if (damageLevel > rankOfTower || damageLevel == 3) {
            for (int i = 1; i <= damageLevel; i++) {
                app.fill(204, 0, 102);
                app.textSize(10);
                app.text("X", x + (i - 1) * (7), y + 32);
            }
        }

        if (speedLevel > rankOfTower||speedLevel == 3) {
            for (int i = 1; i <= speedLevel; i++) {
                app.noFill();
                app.stroke(102, 178, 255);
                app.strokeWeight(1 + i);
                app.rect(x + 4, y + 4, 23, 23);
                app.stroke(0);
                app.strokeWeight(1);
            }
        }

    }

    /**
     * Draws the tower on the game board, including its rank indicators and fireballs.
     */
    public void drawTower() {
        //check if hover on the tower, if hover draw preview
        float distanceToMouse = dist(x + 16, y + 16, app.currentMouseX, app.currentMouseY);
        if (distanceToMouse <= 16) {
            app.noFill();
            app.stroke(255, 255, 0);
            app.ellipse(x + 16, y + 16, towerRange * 2, towerRange * 2);
            app.stroke(0);
        }

        //draw tower
        app.image(getImageByRank(), x, y);
        this.drawRankIndicator();

        //draw fireball
        List<Fireball> removeFireList = new ArrayList<>();
        if (!fireballList.isEmpty() && !app.isPaused) {
            for (Fireball f : fireballList) {
                f.fireMove();
                f.drawFire();
                if (f.isReached() || f.isDisappear()) {
                    removeFireList.add(f);
                    f.checkMonsterStatus();
                }
            }
            fireballList.removeAll(removeFireList);
        }
    }

    public int getRankOfTower() {
        return rankOfTower;
    }

    public void setRankOfTower(int rankOfTower) {
        this.rankOfTower = rankOfTower;
    }

    public int getTowerRange() {
        return towerRange;
    }

    public void setTowerRange(int towerRange) {
        this.towerRange = towerRange;
    }

    public int getTowerDmg() {
        return towerDmg;
    }

    public void setTowerDmg(int towerDmg) {
        this.towerDmg = towerDmg;
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

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public int getRangeLevel() {
        return rangeLevel;
    }

    public void setRangeLevel(int rangeLevel) {
        this.rangeLevel = rangeLevel;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = damageLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public double getFireSpeed() {
        return new BigDecimal(fireSpeed).divide(new BigDecimal(app.getFastForward()), 2, RoundingMode.HALF_UP).doubleValue();
    }

    public void setFireSpeed(double fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tower tower = (Tower) o;
        return x == tower.x && y == tower.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
