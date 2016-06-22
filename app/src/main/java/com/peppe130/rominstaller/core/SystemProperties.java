package com.peppe130.rominstaller.core;


@SuppressWarnings("unused")
public class SystemProperties {

    private static Class<?> CLASS;

    static {
        try {
            CLASS = Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        try {
            return (String) CLASS.getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            return null;
        }
    }

    public static String get(String key, String def) {
        try {
            return (String) CLASS.getMethod("get", String.class, String.class).invoke(null, key,
                    def);
        } catch (Exception e) {
            return def;
        }
    }


    public static int getInt(String key, int def) {
        try {
            return (Integer) CLASS.getMethod("getInt", String.class, int.class).invoke(null, key,
                    def);
        } catch (Exception e) {
            return def;
        }
    }

    public static long getLong(String key, long def) {
        try {
            return (Long) CLASS.getMethod("getLong", String.class, long.class).invoke(null, key,
                    def);
        } catch (Exception e) {
            return def;
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean) CLASS.getMethod("getBoolean", String.class, boolean.class).invoke(
                    null, key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public static void set(String key, String val) {
        try {
            CLASS.getMethod("set", String.class, String.class).invoke(null, key, val);
        } catch (Exception ignored) {
        }
    }

    public static void addChangeCallback(Runnable callback) {
        try {
            CLASS.getMethod("addChangeCallback", Runnable.class).invoke(null, callback);
        } catch (Exception ignored) {
        }
    }

    public static void callChangeCallbacks() {
        try {
            CLASS.getMethod("callChangeCallbacks").invoke(null, (Object[]) null);
        } catch (Exception ignored) {
        }
    }

    private SystemProperties() {}

}