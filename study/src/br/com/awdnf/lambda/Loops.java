package br.com.awdnf.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by alexandrewiggert on 18/11/15.
 */
public class Loops {

    class User {

        private String name;
        private int points;
        private boolean moderator;

        public User(String name, int points) {
            this.name = name;
            this.points = points;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPoints() {
            return points;
        }

        public void becomeModerator() {
            this.moderator = true;
        }

        public boolean isModerator() {
            return moderator;
        }

    }

    // New Java 8 interface which has only one method (accept)
    class UserConsumer implements Consumer<User> {

        //method to consume a Type (in this case, an User)
        public void accept(User u) {
            // in this case our consume action is to call the getName() method
            System.out.println(u.getName());
        }
    }

    public static void main(String[] args) {
        User user1 = new Loops().new User("Paul", 150);
        User user2 = new Loops().new User("John", 120);
        User user3 = new Loops().new User("Bob", 190);

        //Simple way to create an immutable List
        List<User> users = Arrays.asList(user1, user2, user3);

        //for each since Java 5 which implements Iterable interface
        System.out.println("For each since Java 5: Class clazz  : iterable");
        for (User u : users) {
            System.out.println(u.getName());
        }

        System.out.println("=================================");

        System.out.println("New Java 8 forEach method passing a Consumer: list.forEach(consumer)");

        UserConsumer consumer = new Loops().new UserConsumer();
        users.forEach(consumer);

        System.out.println("=================================");

        System.out.println("Or we can also use an anonymous class instead: list.forEach(new Consumer<T>() {...})");

        users.forEach(new Consumer<User>() {

            public void accept(User u) {
                System.out.println(u.getName());
            }

        });

        System.out.println("=================================");

        System.out.println("Since the Consumer interface only has 1 method, we can use Lambda:");

        users.forEach(user -> System.out.println(user.getName() + " - " + user.isModerator()));

        users.forEach(user -> user.becomeModerator());

        System.out.println("Setting all users as moderators with Lambda :)");

        users.forEach(user -> System.out.println(user.getName() + " - " + user.isModerator()));

    }

}
