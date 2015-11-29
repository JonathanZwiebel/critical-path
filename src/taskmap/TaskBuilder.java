package taskmap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Constructed before the tasks are constructed. Takes in the dependencies and future events by Name. Passes the names
 * to the taskmap.Task as an ArrayList. Created by the taskmap.TaskMap. This class is needed because the Tasks cannot be linked to
 * future Tasks before those Tasks are created
 */
public class TaskBuilder {
    public final Task task; // Reference to the task is stored in taskmap.TaskMap
    public final ArrayList<String> dependencies_, future_;

    /**
     * Constructs a taskmap.TaskBuilder with the dependencies and future tasks by name
     * @param task the task that this shell creates
     */
    public TaskBuilder(Task task) {
        this.task = task;
        dependencies_ = new ArrayList<>();
        future_ = new ArrayList<>();
    }

    /**
     * Links the task to their dependencies and future tasks by name
     * @param mapping HashMap linking names of Tasks to their TaskLinkers
     */
    public void linkTask(HashMap<String, TaskBuilder> mapping) {
        ArrayList<Task> dependencies_tasks = new ArrayList<>();
        ArrayList<Task> future_tasks = new ArrayList<>();

        for(String dependent_task : dependencies_) {
            assert mapping.containsKey(dependent_task) : "Dependant taskmap.Task Not Found: " + dependent_task + " for " + task.getName();
            dependencies_tasks.add(mapping.get(dependent_task).task);
        }

        for(String future_task : future_) {
            assert mapping.containsKey(future_task) : "Future taskmap.Task Not Found: " + future_task + " for " + task.getName();
            future_tasks.add(mapping.get(future_task).task);
        }

        task.linkTask(dependencies_tasks, future_tasks);
    }
}
