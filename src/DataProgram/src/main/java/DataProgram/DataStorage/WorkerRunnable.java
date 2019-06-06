package DataProgram.DataStorage;

import javafx.concurrent.Task;

import java.util.Stack;

public class WorkerRunnable implements Runnable
{
    private Stack<Task> tasks;
    private boolean run;

    WorkerRunnable()
    {
        run = true;
        tasks = new Stack<>();
    }

    void queueTask(Task task) {
        tasks.add(task);
        synchronized(this) {
            notify();
        }
    }

    void stop()
    {
        run = false;

    }

    @Override
    public void run() {
        while (run) {
            if (!tasks.empty()) {
                Task currentTask = tasks.pop();
                currentTask.run();
            } else {
                try {
                    synchronized(this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
