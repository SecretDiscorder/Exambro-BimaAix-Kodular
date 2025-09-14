 
package com.bima.locktask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Activity;
import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import android.os.*;
@DesignerComponent(
        version = 1,
        description = "",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "")

@SimpleObject(external = true)
//Libraries
@UsesLibraries(libraries = "")
//Permissions
@UsesPermissions(permissionNames = "")

public class SupportedLockTask extends AndroidNonvisibleComponent {

    //Activity and Context
    private Context context;
    private Activity activity;

    public SupportedLockTask(ComponentContainer container){
        super(container.$form());
        this.activity = container.$context();
        this.context = container.$context();
    }
    @SimpleFunction()
    public void Start() {
        activity.startLockTask();
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startLockTask();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                if (isLockTaskPermitted(activityManager)) {
                    try {
                        activity.startLockTask();
                    } catch (SecurityException e) {
                    }
                } else {
                    activity.startLockTask();
                }
            }
        }
    }
    @SimpleFunction()
    public void Stop() {
        activity.stopLockTask();

        }
    private boolean isLockTaskPermitted(ActivityManager activityManager) {
            if (((ActivityManager) context.getSystemService("activity")) == null) {
                return false;
            }
            try {
                activity.startLockTask();
                return true;
            } catch (SecurityException e) {
                return false;
            }
    }
    @SimpleFunction(description = "Returns the current lock task mode. 'Pinned' if the screen is pinned, 'None' if not pinned.")
    public String Mode() {
        try {
            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                return "Unknown"; // ActivityManager is unavailable
            }

            int state = -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23 and above
                state = am.getLockTaskModeState();
            }

            switch (state) {
                case ActivityManager.LOCK_TASK_MODE_NONE:
                    return "None";
                case ActivityManager.LOCK_TASK_MODE_LOCKED:
                    return "Locked";
                case ActivityManager.LOCK_TASK_MODE_PINNED:
                    return "Pinned";
                default:
                    return "Unknown";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage(); // Return error details
        }
    }
}