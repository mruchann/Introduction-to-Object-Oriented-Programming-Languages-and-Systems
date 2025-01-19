import java.awt.*;
import java.util.ArrayList;

public class Customer extends Entity {
    protected Strategy strategy;
    protected ArrayList<Product> shoppingList;

    public Customer(double x, double y, Strategy strategy, ArrayList<Product> shoppingList) {
        super(x, y);
        this.strategy = strategy;
        this.shoppingList = shoppingList;
    }

    public ArrayList<Product> getShoppingList() {
        return this.shoppingList;
    }

    @Override
    public void draw(Graphics2D g2d) {
        drawHelper(g2d, toString(), Color.GRAY);
    }

    @Override
    public void step() {
        if (shoppingList.isEmpty()) { // Quit
            if (position.getIntX() < Common.getWindowWidth() / 2) {
                position.setX(position.getX() - Common.getCustomerMovementSpeed());
            }
            else {
                position.setX(position.getX() + Common.getCustomerMovementSpeed());
            }
        }

        else if (!strategy.isTargetSet()) { // Target is also set to null after buying
            strategy.setNextTarget(this, Common.getStoreList());
        }

        else { // Shopping list is not empty and has target
            double dist = getPosition().distanceTo(strategy.getTargetPosition());
            if (dist <= 2 * Common.getEntityRadius()) { // Arrived
                strategy.tryToBuy(this);
            }

            else if (dist <= Common.getCustomerMovementSpeed()) { // Doesn't pass the store
                getPosition().setX(strategy.getTargetPosition().getX());
                getPosition().setY(strategy.getTargetPosition().getY());
            }

            else {
                double dx = strategy.getTargetPosition().getX() - getPosition().getX();
                double dy = strategy.getTargetPosition().getY() - getPosition().getIntY();

                double vx = dx / dist * Common.getCustomerMovementSpeed(); // v * cost
                double vy = dy / dist * Common.getCustomerMovementSpeed(); // v * sint

                getPosition().setX(getPosition().getX() + vx);
                getPosition().setY(getPosition().getY() + vy);
            }
        }
    }

    @Override
    public String toString() {
        String text = strategy.getStrategyType();

        for (int i = 0; i < Math.min(3, shoppingList.size()); i++) {
            text += "," + shoppingList.get(i).getProductType();
        }

        return text;
    }
}
