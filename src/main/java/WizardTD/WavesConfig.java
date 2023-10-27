package WizardTD;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration for all waves in the WizardTD game.
 * Reads wave configurations from a provided JSON format and creates a list of Wave objects accordingly.
 */
public class WavesConfig {
    private final List<Wave> waveList;
    private final App app;

    /**
     * Constructs a new waves configuration based on the provided application reference.
     *
     * @param app Application reference.
     */
    public WavesConfig(App app) {
        this.app = app;
        this.waveList = new ArrayList<>();
        this.getWaves(app.getConfigs().getWaves());
    }

    /**
     * Reads the wave configurations from the provided JSON array,
     * and initializes the waves with their respective configurations.
     *
     * @param waves JSONArray containing wave configurations.
     */
    public void getWaves(JSONArray waves) {
        int preWavePauseFrameCount = 0;
        int durationFrameCount = 0;
        for (int i = 0; i < waves.size(); i++) {
            JSONObject value = waves.getJSONObject(i);
            double preWavePause = value.getDouble("pre_wave_pause");
            int duration = value.getInt("duration");
            JSONArray monsters = value.getJSONArray("monsters");
            List<MonstersConfig> monstersConfig = new ArrayList<>();
            for (int j = 0; j < monsters.size(); j++) {
                JSONObject monsterItem = monsters.getJSONObject(j);
                String type = monsterItem.getString("type");
                int hp = monsterItem.getInt("hp");
                float speed = monsterItem.getFloat("speed");
                double armour = monsterItem.getDouble("armour");
                int manaGainedOnKill = monsterItem.getInt("mana_gained_on_kill");
                int quantity = monsterItem.getInt("quantity");
                app.setTotalMonster(app.getTotalMonster() + quantity);
                MonstersConfig monster = new MonstersConfig(type, hp, speed, armour, manaGainedOnKill, quantity);
                monstersConfig.add(monster);
            }

            preWavePauseFrameCount = preWavePauseFrameCount + (int) preWavePause * App.FPS;
            if (i != 0) {
                durationFrameCount = waveList.get(i - 1).getDuration() * App.FPS + durationFrameCount;
            }
            int waveStartFrame = durationFrameCount + preWavePauseFrameCount;
            Wave wave = new Wave(app, monstersConfig, duration, preWavePause, waveStartFrame);
            this.waveList.add(wave);
        }
    }

    /**
     * Retrieves the list of all waves.
     *
     * @return List of Wave objects, each representing a wave in the game.
     */
    public List<Wave> getWaveList() {
        return waveList;
    }
}