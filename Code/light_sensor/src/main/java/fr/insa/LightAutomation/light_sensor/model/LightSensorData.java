package fr.insa.LightAutomation.light_sensor.model;

public class LightSensorData {
    private int intensity;

    public LightSensorData(int intensity) {
        this.intensity = intensity;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
