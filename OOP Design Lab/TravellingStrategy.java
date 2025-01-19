import java.util.ArrayList;
import java.util.List;

public class TravellingStrategy extends Strategy{

    private ArrayList<Store> visited;

    public TravellingStrategy() {
        visited = new ArrayList<>();
    }

    @Override
    public String getStrategyType() {
        return "Tr";
    }

    @Override
    public void setNextTarget(Customer c, List<Store> storeList) {
        double closest = Double.POSITIVE_INFINITY;

        if (visited.size() == storeList.size()) {
            visited.clear();
        }

        for (Store s : storeList) {
            if (!visited.contains(s)) {
                double dist = c.getPosition().distanceTo(s.getPosition());
                if (dist < closest) {
                    closest = dist;
                    target = s;
                }
            }
        }
    }

    @Override
    public void tryToBuy(Customer c) {
        List<Product> shoppingList = c.getShoppingList();

        for (int i = 0; i < shoppingList.size(); i++) {
            String type = shoppingList.get(i).getProductType();
            try {
                target.sell(type);
                shoppingList.remove(i);
                return; // Skips visited.add(target) and target = null;

            } catch (ProductOutOfStockException e) {
                break;
            } catch (ProductNotFoundException e) {
                continue;
            }
        }

        visited.add(target);
        target = null;
    }
}
