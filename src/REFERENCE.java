public class REFERENCE implements Comparable {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        if (this.id == ((REFERENCE) o).getId()) {
            return 0;
        } else if (this.id < ((REFERENCE) o).getId()) {
            return -1;
        } else return 1;
    }

    @Override
    public boolean equals(Object obj) {
        ELEMENT other = (ELEMENT) obj;
        return other.id == this.id;
    }


}
