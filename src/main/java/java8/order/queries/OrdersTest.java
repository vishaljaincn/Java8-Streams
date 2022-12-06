package java8.order.queries;

import java8.order.queries.domain.Customer;
import java8.order.queries.domain.Order;
import java8.order.queries.domain.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrdersTest {

    static List<Product> products;
    static List<Order> orders;
    static List<Customer> customers;

    public static void main(String[] args) {
        prepareData();

        // Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100
        System.out.println("Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100");
        products.stream()
                .filter(p -> p.getCategory().equals("Books"))
                .filter(p -> p.getPrice() > 100)
                .forEach(System.out::println);

        // Exercise 2 — Obtain a list of order with products belong to category “Baby”
        System.out.println("\n\nExercise 2 — Obtain a list of order with products belong to category “Baby”");
        orders.stream()
                .filter(order -> order.getProducts().stream().anyMatch(product -> product.getCategory().equals("Baby")))
                .forEach(System.out::println);


        // Exercise 3 — Obtain a list of product with category = “Toys” and then apply 10% discount
        System.out.println("\n\nExercise 3 — Obtain a list of product with category = “Toys” and then apply 10% discount");
        products.stream()
                .filter(product -> product.getCategory().equals("Toys"))
                .map(product -> product.withPrice(product.getPrice() * 0.90))
                .forEach(System.out::println);

        //Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021
        System.out.println("\n\nExercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021");
        orders.stream()
                .filter(order -> order.getCustomer().getTier() == 2)
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 02, 1)))
                .filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 04, 1)))
                .flatMap(order -> order.getProducts().stream())// Stream.of(list1), Stream.of(list2) -> Stream.of(list1,list2)
                .distinct()
                .forEach(System.out::println);

        //Exercise 5 — Get the cheapest products of “Books” category
        System.out.println("\n\nExercise 5 — Get the cheapest products of “Books” category");
        products.stream()
                .filter(product -> product.getCategory().equals("Books"))
                .min(Comparator.comparing(Product::getPrice))
                .ifPresent(System.out::println);
        // --Or --
        products.stream()
                .filter(product -> product.getCategory().equals("Books"))
                .sorted(Comparator.comparing(Product::getPrice))
                .findFirst()
                .ifPresent(System.out::println);

        // Exercise 6 — Get the 3 most recent placed order
        System.out.println("\n\nExercise 6 — Get the 3 most recent placed order");
        orders.stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .forEach(System.out::println);

        // Exercise 7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
        System.out.println("\n\nExercise 7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list");
        orders.stream()
                .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 03, 15)))
                .peek(System.out::println)
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .forEach(System.out::println); // Note: The order of printing of orders and products is not sequential

        // Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021
        System.out.println("\n\nExercise 8 — Calculate total lump sum of all orders placed in Feb 2021\n");
        System.out.println(orders.stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 01, 31)))
                .filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021, 03, 01)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(product -> product.getPrice())
                .sum()); // Note: sum() returns double

        // Exercise 9 — Calculate order average payment placed on 14-Mar-2021
        System.out.println("\n\nExercise 9 — Calculate order average payment placed on 14-Mar-2021");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        ;
        System.out.println(orders.stream()
                .filter(order -> order.getOrderDate().isAfter(LocalDate.parse("14-Mar-2021", df)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average()// Note: average() returns optionalDouble
                .getAsDouble()); // Note: can use DecimalFormatter to convert to 0.00 format


        // Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
        System.out.println("\n\nExercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”");
        DoubleSummaryStatistics stats = products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
        System.out.printf(
                """
                        Count: %S, \
                        Average: %S, \
                        Max: %S, \
                        Min: %S, \
                        Sum: %S
                        """,
                stats.getCount(), stats.getAverage(), stats.getMax(), stats.getMin(), stats.getSum());

        // Exercise 11 — Obtain a data map with order id and order’s product count
        System.out.println("\n\nExercise 11 — Obtain a data map with order id and order’s product count");
        orders.stream()
                .collect(Collectors.toMap(order -> order.getId(), order -> order.getProducts().size()))
                .forEach((id, count) -> System.out.println(id + ":" + count));

        // Exercise 12 — Produce a data map with order records grouped by customer
        System.out.println("\n\nExercise 12 — Produce a data map with order records grouped by customer");
        orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer))
                .forEach((customer, orders) -> System.out.println(customer.getName() + ":" + orders));

        // Exercise 13 — Produce a data map with order record and product total sum
        System.out.println("\n\nExercise 13 — Produce a data map with order record and product total sum");
        orders.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum())))
                .forEach((orderId, sum) -> System.out.println(orderId + ":" + sum));
        // ~ Better way
        orders.stream()
                .collect(Collectors.toMap(Function.identity(), order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum()))
                .forEach((order, sum) -> System.out.println(order + ": " + sum));

        //Exercise 14 — Obtain a data map with list of product name by category ; COLLECTORS.MAPPING usage <-----------------------
        System.out.println("\n\nExercise 14 — Obtain a data map with list of product name by category");
        products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.toList())))
                .forEach((category, names) -> System.out.println(category + ":" + names.stream().collect(Collectors.joining(","))));

        // Exercise 15 — Get the most expensive product by category
        System.out.println("\n\nExercise 15 — Get the most expensive product by category");
        products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.maxBy(Comparator.comparing(Product::getPrice))))
                .forEach((category, product) -> System.out.println(category + ":" + product.orElse(null)));

        // Exercise 16 — Get the most expensive product price in each category
        System.out.println("\n\nExercise 16 — Get the most expensive product by category");
        // TODO:

    }

    private static void prepareData() {
        Customer customer1 = new Customer(1L, "Stefan Walker", 1);
        Customer customer2 = new Customer(2L, "Daija Von", 1);
        Customer customer3 = new Customer(3L, "Ariane Rodriguez", 1);
        Customer customer4 = new Customer(4L, "Marques Nikolaus", 2);
        Customer customer5 = new Customer(5L, "Rachelle Greenfelder", 0);
        Customer customer6 = new Customer(6L, "Larissa White", 2);
        Customer customer7 = new Customer(7L, "Fae Heidenreich", 1);
        Customer customer8 = new Customer(8L, "Dino Will", 2);
        Customer customer9 = new Customer(9L, "Eloy Stroman", 1);
        Customer customer10 = new Customer(10L, "Brisa O\\'Connell", 1);
        customers = List.of(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8, customer9, customer10);

        Product product1 = new Product(1L, "omnis quod consequatur", "Games", 184.83, null);
        Product product2 = new Product(2L, "vel libero suscipit", "Toys", 12.66, null);
        Product product3 = new Product(3L, "non nemo iure", "Grocery", 498.02, null);
        Product product4 = new Product(4L, "voluptatem voluptas aspernatur", "Toys", 536.80, null);
        Product product5 = new Product(5L, "animi cum rem", "Games", 458.20, null);
        Product product6 = new Product(6L, "dolorem porro debitis", "Toys", 146.52, null);
        Product product7 = new Product(7L, "aspernatur rerum qui", "Books", 656.42, null);
        Product product8 = new Product(8L, "deleniti earum et", "Baby", 41.46, null);
        Product product9 = new Product(9L, "voluptas ut quidem", "Books", 697.57, null);
        Product product10 = new Product(10L, "eos sed debitis", "Baby", 366.90, null);
        Product product11 = new Product(11L, "laudantium sit nihil", "Toys", 95.50, null);
        Product product12 = new Product(12L, "ut perferendis corporis", "Grocery", 302.19, null);
        Product product13 = new Product(13L, "sint voluptatem ut", "Toys", 295.37, null);
        Product product14 = new Product(14L, "quos sunt ipsam", "Grocery", 534.64, null);
        Product product15 = new Product(15L, "qui illo error", "Baby", 623.58, null);
        Product product16 = new Product(16L, "aut ex ducimus", "Books", 551.39, null);
        Product product17 = new Product(17L, "accusamus repellendus minus", "Books", 240.58, null);
        Product product18 = new Product(18L, "aut accusamus quia", "Baby", 881.38, null);
        Product product19 = new Product(19L, "doloremque incidunt sed", "Games", 988.49, null);
        Product product20 = new Product(20L, "libero omnis velit", "Baby", 177.61, null);
        Product product21 = new Product(21L, "consectetur cupiditate sunt", "Toys", 95.46, null);
        Product product22 = new Product(22L, "itaque ea qui", "Baby", 677.78, null);
        Product product23 = new Product(23L, "non et nulla", "Grocery", 70.49, null);
        Product product24 = new Product(24L, "veniam consequatur et", "Books", 893.44, null);
        Product product25 = new Product(25L, "magnam adipisci voluptate", "Grocery", 366.13, null);
        Product product26 = new Product(26L, "reiciendis consequuntur placeat", "Toys", 359.27, null);
        Product product27 = new Product(27L, "dolores ipsum sit", "Toys", 786.99, null);
        Product product28 = new Product(28L, "ut hic tempore", "Toys", 316.09, null);
        Product product29 = new Product(29L, "quas quis deserunt", "Toys", 772.78, null);
        Product product30 = new Product(30L, "excepturi nesciunt accusantium", "Toys", 911.46, null);

        products = List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11, product12, product13, product14, product15, product16, product17, product18, product19, product20, product21, product22, product23, product24, product25, product26, product27, product28, product29, product30);

        // Order
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Order order1 = new Order(1L, LocalDate.parse("2021-02-28", formatter), LocalDate.parse("2021-03-08", formatter), "NEW", customer5, null);
        Order order2 = new Order(2L, LocalDate.parse("2021-02-28", formatter), LocalDate.parse("2021-03-05", formatter), "NEW", customer3, null);
        Order order3 = new Order(3L, LocalDate.parse("2021-04-10", formatter), LocalDate.parse("2021-04-18", formatter), "DELIVERED", customer5, null);
        Order order4 = new Order(4L, LocalDate.parse("2021-03-22", formatter), LocalDate.parse("2021-03-27", formatter), "PENDING", customer3, null);
        Order order5 = new Order(5L, LocalDate.parse("2021-03-04", formatter), LocalDate.parse("2021-03-12", formatter), "NEW", customer1, null);
        Order order6 = new Order(6L, LocalDate.parse("2021-03-30", formatter), LocalDate.parse("2021-04-07", formatter), "DELIVERED", customer9, null);
        Order order7 = new Order(7L, LocalDate.parse("2021-03-05", formatter), LocalDate.parse("2021-03-09", formatter), "PENDING", customer8, null);
        Order order8 = new Order(8L, LocalDate.parse("2021-03-27", formatter), LocalDate.parse("2021-04-05", formatter), "NEW", customer4, null);
        Order order9 = new Order(9L, LocalDate.parse("2021-04-14", formatter), LocalDate.parse("2021-04-18", formatter), "NEW", customer10, null);
        Order order10 = new Order(10L, LocalDate.parse("2021-03-10", formatter), LocalDate.parse("2021-03-19", formatter), "NEW", customer8, null);
        Order order11 = new Order(11L, LocalDate.parse("2021-04-01", formatter), LocalDate.parse("2021-04-04", formatter), "DELIVERED", customer1, null);
        Order order12 = new Order(12L, LocalDate.parse("2021-02-24", formatter), LocalDate.parse("2021-02-28", formatter), "PENDING", customer5, null);
        Order order13 = new Order(13L, LocalDate.parse("2021-03-15", formatter), LocalDate.parse("2021-03-21", formatter), "NEW", customer5, null);
        Order order14 = new Order(14L, LocalDate.parse("2021-03-30", formatter), LocalDate.parse("2021-04-07", formatter), "PENDING", customer4, null);
        Order order15 = new Order(15L, LocalDate.parse("2021-03-13", formatter), LocalDate.parse("2021-03-14", formatter), "DELIVERED", customer5, null);
        Order order16 = new Order(16L, LocalDate.parse("2021-03-13", formatter), LocalDate.parse("2021-03-21", formatter), "NEW", customer1, null);
        Order order17 = new Order(17L, LocalDate.parse("2021-03-31", formatter), LocalDate.parse("2021-03-31", formatter), "DELIVERED", customer6, null);
        Order order18 = new Order(18L, LocalDate.parse("2021-03-25", formatter), LocalDate.parse("2021-03-31", formatter), "PENDING", customer9, null);
        Order order19 = new Order(19L, LocalDate.parse("2021-02-28", formatter), LocalDate.parse("2021-03-09", formatter), "DELIVERED", customer9, null);
        Order order20 = new Order(20L, LocalDate.parse("2021-03-23", formatter), LocalDate.parse("2021-03-30", formatter), "NEW", customer5, null);
        Order order21 = new Order(21L, LocalDate.parse("2021-03-19", formatter), LocalDate.parse("2021-03-24", formatter), "DELIVERED", customer9, null);
        Order order22 = new Order(22L, LocalDate.parse("2021-02-27", formatter), LocalDate.parse("2021-03-01", formatter), "NEW", customer5, null);
        Order order23 = new Order(23L, LocalDate.parse("2021-04-19", formatter), LocalDate.parse("2021-04-24", formatter), "PENDING", customer4, null);
        Order order24 = new Order(24L, LocalDate.parse("2021-03-24", formatter), LocalDate.parse("2021-03-24", formatter), "DELIVERED", customer1, null);
        Order order25 = new Order(25L, LocalDate.parse("2021-03-03", formatter), LocalDate.parse("2021-03-10", formatter), "NEW", customer1, null);
        Order order26 = new Order(26L, LocalDate.parse("2021-03-17", formatter), LocalDate.parse("2021-03-26", formatter), "NEW", customer10, null);
        Order order27 = new Order(27L, LocalDate.parse("2021-03-20", formatter), LocalDate.parse("2021-03-25", formatter), "NEW", customer1, null);
        Order order28 = new Order(28L, LocalDate.parse("2021-04-09", formatter), LocalDate.parse("2021-04-16", formatter), "DELIVERED", customer2, null);
        Order order29 = new Order(29L, LocalDate.parse("2021-04-06", formatter), LocalDate.parse("2021-04-08", formatter), "PENDING", customer1, null);
        Order order30 = new Order(30L, LocalDate.parse("2021-04-19", formatter), LocalDate.parse("2021-04-20", formatter), "DELIVERED", customer1, null);
        Order order31 = new Order(31L, LocalDate.parse("2021-03-03", formatter), LocalDate.parse("2021-03-04", formatter), "NEW", customer3, null);
        Order order32 = new Order(32L, LocalDate.parse("2021-03-15", formatter), LocalDate.parse("2021-03-24", formatter), "DELIVERED", customer2, null);
        Order order33 = new Order(33L, LocalDate.parse("2021-04-18", formatter), LocalDate.parse("2021-04-24", formatter), "PENDING", customer1, null);
        Order order34 = new Order(34L, LocalDate.parse("2021-03-28", formatter), LocalDate.parse("2021-03-28", formatter), "NEW", customer6, null);
        Order order35 = new Order(35L, LocalDate.parse("2021-03-15", formatter), LocalDate.parse("2021-03-17", formatter), "NEW", customer1, null);
        Order order36 = new Order(36L, LocalDate.parse("2021-03-04", formatter), LocalDate.parse("2021-03-08", formatter), "DELIVERED", customer2, null);
        Order order37 = new Order(37L, LocalDate.parse("2021-03-18", formatter), LocalDate.parse("2021-03-25", formatter), "NEW", customer8, null);
        Order order38 = new Order(38L, LocalDate.parse("2021-04-11", formatter), LocalDate.parse("2021-04-20", formatter), "NEW", customer8, null);
        Order order39 = new Order(39L, LocalDate.parse("2021-04-12", formatter), LocalDate.parse("2021-04-17", formatter), "NEW", customer9, null);
        Order order40 = new Order(40L, LocalDate.parse("2021-03-12", formatter), LocalDate.parse("2021-03-12", formatter), "PENDING", customer3, null);
        Order order41 = new Order(41L, LocalDate.parse("2021-02-24", formatter), LocalDate.parse("2021-02-26", formatter), "NEW", customer5, null);
        Order order42 = new Order(42L, LocalDate.parse("2021-04-08", formatter), LocalDate.parse("2021-04-14", formatter), "DELIVERED", customer9, null);
        Order order43 = new Order(43L, LocalDate.parse("2021-03-03", formatter), LocalDate.parse("2021-03-11", formatter), "NEW", customer3, null);
        Order order44 = new Order(44L, LocalDate.parse("2021-03-12", formatter), LocalDate.parse("2021-03-14", formatter), "DELIVERED", customer4, null);
        Order order45 = new Order(45L, LocalDate.parse("2021-04-01", formatter), LocalDate.parse("2021-04-06", formatter), "DELIVERED", customer1, null);
        Order order46 = new Order(46L, LocalDate.parse("2021-03-16", formatter), LocalDate.parse("2021-03-22", formatter), "NEW", customer10, null);
        Order order47 = new Order(47L, LocalDate.parse("2021-04-07", formatter), LocalDate.parse("2021-04-12", formatter), "PENDING", customer2, null);
        Order order48 = new Order(48L, LocalDate.parse("2021-04-05", formatter), LocalDate.parse("2021-04-06", formatter), "NEW", customer2, null);
        Order order49 = new Order(49L, LocalDate.parse("2021-04-10", formatter), LocalDate.parse("2021-04-13", formatter), "NEW", customer7, null);
        Order order50 = new Order(50L, LocalDate.parse("2021-03-18", formatter), LocalDate.parse("2021-03-21", formatter), "NEW", customer9, null);

        order1.getProducts().add(product19);
        order1.getProducts().add(product21);
        order1.getProducts().add(product5);

        order2.getProducts().add(product17);
        order2.getProducts().add(product11);
        order2.getProducts().add(product14);
        order2.getProducts().add(product13);
        order3.getProducts().add(product5);
        order3.getProducts().add(product3);
        order3.getProducts().add(product19);
        order3.getProducts().add(product13);
        order3.getProducts().add(product15);
        order4.getProducts().add(product22);
        order4.getProducts().add(product26);
        order4.getProducts().add(product12);
        order5.getProducts().add(product5);
        order6.getProducts().add(product5);
        order6.getProducts().add(product12);
        order7.getProducts().add(product8);
        order7.getProducts().add(product25);
        order7.getProducts().add(product21);
        order7.getProducts().add(product1);
        order7.getProducts().add(product13);
        order7.getProducts().add(product10);
        order8.getProducts().add(product12);
        order8.getProducts().add(product8);
        order9.getProducts().add(product8);
        order10.getProducts().add(product14);
        order10.getProducts().add(product8);
        order11.getProducts().add(product12);
        order11.getProducts().add(product6);
        order11.getProducts().add(product21);
        order11.getProducts().add(product22);
        order11.getProducts().add(product27);
        order11.getProducts().add(product11);
        order12.getProducts().add(product19);
        order12.getProducts().add(product13);
        order12.getProducts().add(product11);
        order12.getProducts().add(product6);
        order13.getProducts().add(product11);
        order13.getProducts().add(product24);
        order13.getProducts().add(product26);
        order13.getProducts().add(product23);
        order14.getProducts().add(product18);
        order15.getProducts().add(product16);
        order15.getProducts().add(product13);
        order15.getProducts().add(product7);
        order16.getProducts().add(product23);
        order16.getProducts().add(product29);
        order16.getProducts().add(product18);
        order16.getProducts().add(product16);
        order16.getProducts().add(product22);
        order16.getProducts().add(product26);
        order17.getProducts().add(product18);
        order17.getProducts().add(product4);
        order18.getProducts().add(product13);
        order18.getProducts().add(product3);
        order18.getProducts().add(product27);
        order18.getProducts().add(product2);
        order19.getProducts().add(product26);
        order19.getProducts().add(product22);
        order19.getProducts().add(product18);
        order19.getProducts().add(product14);
        order19.getProducts().add(product15);
        order19.getProducts().add(product13);
        order20.getProducts().add(product22);
        order21.getProducts().add(product21);
        order21.getProducts().add(product26);
        order22.getProducts().add(product7);
        order22.getProducts().add(product6);
        order23.getProducts().add(product27);
        order23.getProducts().add(product7);
        order23.getProducts().add(product11);
        order23.getProducts().add(product5);
        order24.getProducts().add(product24);
        order24.getProducts().add(product2);
        order24.getProducts().add(product6);
        order24.getProducts().add(product28);
        order24.getProducts().add(product4);
        order25.getProducts().add(product28);
        order25.getProducts().add(product17);
        order25.getProducts().add(product2);
        order25.getProducts().add(product29);
        order25.getProducts().add(product19);
        order26.getProducts().add(product4);
        order27.getProducts().add(product6);
        order27.getProducts().add(product15);
        order27.getProducts().add(product24);
        order28.getProducts().add(product22);
        order28.getProducts().add(product9);
        order29.getProducts().add(product22);
        order30.getProducts().add(product29);
        order30.getProducts().add(product6);
        order30.getProducts().add(product8);
        order31.getProducts().add(product16);
        order31.getProducts().add(product12);
        order31.getProducts().add(product28);
        order32.getProducts().add(product8);
        order32.getProducts().add(product5);
        order33.getProducts().add(product12);
        order33.getProducts().add(product26);
        order33.getProducts().add(product21);
        order33.getProducts().add(product23);
        order33.getProducts().add(product29);
        order33.getProducts().add(product13);
        order34.getProducts().add(product1);
        order34.getProducts().add(product6);
        order34.getProducts().add(product22);
        order34.getProducts().add(product19);
        order34.getProducts().add(product13);
        order34.getProducts().add(product27);
        order35.getProducts().add(product5);
        order35.getProducts().add(product11);
        order35.getProducts().add(product26);
        order35.getProducts().add(product9);
        order36.getProducts().add(product28);
        order36.getProducts().add(product7);
        order37.getProducts().add(product15);
        order37.getProducts().add(product11);
        order38.getProducts().add(product18);
        order38.getProducts().add(product11);
        order38.getProducts().add(product14);
        order38.getProducts().add(product20);
        order38.getProducts().add(product7);
        order39.getProducts().add(product1);
        order39.getProducts().add(product21);
        order40.getProducts().add(product12);
        order40.getProducts().add(product10);
        order40.getProducts().add(product11);
        order40.getProducts().add(product29);
        order40.getProducts().add(product1);
        order41.getProducts().add(product13);
        order41.getProducts().add(product19);
        order41.getProducts().add(product5);
        order41.getProducts().add(product29);
        order41.getProducts().add(product14);
        order41.getProducts().add(product4);
        order42.getProducts().add(product2);
        order43.getProducts().add(product6);
        order44.getProducts().add(product20);
        order44.getProducts().add(product18);
        order44.getProducts().add(product8);
        order44.getProducts().add(product24);
        order44.getProducts().add(product26);
        order44.getProducts().add(product13);
        order45.getProducts().add(product23);
        order45.getProducts().add(product1);
        order45.getProducts().add(product25);
        order45.getProducts().add(product15);
        order46.getProducts().add(product16);
        order46.getProducts().add(product24);
        order46.getProducts().add(product19);
        order46.getProducts().add(product13);
        order46.getProducts().add(product11);
        order47.getProducts().add(product23);
        order47.getProducts().add(product28);
        order47.getProducts().add(product20);
        order47.getProducts().add(product21);
        order48.getProducts().add(product15);
        order48.getProducts().add(product3);
        order48.getProducts().add(product26);
        order48.getProducts().add(product7);
        order48.getProducts().add(product19);
        order48.getProducts().add(product10);
        order49.getProducts().add(product5);
        order49.getProducts().add(product13);
        order49.getProducts().add(product29);
        order49.getProducts().add(product3);
        order49.getProducts().add(product12);
        order49.getProducts().add(product17);
        order50.getProducts().add(product15);
        order50.getProducts().add(product16);

        orders = List.of(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10, order11, order12, order13, order14, order15, order16, order17, order18, order19, order20, order21, order22, order23, order24, order25, order26, order27, order28, order29, order30, order31, order32, order33, order34, order35, order36, order37, order38, order39, order40, order41, order42, order43, order44, order45, order46, order47, order48, order49, order50);
    }

}
