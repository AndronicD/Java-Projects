import java.util.StringTokenizer;

public class Product{
    private String orderName;
    private String productName;

    Product(String line){
        String[] lines = new String[2];
        StringTokenizer st = new StringTokenizer(line, ",");
        int i = 0;
        while (st.hasMoreTokens()) {
            lines[i] = st.nextToken();
            i++;
        }
        orderName = lines[0];
        productName = lines[1];
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
