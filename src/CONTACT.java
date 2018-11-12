import java.util.ArrayList;

public class CONTACT {

    private String name;
    private SELECTION selection;
    private PART part1;
    private PART part2;
    private String type;

    public CONTACT() {
    }

    public void setPart1(PART part) {
        this.part1 = part;
        System.out.println("Pièce 1 : " + part1.getPart_name());
        this.checkDataAndSetInterfaces();
    }

    public void setPart2(PART part) {
        this.part2 = part;
        System.out.println("Pièce 2 : " + part2.getPart_name());
        this.checkDataAndSetInterfaces();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        switch (type) {
            case "CYLINDRE STRUCT":
                selection = new CYLIND_STRUCT();
                System.out.println("Type CYLINDRE STRUCT appliqué");
                break;
            case "CYLINDRE CPOINT":
                selection = new CYLINDRE();
                System.out.println("Type CYLINDRE CPOINT appliqué");
                break;
        }
        this.checkDataAndSetInterfaces();
    }

    public SELECTION getSelection() {
        return selection;
    }

    public ArrayList<FACE> getInterfaces() {
        ArrayList<FACE> list_interface = new ArrayList<>();
        list_interface.addAll(part1.getFace_exterieur());
        list_interface.addAll(part2.getFace_exterieur());
        return list_interface;
    }

    private void checkDataAndSetInterfaces() {
        if (part1 != null && part2 != null && selection != null) {
            selection.setFaces_to_treat(this.getInterfaces());
            System.out.println("Interfaces du contact implémentées");
        }
    }

    public ArrayList<Double> getParameters() {
        ArrayList<Double> list_parameters = new ArrayList<>();
        switch (type) {
            case "CYLINDRE STRUCT":
                for (int i = 0; i < 2; i++) {
                    list_parameters.add(((CYLIND_STRUCT) selection).getCenter(i));
                }
                list_parameters.add(((CYLIND_STRUCT) selection).getZ_inf());
                list_parameters.add(((CYLIND_STRUCT) selection).getZ_sup());
                list_parameters.add(((CYLIND_STRUCT) selection).getRayon_int());
                list_parameters.add(((CYLIND_STRUCT) selection).getRayon_ext());

                break;
            case "CYLINDRE CPOINT":
                for (int i = 0; i < 3; i++) {
                    list_parameters.add(((CYLINDRE) selection).getCenter_inf(i));
                }
                for (int i = 0; i < 3; i++) {
                    list_parameters.add(((CYLINDRE) selection).getCenter_sup(i));
                }
                list_parameters.add(((CYLINDRE) selection).getRayon_int());
                list_parameters.add(((CYLINDRE) selection).getRayon_ext());
                break;
        }
        return list_parameters;
    }

    public void setParameters(ArrayList<Double> list_parameters) {
        switch (type) {
            case "CYLINDRE STRUCT":
                for (int i = 0; i < 2; i++) {
                    ((CYLIND_STRUCT) selection).setCenter(i, list_parameters.get(i));
                }
                ((CYLIND_STRUCT) selection).setZ_inf(list_parameters.get(2));
                ((CYLIND_STRUCT) selection).setZ_sup(list_parameters.get(3));
                ((CYLIND_STRUCT) selection).setRayon_int(list_parameters.get(4));
                ((CYLIND_STRUCT) selection).setRayon_ext(list_parameters.get(5));
                ((CYLIND_STRUCT) selection).updateIntersection();
                System.out.println("Paramètres CYLINDRE STRUCT mis à jour");
                break;
            case "CYLINDRE CPOINT":
                for (int i = 0; i < 3; i++) {
                    ((CYLINDRE) selection).setCenter_inf(i, list_parameters.get(i));
                }
                for (int i = 0; i < 3; i++) {
                    ((CYLINDRE) selection).setCenter_sup(i, list_parameters.get(i));
                }
                ((CYLINDRE) selection).setRayon_int(list_parameters.get(6));
                ((CYLINDRE) selection).setRayon_ext(list_parameters.get(7));
                ((CYLINDRE) selection).updateIntersection();
                System.out.println("Paramètres CYLINDRE CPOINT mis à jour");
                break;
        }
    }
}
