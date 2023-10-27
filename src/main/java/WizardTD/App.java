package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.math.BigDecimal;
import java.util.*;


public class App extends PApplet {
    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;
    public static int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH * CELLSIZE + TOPBAR;

    public static final int FPS = 60;

    public Random random = new Random();

    public String configPath;

    //Declear Configs, mana, map, game status, game buttons
    public JSONObject currConfig;
    public Config configs;
    public GameMap currMap;
    public GameStatus gameStatus;
    public GameButtons gameButtons;
    public Mana mana;
    public BuildConfig buildConfig;
    //Place tower button declaration
    private List<Button> buttonList;
    private boolean isBuildMode = false;
    private boolean isUpgradeRange = false;
    private boolean isUpgradeSpeed = false;
    private boolean isUpgradeDmg = false;
    public int currentMouseX = 0;
    public int currentMouseY = 0;
    private Set<Tower> towerSet;
    public int costForUpgradeRange;
    public int costForUpgradeSpeed;
    public int costForUpgradeDmg;

    //test for fireball
    public List<Monster> fullMonsterList;

    //Declear the PImage of elements
    public PImage path0, path1, path2, path3, shrub, wizard_house, grass;
    public PImage gremlin, gremlin1, gremlin2, gremlin3, gremlin4, gremlin5, worm, beetle;
    public PImage tower0, tower1, tower2, fireball;
    private Character[][] gameBoard;

    //Declear waves
    private WavesConfig wavesConfig;
    public boolean isGameWin = false;
    public boolean isGameLose = false;
    public boolean isPaused = false;
    public boolean isFastForward = false;

    private int fastForward = 1;

    private int dieMonster;

    private int totalMonster;
    private int waitWinFrames;
    private int currWaitFrame;


    // Feel free to add any additional methods or attributes you want. Please put classes in different files.
    public App() {
        this.configPath = "config.json";
    }


    /**
     * Retrieves the image associated with the specified type.
     *
     * @param type The type of the entity, valid values are "worm", "beetle", or any other.
     * @return Returns the image corresponding to the given type. If no match, returns a default image.
     */
    public PImage getImageByType(String type) {
        switch (type) {
            case "worm":
                return worm;
            case "beetle":
                return beetle;
            default:
                return gremlin;
        }
    }

    public Character[][] getGameBoard() {
        return gameBoard;
    }

    public Config getConfigs() {
        return configs;
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);
        //load configs
        currConfig = loadJSONObject(configPath);
        configs = new Config(currConfig);
        // Load images during setup
        this.loadImages();
        //load map
        currMap = new GameMap(configs, this);
        gameBoard = currMap.getGameBoard();

        totalMonster = 0;
        dieMonster = 0;
        //Monster Movements
        wavesConfig = new WavesConfig(this);


        //load game status and game button
        mana = new Mana(this, configs);
        gameStatus = new GameStatus(this, wavesConfig, mana);
        gameButtons = new GameButtons(this);
        buttonList = Arrays.asList(gameButtons.getBtnFF(), gameButtons.getBtnP(), gameButtons.getBtnT(), gameButtons.getBtnU1(), gameButtons.getBtnU2(), gameButtons.getBtnU3(), gameButtons.getBtnM());

        //building tower
        towerSet = new HashSet<>();
        buildConfig = new BuildConfig(this, configs, mana);

