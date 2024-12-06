package whyxzee.terminalpractice.application;

/**
 * Custom-made timer for scenarios and drills.
 */
public class Timer {
    public long startTime;
    public long endTime;
    public long elapsedTime;
    public long remainingTime;
    public boolean finished;
    public boolean shouldUpdate;

    // Seconds, Minutes
    public int seconds;
    public int minutes;

    public Timer() {
        finished = false;
        shouldUpdate = false;
    }

    public void start(long endTime) {
        this.endTime = endTime;
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
        shouldUpdate = true;
    }

    public void update() {
        if (shouldUpdate) {
            if (elapsedTime >= endTime) {
                finished = true;
            } else {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = endTime - elapsedTime;

                seconds = (int) Math.floor(remainingTime / 1000) % 60;
                minutes = (int) Math.floor(remainingTime / 60000);
            }
        }
    }

    public void reset() {
        finished = shouldUpdate = false;
        elapsedTime = 0;
    }

    @Override
    public String toString() {
        String minuteString = Long.toString(minutes);
        String secondString = Long.toString(seconds);

        if (seconds < 10) {
            secondString = "0" + seconds;
        }
        if (minutes < 10) {
            minuteString = "0" + minutes;
        }
        return minuteString + ":" + secondString;
    }
}
