package amar_maamary;

import java.util.Arrays;

/**
 * Represents a heavy appliance in the smart home (e.g., TV, washer). Appliances
 * have multiple power levels and can be noisy.
 *
 * @author amarmaamary
 */
public class Appliance extends Device {

    private int[] powerLevels;  // Array of allowed power percentages (e.g., {50, 100})
    private int currentLevel;   // Index of currently selected power level
    private boolean noisy;      // True if the appliance is noisy (shouldn't run at night)

    /**
     * Constructor for a non-critical appliance.
     */
    public Appliance(int id, String name, double maxPowerConsumption, int[] powerLevels, boolean noisy) {
        this(id, name, maxPowerConsumption, false, powerLevels, noisy);
    }

    /**
     * Constructor for an appliance with all attributes specified.
     */
    public Appliance(int id, String name, double maxPowerConsumption, boolean critical, int[] powerLevels, boolean noisy) {
        super(id, name, maxPowerConsumption, critical);
        setAppliance(powerLevels, noisy);
    }

    /**
     * Initializes appliance-specific attributes.
     */
    public void setAppliance(int[] powerLevels, boolean noisy) {
        setPowerLevels(powerLevels);
        setNoisy(noisy);
    }

    // -------------------- Getters and Setters --------------------
    
    /**
     * Validates and sets the allowed power levels.
     */
    public void setPowerLevels(int[] powerLevels) {
        if (powerLevels == null || powerLevels.length == 0) {
            System.out.println("Invalid: Power levels array is null or empty.");
            return;
        }

        boolean[] seen = new boolean[101];  // Used to detect duplicates
        int[] defaultLevels = new int[101];  // Default fallback [0..100]

        for (int i = 0; i < defaultLevels.length; i++) {
            defaultLevels[i] = i;
        }

        for (int level : powerLevels) {
            if (level < 1 || level > 100) {
                System.out.println("Invalid: Power level out of range (1–100): " + level);
                this.powerLevels = defaultLevels;
                return;
            }
            if (seen[level]) {
                System.out.println("Invalid: Duplicate power level found: " + level);
                this.powerLevels = defaultLevels;
                return;
            }
            seen[level] = true;
        }

        int[] sorted = Arrays.copyOf(powerLevels, powerLevels.length);
        Arrays.sort(sorted);
        this.powerLevels = sorted;
    }

    public int[] getPowerLevels() {
        return powerLevels;
    }

    /**
     * Sets the current power level index (0-based).
     */
    public void setCurrentLevel(int currentLevel) {
        if (currentLevel >= 0 && currentLevel < powerLevels.length) {
            this.currentLevel = currentLevel;
        } else {
            System.out.println("Invalid power level. Must be between 0 and " + (powerLevels.length - 1));
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    public boolean isNoisy() {
        return noisy;
    }

    /**
     * Turns the appliance on at default level (index 0).
     */
    @Override
    public void turnOn() {
        turnOn(0);
    }

    /**
     * Turns the appliance on at a specific power level index.
     */
    public void turnOn(int level) {
        if (level >= 0 && level < powerLevels.length) {
            this.currentLevel = level;
            super.turnOn();
        } else {
            System.out.println("Invalid power level");
        }
    }

    /**
     * Calculates the current power consumption based on level and status.
     */
    @Override
    public double getCurrentConsumption() {
        if (getStatus() != 1) {
            return 0.0;  // Not running
        }
        return getMaxPowerConsumption() * powerLevels[currentLevel] / 100.0;
    }

    /**
     * Returns a formatted string representing the appliance.
     */
    @Override
    public String toString() {
        String powerLevelsStr = Arrays.toString(powerLevels).replace("[", "").replace("]", "");
        return super.toString() + ", " + (isNoisy() ? "noisy" : "not noisy") + ", level " + currentLevel
                + ", Power levels=" + powerLevelsStr;
    }
}
