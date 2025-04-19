package model;

public enum OrderStatus {
    REQUESTED("Requested"),
    PROCESSING("Processing"),
    REJECTED("Rejected"),
    COMPLETED("Completed");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static OrderStatus fromString(String input) {
        if (input == null) return null;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.label.equalsIgnoreCase(input)) {
                return status;
            }
        }
        return null;
    }
}

