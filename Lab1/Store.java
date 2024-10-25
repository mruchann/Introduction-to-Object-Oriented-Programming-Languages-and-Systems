import java.awt.*;

public class Store extends Entity {
    protected final Product type;
    protected final int price;
    protected int stock;
    protected final int maxStock;

    public Store(double x, double y, Product type, int price, int maxStock) {
        super(x, y);
        this.type = type;
        this.price = price;
        this.stock = this.maxStock = maxStock;
    }

    public String getProductType() {
        return type.getProductType();
    }

    public int getPrice(String itemType) throws RuntimeException {
        if (!getProductType().equals(itemType)) {
            throw new ProductNotFoundException();
        }
        return price;
    }

    @Override
    public void draw(Graphics2D g2d) {
        drawHelper(g2d, toString(), Color.ORANGE);
    }

    @Override
    public void step() {

    }

    public void sell(String itemType) throws RuntimeException {
        if (!getProductType().equals(itemType)) {
            throw new ProductNotFoundException();
        }

        if (stock == 0) {
            throw new ProductOutOfStockException();
        }

        stock--;
    }

    public void replenish() {
        stock = maxStock;
    }

    @Override
    public String toString() {
        return getProductType() + "," + stock + "," + price;
    }
}
