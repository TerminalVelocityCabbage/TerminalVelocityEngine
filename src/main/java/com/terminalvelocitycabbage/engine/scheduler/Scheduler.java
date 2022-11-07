package com.terminalvelocitycabbage.engine.scheduler;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Scheduler {

    private final List<Task> taskList;

    public Scheduler() {
        this.taskList = new ArrayList<>();
    }

    public void tick() {
        //Some tasks may have been marked for removal since the last tick
        //Those marked for removal with subsequent tasks need those to be scheduled for this run
        int size = taskList.size();
        List<Task> toRemove = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Task task = taskList.get(i);

            long l = task.getLock().readLock();
            boolean remove = task.remove();

            if (remove) {
                toRemove.add(task);
                if (task.hasSubsequentTasks()) {
                    task.subsequentTasks().forEach((task1) -> {
                        scheduleTask(task1, task.context().value());
                    });
                }
            }

            task.getLock().unlockRead(l);
        }

        taskList.removeAll(toRemove);

        taskList.forEach(task -> {
            //Some tasks like async tasks might get called more than once if we don't track their status
            long l = task.getLock().readLock();
            if (task.running() || task.remove()) {
                task.getLock().unlockRead(l);
                return;
            }

            task.getLock().unlockRead(l);
            //Check that the tasks here are initialized and error if not
            if (!task.initialized()) Log.crash("Task not initialized error", new IllegalStateException("Schedulers can only execute initialized tasks"));
            //Skip this task if it's delayed and not time to execute yet
            if (task.delay() && task.executeTime() > System.currentTimeMillis()) return;
            //Run the consumer
            if (task.repeat()) {
                if (System.currentTimeMillis() - task.lastExecuteTimeMillis() >= task.repeatInterval()) task.execute();
            } else {
                if (task.async()) {
                    CompletableFuture.supplyAsync(task::context)
                            .thenAcceptAsync(task.getAndMarkConsumerRunning())
                            .thenRunAsync(task::markRemove);
                } else {
                    task.execute();
                }
            }
            //If this is not a task marked at an interval we need to not run it next time, so mark for removal
            if (!task.repeat() && !task.running()) task.markRemove();
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
     * Schedules a given task for execution upon this scheduler's tick method being called if the conditions
     * for execution are met by the executor
     *
     * @param task the task to be added to the scheduler
     * @param previousReturn the return value of the previous run task
     */
    public void scheduleTask(Task task, Object previousReturn) {
        if (getTask(task.identifier()).isPresent()) {
            Log.error("Tried to schedule task of same identifier: " + task.identifier().toString());
            return;
        }
        taskList.add(task.init(previousReturn));
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
