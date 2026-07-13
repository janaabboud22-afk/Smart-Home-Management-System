package amar_maamary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author amarmaamary
 */
public class ManagementSystem {

    /**
     * Data fields
     */
    private String adminPassword; //admin Password tp enter admin mode of at least 8 characters, 1 uppercase, 1 lowercase and one digit.
    private String userPassword;    //user Password of at least 8 characters, 1 uppercase, 1 lowercase and one digit.
    private ArrayList<Room> rooms;
    private double maxAllowedPower;
    private boolean day;
    private ArrayList<Device> waitingListDay;
    private ArrayList<Device> waitingListPower;

    /**
     * Constants for max allowed power
    */
    public static final double LOW = 1000;
    public static final double NORMAL = 4000;
    public static final double HIGH = 10000;

    /**
     * Scanner data field to use 
     */
    private final Scanner input = new Scanner(System.in);

    /**
     * Constructor with adminPassword and userPassword only
     */
    public ManagementSystem(String adminPassword, String userPassword) {
        this(adminPassword, userPassword, NORMAL);
    }

    /**
     * Constants with maximum allowed power
     */
    public ManagementSystem(String adminPassword, String userPassword, double maxAllowedPower) {
        setManagementSystem(adminPassword, userPassword, maxAllowedPower);
        rooms = new ArrayList<>();
        waitingListDay = new ArrayList<>();
        waitingListPower = new ArrayList<>();
    }

    /**
     * Initializes all attributes using setters for validation.
     */
    public void setManagementSystem(String adminPassword, String userPassword, double maxAllowedPower) {
        setAdminPassword(adminPassword);
        setUserPassword(userPassword);
        setMaxAllowedPower(maxAllowedPower);
        day = true;
    }

    // -------------------- Setters --------------------
    
    public void setAdminPassword(String adminPassword) {
        if (checkPasswordValidity(adminPassword)) {
            this.adminPassword = adminPassword;
        } else {
            this.adminPassword = "AdminPassword@1";
        }
    }

    public void setUserPassword(String userPassword) {
        if (checkPasswordValidity(userPassword)) {
            this.userPassword = userPassword;
        } else {
            this.userPassword = "UserPassword@1";
        }
    }

    public void setMaxAllowedPower(double maxAllowedPower) {
        if (maxAllowedPower <= LOW) {
            this.maxAllowedPower = LOW;
        } else if (maxAllowedPower < HIGH && maxAllowedPower > LOW) {
            this.maxAllowedPower = NORMAL;
        } else {
            this.maxAllowedPower = HIGH;
        }
    }

    // -------------------- Functionality methods --------------------
    
