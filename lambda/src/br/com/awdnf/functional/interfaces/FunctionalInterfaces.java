package br.com.awdnf.functional.interfaces;

public class FunctionalInterfaces {


    public static void main(String args[]) {

        //Testing the Validator functional interface!
        Validator<String> zipCodeValidator =
                zipCode -> zipCode.matches("[0-9]{5}-[0-9]{3}");

        System.out.println(zipCodeValidator.validate("99999-999"));
        System.out.println(zipCodeValidator.validate("XXXXX-XXX"));
        System.out.println(zipCodeValidator.validate("99999999"));


        //incompatible types error, java.lang.Object is not a functional interface
        /*Object obj = () -> {
            System.out.println("What am I? Which Lambda?");
        };*/

        Runnable o = () -> {
            System.out.println("What am I?");
        };

        //Dynamically generates the classes depending on how many lambdas are in the class!
        System.out.println(o);
        System.out.println(o.getClass());

        //Lambda enables access to non final variables! woot!
        int variable = 10;
        new Thread(() -> {
            System.out.println(variable);
        }).start();

        //but the class wont compile if you try to reassign it so it has to be effectively
        // final even if not declared so :( meh
        // (Same behavior for Java 8 anonymous classes!)
        //variable = 20;

    }

}
