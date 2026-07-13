package amar_maamary;

/**
 * Represents a light device. May be adjustable or not. Adjustable lights
 * consume power based on intensity level.
 *
 * @author amarmaamary
 */
public class Light extends Device {

    private boolean adjustable; // Whether the light supports intensity adjustment
    private int level;          // Intensity level (0–100)

    /**
     * Constructor for a non-adjustable, non-critical light.
     */
    public Light(int id, String name, double maxPowerConsumption) {
        super(id, name, maxPowerConsumption);
        setLight(false, 100);
    }

    /**
     * Constructor that specifies adjustability.
     */
    public Light(int id, String name, double maxPowerConsumption, boolean adjustable) {
        super(id, name, maxPowerConsumption);
        setLight(adjustable, 100);
    }

    /**
     * Full constructor for a critical or non-critical light.
     */
    public Light(int id, String name, double maxPowerConsumption, boolean critical, boolean adjustable) {
        super(id, name, maxPowerConsumption, critical);
        setLight(adjustable, 100);
    }

    /**
     * Initializes adjustability and level.
     */
    public void setLight(boolean adjustable, int level) {
        setAdjustable(adjustable);
        setLevel(level);
    }

    // -------------------- Getters and Setters --------------------
    
    public void setAdjustable(boolean adjustable) {
        this.adjustable = adjustable;
    }

    public boolean isAdjustable() {
        return adjustable;
    }

    /**
     * Sets the light level between 0 and 100. Defaults to 100 if invalid.
     */
    public void setLevel(int level) {
        if (level >= 0 && level <= 100) {
            this.level = level;
        } else {
            this.level = 100;
        }
    }
    
    public int getLevel() {
        return level;
    }


    /**
     * Turns the light on at 100% intensity.
     */
    @Override
    public void turnOn() {
        turnOn(100);
    }

    /**
     * Turns the light on at a specific intensity (if adjustable).
     */
    public void turnOn(int level) {
        if (adjustable) {
            setLevel(level);
        }
        super.turnOn();
    }

    /**
     * Returns current power usage based on level and status.
     */
    @Override
    public double getCurrentConsumption() {
        if (getStatus() == 1) {
            return adjustable
                    ? getMaxPowerConsumption() * level / 100.0
                    : getMaxPowerConsumption();
        }
        return 0.0;
    }

    /**
     * Returns a formatted string representing the light.
     */
    @Override
    public String toString() {
        return super.toString() + ", " + (adjustable ? "adjustable" : "not adjustable") + ", level=" + level;
    }
}