    /**
     * takes a string password and check if it is formed of at least 8 characters, 1 digit, 1 lowercase and one uppercase characters.
     */
    public boolean checkPasswordValidity(String password) {
        boolean existUpper = false, existLower = false, existDigit = false;
        char currentChar;
        for (int i = 0; i < password.length(); i++) {
            currentChar = password.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                existUpper = true;
            } else {
                existLower = true;
            }
            if (Character.isDigit(currentChar)) {
                existDigit = true;
            }
        }
        return existUpper && existLower && existDigit && (password.length() >= 8);
    }
    
    /**
     * Calculate the current power consumed in the system and return the value
     */
    public double getCurrentPowerConsumption() {
        double total = 0;
        for (Room room : rooms) {
            total += room.getCurrentConsumption();
        }
        return total;
    }

    public void displaySummaryAllRooms() {
        System.out.println("Here is a summary for all existing rooms: ");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println("Room " + (i + 1) + ":\n");
            System.out.println((rooms.get(i)).toBriefString() + "\n");
        }
    }
    
    public void displayDetailsOneRoom(String code) {
        Room room = searchRoomByCode(code);
        if (room != null) {
            System.out.println(room.toString());
        } else {
            System.out.println("Room not found.");
        }
    }
    
    /**
     * Search for a room in the system by its code and return it as a Room object 
     */
    public Room searchRoomByCode(String code) {
        for (Room room : rooms) {
            if (room.getCode().equals(code)) {
                return room;
            }
        }
        return null;
    }
    
    /**
     * Search for a device in the system by its id and return it as a Device object 
     */
    public Device searchDeviceById(int id) {
        for (Room room : rooms) {
            Device device = room.searchDeviceById(id);
            if (device != null) {
                return device;
            }
        }
        return null;
    }
    
    /**
     * Search for a device in the system by its id and print in which room it's found with its details
     */
     public void searchDeviceByIdInWhichRoom(int id) {
        for (Room room : rooms) {
            Device device = room.searchDeviceById(id);
            if (device != null) {
                System.out.println("Device is found in room " + room.getCode() + ": " + device);
                return;
            }
        }
        System.out.println("The device is not found");
    }
    
    
    // -------------------- Admin methods --------------------
    
    /**
     * A method that prompt the admin to enter new password, check for its validity and change it if it's valid or keep it the same if not
     */
    public void changeAdminPassword() {

        //take password
        System.out.print("Enter new admin password: ");
        String newPassword = input.nextLine();

        //checking its validity
        if (checkPasswordValidity(newPassword)) {
            //updating it if valid
            adminPassword = newPassword;
            System.out.println("Admin password updated successfully.");
        } else {
            //keeping it the same if invalid
            System.out.println("Invalid password. Password must have at least:\n- 8 characters\n- One uppercase letter\n- One lowercase letter\n- One digit");
        }
    }
    // -------------------- User methods --------------------
    /**
     * A method that prompt the user to enter new password, check for its validity and change it if it's valid or keep it the same if not
     */
    public void changeUserPassword() {
        //take password
        System.out.print("Enter new user password: ");
        String newPassword = input.nextLine();

        //checking its validity
        if (checkPasswordValidity(newPassword)) {
            //updating it if valid
            userPassword = newPassword;
            System.out.println("User password updated successfully.");
        } else {
            //keeping it the same if invalid
            System.out.println("Invalid password. Password must have at least:\n- 8 characters\n- One uppercase letter\n- One lowercase letter\n- One digit");
        }
    }

    /**
     * Adding a room object to the system
     */
    public void addRoom(Room r) {
        Room room = searchRoomByCode(r.getCode());
        //if the room is not added before add it
        if (room == null) {
            for (Device device : r.getDevicesList()) {
                device.turnOff();
            }
            rooms.add(r);
            System.out.println("The room has been added successfully!");
        } else {
            System.out.println("Room already exist."); //print an error statement if the room exists
        }

    }

    /**
     * Removing a room object of the system
     */
    public void removeRoom(Room r) {
        Room room = searchRoomByCode(r.getCode());
        //if the room is found remove it
        if (room != null) {
            rooms.remove(room);
            System.out.println("The room has been removed successfully!");
        } else {
            System.out.println("Room not found."); //if the room doesn't exist print an error statement
        }
    }

    /**
     * Turning on a specific device (by its id) in a specific room (by its code)
     */
    private void turnOnDevice(String roomCode, int deviceId) {
        //check if the room exists and exit id not 
        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("Room is not found");
            return;
        }

        //Check if the device exists and exit if not
        Device device = searchDeviceById(deviceId);
        if (device == null) {
            System.out.println("Device is not found.");
            return;
        }

        //if device on print it's on
        if (device.getStatus() == 1) {
            System.out.println("Device is already ON.");
            return;
        }

        //if device is off
        // Check power consumption
        if (getCurrentPowerConsumption() + device.getMaxPowerConsumption() > maxAllowedPower) {
            //if it exeeds the max allowed power ask if the user wants to add it to the waiting list or cancel the operation
            System.out.println("Warning: Turning on this device exceeds maximum allowed power.\n1. Add it to Power Waiting List\n2. Cancel");

            int option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1:
                    waitingListPower.add(device);
                    System.out.println("Device added to power waiting list.");
                    break;
                case 2:
                    System.out.println("Operation cancelled.");
                    break;
                default:
                    System.out.println("Invalid choice. Operation unsuccessful.");

            }
        } else {
            // Check noisy devices
            if (device instanceof Appliance) {
                Appliance appliance = (Appliance) device;
                if (appliance.isNoisy() && !day) {
                    System.out.println("Warning: This is a noisy appliance and it's night time.\n1. Turn it ON anyway\n2. Add it to Day Waiting List\n3. Cancel");
                    int option = input.nextInt();
                    input.nextLine();
                    switch (option) {
                        case 1:
                            appliance.turnOn();
                            System.out.println("Device turned on successfully.");
                            break;
                        case 2:
                            waitingListDay.add(device);
                            device.setStatus(2);
                            System.out.println("Device added to day waiting list.");
                            break;
                        case 3:
                            System.out.println("Operation cancelled.");
                            break;
                        default:
                            System.out.println("Invalid choice. Operation unsuccessful.");

                    }
                } else {
                    appliance.turnOn();
                    System.out.println("Device turned on successfully.");
                }
            } else if (device instanceof Light) {
                Light light = (Light) device;
                light.turnOn();
                System.out.println("Device turned on successfully.");
            }
        }

    }

    public void turnOffDevice(String roomCode, int deviceId) {
        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("Room is not found");
            return;
        }

        Device device = searchDeviceById(deviceId);
        if (device == null) {
            System.out.println("Device is not found.");
            return;
        }
        if (device.getStatus() == 0) {
            System.out.println("Device is already OFF.");
            return;
        }
        // Check if critical device (double confirmation needed)
        if (device.isCritical()) {
            System.out.println("This is a critical device. Enter Admin Password to confirm:");
            String pass = input.nextLine();
            if (!pass.equals(adminPassword)) {
                System.out.println("Incorrect password. Operation cancelled.");
                return;
            }
        }

        device.turnOff();
        System.out.println("Device turned off successfully.");

        checkPowerWaitingList();
    }

    private void checkPowerWaitingList() {
        ArrayList<Device> toBeTurnedOn = new ArrayList<>();

        for (Device device : waitingListPower) {
            if (getCurrentPowerConsumption() + device.getMaxPowerConsumption() <= maxAllowedPower) {
                device.turnOn();
                toBeTurnedOn.add(device);
                System.out.println("Device from power waiting list turned on: " + device.getName());
            } else {
                break; // stop checking if we cannot turn on the next
            }
        }
        // Remove devices that have been turned on from waiting list
        waitingListPower.removeAll(toBeTurnedOn);
    }

    public void shutDownOneRoom(String roomCode) {
        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("Room is not found.");
            return;
        }
        System.out.println("In room " + roomCode + ": ");
        for (int i = 0; i < room.getDevicesList().size(); i++) {
            System.out.println("Device " + (i + 1) + ": ");
            turnOffDevice(roomCode, ((room.getDevicesList()).get(i)).getId());
        }
        System.out.println("The room has been successfully turned off.");
    }

    public void shutDownAllDevices() {
        for (Room room : rooms) {
            shutDownOneRoom(room.getCode());
        }
        System.out.println("All devices has been successfully shuted down.");
    }

    public void addDevice(int deviceId, String roomCode) {

        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("The room is not found.");
            return;
        }

        Device existingDevice = searchDeviceById(deviceId);
        if (existingDevice != null) {
            System.out.println("Device with this ID already exists.");
            return;
        }

        System.out.print("Enter device name: ");
        String name = input.nextLine();

        System.out.print("Enter max power consumption (W): ");
        double maxPower = input.nextDouble();
        input.nextLine(); // Consume newline

        System.out.print("Is this a critical device? (yes/no): ");
        char crit = input.next().charAt(0);
        input.nextLine();
        boolean critical = (crit == 'y' || crit == 'Y');

        System.out.println("Select device type:");
        System.out.println("1. Light");
        System.out.println("2. Appliance");
        int type = input.nextInt();
        input.nextLine(); // Consume newline

        Device newDevice;

        if (type == 1) {
            System.out.print("Is the light adjustable? (yes/no): ");
            char c = input.next().charAt(0);
            boolean adjustable = (c == 'y' || c == 'Y');

            if (critical) {
                newDevice = new Light(deviceId, name, maxPower, true, adjustable);
            } else {
                newDevice = new Light(deviceId, name, maxPower, adjustable);
            }

        } else if (type == 2) {
            System.out.print("Enter number of power levels: ");
            int n = input.nextInt();
            int[] levels = new int[n];

            for (int i = 0; i < n; i++) {
                System.out.print("Enter power level " + (i + 1) + " (%): ");
                levels[i] = input.nextInt();
            }

            Arrays.sort(levels); // Sort for safety

            System.out.print("Is this appliance noisy? (yes/no): ");
            char c = input.next().charAt(0);
            input.nextLine();
            boolean noisy = (c == 'y' || c == 'Y');

            if (critical) {
                newDevice = new Appliance(deviceId, name, maxPower, true, levels, noisy);
            } else {
                newDevice = new Appliance(deviceId, name, maxPower, levels, noisy);
            }

        } else {
            System.out.println("Invalid device type. Operation cancelled.");
            return;
        }

        room.addDevice(newDevice);
        System.out.println("Device has been successfully added to room " + room.getCode() + ".");
    }

    public void removeDevice(int deviceCode, String roomCode) {
        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("Room is not found");
            return;
        }

        Device device = searchDeviceById(deviceCode);
        if (device == null) {
            System.out.println("Device is not found");
            return;
        }

        if (device.getStatus() == 1) {
            turnOffDevice(roomCode, device.getId());
        } else if (device.getStatus() == 2) {
            for (int i = 0; i < waitingListDay.size(); i++) {
                if (waitingListDay.get(i).equals(device)) {
                    waitingListDay.remove(i);
                    break;
                }
            }

            for (int i = 0; i < waitingListPower.size(); i++) {
                if (waitingListPower.get(i).equals(device)) {
                    waitingListPower.remove(i);
                    break;
                }
            }

        }

        room.removeDevice(device);
        System.out.println("Device has been succesfully removed.");
    }

    public void setDayTime() {
        if (day) {
            System.out.println("The system is already in day mode.");
            return;
        }
        day = true;
        System.out.println("System set to DAY TIME.");

        ArrayList<Device> stillWaiting = new ArrayList<>();

        for (Device device : waitingListDay) {
            if (getCurrentPowerConsumption() + device.getMaxPowerConsumption() <= maxAllowedPower) {
                device.turnOn();
                System.out.println("Device turned ON from day waiting list: " + device.getName());
            } else {
                stillWaiting.add(device); // move it to power waiting list
                System.out.println("Device moved to power waiting list due to power limit: " + device.getName());
            }
        }

        waitingListDay.clear();
        waitingListPower.addAll(stillWaiting);

        System.out.print("Would you like to turn OFF all lights? (yes/no): ");
        char answer = input.next().charAt(0);
        input.nextLine();
        if (answer == 'y' || answer == 'Y') {
            for (Room room : rooms) {
                for (Device device : room.getDevicesList()) {
                    if (device instanceof Light && device.getStatus() == 1) {
                        device.turnOff();
                        System.out.println("Light turned OFF: " + device.getName());
                    }
                }
            }
        }
    }

    public void setNightTime() {
        if (!day) {
            System.out.println("The system is already in night mode.");
            return;
        }
        day = false;
        System.out.println("System set to NIGHT TIME.");

        for (Room room : rooms) {
            for (Device device : room.getDevicesList()) {
                if (device instanceof Appliance) {
                    Appliance appliance = (Appliance) device;
                    if (appliance.isNoisy() && device.getStatus() == 1) {
                        System.out.println("\nNoisy appliance is ON: " + device.getName() + "\n1. Turn OFF device\n2. Put device in Day Waiting List (Standby)\n3. Keep device ON");
                        System.out.print("Select option: ");
                        int choice = input.nextInt();
                        input.nextLine(); // Consume newline

                        switch (choice) {
                            case 1:
                                turnOffDevice(room.getCode(), device.getId());
                                System.out.println("Device turned OFF.");
                                break;
                            case 2:
                                waitingListDay.add(device);
                                device.setStatus(2); // because it's standby
                                System.out.println("Device put on Day Waiting List.");
                                break;
                            default:
                                System.out.println("Device kept ON.");
                        }
                    }
                }
            }
        }
    }

    public void displayInfo() {
        System.out.println("The system covers " + rooms.size() + " room(s):");
        for (Room room : rooms) {
            System.out.println(room.toBriefString());
        }
    }

    public void displayMainMenu() {
        System.out.println("1. Admin Mode \n"
                + "2. User Mode \n"
                + "3. Exit");
    }

    public void evaluatingPassword(String password) {
        String choicePass;
        do {
            System.out.print("Enter the password please: ");
            choicePass = input.nextLine();
            if (!(choicePass.equals(password))) {
                System.out.println("Incorrect Password. Please Try Again!");
            }
        } while (!(choicePass.equals(password)));
    }

    //Admin Necessary Methods
    public void displayAdminMenu() {
        System.out.println("\t\tAdmin Menu \n1. Change admin passwords  \n2. Change user passwords  \n3. Change power mode to one of the three possible modes \n"
                + "4. Set day/time mode \n5. Add room \n6. Delete room \n7. Search room \n8. Add a device \n9. Delete a device \n10. Search a device \n11. Exit admin mode \n");
    }

    public void adminMode() {
        evaluatingPassword(adminPassword);
        System.out.println("");
        System.out.println("Welcome to the Admin Mode: ");
        System.out.println("");
        int operationNumber;
        do {
            displayAdminMenu();
            System.out.print("Please Select the number of the desired operation: ");

            do {
                operationNumber = input.nextInt();
                input.nextLine();
                if (operationNumber < 1 || operationNumber > 11) {
                    System.out.println("Invalid choice. Please enter a choice between 1 and 11.");
                }
            } while (operationNumber < 1 || operationNumber > 11);

            System.out.println("");

            String str;
            int id;
            switch (operationNumber) {
                case 1:
                    changeAdminPassword();

                    break;
                case 2:
                    changeUserPassword();
                    break;
                case 3:
                    System.out.println("Please enter the maximum allowed power (1000 or 4000 or 10000 watts): ");
                    int maxAllowedP = input.nextInt();
                    input.nextLine();
                    setMaxAllowedPower(maxAllowedP);
                    System.out.println("Max allowed power is set to " + maxAllowedPower);
                    break;
                case 4:
                    System.out.println("Select Mode: \n1. Night Mode\n2. Day Mode");
                    int choice = input.nextInt();
                    input.nextLine();
                    switch (choice) {
                        case 1:
                            setNightTime();
                            break;
                        case 2:
                            setDayTime();
                            break;
                        default:
                            System.out.println("Invalid choice. The system remains in " + (day ? "day" : "night") + " mode.");
                    }
                    break;
                case 5:
                    System.out.print("Enter the room code: ");
                    str = input.nextLine();
                    System.out.print("Enter the description of the room: ");
                    String desc = input.nextLine();
                    addRoom(new Room(str, desc));
                    break;
                case 6:
                    System.out.print("Enter the room code: ");
                    str = input.nextLine();
                    removeRoom(new Room(str));
                    break;
                case 7:
                    System.out.println("Enter the room code: ");
                    str = input.nextLine();
                    System.out.println(searchRoomByCode(str));
                    break;
                case 8:
                    System.out.print("Enter the device id: ");
                    id = input.nextInt();
                    input.nextLine();
                    System.out.print("Enter the room code:");
                    str = input.nextLine();
                    addDevice(id, str);
                    break;
                case 9:
                    System.out.print("Enter the device id: ");
                    id = input.nextInt();
                    input.nextLine();
                    System.out.println("Enter the room code:");
                    str = input.nextLine();
                    removeDevice(id, str);
                    break;
                case 10:
                    System.out.println("Enter the device id: ");
                    id = input.nextInt();
                    input.nextLine();
                    searchDeviceById(id);
                    break;

                case 11:
                    System.out.println("Exiting the admin mode ... ");
                    System.out.println("");
                    break;

                default:
                    System.out.println("Invalid choice.");

            }
        } while (operationNumber != 11);

    }

    //User Necessary Methods
    public void displayUserMenu() {
        System.out.println("\t\tUser Menu \n"
                + "1. Check all rooms info \n"
                + "2. Check all devices info  \n"
                + "3. Check all running devices \n"
                + "4. Check all standby devices in the day waiting list \n"
                + "5. Check all standby devices in the power waiting list \n"
                + "6. Search for a given room \n"
                + "7. Search for a given device \n"
                + "8. Turn on a device  \n"
                + "9. Turn off a device  \n"
                + "10. Turn off all devices from one specific room \n"
                + "11. Turn off all devices in the house \n"
                + "12. Check current power consumption  \n"
                + "13. Set day mode \n"
                + "14. Set night mode \n"
                + "15. Exit control mode");
    }

    public void checkRunningDevices() {
        System.out.println("This is a list of running devices in each room:");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println("In room " + (i + 1));
            for (Device device : (rooms.get(i)).getDevicesList()) {
                if (device.getStatus() == 1) {
                    System.out.println("Device " + device.getName() + " (" + device.getId() + ") is on.");
                }
            }
        }
    }

    public void checkingStandbyDayDevices() {
        System.out.println("This is a list of stanby devices in day waiting list: ");
        for (int i = 0; i < waitingListDay.size(); i++) {
            System.out.println("Device " + (i + 1) + ": ");
            System.out.println(waitingListDay.get(i));
        }

    }

    public void checkingStandbyPowerDevices() {
        System.out.println("This is a list of stanby devices in day power list: ");
        for (int i = 0; i < waitingListPower.size(); i++) {
            System.out.println("Device " + (i + 1) + ": ");
            System.out.println(waitingListPower.get(i));
        }

    }

    public void userMode() {
        evaluatingPassword(userPassword);
        System.out.println("");
        System.out.println("Welcome to the User Mode: ");
        System.out.println("");
        int operationNumber;
        do {
            displayUserMenu();
            System.out.println("");
            System.out.print("Please Select the number of the desired operation: ");

            do {
                operationNumber = input.nextInt();
                input.nextLine();
                if (operationNumber < 1 || operationNumber > 15) {
                    System.out.println("Invalid choice. Please enter a choice between 1 and 15.");
                }
            } while (operationNumber < 1 || operationNumber > 15);

            System.out.println("");
            String code;
            int id;
            switch (operationNumber) {
                case 1:
                    displayInfo();
                    break;
                case 2:
                    for (Room room : rooms) {
                        System.out.println(room);
                    }
                    break;
                case 3:
                    checkRunningDevices();
                    break;
                case 4:
                    checkingStandbyDayDevices();
                    break;
                case 5:
                    checkingStandbyPowerDevices();
                    break;
                case 6:
                    System.out.print("Enter the room code: ");
                    code = input.nextLine();
                    System.out.println(searchRoomByCode(code));
                    break;
                case 7:
                    System.out.print("Enter the id of the device");
                    id = input.nextInt();
                    input.nextLine();
                    searchDeviceByIdInWhichRoom(id);
                    break;
                case 8:
                    System.out.print("Enter the device id to turn on: ");
                    id = input.nextInt();
                    input.nextLine();
                    System.out.println("Enter the code of the room: ");
                    code = input.nextLine();
                    turnOnDevice(code, id);
                    break;
                case 9:
                    System.out.print("Enter the device id to turn on: ");
                    id = input.nextInt();
                    input.nextLine();
                    System.out.println("Enter the code of the room: ");
                    code = input.nextLine();
                    turnOffDevice(code, id);
                    break;
                case 10:
                    System.out.println("Enter the code of the room: ");
                    code = input.nextLine();
                    shutDownOneRoom(code);
                    break;

                case 11:
                    shutDownAllDevices();
                    break;
                case 12:
                    System.out.println("The current power consumption is " + getCurrentPowerConsumption() + " watt(s)");
                    break;
                case 13:
                    setDayTime();
                    break;
                case 14:
                    setNightTime();
                    break;
                case 15:
                    System.out.println("Exiting the user mode ... ");
                    System.out.println("");
                    break;

                default:
                    System.out.println("Invalid choice.");

            }
        } while (operationNumber != 15);
    }

    public void run() {
        int answer = 0;
        while (answer != 3) {
            System.out.println("");
            System.out.println("\tWelcome to The Smart Home Manegement System: ");
            System.out.println("");
            displayMainMenu();
            System.out.print("Please select a valid mode from the following menu(1,2,3): ");
            do {
                answer = input.nextInt();
                input.nextLine();
                if (answer < 1 || answer > 3) {
                    System.out.println("Invalid choice. Please enter a choice between 1 and 3.");
                }
            } while (answer < 1 || answer > 3);

            System.out.println("");

            switch (answer) {
                case 1:
                    adminMode();
                    break;
                case 2:
                    userMode();
                    break;
                default:
                    System.out.println("The System is closing. Thank you.");
            }

        }
    }

}
