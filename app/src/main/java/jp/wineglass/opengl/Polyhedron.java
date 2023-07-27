package jp.wineglass.opengl;

public enum Polyhedron {
    TETRAHEDRON(4), CUBE(6), OCTAHEDRON(8), DODECAHEDRON(12), ICOSAHEDRON(20);

    public final int value;

    private Polyhedron(final int value) {
        this.value = value;
    }

    public static Polyhedron getType(final int id) {
        for (Polyhedron type : Polyhedron.values()) {
            if (type.value == id) {
                return type;
            }
        }
        return null;
    }

    public static String toJpName(String name) {
        Polyhedron value = Polyhedron.valueOf(name);
        switch (value) {
            case TETRAHEDRON:
                return "正四面体";
            case CUBE:
                return "正六面体";
            case OCTAHEDRON:
                return "正八面体";
            case DODECAHEDRON:
                return "正十二面体";
            case ICOSAHEDRON:
                return "正二十面体";
        }
        return "";
    }

    public static String fromJpName(String jpName) {
        switch (jpName) {
            case "正四面体":
                return "TETRAHEDRON";
            case "正六面体":
                return "CUBE";
            case "正八面体":
                return "OCTAHEDRON";
            case "正十二面体":
                return "DODECAHEDRON";
            case "正二十面体":
                return "ICOSAHEDRON";
        }
        return "";
    }
}
