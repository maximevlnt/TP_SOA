package fr.insa.LightAutomation.light_actuator.model;

public class LightActuatorStatus {
    private boolean isOn;

    public LightActuatorStatus(boolean isOn) {
        this.isOn = isOn;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}