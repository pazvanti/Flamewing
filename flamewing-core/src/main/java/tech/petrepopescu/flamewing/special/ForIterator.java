package tech.petrepopescu.flamewing.special;

import java.util.Iterator;

public class ForIterator<T> {
    private int index = 0;

    private final Iterator<T> underlyingIterator;

    public ForIterator(Iterator<T> underlyingIterator) {
        this.underlyingIterator = underlyingIterator;
    }

    public int index() {
        return this.index;
    }

    public boolean hasNext() {
        return this.underlyingIterator.hasNext();
    }

    public T next() {
        T next = this.underlyingIterator.next();
        this.index++;
        return next;
    }

    public T nextNoIncrease() {
        return this.underlyingIterator.next();
    }

    public void increase() {
        this.index++;
    }

    public boolean first() {
        return this.index == 0;
    }

    public boolean last() {
        return !underlyingIterator.hasNext();
    }
}
