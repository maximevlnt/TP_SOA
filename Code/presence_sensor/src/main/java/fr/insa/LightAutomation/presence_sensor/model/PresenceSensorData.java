package fr.insa.LightAutomation.presence_sensor.model;

public class PresenceSensorData {
    public boolean isPresent;

    public PresenceSensorData(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
