 
package com.thekstudio.countdown;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import java.util.List;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import com.google.appinventor.components.annotations.*;
import android.os.CountDownTimer;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
@DesignerComponent(
        version = 1,
        description = "Simple Countdown",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = ""
)

@SimpleObject(external = true)
@SuppressWarnings("deprecation")
public class Countdown extends AndroidNonvisibleComponent {
    private CountDownTimer timer;
    private long remainingTime;
    private boolean isPaused = false;
    private boolean isRunning = false;

    public Countdown(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleEvent(description = "Triggered when the countdown is ticking. Returns seconds and formatted time.")
    public void Countdown(long secondsRemaining, String formattedTime) {
        EventDispatcher.dispatchEvent(this, "Countdown", secondsRemaining, formattedTime);
    }

    @SimpleEvent(description = "Triggered when the countdown finishes.")
    public void Finished() {
        EventDispatcher.dispatchEvent(this, "Finished");
    }

    @SimpleEvent(description = "Triggered when a countdown is already running.")
    public void AlreadyRunning() {
        EventDispatcher.dispatchEvent(this, "AlreadyRunning");
    }

    @SimpleFunction(description = "Starts the countdown timer with the specified duration in milliseconds.")
    public void Start(long duration) {
        if (isRunning) {
            AlreadyRunning();
            return;
        }

        isRunning = true;
        isPaused = false;
        remainingTime = duration;

        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                long seconds = millisUntilFinished / 1000;
                String formattedTime = formatTime(seconds);
                Countdown(seconds, formattedTime);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                Finished();
            }
        };

        timer.start();
    }

    @SimpleFunction(description = "Pauses the countdown timer.")
    public void Pause() {
        if (isRunning && timer != null) {
            timer.cancel();
            isPaused = true;
            isRunning = false;
        }
    }

    @SimpleFunction(description = "Resumes the countdown timer if it was paused.")
    public void Resume() {
        if (isPaused && remainingTime > 0) {
            Start(remainingTime);
            isPaused = false;
        }
    }

    @SimpleFunction(description = "Stops the countdown timer.")
    public void Stop() {
        if (timer != null) {
            timer.cancel();
        }
        isRunning = false;
        isPaused = false;
        remainingTime = 0;
    }

    @SimpleFunction(description = "Returns whether the countdown timer is running.")
    public boolean IsRunning() {
        return isRunning;
    }

    @SimpleFunction(description = "Returns whether the countdown timer is paused.")
    public boolean IsPaused() {
        return isPaused;
    }

    private String formatTime(long seconds) {
        if (seconds >= 3600) {
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        } else if (seconds >= 60) {
            return String.format("%02d:%02d", seconds / 60, seconds % 60);
        } else {
            return String.format("%02d", seconds);
        }
    }
}
