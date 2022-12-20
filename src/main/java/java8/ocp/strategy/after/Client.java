package java8.ocp.strategy.after;

import java.util.Arrays;

public class Client {

    public static void main(String[] args) {
        int[] array = {21, 12, 33, 4, 5};
        Sorter sorter = new Sorter(new BubbleSort());
        sorter.sort(array);

        System.out.println(Arrays.toString(array));
    }
}
