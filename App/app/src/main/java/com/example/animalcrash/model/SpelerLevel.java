package com.example.animalcrash.model;

/**
 * @author Omer Erdem 500802898 IS107
 * Hiermee kan je de level van de speler berekenen
 */

public class SpelerLevel {
    private int xp;
    private int level;
    private int xpLeft;
    private int xpNeeded;
    private int progress;

    private final int EXTRA_XP_REQUIRED_PER_LEVEL = 100;

    public SpelerLevel(int xp) {
        this.xp = xp;
        this.level = 1;
        this.xpLeft = 0;
        this.xpNeeded = EXTRA_XP_REQUIRED_PER_LEVEL;
        this.progress = 0;

        calculateLevel();
    }

    public void calculateLevel(){
        xpLeft = xp;
        while (xpLeft >= xpNeeded) {
            xpLeft -= xpNeeded;
            xpNeeded += EXTRA_XP_REQUIRED_PER_LEVEL;
            level++;
        }
        progress = xpLeft * 100 / xpNeeded;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getXpLeft() {
        return xpLeft;
    }

    public int getXpNeeded() {
        return xpNeeded;
    }

    public int getProgress() {
        return progress;
    }

    public String dataResult(){
        StringBuilder str = new StringBuilder();
        str.append("Testdata:")
                .append(getXp()).append("\n")
                .append(getLevel()).append("\n")
                .append(getXpLeft()).append("\n")
                .append(getXpNeeded()).append("\n")
                .append(getProgress());
        return str.toString();
    }
}
