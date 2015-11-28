package br.com.awdnf.functional.interfaces;

//Explicitly telling this is a Functional Interface (not only relying on the fact it has only one method)
@FunctionalInterface
public interface Validator<T> {

    boolean validate(T type);

    /*If we try to add another method to this interface, the code won't compile because it has
      the @FunctionalInterface method. Nice way to prevent someone accidently changing this interface
      to a non-functional*/

    //void anotherMethod();

}
