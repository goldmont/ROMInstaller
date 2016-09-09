/*

    Copyright © 2016, Giuseppe Montuoro.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

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

            return (String) CLASS.getMethod("get", String.class, String.class).invoke(null, key, def);

        } catch (Exception e) {

            return def;

        }

    }


    public static int getInt(String key, int def) {

        try {

            return (Integer) CLASS.getMethod("getInt", String.class, int.class).invoke(null, key, def);

        } catch (Exception e) {

            return def;

        }

    }

    public static long getLong(String key, long def) {

        try {

            return (Long) CLASS.getMethod("getLong", String.class, long.class).invoke(null, key, def);

        } catch (Exception e) {

            return def;

        }

    }

    public static boolean getBoolean(String key, boolean def) {

        try {

            return (Boolean) CLASS.getMethod("getBoolean", String.class, boolean.class).invoke(null, key, def);

        } catch (Exception e) {

            return def;

        }

    }

    public static void set(String key, String val) {

        try {

            CLASS.getMethod("set", String.class, String.class).invoke(null, key, val);

        } catch (Exception ignored) {

            ignored.printStackTrace();

        }

    }

    public static void addChangeCallback(Runnable callback) {

        try {

            CLASS.getMethod("addChangeCallback", Runnable.class).invoke(null, callback);

        } catch (Exception ignored) {

            ignored.printStackTrace();

        }

    }

    public static void callChangeCallbacks() {

        try {

            CLASS.getMethod("callChangeCallbacks").invoke(null, (Object[]) null);

        } catch (Exception ignored) {

            ignored.printStackTrace();

        }

    }

    private SystemProperties() {}

}