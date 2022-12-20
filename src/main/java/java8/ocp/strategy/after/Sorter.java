package java8.ocp.strategy.after;

public class Sorter {

    SortingStrategy strategy; //composition

    public Sorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(int[] array) {
        strategy.sort(array);
    }
}