        //test for fireball
        this.fullMonsterList = new ArrayList<>();
        for (Wave wave : this.wavesConfig.getWaveList()) {
            this.fullMonsterList.addAll(wave.getMonsterList());
        }
        //declear variable
        isGameLose = false;
        isGameWin = false;
        isPaused = false;
        isBuildMode = false;
        isUpgradeRange = false;
        isUpgradeSpeed = false;
        isUpgradeDmg = false;
        isFastForward = false;
        fastForward = 1;
        waitWinFrames = 30;
        currWaitFrame = 0;
    }

    /**
     * Loads all necessary images for the game. This includes images for maps, monsters, and towers.
     */
    private void loadImages() {
        //load map images;
        tower0 = loadImage("src/main/resources/WizardTD/tower0.png");
        tower1 = loadImage("src/main/resources/WizardTD/tower1.png");
        tower2 = loadImage("src/main/resources/WizardTD/tower2.png");
        path0 = loadImage("src/main/resources/WizardTD/path0.png");
        path1 = loadImage("src/main/resources/WizardTD/path1.png");
        path2 = loadImage("src/main/resources/WizardTD/path2.png");
        path3 = loadImage("src/main/resources/WizardTD/path3.png");
        shrub = loadImage("src/main/resources/WizardTD/shrub.png");
        wizard_house = loadImage("src/main/resources/WizardTD/wizard_house.png");
        grass = loadImage("src/main/resources/WizardTD/grass.png");

        //load monsters
        gremlin = loadImage("src/main/resources/WizardTD/gremlin.png");
        gremlin1 = loadImage("src/main/resources/WizardTD/gremlin1.png");
        gremlin2 = loadImage("src/main/resources/WizardTD/gremlin2.png");
        gremlin3 = loadImage("src/main/resources/WizardTD/gremlin3.png");
        gremlin4 = loadImage("src/main/resources/WizardTD/gremlin4.png");
        gremlin5 = loadImage("src/main/resources/WizardTD/gremlin5.png");
        beetle = loadImage("src/main/resources/WizardTD/beetle.png");
        worm = loadImage("src/main/resources/WizardTD/worm.png");


        //load Tower
        tower0 = loadImage("src/main/resources/WizardTD/tower0.png");
        tower1 = loadImage("src/main/resources/WizardTD/tower1.png");
        tower2 = loadImage("src/main/resources/WizardTD/tower2.png");

        fireball = loadImage("src/main/resources/WizardTD/fireball.png");
    }

    /**
     * Increases the current mana based on the amount gained by killing a monster.
     * If the new mana exceeds the maximum allowed mana, it is capped at the maximum value.
     *
     * @param manaGain The base amount of mana gained from killing a monster.
     */
    public void manaGainedByKilling(int manaGain) {
        mana.setCurrentMana(mana.getCurrentMana() + (int) (mana.getManaMultiplier() * manaGain));
        if (mana.getCurrentMana() > mana.getMaxMana()) {
            mana.setCurrentMana(mana.getMaxMana());
        }
    }

    /**
     * Decreases the current mana based on the amount lost when a monster is banished.
     * If the new mana drops below 0, it is set to 0 and the game is marked as lost.
     *
     * @param manaLose The amount of mana to be deducted when a monster is banished.
     */
    public void banishedManaLose(int manaLose) {
        mana.setCurrentMana(mana.getCurrentMana() - manaLose);
        if (mana.getCurrentMana() < 0) {
            mana.setCurrentMana(0);
            isGameLose = true;
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {
        if (key == 'r' || key == 'R') {
            setup();
            frameCount = 0;
        }
        if (key == 'f' || key == 'F') {
            gameButtons.getBtnFF().setSelected(true);
            isFastForward = !isFastForward;
            if (isFastForward) {
                fastForward = 2;
            } else {
                fastForward = 1;
            }
        }

        if (key == 'p' || key == 'P') {
            gameButtons.getBtnP().setSelected(true);
            isPaused = !isPaused;
        }
        if (key == 't' || key == 'T') {
            gameButtons.getBtnT().setSelected(true);
            //change the state of the multiple click
            if (gameButtons.getBtnT().isConfirmed) {
                gameButtons.getBtnT().setConfirmed(false);
                isBuildMode = true;
            } else {
                gameButtons.getBtnT().setConfirmed(true);
                isBuildMode = false;
            }
        }

        if (key == '1') {
            if (gameButtons.getBtnU1().isConfirmed) {
                gameButtons.getBtnU1().setConfirmed(false);
                isUpgradeRange = true;
            } else {
                gameButtons.getBtnU1().setConfirmed(true);
                isUpgradeRange = false;
            }
        }
        if (key == '2') {
            //change the state of the multiple click
            if (gameButtons.getBtnU2().isConfirmed) {
                gameButtons.getBtnU2().setConfirmed(false);
                isUpgradeSpeed = true;
            } else {
                gameButtons.getBtnU2().setConfirmed(true);
                isUpgradeSpeed = false;
            }
        }
        if (key == '3') {
            //change the state of the multiple click
            if (gameButtons.getBtnU3().isConfirmed) {
                gameButtons.getBtnU3().setConfirmed(false);
                isUpgradeDmg = true;
            } else {
                gameButtons.getBtnU3().setConfirmed(true);
                isUpgradeDmg = false;
            }
        }


        if (key == 'm' || key == 'M') {
            gameButtons.getBtnM().setSelected(true);
            handleManaPool();
        }

    }


    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {
        for (Button btn : buttonList) {
            btn.setSelected(false);
        }
    }

    /**
     * Handles the operations related to the mana pool.
     * If the current mana exceeds or is equal to the current spell cost, it deducts the cost,
     * increases the maximum mana based on a multiplier, adjusts the spell cost for future usage,
     * and modifies the mana gain multiplier.
     */
    private void handleManaPool() {
        if (mana.getCurrentMana() >= mana.getCurrSpellCost()) {
            mana.manaCost(mana.getCurrSpellCost());
            mana.setMaxMana((int) (mana.getMaxMana() * configs.getManaPoolSpellCapMultiplier()));
            mana.setCurrSpellCost(mana.getCurrSpellCost() + configs.getManaPoolSpellCostIncreasePerUse());
            mana.setManaMultiplier(mana.getManaMultiplier() + configs.getManaPoolSpellManaGainedMultiplier() - 1);
        }
    }

    /**
     * Handles the hover effect for buttons.
     * Updates the current mouse X and Y positions and checks if they hover over any buttons.
     *
     * @param e MouseEvent containing the current mouse details.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        for (Button btn : buttonList) {
            handleButtonHover(btn, currentMouseX, currentMouseY);
        }

    }

    /**
     * Handles the mouse press events.
     * If in build mode and has enough mana, it allows for tower placement.
     * Else, checks if any button was pressed.
     *
     * @param e MouseEvent containing the details of the mouse press event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        //entering building model
        if (isBuildMode) {
            if (mana.getCurrentMana() > buildConfig.getTower_cost()) {
                if (currMap.canPlaceTower(currentMouseX, currentMouseY)) {
                    buildConfig.buildTowerPreview(mouseX, mouseY, 0);
                }
            }
        }
        //regular pressed button
        for (Button btn : buttonList) {
            if (btn.isInsideBtn(mouseX, mouseY)) {
                handleButtonPressed(btn, mouseX, mouseY);
            }
        }
    }

    /**
     * Handles the mouse release events.
     * Checks if any button was released.
     *
     * @param e MouseEvent containing the details of the mouse release event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        for (Button btn : buttonList) {
            handleButtonRelease(btn, mouseX, mouseY);
        }
    }


    /**
     * Handles the button press events and updates the game state accordingly.
     * This method checks which button was pressed and toggles the state of various game settings
     * such as Fast Forward, Pause, Build Mode, and different upgrades.
     *
     * @param btn    The button that was pressed.
     * @param mouseX The x-coordinate of the mouse click.
     * @param mouseY The y-coordinate of the mouse click.
     */
    private void handleButtonPressed(Button btn, int mouseX, int mouseY) {
        btn.setHovered(false);
        btn.setSelected(true);
        if (btn.equals(gameButtons.getBtnFF())) {
            isFastForward = !isFastForward;
            if (isFastForward) {
                fastForward = 2;
            } else {
                fastForward = 1;
            }
        }
        if (btn.equals(gameButtons.getBtnP())) {
            isPaused = !isPaused;
        }
        if (btn.equals(gameButtons.getBtnT())) {
            //change the state of the multiple click
            if (gameButtons.getBtnT().isConfirmed) {
                gameButtons.getBtnT().setConfirmed(false);
                isBuildMode = true;
            } else {
                gameButtons.getBtnT().setConfirmed(true);
                isBuildMode = false;
            }
        }
        if (btn.equals(gameButtons.getBtnU1())) {
            //change the state of the multiple click
            if (gameButtons.getBtnU1().isConfirmed) {
                gameButtons.getBtnU1().setConfirmed(false);
                isUpgradeRange = true;
            } else {
                gameButtons.getBtnU1().setConfirmed(true);
                isUpgradeRange = false;
            }
        }
        if (btn.equals(gameButtons.getBtnU2())) {
            //change the state of the multiple click
            if (gameButtons.getBtnU2().isConfirmed) {
                gameButtons.getBtnU2().setConfirmed(false);
                isUpgradeSpeed = true;
            } else {
                gameButtons.getBtnU2().setConfirmed(true);
                isUpgradeSpeed = false;
            }

        }
        if (btn.equals(gameButtons.getBtnU3())) {
            //change the state of the multiple click
            if (gameButtons.getBtnU3().isConfirmed) {
                gameButtons.getBtnU3().setConfirmed(false);
                isUpgradeDmg = true;
            } else {
                gameButtons.getBtnU3().setConfirmed(true);
                isUpgradeDmg = false;
            }
        }

        if (btn.equals(gameButtons.getBtnM())) {
            handleManaPool();
        }
    }


    /**
     * Handles the button release events, performing game actions such as building towers or upgrading them.
     * This method checks which button was released and performs the associated action. Actions can include
     * placing a tower on the map, upgrading a tower's range, speed, or damage.
     *
     * @param btn    The button that was released.
     * @param mouseX The x-coordinate of the mouse release.
     * @param mouseY The y-coordinate of the mouse release.
     */
    private void handleButtonRelease(Button btn, int mouseX, int mouseY) {
        btn.setSelected(false);
        if (btn.equals(gameButtons.getBtnT())) {
            if (isBuildMode && currMap.canPlaceTower(currentMouseX, currentMouseY) && mana.getCurrentMana() >= buildConfig.getTower_cost()) {
                mana.manaCost(buildConfig.getTower_cost());
                Tower t = new Tower(currentMouseX, currentMouseY, configs, this, 0);
                towerSet.add(t);
                isBuildMode = false;
                // default confirmed
                btn.setConfirmed(true);
            }
        }
        if (btn.equals(gameButtons.getBtnU1())) {
            if (isUpgradeRange) {
                for (Tower t : towerSet) {
                    float distanceToMouse = dist(t.getX() + 16, t.getY() + 16, currentMouseX, currentMouseY);
                    if (distanceToMouse <= 16) {
                        int upgradeRangeCost = t.getUpgradeCost(t.getRangeLevel());
                        if (mana.getCurrentMana() >= upgradeRangeCost && t.getRangeLevel() < 3) {
                            mana.setCurrentMana(mana.getCurrentMana() - upgradeRangeCost);
                            t.setRangeLevel(t.getRangeLevel() + 1);
                            t.setTowerRange(t.getTowerRange() + 32);
                        }
                        isUpgradeRange = false;
                        btn.setConfirmed(true);
                    }
                }
            }
        }
        if (btn.equals(gameButtons.getBtnU2())) {
            if (isUpgradeSpeed) {
                for (Tower t : towerSet) {
                    float distanceToMouse = dist(t.getX() + 16, t.getY() + 16, currentMouseX, currentMouseY);
                    if (distanceToMouse <= 16) {
                        int upgradeSpeedCost = t.getUpgradeCost(t.getSpeedLevel());
                        if (mana.getCurrentMana() >= upgradeSpeedCost && t.getSpeedLevel() < 3) {
                            mana.setCurrentMana(mana.getCurrentMana() - upgradeSpeedCost);
                            t.setSpeedLevel(t.getSpeedLevel() + 1);
                            t.setFireSpeed(t.getFireSpeed() - 0.5);
                        }
                        isUpgradeSpeed = false;
                        btn.setConfirmed(true);
                    }
                }
            }
        }
        if (btn.equals(gameButtons.getBtnU3())) {
            if (isUpgradeDmg) {
                for (Tower t : towerSet) {
                    float distanceToMouse = dist(t.getX() + 16, t.getY() + 16, currentMouseX, currentMouseY);
                    if (distanceToMouse <= 16) {
                        int upgradeDmgCost = t.getUpgradeCost(t.getDamageLevel());
                        if (mana.getCurrentMana() >= upgradeDmgCost && t.getDamageLevel() < 3) {
                            mana.setCurrentMana(mana.getCurrentMana() - upgradeDmgCost);
                            t.setDamageLevel(t.getDamageLevel() + 1);
                            t.setTowerDmg(BigDecimal.valueOf(1.5).multiply(new BigDecimal(t.getTowerDmg())).intValue());
                        }
                        isUpgradeDmg = false;
                        btn.setConfirmed(true);
                    }
                }
            }
        }
    }


    /**
     * Checks if the mouse is hovering over the given button and updates the button's hover state.
     * The hover state of the button is updated based on whether the mouse's current position is inside the button's bounds.
     *
     * @param btn    The button to check for hover.
     * @param mouseX The x-coordinate of the current mouse position.
     * @param mouseY The y-coordinate of the current mouse position.
     */
    private void handleButtonHover(Button btn, int mouseX, int mouseY) {
        btn.setHovered(btn.isInsideBtn(mouseX, mouseY));
    }

    /**
     * Determines if the game is currently in any of the tower upgrade modes.
     * <p>
     * The game can be in one of several upgrade modes: range upgrade, speed upgrade, or damage upgrade.
     * This method checks if any of these modes are currently active.
     * </p>
     *
     * @return true if the game is in any upgrade mode, false otherwise.
     */
    public boolean isUpgradeMode() {
        return isUpgradeRange || isUpgradeSpeed || isUpgradeDmg;
    }



    /**
     * Displays hover text for various buttons when they are being hovered over by the mouse.
     * When a button is hovered, certain information relevant to that button is shown to the user.
     * For instance, when the tower build button (btnT) is hovered, the cost of building a tower is displayed.
     * Similarly, if any of the upgrade modes are active, hovering will display upgrade information.
     * Hovering over the mana pool button will show its cost.
     */
    public void showHoverText() {
        //show text when hover
        //text for btnT
        if (gameButtons.getBtnT().isHovered) {
            buildConfig.showTowerCost();
        }
        if (this.isUpgradeMode()) {
            buildConfig.showUpgradeInfo();
        }
        if (gameButtons.getBtnM().isHovered) {
            buildConfig.showManaPoolCost();
        }
    }

    /**
     * Handles the display of tower previews and upgrade costs based on button interactions and user input.
     * <p>
     * This method controls the visual feedback given to users when interacting with buttons or hovering
     * over towers. When the tower build button (btnT) is active, a preview of the tower is shown if the
     * current location can accommodate a tower and the user has enough mana. If any upgrade modes are
     * active, this method will calculate and display the respective costs for the upgrades.
     * The method determines the proximity of the mouse to the tower, and if close enough,
     * retrieves the upgrade cost for range, speed, or damage based on the upgrade mode selected.
     * </p>
     */
    public void showButtonOperations() {
        costForUpgradeRange = 0;
        costForUpgradeSpeed = 0;
        costForUpgradeDmg = 0;

        //preview the tower when btnT
        if (isBuildMode && currMap.canPlaceTower(currentMouseX, currentMouseY) && mana.getCurrentMana() >= buildConfig.getTower_cost()) {
            //show cost when hover
            int rank = 0;
            if (isUpgradeMode()) {
                if (isUpgradeRange) {
                    costForUpgradeRange = 20;
                    rank += 1;
                }
                if (isUpgradeSpeed) {
                    costForUpgradeSpeed = 20;
                    rank += 2;
                }
                if (isUpgradeDmg) {
                    costForUpgradeDmg = 20;
                    rank += 3;
                }
            }

            buildConfig.buildTowerPreview(currentMouseX, currentMouseY, rank);
        }

        //show upgrade cost;
        if (isUpgradeMode()) {
            for (Tower t : towerSet) {
                float distanceToMouse = dist(t.getX() + 16, t.getY() + 16, currentMouseX, currentMouseY);
                if (distanceToMouse <= 16) {
                    if (isUpgradeRange) {
                        costForUpgradeRange = t.getUpgradeCost(t.getRangeLevel());
                    }
                    if (isUpgradeSpeed) {
                        costForUpgradeSpeed = t.getUpgradeCost(t.getSpeedLevel());
                    }
                    if (isUpgradeDmg) {
                        costForUpgradeDmg = t.getUpgradeCost(t.getDamageLevel());
                    }

                }
            }
        }

    }

    /**
     * Handles the movement of all monsters in every wave during the game's update cycle.
     * This method iterates over all waves in the game, updating the position and status
     * of every monster present in each wave. Monsters are updated based on their internal
     * logic (handled by the {@code tick()} method). Additionally, the method also updates
     * the frame count for each wave, taking into account any fast-forward settings applied.
     */
    public void monsterMove() {
        for (int i = 0; i < this.gameStatus.getNumOfWaves(); i++) {
            Wave currWave = this.wavesConfig.getWaveList().get(i);

            List<Monster> monsterList = currWave.getMonsterList();
            for (Monster monster : monsterList) {
                monster.tick();
            }
            //frame count
            currWave.setFrameCount(currWave.getFrameCount() + getFastForward());
        }

    }

    /**
     * Determines if the player has achieved a genuine victory in the game.
     * The method checks if the game state is set to win (`isGameWin`). If the game is in a win state,
     * it increments the current waiting frame count (`currWaitFrame`). Once the current waiting frame count
     * reaches a predefined threshold (`waitWinFrames`), it acknowledges the win as genuine and returns true.
     *
     * @return {@code true} if the player has genuinely won after waiting for the specified frames, otherwise {@code false}.
     */
    private boolean isRealWin() {
        if (isGameWin) {
            currWaitFrame++;
            if (currWaitFrame >= waitWinFrames) {
                return true;
            }
        }
        return false;
    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        if (isGameLose) {
            gameStatus.drawLoseMessage();
        } else if (isRealWin()) {
            gameStatus.drawWinMessage();
        } else if (isPaused) {
            //test for place tower
            for (Tower t : towerSet) {
                t.shoot(this.fullMonsterList);
                t.drawTower();
            }
            gameStatus.showStatus();
            gameButtons.showButtons();
            this.showHoverText();
            currMap.drawHouse();
            gameStatus.drawPauseMessage();
        } else if (isFastForward) {
            frameCount = frameCount;
            //draw map
            background(132, 112, 73);
            currMap.drawMap();
            gameStatus.calculateTimeOfNext();
            this.monsterMove();

            this.showButtonOperations();
            //test for place tower
            for (Tower t : towerSet) {
                t.shoot(this.fullMonsterList);
                t.drawTower();
            }
            gameStatus.showStatus();
            gameButtons.showButtons();
            this.showHoverText();
            currMap.drawHouse();
        } else {
            //draw map
            background(132, 112, 73);
            currMap.drawMap();
            gameStatus.calculateTimeOfNext();
            this.monsterMove();

            this.showButtonOperations();
            //test for place tower
            for (Tower t : towerSet) {
                t.shoot(this.fullMonsterList);
                t.drawTower();
            }
            gameStatus.showStatus();
            gameButtons.showButtons();
            this.showHoverText();
            currMap.drawHouse();
        }
    }

    public int getFastForward() {
        return fastForward;
    }

    public int getDieMonster() {
        return dieMonster;
    }

    public int getTotalMonster() {
        return totalMonster;
    }

    public void setDieMonster(int dieMonster) {
        this.dieMonster = dieMonster;
    }

    public void setTotalMonster(int totalMonster) {
        this.totalMonster = totalMonster;
    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }


}
