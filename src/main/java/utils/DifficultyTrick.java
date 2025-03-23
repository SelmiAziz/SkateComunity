package utils;

public enum DifficultyTrick {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT;

    public static DifficultyTrick fromString(String value) {
        for (DifficultyTrick d : DifficultyTrick.values()) {
            if (d.name().toLowerCase().equalsIgnoreCase(value)) {
                return d;
            }
        }
        return null;
    }


}