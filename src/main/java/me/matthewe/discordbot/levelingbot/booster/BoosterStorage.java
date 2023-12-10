package me.matthewe.discordbot.levelingbot.booster;

import me.matthewe.discordbot.utilities.StorageCache;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 6:37 PM for the project LevelingBot
 */
public class BoosterStorage extends StorageCache<UUID, Booster> {
    private Queue<Booster> boosterQueue;
    private Booster currentBooster;
    private long currentBoosterTime;

    public BoosterStorage() {
        super("boosters", "boosters.json");
        this.boosterQueue = new PriorityQueue<>();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 2000L);

    }

    public long getCurrentBoosterTime() {
        return currentBoosterTime;
    }

    public void saveQueue() {
        map.clear();
        boosterQueue.forEach(booster -> {
            booster.setQueuePosition(new ArrayList<>(boosterQueue).indexOf(booster));
            map.put(booster.getUuid(), booster);
        });
        boosterQueue.clear();


    }

    public void loadMap() {
        boosterQueue.addAll(map.values());
    }

    public void addBooster(Booster booster) {
        Booster booster1 = create(booster.getUuid(), booster);
        boosterQueue.add(booster1);
        booster1.setQueuePosition(boosterQueue.size());
        update();
    }


    public Booster getActiveBooster() {
        return currentBooster;
    }

    public void update() {
        if ((System.currentTimeMillis() > currentBoosterTime) && (currentBooster != null)) {
            if (map.containsKey(currentBooster.getUuid())) {
                map.remove(currentBooster.getUuid());
            }
            currentBooster = null;
            currentBoosterTime = 0L;
        }
        if ((currentBooster == null) && !boosterQueue.isEmpty()) {
            Booster booster = boosterQueue.poll();

            currentBooster = booster;
            currentBoosterTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(booster.getTime());
        }
    }

    @Override
    public UUID parseFromString(String s) {
        return UUID.fromString(s);
    }

    @Override
    public Class<Booster> getEntityClass() {
        return Booster.class;
    }
}
