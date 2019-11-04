package pl.carrifyandroid.Utils;

import android.content.Context;

import de.adorsys.android.securestoragelibrary.SecurePreferences;

public class StorageHelper {
    public StorageHelper(Context context) {
        Context context1 = context;
    }

    public String getString(String name) {
        return SecurePreferences.getStringValue(name, "");
    }

    public int getInt(String name) {
        return SecurePreferences.getIntValue(name, 0);
    }

    public boolean getBool(String name) {
        return SecurePreferences.getBooleanValue(name, false);
    }

    public void setString(String key, String name) {
        SecurePreferences.setValue(key, name);
    }

    public void setInteger(String key, int name) {
        SecurePreferences.setValue(key, name);
    }

    public void setBool(String key, Boolean name) {
        SecurePreferences.setValue(key, name);
    }

    public void logout() {
        SecurePreferences.clearAllValues();
    }

    public void removeVal(String key) {
        SecurePreferences.removeValue(key);
    }
}
