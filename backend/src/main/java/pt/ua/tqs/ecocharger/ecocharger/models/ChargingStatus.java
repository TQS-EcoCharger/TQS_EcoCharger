package pt.ua.tqs.ecocharger.ecocharger.models;

public enum ChargingStatus {

    /**
     * Charging session has started and is currently active.
     */
    IN_PROGRESS,

    /**
     * Charging session completed successfully.
     */
    COMPLETED,

    /**
     * Charging was cancelled manually by the user or automatically by the system.
     */
    CANCELLED,

    /**
     * Charging failed due to technical or business logic issues (e.g. payment, hardware error).
     */
    FAILED
}
