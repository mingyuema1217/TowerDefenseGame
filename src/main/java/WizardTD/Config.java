package WizardTD;

import processing.data.JSONObject;
import processing.data.JSONArray;

/**
 * Represents the configuration for the WizardTD game, loaded from a JSON object.
 */
public class Config {
    private final String layout;

    private final JSONArray waves;

    private final int initialTowerRange;

    private final double initialTowerFiringSpeed;

    private final int initialTowerDamage;

    private final int initialMana;

    private final int initialManaCap;

    private final int initialManaGainedPerSecond;

    private final int towerCost;

    private final int manaPoolSpellInitialCost;

    private final int manaPoolSpellCostIncreasePerUse;

    private final double manaPoolSpellCapMultiplier;

    private final double manaPoolSpellManaGainedMultiplier;

    public Config(JSONObject config){
        this.layout = config.getString("layout");
        this.waves = config.getJSONArray("waves");
        this.initialTowerRange = config.getInt("initial_tower_range");
        this.initialTowerFiringSpeed = config.getDouble("initial_tower_firing_speed");
        this.initialTowerDamage = config.getInt("initial_tower_damage");
        this.initialMana = config.getInt("initial_mana");
        this.initialManaCap = config.getInt("initial_mana_cap");
        this.initialManaGainedPerSecond = config.getInt("initial_mana_gained_per_second");
        this.towerCost = config.getInt("tower_cost");
        this.manaPoolSpellInitialCost = config.getInt("mana_pool_spell_initial_cost");
        this.manaPoolSpellCostIncreasePerUse = config.getInt("mana_pool_spell_cost_increase_per_use");
        this.manaPoolSpellCapMultiplier = config.getDouble("mana_pool_spell_cap_multiplier");
        this.manaPoolSpellManaGainedMultiplier = config.getDouble("mana_pool_spell_mana_gained_multiplier");
    }

    public String getLayout() {
        return this.layout;
    }
    public JSONArray getWaves() {
        return this.waves;
    }

    public int getInitialTowerRange() {
        return this.initialTowerRange;
    }

    public double getInitialTowerFiringSpeed() {
        return this.initialTowerFiringSpeed;
    }

    public int getInitialTowerDamage() {
        return this.initialTowerDamage;
    }

    public int getInitialMana() {
        return this.initialMana;
    }

    public int getInitialManaCap() {
        return this.initialManaCap;
    }

    public int getInitialManaGainedPerSecond() {
        return this.initialManaGainedPerSecond;
    }

    public int getTowerCost() {
        return this.towerCost;
    }

    public int getManaPoolSpellInitialCost() {
        return this.manaPoolSpellInitialCost;
    }

    public int getManaPoolSpellCostIncreasePerUse() {
        return this.manaPoolSpellCostIncreasePerUse;
    }

    public double getManaPoolSpellCapMultiplier() {
        return this.manaPoolSpellCapMultiplier;
    }

    public double getManaPoolSpellManaGainedMultiplier() {
        return this.manaPoolSpellManaGainedMultiplier;
    }
}