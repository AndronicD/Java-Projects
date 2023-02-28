package implement;

import enums.Category;

public class GiftInput {
    private String productName;
    private Double price;
    private Category category;
    private Integer quantity;

    /**
     * getter
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * setter
     */
    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * getter
     */
    public String getProductName() {
        return productName;
    }

    /**
     * setter
     */
    public void setProductName(final String productName) {
        this.productName = productName;
    }

    /**
     * getter
     */
    public Double getPrice() {
        return price;
    }

    /**
     * setter
     */
    public void setPrice(final Double price) {
        this.price = price;
    }

    /**
     * getter
     */
    public Category getCategory() {
        return category;
    }

    /**
     * setter
     */
    public void setCategory(final Category category) {
        this.category = category;
    }

}
