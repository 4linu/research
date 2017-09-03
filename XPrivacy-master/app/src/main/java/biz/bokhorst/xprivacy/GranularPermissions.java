package biz.bokhorst.xprivacy;

import android.provider.ContactsContract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 8/30/2017.
 */
public class GranularPermissions {

    public Map<String, String> granularPermissionsMap;

    private static GranularPermissions __instance = null;

    private Map<String, String> createMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PrivacyManager.cContacts, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.toString());
        result.put(PrivacyManager.cStorage, "path");
        return result;
    }

    private GranularPermissions()
    {
        granularPermissionsMap = createMap();
    }

    public boolean contains(String category)
    {
        return granularPermissionsMap.containsKey(category);
    }

    public static GranularPermissions getInstance()
    {
        if (__instance == null)
        {
            __instance = new GranularPermissions();
        }

        return __instance;
    }

    public String get(String category) {
        return granularPermissionsMap.get(category);
    }
}
