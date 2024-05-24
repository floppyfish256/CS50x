import java.awt.Color;

public class Quest {

    private String text;
    private Color color;

    public Quest(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }
}