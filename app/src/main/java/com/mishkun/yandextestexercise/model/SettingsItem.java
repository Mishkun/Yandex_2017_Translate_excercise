package com.mishkun.yandextestexercise.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishkun on 21.03.2017.
 */

public class SettingsItem {
    public final String name;
    public final int id;
    public boolean enabled;

    public SettingsItem(String name, int id, boolean enabled) {
        this.name = name;
        this.id = id;
        this.enabled = enabled;
    }

    public static class DummySettingsContent {

        public static final List<SettingsItem> ITEMS = new ArrayList<>();

        static {
            addItem("Cool Setting", true);
            addItem("Cooler Setting", true);
            addItem("Awesome Setting", false);
        }

        private static void addItem(String name, boolean enabled) {
            ITEMS.add(new SettingsItem(name, ITEMS.size(), enabled));
        }
    }
}
