package WizardTD;

import java.util.*;

/**
 * Represents a wave of monsters in the WizardTD game. A wave is a group of monsters
 * that spawn over a period of time with specific configurations.
 */
public class Wave {
    private int duration;
    private double preWavePause;
    //config
    private List<MonstersConfig> monstersConfig;
    //real monsters
    private List<Monster> monsterList;

    private final App app;

    private long frameCount = 0;

    private int frameSum;

    private int waveStartFrame;


    /**
     * Constructs a new wave with specified configurations and timings.
     *
     * @param app Application reference.
     * @param monstersConfig List of monster configurations to determine type and quantity of monsters in the wave.
     * @param duration Duration in seconds for which this wave lasts.
     * @param preWavePause Time in seconds before this wave starts.
     * @param waveStartFrame Frame number at which the wave starts.
     */
    public Wave(App app, List<MonstersConfig> monstersConfig, int duration, double preWavePause, int waveStartFrame) {
        this.app = app;
        this.duration = duration;
        this.monstersConfig = monstersConfig;
        this.preWavePause = preWavePause;
        this.waveStartFrame = waveStartFrame;
        this.createMonster();
    }

    /**
     * Creates and initializes monsters for this wave based on the configurations provided.
     * This method also determines the frame interval at which each monster spawns.
     */
    public void createMonster() {
        int countMonster = 0;
        List<Monster> monsterList = new ArrayList<>();
        for (MonstersConfig monstersConfig : monstersConfig) {
            countMonster = countMonster + monstersConfig.getQuantity();
            for (int q = 0; q < monstersConfig.getQuantity(); q++) {
                Monster monster = new Monster(app, this, monstersConfig);
                monsterList.add(monster);
            }
        }

        //monster frame Interval
        this.frameSum = this.duration * App.FPS;

        int monsterFrameInterval = this.frameSum / countMonster;

        Collections.shuffle(monsterList);
        int monsterInWaveStartFrame = 0;
        for (Monster monster : monsterList) {
            monster.setMonsterInWaveStartFrame(monsterInWaveStartFrame);
            monsterInWaveStartFrame = monsterInWaveStartFrame + monsterFrameInterval;
        }
        this.setMonsterList(monsterList);
    }


    public int getFrameSum() {
        return frameSum;
    }

    public void setFrameSum(int frameSum) {
        this.frameSum = frameSum;
    }

    public long getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(long frameCount) {
        this.frameCount = frameCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<MonstersConfig> getMonstersConfig() {
        return monstersConfig;
    }

    public void setMonstersConfig(List<MonstersConfig> monstersConfig) {
        this.monstersConfig = monstersConfig;
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<Monster> monsterList) {
        this.monsterList = monsterList;
    }

    public double getPreWavePause() {
        return preWavePause;
    }

    public void setPreWavePause(double preWavePause) {
        this.preWavePause = preWavePause;
    }

    public void setWaveStartFrame(int waveStartFrame) {
        this.waveStartFrame = waveStartFrame;
    }

    public int getWaveStartFrame() {
        return waveStartFrame;
    }


}