package WizardTD;

import static org.junit.jupiter.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class ManaTest {
    private Mana mana;
    private App mockApp;
    private Config mockConfig;
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
        mockApp = new App(){
            @Override
            public int getFastForward() {
                return 1;
            }
        };

        mockConfig = new Config(sampleConfig);
        mana = new Mana(mockApp, mockConfig);

    }

    @Test
    public void testInitialization() {
        assertEquals(100, mana.getCurrentMana());  // Assuming a getter for currentMana
        assertEquals(200, mana.getMaxMana());  // Assuming a getter for maxMana
    }

    @Test
    public void testManaGainedPerSecond() {
        mockApp.frameCount = 59;
        mana.manaGainedPerSecond();
        assertEquals(100, mana.getCurrentMana());

        mockApp.frameCount = 119;
        mana.manaGainedPerSecond();
        assertEquals(100, mana.getCurrentMana());

        // Fast forwarding through frames to reach mana cap
        for (int i = 0; i < 20; i++) {
            mockApp.frameCount += 59;
            mana.manaGainedPerSecond();
        }
        assertEquals(100, mana.getCurrentMana());
    }

    @Test
    public void testManaCost() {
        mana.manaCost(50);
        assertEquals(50, mana.getCurrentMana());
    }

    @Test
    public void testManaGain() {
        mana.manaGain(50);
        assertEquals(150, mana.getCurrentMana());
    }
}