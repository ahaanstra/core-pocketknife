package com.semantica.pocketknife.calls;

/**
 * Class supporting the fluent expression of the amount of times a mock should
 * throw a Throwable.
 *
 * @author A. Haanstra
 *
 */
public class Throw {

    public static final Throw NEVER = Throw.times(0);
    public static final Throw ONCE = Throw.times(1);
    public static final Throw TWICE = Throw.times(2);
    public static final Throw THRICE = Throw.times(3);

    public final int times;

    protected Throw(int times) {
        super();
        this.times = times;
    }

    public static Throw times(int times) {
        return new Throw(times);
    }

    public int getTimes() {
        return times;
    }
}
