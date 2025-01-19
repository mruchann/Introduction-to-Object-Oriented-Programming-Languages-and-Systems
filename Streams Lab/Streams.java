import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streams {
    private static String inputFile;
    private static int q;

    public static void main(String[] args) throws IOException {
        inputFile = args[0];
        q = Integer.parseInt(args[1]);
        process();
    }

    public static Stream<Row> getRowStream() throws IOException {
        return Files.lines(Paths.get(inputFile))
            .skip(1)
            .map(line -> line.split(",", -1))
            .map(Row::toRow);
    }

    public static long getFrequency(Predicate<Row> datePredicate, Predicate<Row> presencePredicate) throws IOException {
        return getRowStream()
            .filter(datePredicate)
            .filter(presencePredicate)
            .count();
    }

    public static long getNumberOfPurchases(Predicate<Row> datePredicate, Function<Row, Integer> mapFunction) throws IOException {
        return getRowStream()
            .filter(datePredicate)
            .map(mapFunction)
            .reduce(Integer::sum)
            .orElse(0);
    }

    public static double getAverageAge(Predicate<Row> presencePredicate) throws IOException {
        return getRowStream()
            .filter(presencePredicate)
            .mapToInt(row -> row.age)
            .average()
            .orElse(0.0);
    }

    public static double getInflation(Predicate<Row> presencePredicate, Function<Row, Double> mapFunction) throws IOException {
        return getRowStream()
            .filter(presencePredicate)
            .max(Comparator.comparing(row -> row.date))
            .map(mapFunction)
            .orElse(0.0)

            -

            getRowStream()
                .filter(presencePredicate)
                .min(Comparator.comparing(row -> row.date))
                .map(mapFunction)
                .orElse(0.0);

    }

    public static void process() throws IOException {
        Stream<Row> rows = getRowStream();

        switch(q) {
            case 1:
                int answer1 = rows
                    .filter(row -> row.name.startsWith("A"))
                    .mapToInt(Row::getQuantities)
                    .sum();
                System.out.println(answer1);
                break;

            case 2:
                double answer2 = rows
                    .mapToDouble(Row::getMostExpensiveProductPrice)
                    .max()
                    .orElse(0.0);
                System.out.println(answer2);
                break;

            case 3:
                LocalDate answer3 = rows
                    .max(Comparator.comparingDouble(Row::getPurchaseAmount))
                    .map(row -> row.date)
                    .orElse(null);
                System.out.println(answer3);
                break;

            case 4:
                String answer4 = "";
                int[] freq = { 0, 0, 0, 0, 0 };

                getRowStream()
                    .filter(row -> row.date.isBefore(LocalDate.of(2000, 1, 1)))
                    .forEach( row -> {
                        freq[0] += row.quantityOfBread > 0 ? 1 : 0;
                        freq[1] += row.quantityOfMilk > 0 ? 1 : 0;
                        freq[2] += row.quantityOfEgg > 0 ? 1 : 0;
                        freq[3] += row.quantityOfPotatoes > 0 ? 1 : 0;
                        freq[4] += row.quantityOfTomatoes > 0 ? 1 : 0;
                    });

                int maxFreq = Arrays.stream(freq).max().orElse(0);
                if (maxFreq == freq[0]) {
                    answer4 = "bread";
                } else if (maxFreq == freq[1]) {
                    answer4 = "milk";
                } else if (maxFreq == freq[2]) {
                    answer4 = "egg";
                } else if (maxFreq == freq[3]) {
                    answer4 = "potatoes";
                } else if (maxFreq == freq[4]) {
                    answer4 = "tomatoes";
                }
                System.out.println(answer4);
                break;

            case 5:
                String answer5 = "";
                int[] purchased = { 0, 0, 0, 0, 0 };

                getRowStream()
                    .filter(row -> row.date.isAfter(LocalDate.of(1999, 12, 31)))
                    .forEach(row -> {
                        purchased[0] += row.quantityOfBread;
                        purchased[1] += row.quantityOfMilk;
                        purchased[2] += row.quantityOfEgg;
                        purchased[3] += row.quantityOfPotatoes;
                        purchased[4] += row.quantityOfTomatoes;
                    });

                int minPurchase = Arrays.stream(purchased).min().orElse(0);
                if (minPurchase == purchased[0]) {
                    answer5 = "bread";
                } else if (minPurchase == purchased[1]) {
                    answer5 = "milk";
                } else if (minPurchase == purchased[2]) {
                    answer5 = "egg";
                } else if (minPurchase == purchased[3]) {
                    answer5 = "potatoes";
                } else if (minPurchase == purchased[4]) {
                    answer5 = "tomatoes";
                }
                System.out.println(answer5);
                break;

            case 6:
                String answer6 = "";

                int[] n = { 0, 0, 0, 0, 0 };
                int[] ageSum = { 0, 0, 0, 0, 0 };

                getRowStream()
                    .forEach(row -> {
                        if (row.quantityOfBread > 0) {
                            ageSum[0] += row.age;
                            n[0]++;
                        }

                        if (row.quantityOfMilk > 0) {
                            ageSum[1] += row.age;
                            n[1]++;
                        }

                        if (row.quantityOfEgg > 0) {
                            ageSum[2] += row.age;
                            n[2]++;
                        }

                        if (row.quantityOfPotatoes > 0) {
                            ageSum[3] += row.age;
                            n[3]++;
                        }

                        if (row.quantityOfTomatoes > 0) {
                            ageSum[4] += row.age;
                            n[4]++;
                        }
                    });

                IntBinaryOperator function = (i, j) -> (ageSum[i] / (double) n[i]) < (ageSum[j] / (double) n[j]) ? i : j;

                int index = IntStream
                    .range(0, 5)
                    .filter(i -> n[i] > 0)
                    .reduce(function)
                    .orElse(-1);

                if (index == 0) {
                    answer6 = "bread";
                } else if (index == 1) {
                    answer6 = "milk";
                } else if (index == 2) {
                    answer6 = "egg";
                } else if (index == 3) {
                    answer6 = "potatoes";
                } else if (index == 4) {
                    answer6 = "tomatoes";
                }
                System.out.println(answer6);
                break;

            case 7:
                String answer7 = "";

                LocalDate[] firstDates = {LocalDate.MAX, LocalDate.MAX, LocalDate.MAX, LocalDate.MAX, LocalDate.MAX};
                LocalDate[] lastDates = {LocalDate.MIN, LocalDate.MIN, LocalDate.MIN, LocalDate.MIN, LocalDate.MIN};

                double[] firstPrice = {0.0, 0.0, 0.0, 0.0, 0.0};
                double[] lastPrice = {0.0, 0.0, 0.0, 0.0, 0.0};

                // you get the idea!

                getRowStream()
                    .forEach(row -> {
                        if (row.quantityOfBread > 0) {
                            if (row.date.isBefore(firstDates[0])) {
                                firstPrice[0] = row.priceOfBread;
                                firstDates[0] = row.date;
                            }

                            if (row.date.isAfter(lastDates[0])) {
                                lastPrice[0] = row.priceOfBread;
                                lastDates[0] = row.date;
                            }
                        }

                        if (row.quantityOfMilk > 0) {

                        }

                        if (row.quantityOfEgg > 0) {

                        }

                        if (row.quantityOfPotatoes > 0) {

                        }

                        if (row.quantityOfTomatoes > 0) {

                        }
                    });

                double maxInflation = Collections.max(List.of(breadInflation, milkInflation, eggInflation, potatoesInflation, tomatoesInflation));

                if (maxInflation == breadInflation) {
                    answer7 = "bread";
                } else if (maxInflation == milkInflation) {
                    answer7 = "milk";
                } else if (maxInflation == eggInflation) {
                    answer7 = "egg";
                } else if (maxInflation == potatoesInflation) {
                    answer7 = "potatoes";
                } else if (maxInflation == tomatoesInflation) {
                    answer7 = "tomatoes";
                }
                System.out.println(answer7);
                break;
        }
    }
}
