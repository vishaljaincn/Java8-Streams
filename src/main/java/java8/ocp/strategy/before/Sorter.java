package java8.ocp.strategy.before;

public class Sorter {
    String type;

    public Sorter(String type) {
        this.type = type;
    }

    public void sort(int[] array) {
        if(type.equals("bubble")) {
            bubbleSort(array);
        } else if(type.equals("selection")) {
            selectionSort(array);
        }

        // OCP VIOLATION:
        // For new sorting algorithms, we need to modify this class and add new if-else statements here.
    }

    private static void selectionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private static void bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}
