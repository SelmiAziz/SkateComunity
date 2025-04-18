package model;

public enum Region {

    LAZIO("Lazio", 0),
    UMBRIA("Umbria", 1),
    TOSCANA("Toscana", 2),
    ABRUZZO("Abruzzo", 2),
    MARCHE("Marche", 2),
    CAMPANIA("Campania", 3),
    MOLISE("Molise", 3),
    EMILIA_ROMAGNA("Emilia-Romagna", 3),
    LIGURIA("Liguria", 4),
    PUGLIA("Puglia", 4),
    BASILICATA("Basilicata", 4),
    PIEMONTE("Piemonte", 5),
    LOMBARDIA("Lombardia", 5),
    CALABRIA("Calabria", 6),
    VENETO("Veneto", 5),
    TRENTINO_ALTO_ADIGE("Trentino-Alto Adige", 6),
    FRIULI_VENEZIA_GIULIA("Friuli-Venezia Giulia", 6),
    SARDEGNA("Sardegna", 7),
    SICILIA("Sicilia", 7),
    VALLE_D_AOSTA("Valle d'Aosta", 6);

    private final String name;
    private final int cost;

    Region(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public int getCostRegion() {
        return cost;
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
