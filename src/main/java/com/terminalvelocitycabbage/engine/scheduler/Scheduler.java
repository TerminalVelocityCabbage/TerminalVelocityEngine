package com.terminalvelocitycabbage.engine.scheduler;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.resources.Identifier;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler {

    //Stores all tasks that need to be added to the taskList.
    //Allow tasks to be scheduled by other tasks without concurrent modification exceptions.
    private final Queue<Task> taskQueue;
    //The current task list that is being executed
    private final Queue<Task> taskList;

    public Scheduler() {
        taskList = new ConcurrentLinkedQueue<>();
        taskQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Drains all tasks in this queue until completed
     */
    public void drain() {

        //Execute all immediate tasks
        tick();

        //Add tasks scheduled for execution last tick and reset the queue for this tick
        taskList.addAll(taskQueue);
        taskQueue.clear();

        //Execute remaining tasks in order of the least delay
        taskList.stream().sorted().forEach(Task::execute);
    }

    /**
     * To be called every game tick, processes all the task queues and processes all tasks.
     */
    public void tick() {

        //Add tasks scheduled for execution last tick and reset the queue for this tick
        taskList.addAll(taskQueue);
        taskQueue.clear();

        //Those tasks marked for removal with subsequent tasks need those to be scheduled for this run
        Iterator<Task> it = taskList.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            long l = task.getLock().readLock();
            if (task.remove()) {
                if (task.hasSubsequentTasks()) {
                    task.subsequentTasks().forEach((task1) -> {
                        scheduleTask(task1, task.context().value());
                    });
                }
                it.remove();
            }
            task.getLock().unlockRead(l);
        }

        //Process all the tasks
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
            //Skip this task if it has a tick delay and decrement that delay
            if (task.delay() && task.tickDelay() > 0) {
                task.decrimentTickDelay();
                return;
            }
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
        taskQueue.add(task.init());
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
        taskQueue.add(task.init(previousReturn));
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
        for (Task task : taskQueue) {
            if (task.identifier().equals(identifier)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    public int getTaskQueueSize() {
        return taskQueue.size();
    }

    public int getTaskListSize() {
        return taskList.size();
    }
}
