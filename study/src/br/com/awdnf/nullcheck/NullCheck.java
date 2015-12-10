package br.com.awdnf.nullcheck;

import java.util.Optional;
import java.util.function.Supplier;

public class NullCheck {

    public static void main(String[] args) {

        //study following the post at
        //http://winterbe.com/posts/2015/03/15/avoid-null-checks-in-java/

        //old fashioned NPE safe check
        Outer outer = new Outer();

        if (outer != null && outer.nested != null
                && outer.nested.inner != null) {

            System.out.println(outer.nested.inner.foo);

        }

        //we can use the Optional
        //map() accepts lambda Function type
        //and wraps each one in an Optional
        //all Null checks are handled under the hood
        Optional.of(new Outer())
                .map(Outer::getNested)
                .map(Nested::getInner)
                .map(Inner::getFoo)
                .ifPresent(System.out::println);

        //or we can also use a Supplier Function to resolve
        //nested path
        Outer obj = new Outer();

        resolve(() -> obj.getNested().getInner().getFoo())
            .ifPresent(System.out::println);

    }

    public static <T> Optional<T> resolve(Supplier<T> resolver) {

        try {

            T result = resolver.get();
            return Optional.ofNullable(result);

        }  catch (NullPointerException e) {

            return Optional.empty();

        }
    }

}

class Outer {

    Nested nested;

    Nested getNested() {
        return this.nested;
    }

}

class Nested {

    Inner inner;

    Inner getInner() {
        return this.inner;
    }

}

class Inner {

    String foo;

    String getFoo() {
        return this.foo;
    }

}
