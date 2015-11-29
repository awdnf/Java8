package br.com.awdnf.streams;

import br.com.awdnf.defaultmethods.User;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class MoreStreams {

    public static void main(String[] args) throws Exception {

        Random random = new Random();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100 ; i++) {
            users.add(new User("User " + i, random.nextInt(500)));
        }

        //given a List, we can sort using comparing
        users.sort(comparingInt(User::getPoints));

        /* with Streams we have the sorted() method
           the main difference is that with stream
           the original list doesnt change
        */
        users.stream()
                .filter(u -> u.getPoints() > 200)
                .sorted(comparing(User::getName))
                .collect(toList())
                .forEach(System.out::println);

        System.out.println("=====================");

        //findAny method, assuming que want
        //to find any moderator
        //since Stream is Lazy, no need to filter
        // the whole stream
        Optional<User> optionalUser = users.stream()
                .filter(User::isModerator)
                .findAny();

        System.out.println(optionalUser);

        System.out.println("=====================");

        //we can use the peek() method to execute a task everytime
        //the stream processes an element
        //the first peek prints users until it fullfills the filter predicate
        //and the second only after it fullfills the filter predicate
        users.stream()
                .peek(System.out::println)
                .filter(u -> u.getPoints() > 100)
                .peek(System.out::println)
                .findAny();

        System.out.println("=====================");

        //this code for instance doesnt print anything because
        //its awaiting a terminal operation like findAny, collect
        //or forEach
        //worth mentioning that sorted is a statefull method and
        //therefore might process the whole stream
        users.stream()
                .filter(u -> u.getPoints() > 100)
                .peek(System.out::println);

        System.out.println("=====================");

        //some operations are known as reduction like average()
        double averagePoints = users.stream()
                .mapToInt(User::getPoints)
                .average()
                .getAsDouble();

        System.out.println(averagePoints);

        System.out.println("=====================");

        //there are others reduction operations
        //like count, min, max, sum
        Optional<User> max = users.stream()
                .max(comparing(User::getPoints));
        User userMaxPoints = max.get();
        System.out.println(userMaxPoints);

        System.out.println("=====================");

        int totalPoints = users.stream()
                .mapToInt(User::getPoints)
                .sum();

        System.out.println(totalPoints);

        //the following code equals to the sum method
        int initialValue = 0;
        //functional interface which defines applyAsInt
        IntBinaryOperator sum = (a, b) -> a + b;
        int total = users.stream()
                .mapToInt(User::getPoints)
                .reduce(initialValue, sum);

        //or using local vars and lambda
        int totalLambda = users.stream()
                .mapToInt(User::getPoints)
                .reduce(0, (a, b) -> a + b);

        //or even taking advantage of the Integer.sum static method
        //no additional advantages over the reduce
        int sumStatic = users.stream()
                .mapToInt(User::getPoints)
                .reduce(0, Integer::sum);

        //but we can use other operations not
        //present on the Stream like multiply
        int product = users.stream()
                .mapToInt(User::getPoints)
                .reduce(0, (a, b) -> a * b);

        System.out.println("=====================");

        //we can also use the Iterator interface with stream
        //if we want to change objects within a stream
        //if its not being users with parallel streams
        users.stream().iterator()
                .forEachRemaining(System.out::println);

        System.out.println("=====================");

        //another example with testing predicates
        //when we dont need a filtered list
        boolean hasModerator = users.stream()
                .anyMatch(User::isModerator);
        System.out.println(hasModerator);

        boolean onlyModerators = users.stream()
                .allMatch(User::isModerator);
        System.out.println(onlyModerators);

        boolean noModerators = users.stream()
                .noneMatch(User::isModerator);
        System.out.println(noModerators);

        System.out.println("=====================");

        //more stream exercises

        long count = users.stream().count();
        System.out.println(count);

        System.out.println("=====================");

        users.stream()
                .filter(u -> u.getPoints() > 300)
                .skip(10)
                .collect(toList())
                .forEach(System.out::println);

        System.out.println("=====================");

        users.stream()
                .sorted(comparingInt(User::getPoints).reversed())
                .filter(u -> u.getPoints() > 300)
                .limit(10)
                .collect(toList())
                .forEach(System.out::println);

        System.out.println("=====================");

        Stream<User> emptyStream = Stream.empty();

        Stream<User> streamOf = Stream.of(
                users.get(0), users.get(1), users.get(3));

        Stream<String> streamArray = Arrays.stream(args);

        System.out.println("=====================");

        //infinite stream
        Supplier<Integer> supplier = () -> random.nextInt();
        Stream<Integer> stream = Stream.generate(supplier);

        IntStream intStream = IntStream.generate(() -> random.nextInt());

        //carefull with methods that needs
        //to process the whole stream cuz
        //its infinite
        //int value = intStream.sum();

        //we can however have circuit-break operations
        //the boxed() method returns a Stream<Integer>
        //instead of an IntStream, enabling us to
        //invoke collect()
        List<Integer> list = intStream
                .limit(100)
                .boxed()
                .collect(toList());

        //fluent interface version
        List<Integer> listFluent = IntStream
                .generate(() -> random.nextInt())
                .limit(100)
                .boxed()
                .collect(Collectors.toList());

        IntStream.generate(new MoreStreams().new Fibonacci())
                .limit(10)
                .forEach(System.out::println);

        System.out.println("=====================");

        //another circuit break operation is findFirst()
        //filter() is not circuit-break
        int greaterThan100 = IntStream
                .generate(new MoreStreams().new Fibonacci())
                .filter(fibonacci -> fibonacci > 100)
                .findFirst()
                .getAsInt();

        System.out.println(greaterThan100);

        System.out.println("=====================");

        //if we need to keep track of only 1 variable
        //we can use iterate instead of generate
        //which takes an UnaryOperator
        IntStream.iterate(0, x -> x + 1)
                .limit(10)
                .forEach(System.out::println);

        System.out.println("=====================");

        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .forEach(System.out::println);

        System.out.println("=====================");

        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(System.out::println);

        System.out.println("=====================");

        Files.list(Paths.get("./study/src/br/com/awdnf"))
                .filter(f -> f.toFile().isDirectory())
                .forEach(System.out::println);

        System.out.println("=====================");

        //wont compile because lambda is throwing exception
        /*Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .map(p -> Files.lines(p))
                .forEach(System.out::println);
        */

        //this map returns a Stream<Stream<String>>
        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .map(p -> lines(p))
                .forEach(System.out::println);

        System.out.println("=====================");

        //we can "fix" this using flatMap
        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .flatMap(p -> lines(p))
                .forEach(System.out::println);

        System.out.println("=====================");

        //we can also chain it (getting all chars in the files)
        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .flatMap(p -> lines(p))
                .flatMapToInt((String s) -> s.chars())
                .forEach(System.out::println);

        System.out.println("=====================");

        Group englishSpeakers = new MoreStreams().new Group();
        englishSpeakers.add(users.get(0));
        englishSpeakers.add(users.get(1));
        englishSpeakers.add(users.get(2));

        Group spanishSpeakers = new MoreStreams().new Group();
        spanishSpeakers.add(users.get(1));
        spanishSpeakers.add(users.get(2));
        spanishSpeakers.add(users.get(3));

        List<Group> groups = Arrays.asList(englishSpeakers, spanishSpeakers);

        //this way we can have non duplicates users from both groups
        //flatMap "unwraps" (flats :p) the nested Streams
        groups.stream()
                .flatMap(g -> g.getUsers().stream())
                .distinct()
                .forEach(System.out::println);

        System.out.println("=====================");

        //instead of distinct() we can also collect
        //it to a Set in order to get non duplicates
        groups.stream()
                .flatMap(g -> g.getUsers().stream())
                .collect(toSet())
                .forEach(System.out::println);

        System.out.println("=====================");

    }

    //Keeping state can be usefull for Suppliers
    //creating this class because lambda cant
    //declare attributes
    class Fibonacci implements IntSupplier {

        private int previous = 0;
        private int next = 1;

        public int getAsInt() {
            next = next + previous;
            previous = next - previous;
            return previous;
        }
    }

    static Stream<String> lines(Path p) {
        try {
            return Files.lines(p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    class Group {

        private Set<User> users = new HashSet<>();

        public void add(User u) {
            users.add(u);
        }

        public Set<User> getUsers() {
            return Collections.unmodifiableSet(this.users);
        }
    }


}
