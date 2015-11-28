package br.com.awdnf.streams;

import br.com.awdnf.defaultmethods.User;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Streams {

    public static void main(String args[]) {

        Random random = new Random();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100 ; i++) {
            users.add(new User("User " + i, random.nextInt(500)));
        }

        //one way to turn the top 10 users into moderators with Java 8
        //much cleaner than older Java versions
        users.sort(comparing(User::getPoints).reversed());
        users.subList(0, 10).forEach(User::becomeModerator);

        //what if we want only the users with more than 100 points?
        //easy and clean with Stream
        Stream<User> stream = users.stream()
                .filter(u -> u.getPoints() > 100);

        //or even cleaner
        users.stream().filter(u -> u.getPoints() > 100);

        //Still prints all users
        //Stream doesnt affect the original source list
        users.forEach(System.out::println);
        System.out.println(users.size());

        System.out.println("=====================");

        //therefore the stream returns a new stream with the filter result
        //now only users with more than 100 points gets printed
        //its not a Collection thou (no .size() method for ie)
        stream.forEach(System.out::println);

        System.out.println("=====================");

        users.stream().filter(u -> u.getPoints() > 400)
                .forEach(User::becomeModerator);

        //printing only moderators using method reference
        users.stream().filter(User::isModerator)
                .forEach(System.out::println);

        System.out.println("=====================");

        //so, how do we get a List from a Stream?
        //we could create a new collection and add the filter results to it
        //ps: finally we can use empty diamond operator, it gets inferred :)
        List<User> moderators = new ArrayList<>();

        //this aproach still looks like Java 7 thou
        //there must be a simpler way right?
        //still, THIS is a good example of how instance
        // method reference is useful!
        users.stream().filter(User::isModerator)
                .forEach(moderators::add);

        moderators.forEach(System.out::println);

        System.out.println("=====================");

        //So, introducing Collectors :)

        //still too much boilerplate code :/
        //but another good example of method reference
        Supplier<ArrayList<User>> supplier =
                ArrayList<User>::new;

        BiConsumer<ArrayList<User>, User> accumulator =
                ArrayList::add;

        BiConsumer<ArrayList<User>, ArrayList<User>> combiner =
                ArrayList::addAll;

        List<User> over100 = users.stream().filter(u -> u.getPoints() > 100)
                .collect(supplier, accumulator, combiner);

        over100.forEach(System.out::println);

        System.out.println("=====================");

        //its possible to simplify but its not very readable
        List<User> users2 = users.stream()
                .filter(user -> user.getPoints() > 100)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        users2.forEach(System.out::println);

        System.out.println("=====================");

        //Lucky us, there are some built-in ready Collectors
        //Thing is...do we know this is Thread safe?
        //Is it Mutable?
        //What is the implementation its using?
        users.stream().filter(User::isModerator)
                .collect(toList())
                .forEach(System.out::println);

        System.out.println("=====================");

        //Collectors toSet() example
        users.stream().filter(User::isModerator)
                .collect(toSet())
                .forEach(System.out::println);

        System.out.println("=====================");

        //toCollection() lets you choose which
        //implementation you want to use
        users.stream().filter(u -> u.getPoints() > 450)
                .collect(toCollection(HashSet::new));
                //lambda version
                //.collect(toCollection(() -> new HashSet<User>()))

        //we can even call the toArray(IntSupplier) method
        users.stream().filter(u -> u.getPoints() > 450)
                .toArray(User[]::new);

        //what if we want to extract the points from the users?
        //we can user the map() method
        users.stream().map(u -> u.getPoints()).collect(toList());

        //Since its a functional interface we dont even have to worry
        //about arguments, return or method name (lambda ftw)
        //and ofc we can combine it with filter() :)
        users.stream()
                .filter(User::isModerator)
                .map(User::getPoints)
                .collect(toList())
                .forEach(System.out::println);

        System.out.println("=====================");

        //we can use the mapToInt impl to prevent
        //undesired autoboxing overhead
        users.stream()
                .mapToInt(User::getPoints)
                .forEach(System.out::println);

        System.out.println("=====================");

        //Since mapToInt is returns a ToIntFunction
        //we get some extra benefits like getting the average
        double avgPoints = users.stream()
                .mapToInt(User::getPoints)
                .average()
                .getAsDouble();
        System.out.println(avgPoints);

        //or max
        int maxPoint = users.stream()
                .mapToInt(User::getPoints)
                .max()
                .getAsInt();
        System.out.println(maxPoint);

        System.out.println("=====================");

        //why we had to call getAsDouble() on average() method?
        //introducing java.util.Optional and its primitive types

        OptionalDouble average = users.stream()
                .mapToInt(User::getPoints)
                .average();

        //this way if the users list is empty
        //we wont get a infinte positive number
        double averageDouble = average.orElse(0.0);

        //or even throw an Exception
        double averagePoints = users.stream()
                .mapToInt(User::getPoints)
                .average()
                .orElseThrow(IllegalStateException::new);

        //we can also check if the value is present
        //and pass a consumer as argument
        users.stream()
                .mapToInt(User::getPoints)
                .average()
                .ifPresent(value -> System.out.println(value));

        System.out.println("=====================");

    }

}
