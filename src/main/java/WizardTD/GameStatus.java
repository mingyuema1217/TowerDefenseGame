package WizardTD;

import static WizardTD.App.FPS;

/**
 * Represents the game status and provides methods for rendering on-screen status indicators.
 */
public class GameStatus {
    private final App app;
    private final WavesConfig wavesConfig;
    private int numOfWaves;
    private int timeOfNextWave;
    private int frameOfNextWave;
    private boolean notShowWaveIndicator = false;
    private int currentMana;
    private int maxMana;


    private Mana mana;

    /**
     * Initializes a new instance of the GameStatus class with the specified app, waves configuration, and mana settings.
     *
     * @param app            The main application or game instance where the game status will be displayed.
     * @param wavesConfig    The configuration detailing the waves of enemies in the game.
     * @param mana           The player's current mana and its associated settings.
     */
    public GameStatus(App app, WavesConfig wavesConfig, Mana mana) {
        this.app = app;
        this.wavesConfig = wavesConfig;
        this.frameOfNextWave = (int) wavesConfig.getWaveList().get(0).getPreWavePause() * FPS;
        this.currentMana = mana.getCurrentMana();
        this.maxMana = mana.getMaxMana();
        this.mana = mana;
    }

    /**
     * Displays a message indicating the time left until the next wave starts.
     */
    public void waveIndicator() {
        if (notShowWaveIndicator) {
            return;
        }
        app.fill(0);
        app.textSize(20);
        String waveInfo = "Wave " + (numOfWaves + 1) + " starts: " + timeOfNextWave;
        app.text(waveInfo, 10, 30);
    }


    /**
     * Displays the player's current mana and max mana as a bar and text.
     */
    public void manaIndicator() {
        app.fill(0);
        app.textSize(20);
        app.text("MANA:", 320, 30);
        //Mana display on the top of monster
        mana.manaGainedPerSecond();
        currentMana = mana.getCurrentMana();
        maxMana = mana.getMaxMana();
        int manaWidth = 340;
        float percentOfmana = (float) currentMana / maxMana;
        float blueWidth = percentOfmana * 340;
        float whiteWidth = manaWidth - blueWidth;

        app.strokeWeight(2);
        //draw white mana
        app.fill(72, 209, 204);
        app.rect(390, 12, blueWidth, 18);
        //draw blue mana
        app.fill(255, 255, 255);
        app.rect(390 + blueWidth, 12, whiteWidth, 18);
        app.strokeWeight(1);

        // Adding text in the middle of the bar

        String manaText = currentMana + "/" + maxMana;

        //center text int the mana indicator
        float textWidth = app.textWidth(manaText);
        float textHeight = app.textAscent() + app.textDescent();
        // Calculate the starting x and y coordinates for the text
        float textX = 390 + (manaWidth - textWidth) / 2;
        float textY = 12 + (18 - textHeight) / 2 + app.textAscent();

        app.textSize(15);
        app.fill(0);
        app.text(manaText, textX, textY);
        app.textSize(20);
    }

    public void fillBackground() {
        app.noStroke();
        // Fill the top bar to overlap monster
        app.fill(132, 112, 73);
        app.rect(0, 0, 640, 40);
        // Fill the sidebar to overlap monster
        app.fill(132, 112, 73);
        app.rect(640, 0, 120, 680);
        app.stroke(0);
        app.strokeWeight(1);
    }

    public void drawWinMessage() {
        app.textSize(50);
        app.fill(255, 255, 255);
        app.text("YOU WIN", 200, 250);
    }

    public void drawLoseMessage() {
        app.textSize(50);
        app.fill(255, 0, 0);
        app.text("YOU LOST", 200, 250);
        app.textSize(30);
        app.fill(255, 255, 255);
        app.text("Press 'r' to restart", 200, 300);
    }

    public void drawPauseMessage() {
        app.textSize(50);
        app.fill(255, 0, 0);
        app.text("Game Pause", 200, 250);
        app.textSize(30);
        app.fill(255, 255, 255);
        app.text("Press 'p' to continue", 200, 300);
    }


    public void showStatus() {
        this.fillBackground();
        this.waveIndicator();
        this.manaIndicator();
    }


    /**
     * Calculates and updates the time left until the next wave starts.
     */
    public void calculateTimeOfNext() {
        if (notShowWaveIndicator) {
            return;
        }
        //if we need to switch to the next wave
        if (this.getFrameOfNextWave() <= 0) {
            int index = this.getNumOfWaves();
            //get Current wave
            Wave currWave = this.wavesConfig.getWaveList().get(index);
            // if the current wave is the last wave, then don't show anything
            if (this.getNumOfWaves() == this.wavesConfig.getWaveList().size() - 1) {
                this.notShowWaveIndicator = true;
            } else {
                Wave nextWave = this.wavesConfig.getWaveList().get(index + 1);
                //get the frame of next wave starts which is current wave duration + next wave pause
                this.setFrameOfNextWave((int) nextWave.getPreWavePause() * FPS + currWave.getDuration() * FPS);
            }
            this.setNumOfWaves(this.getNumOfWaves() + 1);
        }

        this.frameOfNextWave = frameOfNextWave - app.getFastForward();

        this.timeOfNextWave = frameOfNextWave / FPS;
    }

    public int getNumOfWaves() {
        return numOfWaves;
    }

    public void setNumOfWaves(int numOfWaves) {
        this.numOfWaves = numOfWaves;
    }

    public int getFrameOfNextWave() {
        return frameOfNextWave;
    }

    public void setFrameOfNextWave(int frameOfNextWave) {
        if (this.frameOfNextWave != 0 || frameOfNextWave == 0) {
            return;
        }
        this.frameOfNextWave = frameOfNextWave;
    }

    public int getTimeOfNextWave() {
        return timeOfNextWave;
    }

    public void setTimeOfNextWave(int timeOfNextWave) {
        this.timeOfNextWave = timeOfNextWave;
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

    public App getApp() {
        return app;
    }

    public WavesConfig getWavesConfig() {
        return wavesConfig;
    }

    public boolean isNotShowWaveIndicator() {
        return notShowWaveIndicator;
    }

    public void setNotShowWaveIndicator(boolean notShowWaveIndicator) {
        this.notShowWaveIndicator = notShowWaveIndicator;
    }

    public Mana getMana() {
        return mana;
    }

    public void setMana(Mana mana) {
        this.mana = mana;
    }
}
