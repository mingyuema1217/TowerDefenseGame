package WizardTD;

/**
 * This class represents configurations and related functionalities
 * for building towers in the game.
 */
public class BuildConfig {
    /** Represents the range of a tower. */
    private int tower_range;

    /** The firing speed of a tower. */
    private double tower_firing_speed;

    /** Represents the damage a tower can deal. */
    private int tower_damage;

    /** Represents the cost to build a tower. */
    private int tower_cost;

    /** A reference to the game application. */
    private final App app;

    /** Contains game-wide configurations. */
    private Config config;

    /** Represents the player's mana. */
    private Mana mana;


    /**
     * Initializes an instance of BuildConfig with provided app, config, and mana.
     * @param app The game application reference.
     * @param config The game configurations.
     * @param mana The player's mana.
     */
    public BuildConfig(App app, Config config, Mana mana){
        this.tower_cost = config.getTowerCost();
        this.tower_damage = config.getInitialTowerDamage();
        this.tower_range = config.getInitialTowerRange();
        this.tower_firing_speed = config.getInitialTowerFiringSpeed();
        this.config = config;
        this.app = app;
        this.mana = mana;
    }

    /**
     * Visualizes how the tower will look like when placed on the map grid.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param rank Determines the appearance or capabilities of the tower.
     */
    public void buildTowerPreview(int x, int y, int rank){
        int gridX = (x / 32) * 32;
        int gridY = ((y -40)/ 32) * 32 + 40;
        //center the tower
        if (rank == 6){
            app.noFill();
            app.stroke(255, 255, 0);
            app.ellipse(gridX + 16, gridY + 16, (tower_range+32) * 2, (tower_range+32) * 2);
            app.stroke(0);
            app.image(app.tower1, gridX, gridY);
        }
        else if(rank == 1){
            app.noFill();
            app.stroke(255, 255, 0);
            app.ellipse(gridX + 16, gridY + 16, (tower_range+32) * 2, (tower_range+32) * 2);
            app.stroke(0);
            app.image(app.tower0, gridX, gridY);
        }
        else {
            app.noFill();
            app.stroke(255, 255, 0);
            app.ellipse(gridX + 16, gridY + 16, tower_range * 2, tower_range * 2);
            app.stroke(0);
            app.image(app.tower0, gridX, gridY);
        }
    }

    /**
     * A utility method to display text at a given position on the screen.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param text The text to be displayed.
     */
    public void showText(int x, int y, String text){
        app.fill(255);
        app.rect(x, y, 100, 20);
        app.fill(0);
        app.textSize(12);
        app.text(text, x + 5, y + 15);
        app.textSize(20);
    }

    /** Displays the individual upgrade costs for range, speed, and damage. */
    public void showUpgradeDetail(){
        app.fill(255);
        app.rect(650, 520, 100, 60);
        app.fill(0);
        app.textSize(12);
        app.text("Range: " + app.costForUpgradeRange, 650 + 5, 520 + 15);
        app.text("Speed: " + app.costForUpgradeSpeed, 650 + 5, 540 + 15);
        app.text("Damage: " + app.costForUpgradeDmg, 650 + 5, 560 + 15);
    }

    /** Presents a summary of all upgrade costs including a total sum. */
    public void showUpgradeInfo(){
        showText(650, 500, "Upgrade Cost");
        this.showUpgradeDetail();
        int total = app.costForUpgradeRange + app.costForUpgradeSpeed + app.costForUpgradeDmg;
        showText(650, 580, "Total: " + total);
    }

    /** Displays the cost of building a tower. */
    public void showTowerCost() {
        String text = "Tower Cost:" + this.tower_cost;
        int rectX = 640 - 100 - 10; //slightly on the left boarder of map
        int rectY = 180;
        this.showText(rectX,rectY, text);
    }

    /** Shows the cost associated with the current spell or action. */
    public void showManaPoolCost() {
        String text = "Mana Cost:" + this.mana.getCurrSpellCost();
        int rectX = 640 - 100 - 10; //slightly on the left boarder of map
        int rectY = 420;
        this.showText(rectX,rectY, text);
    }



    public int getTower_range() {
        return tower_range;
    }

    public void setTower_range(int tower_range) {
        this.tower_range = tower_range;
    }

    public double getTower_firing_speed() {
        return tower_firing_speed;
    }

    public void setTower_firing_speed(double tower_firing_speed) {
        this.tower_firing_speed = tower_firing_speed;
    }

    public int getTower_damage() {
        return tower_damage;
    }

    public void setTower_damage(int tower_damage) {
        this.tower_damage = tower_damage;
    }

    public int getTower_cost() {
        return tower_cost;
    }

    public void setTower_cost(int tower_cost) {
        this.tower_cost = tower_cost;
    }
}
