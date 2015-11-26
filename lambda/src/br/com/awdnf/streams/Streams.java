package br.com.awdnf.streams;

import br.com.awdnf.defaultmethods.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Streams {

    public static void main(String args[]) {

        Random random = new Random();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100 ; i++) {
            users.add(new User("User " + i, random.nextInt(500)));
        }

        users.forEach(u -> System.out.println(u.getPoints()));

    }

}
