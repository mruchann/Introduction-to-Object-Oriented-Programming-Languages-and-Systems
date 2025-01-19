import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Row {
    public String name;
    public int age;
    public LocalDate date;

    public double priceOfBread;
    public int quantityOfBread;

    public double priceOfMilk;
    public int quantityOfMilk;

    public double priceOfEgg;
    public int quantityOfEgg;

    public double priceOfPotatoes;
    public int quantityOfPotatoes;

    public double priceOfTomatoes;
    public int quantityOfTomatoes;

    public Row() {

    }

    public int getQuantities() {
        return this.quantityOfBread + this.quantityOfMilk + this.quantityOfEgg + this.quantityOfPotatoes + this.quantityOfTomatoes;
    }

    public double getMostExpensiveProductPrice() {
        List<Double> prices = List.of(this.priceOfBread, this.priceOfMilk, this.priceOfEgg, this.priceOfPotatoes, this.priceOfTomatoes);
        return Collections.max(prices);
    }

    public double getPurchaseAmount() {
        return this.priceOfBread * this.quantityOfBread +
            this.priceOfMilk * this.quantityOfMilk +
            this.priceOfEgg * this.quantityOfEgg +
            this.priceOfPotatoes * this.quantityOfEgg +
            this.priceOfTomatoes * this.quantityOfTomatoes;
    }

    static Row toRow(String[] lines) {
        Row row = new Row();

        row.name = lines[0];
        row.age = Integer.parseInt(lines[1]);
        row.date = LocalDate.parse(lines[2]);

        row.priceOfBread = lines[3].isEmpty() ? 0.0 : Double.parseDouble(lines[3]);
        row.quantityOfBread = lines[4].isEmpty() ? 0 : Integer.parseInt(lines[4]);

        row.priceOfMilk = lines[5].isEmpty() ? 0.0 : Double.parseDouble(lines[5]);
        row.quantityOfMilk = lines[6].isEmpty() ? 0 : Integer.parseInt(lines[6]);

        row.priceOfEgg = lines[7].isEmpty() ? 0.0 : Double.parseDouble(lines[7]);
        row.quantityOfEgg = lines[8].isEmpty() ? 0 : Integer.parseInt(lines[8]);

        row.priceOfPotatoes = lines[9].isEmpty() ? 0.0 : Double.parseDouble(lines[9]);
        row.quantityOfPotatoes = lines[10].isEmpty() ? 0 : Integer.parseInt(lines[10]);

        row.priceOfTomatoes = lines[11].isEmpty() ? 0.0 : Double.parseDouble(lines[11]);
        row.quantityOfTomatoes = lines[12].isEmpty() ? 0 : Integer.parseInt(lines[12]);

        return row;
    }

}
