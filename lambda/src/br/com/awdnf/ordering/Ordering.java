package br.com.awdnf.ordering;

import br.com.awdnf.defaultmethods.User;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Ordering {

    public static void main(String[] args) {

        User user1 = new User("User C", 100);
        User user2 = new User("User A", 200);
        User user3 = new User("User B", 300);

        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        users.forEach(u -> System.out.println(u.getName()));

        Comparator<User> comparator = new Comparator<User>() {

            public int compare(User u1, User u2) {
                return u1.getName().compareTo(u2.getName());
            }

        };

        //sorting using name comparator :)
        Collections.sort(users, comparator);
        users.forEach(u -> System.out.println(u.getDisplayName()));

        //sorting using points comparator built with lambda and List sort default method :))
        //Comparator<User> pointsComparator = (u1, u2) -> u1.getPoints() - u2.getPoints();
        users.sort((u1, u2) -> u1.getPoints() - u2.getPoints());
        users.forEach(u -> System.out.println(u.getDisplayName()));

        //or even the Comparator static methods
        users.sort(Comparator.comparing(u -> u.getDisplayName()));
        users.forEach(u -> System.out.println(u.getDisplayName()));

        List<String> fruits = Arrays.asList("Banana", "Apple", "Avocado", "Kiwi");

        fruits.sort(Comparator.naturalOrder());
        fruits.forEach(f -> System.out.println(f));

        fruits.sort(Comparator.reverseOrder());
        fruits.forEach(f -> System.out.println(f));

        Function<User, String> extractName = u -> u.getName();
        Comparator<User> comparatorFunction = Comparator.comparing(extractName);
        users.sort(comparatorFunction);

        users.forEach(u -> System.out.println(u.getName()));

        //preventing unnecessary autoboxing
        ToIntFunction<User> extraiPontos = u -> u.getPoints();
        Comparator<User> pointsComparatorFunction = Comparator.comparingInt(extraiPontos);
        users.sort(pointsComparatorFunction);

        users.forEach(user -> System.out.println(user.getDisplayName()));

        //or the short version :)
        users.sort(Comparator.comparingInt(user -> user.getPoints()));
        users.forEach(user -> System.out.println(user.getDisplayName()));

    }

}
