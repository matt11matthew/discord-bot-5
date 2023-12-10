package me.matthewe.discordbot.utilities;


@FunctionalInterface
public interface Callback<A> {
    void call(A a);
}
