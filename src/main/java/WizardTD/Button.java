package WizardTD;

/**
 * Represents a GUI button in the WizardTD game.
 */
public class Button {
    /** The x-coordinate of the button's top-left corner. */
    protected float x;

    /** The y-coordinate of the button's top-left corner. */
    protected float y;

    /** Reference to the game application for rendering. */
    protected App app;

    /** Indicates if the button is currently selected. */
    protected boolean isSelected = false;

    /** Indicates if the mouse cursor is hovering over the button. */
    protected boolean isHovered = false;

    /** Text label displayed on the button. */
    protected String label;

    /** First line of the button's description. */
    protected String description1;

    /** Second line of the button's description. */
    protected String description2;

    /** Indicates if the button action or choice is confirmed. */
    protected boolean isConfirmed = true;

    /**
     * Initializes a new Button.
     *
     * @param app The game application reference.
     * @param x The x-coordinate of the button's top-left corner.
     * @param y The y-coordinate of the button's top-left corner.
     * @param label Text label to be displayed on the button.
     * @param description1 First line of the button's description.
     * @param description2 Second line of the button's description.
     */
    public Button(App app, float x, float y, String label, String description1,String description2) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.label = label;
        this.description1 = description1;
        this.description2 = description2;
    }

    /**
     * Renders the button on the screen.
     */
    public void drawButton() {
        if(isHovered) {
            app.fill(160, 160, 160);
        }
        else if(isSelected||!isConfirmed) {
            app.fill(255, 255, 0);
        } else {
            app.noFill();
        }
        //Weighted Black border
        app.stroke(0);
        app.strokeWeight(3);
        app.rect(x, y, 50, 50);

        // Center the text in the button.
        float textWidth = app.textWidth(label);
        float textHeight = app.textAscent() + app.textDescent();

        float textX = x + (50 - textWidth) / 2;
        float textY = y + (50 - textHeight) / 2 + app.textAscent();

        //Label the button
        app.fill(0);
        app.text(label, textX, textY);
        //50 is button size, so plus 60 on x to move the text right behind the button
        app.textSize(11);
        app.text(description1,x+54,y+20);
        //second description should be located under the first one
        app.text(description2,x+54,y+20+textHeight);

        //Set features as default
        app.textSize(20);
        app.strokeWeight(1);
    }

    /**
     * Checks if a given point is within the button's area.
     *
     * @param px The x-coordinate of the point.
     * @param py The y-coordinate of the point.
     * @return true if the point is inside the button, false otherwise.
     */
    public boolean isInsideBtn(float px, float py) {
        return px >= x && px <= x + 50 && py >= y && py <= y + 50;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
