import java.util.List;

public class CheapestStrategy extends Strategy {

    @Override
    public String getStrategyType() {
        return "Ch";
    }

    @Override
    public void setNextTarget(Customer c, List<Store> storeList) {
        int cheapest = Integer.MAX_VALUE;

        for (Store s : Common.getStoreList()) {
            String type = c.getShoppingList().get(0).getProductType();
            try {
                int price = s.getPrice(type);
                if (price < cheapest) {
                    cheapest = price;
                    target = s;
                }
            } catch (ProductNotFoundException e) {

            }
        }
    }

    @Override
    public void tryToBuy(Customer c) {
        String type = c.getShoppingList().get(0).getProductType();

        try {
            target.sell(type);

            c.getShoppingList().remove(0);
            target = null;

        } catch (ProductOutOfStockException e) {

        }
    }
}
