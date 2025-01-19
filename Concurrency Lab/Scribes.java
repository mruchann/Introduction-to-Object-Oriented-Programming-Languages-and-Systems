import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scribes {

    private static final Lock penlock = new ReentrantLock(true);
    private static final Lock inkBottlelock = new ReentrantLock(true);

    private static final Condition noPen = penlock.newCondition();
    private static final Condition noInkBottle = inkBottlelock.newCondition();

    private static int scribes;
    private static int pens;
    private static int inkBottles;

    public static void main(String[] args) {
        scribes = Integer.parseInt(args[0]);
        pens = Integer.parseInt(args[1]);
        inkBottles = Integer.parseInt(args[2]);

        ExecutorService executor = Executors.newFixedThreadPool(scribes);

        for (int i = 1; i <= scribes; i++) {
            executor.execute(new ScribeTask(i));
        }

        executor.shutdown();

        while (!executor.isTerminated());

    }

    public static class ScribeTask implements Runnable {
        int number;

        public ScribeTask(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (true) {
                takeInkBottle();
                takePen();

                Actions.write(number);

                putPen();
                putInkBottle();
            }
        }

        public void takePen() {
            penlock.lock();

            try {
                while (pens == 0) {
                    noPen.await();
                }

                pens--;
                Actions.takePen(number);
            }

            catch (InterruptedException e) {
                e.printStackTrace();
            }

            finally {
                penlock.unlock();
            }
        }

        public void putPen() {
            penlock.lock();

            try {
                pens++;
                Actions.putPen(number);

                noPen.signal();
            }

            finally {
                penlock.unlock();
            }
        }

        public void takeInkBottle() {
            inkBottlelock.lock();

            try {
                while (inkBottles == 0) {
                    noInkBottle.await();
                }

                inkBottles--;
                Actions.takeBottle(number);
            }

            catch (InterruptedException e) {
                e.printStackTrace();
            }

            finally {
                inkBottlelock.unlock();
            }
        }

        public void putInkBottle() {
            inkBottlelock.lock();

            try {
                inkBottles++;
                Actions.putBottle(number);

                noInkBottle.signal();
            }

            finally {
                inkBottlelock.unlock();
            }
        }
    }
}
