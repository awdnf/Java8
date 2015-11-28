package br.com.awdnf.defaultmethods;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface UserConsumer<T> {

    void accept(T type);

    //Intefaces can have more methods, only if declared default (sweet!)

    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T type) -> {
            accept(type); after.accept(type);
        };
    }

}
