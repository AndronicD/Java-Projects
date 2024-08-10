import java.util.StringTokenizer;

public class Order{
    private String ID;
    private Integer numProd;

    Order(String line){
        String[] lines = new String[2];
        StringTokenizer st = new StringTokenizer(line, ",");
        int i = 0;
        while (st.hasMoreTokens()) {
            lines[i] = st.nextToken();
            i++;
        }
        ID = lines[0];
        numProd = Integer.parseInt(lines[1]);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getNumProd() {
        return numProd;
    }

    public void setNumProd(Integer numProd) {
        this.numProd = numProd;
    }
}
