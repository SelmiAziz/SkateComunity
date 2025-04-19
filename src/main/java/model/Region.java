package model;

public enum Region {
    LAZIO("Lazio", 1),
    UMBRIA("Umbria", 2),
    TOSCANA("Toscana", 3),
    ABRUZZO("Abruzzo", 3),
    MARCHE("Marche", 3),
    CAMPANIA("Campania", 4),
    MOLISE("Molise", 4),
    EMILIA_ROMAGNA("Emilia-Romagna", 4),
    LIGURIA("Liguria", 5),
    PUGLIA("Puglia", 5),
    BASILICATA("Basilicata", 5),
    PIEMONTE("Piemonte", 6),
    LOMBARDIA("Lombardia", 6),
    CALABRIA("Calabria", 7),
    VENETO("Veneto", 6),
    TRENTINO_ALTO_ADIGE("Trentino-Alto Adige", 7),
    FRIULI_VENEZIA_GIULIA("Friuli-Venezia Giulia", 7),
    SARDEGNA("Sardegna", 8),
    SICILIA("Sicilia", 8),
    VALLE_D_AOSTA("Valle d'Aosta", 7);

    private final String name;
    private final int estimatedDays;

    Region(String name, int estimatedDays) {
        this.name = name;
        this.estimatedDays = estimatedDays;
    }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Region fromString(String input) {
        if (input == null) return null;
        for (Region region : Region.values()) {
            if (region.name.equalsIgnoreCase(input)) {
                return region;
            }
        }
        return null;
    }
}

