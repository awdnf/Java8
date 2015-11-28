package br.com.awdnf.methodreferences;

import br.com.awdnf.defaultmethods.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

public class MethodReferences {

    public static void main(String[] varargs) {

        User user1 = new User("User C", 100);
        User user2 = new User("User A", 200);
        User user3 = new User("User B", 300);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        users.forEach(u -> System.out.println(u.isModerator()));

        //instead of using forEach and lambda to turn all users to moderator
        //users.forEach(u -> u.becomeModerator());

        //hmmm seems like Ruby :p
        users.forEach(User::becomeModerator);

        //this is the resulting compiled code snippet
        /*Consumer<User> becomeModerator = User::becomeModerator;
        users.forEach(becomeModerator);*/

        //no reflection is being used, all solved at compile time, so, no performance overhead costs :)

        users.forEach(u -> System.out.println(u.isModerator()));

        //syntax becomes very clear here
        Function<User, String> byName = User::getName;
        Function<User, Integer> byPoints = User::getPoints;
        users.sort(comparing(byName));

        users.add(null);
        users.sort(nullsFirst(comparing(byName).thenComparingInt(User::getPoints)));
        users.forEach(u -> System.out.println(u));

        users.sort(nullsLast(comparing(byName).thenComparing(byPoints)));
        users.forEach(u -> System.out.println(u));

        //im sure there is only 1 null object here so its ok to use remove :)
        users.remove(null);
        users.sort(comparing(byPoints).reversed());
        users.forEach(u -> System.out.println(u.getDisplayName()));

        User alex = new User("Alexandre Wiggert", 42);
        System.out.println(alex.isModerator());

        Runnable block = alex::becomeModerator;
        block.run();

        System.out.println(alex.isModerator());

        Consumer<User> toggleConsumer = User::toggleModerator;

        System.out.println("Beginning toggle.....");

        toggleConsumer.accept(alex);
        System.out.println(alex.isModerator());

        toggleConsumer.accept(alex);
        System.out.println(alex.isModerator());

        toggleConsumer.accept(alex);
        System.out.println(alex.isModerator());

        //wow such code much possibilities!
        users.forEach(System.out::println);

        //playing with silly constructor references
        Supplier<User> userFactory = User::new;
        User newUser = userFactory.get();

        System.out.println(newUser);

        Function<String, User> namedUserFactory = User::new;
        User userA = namedUserFactory.apply("User A");
        User userB = namedUserFactory.apply("User B");

        System.out.println(userA.getName());
        System.out.println(userB.getName());


        BiFunction<String, Integer, User> fullUserFactory = User::new;
        User userC = fullUserFactory.apply("Full User C", 500);
        User userD = fullUserFactory.apply("Full User D", 300);

        System.out.println(userC);
        System.out.println(userD);

        // use which one? unnecessary autoboxing
        BiFunction<Integer, Integer, Integer> max = Math::max;
        ToIntBiFunction<Integer, Integer> max2 = Math::max;
        IntBinaryOperator max3 = Math::max;

    }

}
