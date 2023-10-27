package WizardTD;

/**
 * Represents a collection of buttons used for game controls in the WizardTD game.
 * Each button in this class is responsible for specific in-game functionalities
 * such as pausing the game, increasing the speed, building towers, and performing upgrades.
 */
public class GameButtons {
    private final App app;
    private Button btnFF;
    private Button btnP;
    private Button btnT;
    private Button btnU1;
    private Button btnU2;
    private Button btnU3;
    private Button btnM;



    private int intervalOfButtons = 60;

    /**
     * Constructs a set of game control buttons, each with specific functionality in the game.
     *
     * @param app Reference to the game's main application to facilitate drawing and interaction.
     */
    public GameButtons(App app) {
        this.app = app;
        this.btnFF = new Button(app, 650, 50, "FF", "2x speed", "");
        this.btnP = new Button(app, 650, 50 + intervalOfButtons, "P", "PAUSE", "");
        this.btnT = new Button(app, 650, 50 + 2 * intervalOfButtons, "T", "Build", "tower");
        this.btnU1 = new Button(app, 650, 50 + 3 * intervalOfButtons, "U1", "Upgrade", "range");
        this.btnU2 = new Button(app, 650, 50 + 4 * intervalOfButtons, "U2", "Upgrade", "speed");
        this.btnU3 = new Button(app, 650, 50 + 5 * intervalOfButtons, "U3", "Upgrade", "damage");
        this.btnM = new Button(app, 650, 50 + 6 * intervalOfButtons, "M", "Mana", "Pool");
    }

    public void buttonForFF() {
        btnFF.drawButton();
    }

    public void buttonForPause() {
        btnP.drawButton();
    }

    public void buttonForBuildTower() {
        btnT.drawButton();
    }

    public void buttonForUpgradeRange() {
        btnU1.drawButton();
    }

    public void buttonForUpgradeSpeed() {
        btnU2.drawButton();
    }

    public void buttonForUpgradeDamage() {
        btnU3.drawButton();
    }

    public void buttonForManaSpell() {
        btnM.drawButton();
    }


    /**
     * Displays all the game control buttons on the screen.
     * This includes buttons for fast-forwarding, pausing, building towers,
     * performing different types of tower upgrades, and using the mana spell.
     */
    public void showButtons() {
        this.buttonForFF();
        this.buttonForPause();
        this.buttonForBuildTower();
        this.buttonForUpgradeRange();
        this.buttonForUpgradeSpeed();
        this.buttonForUpgradeDamage();
        this.buttonForManaSpell();
    }

    public Button getBtnFF() {
        return btnFF;
    }

    public Button getBtnP() {
        return btnP;
    }

    public Button getBtnT() {
        return btnT;
    }

    public Button getBtnU1() {
        return btnU1;
    }

    public Button getBtnU2() {
        return btnU2;
    }

    public Button getBtnU3() {
        return btnU3;
    }

    public Button getBtnM() {
        return btnM;
    }


}
