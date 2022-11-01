package com.terminalvelocitycabbage.engine.scheduler;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scheduler {

    private final List<Task> taskList;

    public Scheduler() {
        this.taskList = new ArrayList<>();
    }

    public void tick() {
        //Some tasks may have been marked for removal since the last tick
        //Remove all of those tasks marked as such
        taskList.removeIf(Task::remove);
        taskList.forEach(task -> {
            //Check that the tasks here are initialized and error if not
            if (!task.initialized()) Log.crash("Task not initialized error", new IllegalStateException("Schedulers can only execute initialized tasks"));
            //Skip this task if it's delayed and not time to execute yet
            if (task.delay() && task.executeTime() > System.currentTimeMillis()) return;
            //Run the consumer
            if (task.repeat()) {
                if (System.currentTimeMillis() - task.lastExecuteTimeMillis() >= task.repeatInterval()) task.execute();
            } else {
                task.execute();
            }
            //If this is not a task marked at an interval we need to not run it next time, so mark for removal
            if (!task.repeat()) task.markRemove();
        });
    }

    /**
     * Schedules a given task for execution upon this scheduler's tick method being called if the conditions
     * for execution are met by the executor
     *
     * @param task the task to be added to the scheduler
     */
    public void scheduleTask(Task task) {
        if (getTask(task.identifier()).isPresent()) {
            Log.error("Tried to schedule task of same identifier: " + task.identifier().toString());
            return;
        }
        taskList.add(task.init());
    }

    /**
     * Gets a task by Identifier if it exists on this scheduler
     *
     * @param identifier The identifier of the task which you hope to get
     * @return a Optional of type Task object scheduled in this scheduler with a matching identifier
     */
    public Optional<Task> getTask(Identifier identifier) {
        for (Task task : taskList) {
            if (task.identifier().equals(identifier)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

}
