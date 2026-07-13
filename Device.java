package amar_maamary;

/**
 * Represents a generic electrical device in the smart home system.
 * @author amarmaamary
 */
public class Device {
    private int id;                          // Unique identifier between 100 and 999
    private String name;                     // Name of the device (e.g., "MainLight-Kitchen")
    private int status;                      // 0 = Off, 1 = On, 2 = Standby
    private double maxPowerConsumption;      // Max power usage in watts
    private boolean critical;                // Indicates if device is critical

    /**
     * Constructor for a non-critical device with default status (Off).
     */
    public Device(int id, String name, double maxPowerConsumption) {
        setDevice(id, name, maxPowerConsumption, false, 0);
    }

    /**
     * Constructor for a device where critical flag is specified.
     */
    public Device(int id, String name, double maxPowerConsumption, boolean critical) {
        setDevice(id, name, maxPowerConsumption, critical, 0);
    }

    /**
     * Initializes all attributes using setters for validation.
     */
    public void setDevice(int id, String name, double maxPowerConsumption, boolean critical, int status) {
        setId(id);
        setName(name);
        setMaxPowerConsumption(maxPowerConsumption);
        setCritical(critical);
        setStatus(status);
    }

    // -------------------- Getters and Setters --------------------

    public int getId() {
        return id;
    }

    /**
     * Sets the device ID. Must be between 100 and 999.
     */
    public void setId(int id) {
        if (id >= 100 && id <= 999) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the device. Cannot be null or blank.
     */
    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public int getStatus() {
        return status;
    }

    /**
     * Sets the device status (0 = Off, 1 = On, 2 = Standby).
     */
    public void setStatus(int status) {
        if (status >= 0 && status <= 2) {
            this.status = status;
        }
    }

    public double getMaxPowerConsumption() {
        return maxPowerConsumption;
    }

    /**
     * Sets the max power consumption. Must be positive; defaults to 50W if invalid.
     */
    public void setMaxPowerConsumption(double maxPowerConsumption) {
        if (maxPowerConsumption > 0) {
            this.maxPowerConsumption = maxPowerConsumption;
        } else {
            this.maxPowerConsumption = 50.0;
        }
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    // -------------------- Core Functional Methods --------------------

    /**
     * Turns the device ON (status = 1).
     */
    public void turnOn() {
        setStatus(1);
    }

    /**
     * Turns the device OFF (status = 0).
     */
    public void turnOff() {
        setStatus(0);
    }

    /**
     * Returns the current power consumption.
     * If ON, returns max consumption; otherwise 0.
     */
    public double getCurrentConsumption() {
        return (status == 1) ? maxPowerConsumption : 0.0;
    }

    // -------------------- Utility Methods --------------------

    @Override
    public String toString() {
        String state = (status == 0 ? "Off" : (status == 1 ? "On" : "Standby"));
        return "id= " + id + ", name=" + name + ", status=" + state
                + ", maximum power consumption=" + maxPowerConsumption
                + ", " + (critical ? "critical" : "not critical");
    }

    /**
     * Compares two devices based on ID, name, power, and criticality.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            Device d = (Device) obj;
            return d.id == id &&
                   d.name.equals(name) &&
                   d.maxPowerConsumption == maxPowerConsumption &&
                   d.critical == critical;
        }
        return false;
    }
}

