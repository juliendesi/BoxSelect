public enum TypeBoite {
    DEFAULT(""),
    CYLINDRIQUE_STRUCT("CYLINDRE STRUCT"),
    CYLINDRIQUE_CPOINT("CYLINDRE CPOINT");

    private String name;

    //Constructeur
    TypeBoite(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
