package WizardTD;
/**
 * Represents the Mana resource in the game, responsible for tracking,
 * updating, and providing details about the player's current and maximum mana.
 * The class also handles the mechanism for gaining mana every second based on
 * certain conditions and multipliers.
 */
public class Mana {
    private final App app;
    private int currentMana;
    private int currSpellCost;
    private int maxMana;
    private Config config;
    private double manaMultiplier;
    private double CapMultiplier;

    /**
     * Constructs a new Mana object, initializing its properties based on
     * the provided configuration.
     *
     * @param app    The main application or game instance, used to determine
     *               certain game states and timing.
     * @param config The game configuration detailing initial mana values,
     *               gain rates, and other related settings.
     */
    public Mana(App app, Config config) {
        this.currentMana = config.getInitialMana();
        this.maxMana = config.getInitialManaCap();
        this.currSpellCost = config.getManaPoolSpellInitialCost();
        this.app = app;
        this.config = config;
        this.manaMultiplier = 1;
        this.CapMultiplier = 1;
    }

    /**
     * Calculates and updates the player's mana based on a predefined rate and
     * multipliers. This method is designed to be called every frame and will
     * increment the player's mana by a certain amount every second, or faster
     * based on the fast forward setting. The player's mana will not exceed
     * the maximum mana cap.
     */
    public void manaGainedPerSecond() {
        if (app.frameCount % (App.FPS / app.getFastForward()) == 0) {
            currentMana += (int) (manaMultiplier * config.getInitialManaGainedPerSecond());
            if (currentMana > maxMana) {
                currentMana = maxMana;
            }
        }
    }

    public void manaCost(int cost) {
        currentMana -= cost;
    }

    public void manaGain(int gain) {
        currentMana += (int) (manaMultiplier * gain);
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getCurrSpellCost() {
        return currSpellCost;
    }

    public void setCurrSpellCost(int currSpellCost) {
        this.currSpellCost = currSpellCost;
    }

    public double getManaMultiplier() {
        return manaMultiplier;
    }

    public void setManaMultiplier(double manaMultiplier) {
        this.manaMultiplier = manaMultiplier;
    }

    public double getCapMultiplier() {
        return CapMultiplier;
    }

    public void setCapMultiplier(double capMultiplier) {
        CapMultiplier = capMultiplier;
    }
}
