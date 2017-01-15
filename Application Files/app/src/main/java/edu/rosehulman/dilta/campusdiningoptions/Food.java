package edu.rosehulman.dilta.campusdiningoptions;

/**
 * Created by dilta on 1/15/2017.
 */

public class Food {

    private String name;
    private String calories;
    private String[] icons;

    public Food() {

    }

    public String getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }

    public String[] getIcons() {
        return icons;
    }

    public void setIcons(String[] icons) {
        this.icons = icons;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setName(String name) {
        this.name = name;
    }
}
