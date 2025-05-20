package pt.ua.tqs.ecocharger.ecocharger.models;

public class Connectors {

    private String connectorType;
    private int ratedPowerKW;
    private int voltageV;
    private int currentA;
    private String currentType;

    public Connectors() {
    }

    public Connectors(String connectorType, int ratedPowerKW, int voltageV, int currentA, String currentType) {
        this.connectorType = connectorType;
        this.ratedPowerKW = ratedPowerKW;
        this.voltageV = voltageV;
        this.currentA = currentA;
        this.currentType = currentType;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public int getRatedPowerKW() {
        return ratedPowerKW;
    }

    public void setRatedPowerKW(int ratedPowerKW) {
        this.ratedPowerKW = ratedPowerKW;
    }

    public int getVoltageV() {
        return voltageV;
    }

    public void setVoltageV(int voltageV) {
        this.voltageV = voltageV;
    }

    public int getCurrentA() {
        return currentA;
    }

    public void setCurrentA(int currentA) {
        this.currentA = currentA;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    @Override
    public String toString() {
        return "Connectors [connectorType=" + connectorType + ", ratedPowerKW=" + ratedPowerKW + ", voltageV="
                + voltageV + ", currentA=" + currentA + ", currentType=" + currentType + "]";
    }


    
}
