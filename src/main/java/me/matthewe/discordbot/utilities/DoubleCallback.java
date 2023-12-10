package me.matthewe.discordbot.utilities;

/**
 * Created by Matthew Eisenberg on 6/17/2018 at 12:48 PM for the project GroupifySMS
 */
@FunctionalInterface
public interface DoubleCallback<A, B> {
    void call(A a, B b);
}
