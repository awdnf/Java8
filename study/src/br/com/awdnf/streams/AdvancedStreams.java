package br.com.awdnf.streams;

import br.com.awdnf.defaultmethods.User;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class AdvancedStreams {

    private static long total = 0;

    public static void main(String[] args) throws Exception {

        Random random = new Random();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            users.add(new User(i, "User " + i, random.nextInt(500)));
        }

        //getting a LongStream with the files line numbers
        LongStream linesStream = Files
                .list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .mapToLong(p -> lines(p).count());

        //collecting to a List
        List<Long> linesList = Files
                .list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .map(p -> lines(p).count())
                .collect(toList());

        Map<Path, Long> linesPerFile = new HashMap<>();

        Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> linesPerFile.put(p, lines(p).count()));

        System.out.println(linesPerFile);

        System.out.println("==================================");

        Map<Path, Long> lines =
                Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                .filter(p -> p.toString().endsWith(".java"))
                .collect(Collectors.toMap(
                        p -> p,
                        p -> lines(p).count()));

        System.out.println(lines);

        System.out.println("==================================");

        Map<Path, List<String>> filesContent =
                Files.list(Paths.get("./study/src/br/com/awdnf/streams"))
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(toMap(
                            Function.identity(),
                            p -> lines(p).collect(toList())));

        System.out.println(filesContent);

        System.out.println("==================================");

        Map<Long, User> idToUser =
                users.stream()
                    .collect(toMap(
                            User::getId,
                            Function.identity()));

        System.out.println(idToUser);

        System.out.println("==================================");

        Map<Integer, List<User>> pointsToUsers = new HashMap<>();

        for (User u : users) {
            pointsToUsers
                    .computeIfAbsent(u.getPoints(),
                            user -> new ArrayList<>())
                    .add(u);
        }

        System.out.println(pointsToUsers);

        System.out.println("==================================");

        Map<Integer, List<User>> pointsUserStream = users.stream()
                .collect(groupingBy(User::getPoints));

        System.out.println(pointsUserStream);
        System.out.println("==================================");

        users.stream().filter(u -> u.getPoints() > 300)
                .forEach(User::becomeModerator);

        Map<Boolean, List<User>> moderators = users.stream()
                .collect(partitioningBy(User::isModerator));

        System.out.println(moderators);
        System.out.println("==================================");

        Map<Boolean, List<String>> moderatorsToNames = users.stream()
                .collect(partitioningBy(User::isModerator,
                        Collectors.mapping(User::getName, toList())));

        System.out.println(moderatorsToNames);
        System.out.println("==================================");

        Map<Boolean, Integer> pointsByType = users.stream()
                .collect(partitioningBy(User::isModerator,
                        Collectors.summingInt(User::getPoints)));

        System.out.println(pointsByType);

        System.out.println("==================================");

        String names = users.stream()
                .map(User::getName)
                .collect(Collectors
                        .joining(System.getProperty("line.separator")));

        System.out.println(names);

        System.out.println("==================================");

        //ok, so this example runs on the same thread
        List<User> usersFiltered = users.stream()
                .filter(u -> u.getPoints() > 200)
                .sorted(comparing(User::getName))
                .collect(Collectors.toList());

        System.out.println("==================================");

        List<User> paralellUsers = users.parallelStream()
                .filter(u -> u.getPoints() > 200)
                .sorted(comparing(User::getName))
                .collect(Collectors.toList());

        System.out.println("==================================");

        long start = System.currentTimeMillis();
        long sum = LongStream.range(0, 1_000_000_000)
                .parallel()
                .filter(x -> x % 2 == 0)
                .sum();
        long end = System.currentTimeMillis();
        System.out.println("Paralell: " + (end - start) + " ms");
        System.out.println(sum);

        start = System.currentTimeMillis();
        long sumSingleThread = LongStream.range(0, 1_000_000_000)
                .filter(x -> x % 2 == 0)
                .sum();
        end = System.currentTimeMillis();
        //not exactly double of the paralell because of the overhead
        //but a considerable difference
        System.out.println("Sequential: " + (end - start) + " ms");
        System.out.println(sumSingleThread);

        System.out.println("==================================");

        start = System.currentTimeMillis();
        sum = LongStream.range(0, 1_000_000)
                .parallel()
                .filter(x -> x % 2 == 0)
                .sum();
        end = System.currentTimeMillis();
        System.out.println("Paralell: " + (end - start) + " ms");
        System.out.println(sum);

        start = System.currentTimeMillis();
        sumSingleThread = LongStream.range(0, 1_000_000)
                .filter(x -> x % 2 == 0)
                .sum();
        end = System.currentTimeMillis();
        //if the input is not big, sequential is faster
        //also because of the overhead of parallelizing
        System.out.println("Sequential: " + (end - start) + " ms");
        System.out.println(sumSingleThread);

        System.out.println("==================================");

        //unsafe parallel stream usage example
        //each time you run, different result
        //if you have a multi-core processor
        //concurrent usage of a shared variable
        //enables undesired operations interleaving
        //although you can syncronize inside lambda
        //but at a great performance loss cost
        //so, shared state between threads remains an issue
        //note: all parallel stream work is done with Spliterators
        LongStream.range(0, 1_000_000_000)
                .parallel()
                .filter(x -> x % 2 == 0)
                .forEach(n -> total += n);

        System.out.println(total);

        System.out.println("==================================");
    }

    static Stream<String> lines(Path p) {
        try {
            return Files.lines(p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
