import java.util.List;

public class ClosestStrategy extends Strategy {

    private Store prev;

    private double distanceMetric(Position a, Position b) {
        return a.distanceTo(b);
    }

    @Override
    public String getStrategyType() {
        return "Cl";
    }

    @Override
    public void setNextTarget(Customer c, List<Store> storeList) {
        double closest = Double.POSITIVE_INFINITY;

        for (Store s : storeList) {
            if (s == prev) {
                continue;
            }

            String type = c.getShoppingList().get(0).getProductType();
            if (s.getProductType().equals(type)) {
                double dist = distanceMetric(c.getPosition(), s.getPosition());

                if (dist < closest) {
                    closest = dist;
                    target = s;
                }
            }
        }
    }

    @Override
    public void tryToBuy(Customer c) {
        String type = c.getShoppingList().get(0).getProductType();

        try {
            target.sell(type);
            c.getShoppingList().remove(0);

        } catch (ProductOutOfStockException e) {
            prev = target;
        }

        target = null;
    }
}
