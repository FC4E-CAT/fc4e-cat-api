package org.grnet.cat.entities;

public class Page {

    public final int index;
    public final int size;

    public Page(int size) {
        this(0, size);
    }

    public Page(int index, int size) {
        if (index < 0) {
            throw new IllegalArgumentException("Page index must be >= 0 : " + index);
        } else if (size <= 0) {
            throw new IllegalArgumentException("Page size must be > 0 : " + size);
        } else {
            this.index = index;
            this.size = size;
        }
    }

    public static Page of(int index, int size) {
        return new Page(index, size);
    }

    public static Page ofSize(int size) {
        return new Page(size);
    }

    public Page next() {
        return new Page(this.index + 1, this.size);
    }

    public Page previous() {
        return this.index > 0 ? new Page(this.index - 1, this.size) : this;
    }

    public Page first() {
        return this.index > 0 ? new Page(0, this.size) : this;
    }

    public Page index(int newIndex) {
        return newIndex != this.index ? new Page(newIndex, this.size) : this;
    }
}
