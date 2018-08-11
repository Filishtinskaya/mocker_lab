package de.oth.mocker;

/**
 * Contains information about what has to be verified (calling method exactly x times, at least/most x times, never).
 */
public class Verifier {
    enum Type {
        EXACTLY, AT_LEAST, AT_MOST;
    };

    Type type;
    int times;

    public Verifier(Type type, int times){
        this.type = type;
        this.times = times;
    };
}
