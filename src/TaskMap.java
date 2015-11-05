import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data structure that manages double-linked Tasks and can perform passes on them
 *
 * // TODO [MAJOR]: Allow for put, remove and modification after linkage
 * // TODO: Allow for multiple heads and tails by adding ghosts to the end
 * Operations:
 * put - Adds a task to the map - O(1)
 * link - Links all of the tasks together to create a double linked list - O(n^2) // TODO: Fix this
 * forwardPass - Performs a forward pass on the tasks in the map - O(n * lg(n))? // TODO: Verify complexity
 */
public class TaskMap {
    HashMap<String, TaskLinker> mapping;
    Task head_ = null, tail_ = null;
    boolean linked_;

    public TaskMap() {
        linked_ = false;
    }

    /**
     * Adds an element to this TaskMap. Must be done before passes
     * O(1) time
     * @param name unique identifier of the task
     * @param time time of the task
     * @param dependencies names of the tasks the precede this task
     * @param future names of the tasks that the completion of this task allows to begin
     * TODO: Only pass past tasks, calculate future tasks. Will ensure that is full overlap.
     */
    public void put(String name, float time, ArrayList<String> dependencies, ArrayList<String> future)  {
        assert !linked_;
        assert !mapping.containsKey(name);
        assert !name.isEmpty();
        assert time >= 0;

        Task task = new Task(name, time);

        if(dependencies.size() == 0) {
            assert head_ == null;
            head_ = task;
        }

        if(future.size() == 0) {
            assert tail_ == null;
            tail_ = task;
        }

        TaskLinker linker = new TaskLinker(task, dependencies, future);
        mapping.put(name, linker);
    }

    /**
     * Links all of the tasks into a double linked list
     * O(n^2) time
     * TODO: Allow for relinking
     */
    public void link() {
        for(TaskLinker linker : mapping.values()) {
            linker.linkTask(mapping);
        }
        linked_ = true;
    }

    /**
     * Performs a forward pass on all of the tasks starting from the head task
     * O(n * lg(n)) time
     */
    public void forwardPass() {
        assert linked_ = true;
        head_.forwardPassStart();
    }
}