package WizardTD;

import static processing.core.PApplet.dist;

/**
 * Represents a fireball that is cast from a tower and targets a monster in the game.
 */
public class Fireball {
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private double speed;
    private final App app;
    private Monster monster;
    private Tower tower;
    private boolean isDisappear;

    /**
     * Constructs a Fireball targeting a specified monster, originating from a specific location.
     *
     * @param startX Starting x-coordinate of the fireball.
     * @param startY Starting y-coordinate of the fireball.
     * @param monster Target monster.
     * @param app Reference to the game's main application.
     * @param tower The tower that casts this fireball.
     */
    public Fireball(int startX, int startY, Monster monster, App app, Tower tower) {
        this.x = startX;
        this.y = startY;
        this.targetX = monster.getXCenter();
        this.targetY = monster.getYCenter();
        this.app = app;
        this.monster = monster;
        this.speed = 5;
        this.tower = tower;
    }

    /**
     * Checks if the fireball has reached its target (the monster).
     *
     * @return true if the fireball has reached its target; false otherwise.
     */
    public boolean isReached() {
        if (x == targetX) {
            if (y == targetY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the status of the monster being targeted. If the fireball has reached the monster and has not disappeared,
     * the monster will lose a certain amount of HP based on the tower's damage value.
     */
    public void checkMonsterStatus() {
        if (isReached() && !isDisappear) {
            monster.hpLose(tower.getTowerDmg());
        }
    }

    /**
     * Moves the fireball towards its target. The movement is based on the speed and direction to the target.
     */
    public void fireMove() {
        float distance = dist(x, y, targetX, targetY);
        if (distance < speed) {
            x = targetX;
            y = targetY;
        } else {
            float dx = targetX - x;
            float dy = targetY - y;
            x += dx / distance * speed;
            y += dy / distance * speed;
        }
        float towerRangeDis = dist(x, y, tower.getX(), tower.getY());
        if (towerRangeDis > tower.getTowerRange()) {
            this.isDisappear = true;
        }
    }

    /**
     * Draws the fireball on the screen. If the monster has vanished or the fireball has disappeared,
     * it will not be drawn.
     */
    public void drawFire() {
        if (monster.isVanish() || isDisappear) {
            return;
        }
        app.image(app.fireball, x, y);
    }

    /**
     * Checks if the fireball has disappeared.
     *
     * @return true if the fireball has disappeared; false otherwise.
     */
    public boolean isDisappear() {
        return isDisappear;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public double getSpeed() {
        return speed;
    }

    public App getApp() {
        return app;
    }

    public Monster getMonster() {
        return monster;
    }

    public Tower getTower() {
        return tower;
    }
}
