package java8.random;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


// Count occurrences of each character in a string
public class CharacterCounter {

    // Using Java7
    public Map<Character, Integer> count0(String str) {
        Map<Character, Integer> map = new java.util.HashMap<>();
        for (char c : str.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        return map;
    }

    public Map<Character, Long> count1(String str) {
        return str.chars() // IntStream
                .mapToObj(c -> (char) c) // convert int to char
                .collect(Collectors.groupingBy(c -> c, Collectors.counting())); // Map<Character, Long>
                //.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // Function.identity() is the same as c -> c
    }

    public Map<Character, Integer> count2(String str) {
        return str.chars() // IntStream
                .mapToObj(c -> (char) c) // convert int to char
                .collect(Collectors.toMap(
                        k -> k,
                        v -> 1,
                        Integer::sum)); // Map<Character, Integer>
    }

    public Map<Character, Integer> count3(String str) {
        return str.chars() // IntStream
                .boxed() // Stream<Integer>
                .collect(Collectors.toMap(
                        k -> Character.valueOf((char) k.intValue()),
                        v -> 1,
                        Integer::sum)); // Map<Character, Integer>
    }

    public Map<Character, Long> count4(String str) {
        return Stream.of(str.split("")) // Stream<String>
                .collect(Collectors.groupingBy(c -> c.charAt(0), Collectors.counting())); // Map<Character, Long>
    }


    public static void main(String[] args) throws Exception {
        CharacterCounter obj = new CharacterCounter();
        String str = "Hello this is a test string";

        // Execute the methods dynamically from count0 to count4
        /*IntStream.rangeClosed(0, 4)
                .forEach(i -> {
                    try {
                        System.out.println(
                                CharacterCounter.class
                                        .getMethod("count" + i, String.class)
                                        .invoke(new CharacterCounter(), str));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });*/

        System.out.println(obj.count0(str));
        System.out.println(obj.count1(str));
        System.out.println(obj.count2(str));
        System.out.println(obj.count3(str));
        System.out.println(obj.count4(str));

        // Note: To make it case insensitive count just convert the string to lower case
    }
}
