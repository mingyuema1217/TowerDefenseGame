package WizardTD;

/**
 * Represents the configuration settings for different types of monsters in the game.
 */
public class MonstersConfig {
    private final String type;

    private final int hp;

    private final float speed;

    private final double armour;

    private final int manaGainedOnKill;

    private final int quantity;

    /**
     * Initializes a new MonstersConfig with specified parameters.
     *
     * @param type The monster's type.
     * @param hp The monster's health points.
     * @param speed The monster's movement speed.
     * @param armour The monster's defence multiplier.
     * @param manaGainedOnKill Mana gained by player upon killing this monster.
     * @param quantity Number of this monster type in a wave.
     */
    public MonstersConfig(String type, int hp, float speed, double armour, int manaGainedOnKill, int quantity) {
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public double getArmour() {
        return armour;
    }

    public int getManaGainedOnKill() {
        return manaGainedOnKill;
    }

    public int getQuantity() {
        return quantity;
    }
}