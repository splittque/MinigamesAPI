package me.splitque.minigames.timer;

public class Timer {
    private boolean timerStarted = false;

    public void startTimer(Task task, int second) {
        if (timerStarted) return;
        Runnable timer = () -> {
            timerStarted = true;
            while (timerStarted) {
                try {
                    Thread.sleep(second * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        };
        Thread thread = new Thread(timer);
        thread.start();
    }
    public void stopTimer() {
        timerStarted = false;
    }
}
