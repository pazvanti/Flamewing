package tech.petrepopescu.flamewing.utils;

public class Pair<T, U> {
    private final T first;
    private final U second;

    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getLeft() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public U getRight() {
        return second;
    }
}
