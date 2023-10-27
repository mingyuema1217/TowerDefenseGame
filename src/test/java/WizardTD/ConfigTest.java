package WizardTD;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.data.JSONArray;
import processing.data.JSONObject;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigTest {
    private Config config;
    JSONArray SampleJsonArray;
    JSONArray SampleValue;

    @BeforeEach
    public void setUp() {
        SampleJsonArray = new JSONArray();
        SampleValue = new JSONArray();
        SampleValue.setInt(0, 1);
        JSONObject sampleConfig = new JSONObject();
        sampleConfig.setString("layout", "SampleLayout");
        sampleConfig.setJSONArray("waves", SampleValue);
        sampleConfig.setInt("initial_tower_range", 5);
        sampleConfig.setDouble("initial_tower_firing_speed", 1.5);
        sampleConfig.setInt("initial_tower_damage", 10);
        sampleConfig.setInt("initial_mana", 100);
        sampleConfig.setInt("initial_mana_cap", 200);
        sampleConfig.setInt("initial_mana_gained_per_second", 5);
        sampleConfig.setInt("tower_cost", 50);
        sampleConfig.setInt("mana_pool_spell_initial_cost", 30);
        sampleConfig.setInt("mana_pool_spell_cost_increase_per_use", 5);
        sampleConfig.setDouble("mana_pool_spell_cap_multiplier", 1.2);
        sampleConfig.setDouble("mana_pool_spell_mana_gained_multiplier", 2.0);
        config = new Config(sampleConfig);
    }

    @Test
    public void testGetLayout() {
        assertEquals("SampleLayout", config.getLayout());
    }

    @Test
    public void testGetWaves() {
        JSONArray expectedWaves = new JSONArray();
        //expectedWaves
        expectedWaves.setInt(0, 1);

        JSONArray actualWaves = config.getWaves();

        assertNotNull(actualWaves);

        // Compare each element
        assertEquals(expectedWaves.size(), actualWaves.size());
        for (int i = 0; i < expectedWaves.size(); i++) {
            assertEquals(expectedWaves.getInt(i), actualWaves.getInt(i));
        }
    }

    @Test
    public void testGetInitialTowerRange() {
        assertEquals(5, config.getInitialTowerRange());
    }

    @Test
    public void testGetInitialTowerFiringSpeed() {
        assertEquals(1.5, config.getInitialTowerFiringSpeed(),0.001);
    }

    @Test
    public void testGetInitialTowerDamage() {
        assertEquals(10, config.getInitialTowerDamage());
    }

    @Test
    public void testGetInitialMana() {
        assertEquals(100, config.getInitialMana());
    }

    @Test
    public void testGetInitialManaCap() {
        assertEquals(200, config.getInitialManaCap());
    }

    @Test
    public void testGetInitialManaGainedPerSecond() {
        assertEquals(5, config.getInitialManaGainedPerSecond());
    }

    @Test
    public void testGetTowerCost() {
        assertEquals(50, config.getTowerCost());
    }

    @Test
    public void testGetManaPoolSpellInitialCost() {
        assertEquals(30, config.getManaPoolSpellInitialCost());
    }

    @Test
    public void testGetManaPoolSpellCostIncreasePerUse() {
        assertEquals(5, config.getManaPoolSpellCostIncreasePerUse());
    }

    @Test
    public void testGetManaPoolSpellCapMultiplier() {
        assertEquals(1.2, config.getManaPoolSpellCapMultiplier(),0.001);
    }

    @Test
    public void testGetManaPoolSpellManaGainedMultiplier() {
        assertEquals(2.0, config.getManaPoolSpellManaGainedMultiplier(),0.001);
    }
}

