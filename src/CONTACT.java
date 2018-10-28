import java.util.ArrayList;

public class CONTACT {

    private SELECTION selection;
    private PART part1;
    private PART part2;
    private String type;

    public CONTACT() {
    }

    public void setPart1(PART part) {
        this.part1 = part;
        this.checkDataAndSetInterfaces();
    }

    public void setPart2(PART part) {
        this.part2 = part;
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
                break;
            case "CYLINDRE CPOINT":
                selection = new CYLINDRE();
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
        }
    }
}
