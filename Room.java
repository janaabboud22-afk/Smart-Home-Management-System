package amar_maamary;

import java.util.ArrayList;

/**
 * Represents a room in the smart home system. Each room has a unique code, a
 * description, and a list of associated devices.
 *
 * @author lapto
 */
public class Room {

    private String code;                    // Unique room code (e.g., "LR", "K1")
    private String description;             // Description of the room
    private ArrayList<Device> devicesList;  // List of devices in the room

    /**
     * Constructor with only code. Uses default description.
     */
    public Room(String code) {
        this(code, "No description");
    }

    /**
     * Constructor with both code and description.
     */
    public Room(String code, String description) {
        setCode(code);
        setDescription(description);
        devicesList = new ArrayList<>();
    }

    // -------------------- Getters and Setters --------------------
    /**
     * Sets the room code. Uses default "#123456" if null or empty.
     */
    public void setCode(String code) {
        if (code == null || code.isEmpty()) {
            this.code = "#123456";
        } else {
            this.code = code;
        }
    }

    public String getCode() {
        return code;
    }

    /**
     * Sets the room description. Defaults to "No Description Provided" if
     * invalid.
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            this.description = "No Description Provided";
        } else {
            this.description = description;
        }
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Device> getDevicesList() {
        return devicesList;
    }

    /**
     * Returns the total current power consumption of all devices in the room.
     */
    public double getCurrentConsumption() {
        double totalConsumption = 0;
        for (Device device : devicesList) {
            totalConsumption += device.getCurrentConsumption();
        }
        return totalConsumption;
    }

    /**
     * Returns the number of lights in the room.
     */
    public int getNbLights() {
        int count = 0;
        for (Device device : devicesList) {
            if (device instanceof Light) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of appliances in the room.
     */
    public int getNbAppliances() {
        int count = 0;
        for (Device device : devicesList) {
            if (device instanceof Appliance) {
                count++;
            }
        }
        return count;
    }

    // -------------------- Device Management --------------------
    /**
     * Adds a device to the room if it's not already present.
     */
    public void addDevice(Device d) {
        if (searchDeviceById(d.getId()) == null) {
            devicesList.add(d);
            System.out.println("Device with ID " + d.getId() + " added successfully.");
        } else {
            System.out.println("Device with ID " + d.getId() + " already exists.");
        }
    }

    /**
     * Removes a device from the room if it exists.
     *
     * @param d Device to remove
     */
    public void removeDevice(Device d) {
        Device foundDevice = searchDeviceById(d.getId());
        if (foundDevice == null) {
            System.out.println("Device not found in the list.");
            return;
        }
        devicesList.remove(foundDevice);
        System.out.println("Device removed successfully.");
    }

    /**
     * Searches for a device in the room by its ID.
     *
     * @return The matching device, or null if not found
     */
    public Device searchDeviceById(int id) {
        for (Device device : devicesList) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    // -------------------- String Representations --------------------
    /**
     * Returns a full description of the room including its devices.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();// Create a StringBuilder to build the string 
        String s = "Room Code: " + code + "\nDescription: " + description + "\nDevices:\n";
        sb.append(s);
        if (devicesList.isEmpty()) {
            sb.append("\tNo devices in this room.");
        } else {
            for (Device device : devicesList) {
                s = "\t" + device + "\n";
                sb.append(s);
            }

        }
        return sb.toString();// Return the full string that represents the room and its devices
    }

    /**
     * Returns a short summary of the room with device counts.
     */
    public String toBriefString() {
        return "Room Code: " + code
                + "\nTotal Devices: " + devicesList.size()
                + "\nNumber of Lights: " + getNbLights()
                + "\nNumber of Appliances: " + getNbAppliances();
    }
}
