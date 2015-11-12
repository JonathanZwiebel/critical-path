import java.util.ArrayList;
import java.util.HashMap;

/**
 * Constructed before the tasks are constructed. Takes in the dependencies and future events by Name. Passes the names
 * to the Task as an ArrayList. Created by the TaskMap. This class is needed because the Tasks cannot be linked to
 * future Tasks before those Tasks are created
 */
public class TaskLinker {
    public Task task; // Reference to the task is stored in TaskMap
    private ArrayList<String> dependencies_, future_;

    /**
     * Constructs a TaskLinker with the dependencies and future tasks by name
     * @param task the task that this shell creates
     * @param dependencies names of previous tasks
     * @param future names of future tasks
     */
    public TaskLinker(Task task, ArrayList<String> dependencies, ArrayList<String> future) {
        this.task = task;
        dependencies_ = dependencies;
        future_ = future;
    }

    /**
     * Links the task to their dependencies and future tasks by name
     * @param mapping HashMap linking names of Tasks to their TaskLinkers
     */
    public void linkTask(HashMap<String, TaskLinker> mapping) {
        ArrayList<Task> dependencies_tasks = new ArrayList();
        ArrayList<Task> future_tasks = new ArrayList();

        for(String dependent_task : dependencies_) {
            assert mapping.containsKey(dependent_task);
            dependencies_tasks.add(mapping.get(dependent_task).task);
        }

        for(String future_task : future_) {
            assert mapping.containsKey(future_task) : "Future Task Not Found: " + future_task;
            future_tasks.add(mapping.get(future_task).task);
        }

        task.linkTask(dependencies_tasks, future_tasks);
    }
}
