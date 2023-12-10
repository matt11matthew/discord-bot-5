package me.matthewe.discordbot.levelingbot.user;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Matthew Eisenberg on 7/27/2018 at 7:25 PM for the project GroupifyPayment
 */
public class LevelUsers implements Iterable<LevelUser>  {
    private Map<Long, LevelUser> users;




    public LevelUsers(Map<Long, LevelUser> users) {
        this.users = users;
    }

    public Map<Long, LevelUser> getUsers() {
        return users;
    }

    @Override
    public Iterator<LevelUser> iterator() {
        return users.values().iterator();
    }

}
