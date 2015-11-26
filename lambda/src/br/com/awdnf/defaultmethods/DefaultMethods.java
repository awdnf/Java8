package br.com.awdnf.defaultmethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultMethods {

    public static void main(String... args) {

        User user1 = new User("User 1", 100);
        User user2 = new User("User 2", 200);
        User user3 = new User("User 3", 300);

        List<User> users = Arrays.asList(user1, user2, user3);

        Consumer<User> displayMessage = u ->
                System.out.println("Preparing to print name...");

        Consumer<User> printName = u ->
                System.out.println(u.getName());

        //nice syntax sugar here
        users.forEach(displayMessage.andThen(printName));

        Predicate<User> predicate = new Predicate<User>() {

            @Override
            public boolean test(User user) {
                return user.getPoints() > 100;
            }

        };

        //new list because remove method (called by removeIf) wont accept an immutable list returned by Arrays.asList
        List<User> users2 = new ArrayList<>();
        users2.addAll(users);

        users2.removeIf(predicate);
        users2.forEach(u -> System.out.println(u.getName()));

        //we can also use lambda for the predicate instead of anonymous class!
        users2.removeIf(user -> user.getPoints() < 400);
        System.out.println(users2.size());

    }

}
