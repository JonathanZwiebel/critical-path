import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Data structure that manages double-linked Tasks and can perform passes on them
 *
 * // TODO [MAJOR]: Allow for put, remove and modification after linkage
 * // TODO: Allow for multiple heads and tails by adding ghosts to the end
 * // TODO: Add backward pass
 * Operations:
 * put - Adds a task to the map - O(1)
 * link - Links all of the tasks together to create a double linked list - O(n^2) // TODO: Fix this
 * forwardPass - Performs a forward pass on the tasks in the map - O(n * lg(n))? // TODO: Verify complexity
 */
public class TaskBoundedMap {
    HashMap<String, TaskLinker> mapping;
    Task head_ = null, tail_ = null;
    boolean linked_;
    float project_time_;

    public TaskBoundedMap() {
        mapping =  new HashMap();
        linked_ = false;
        project_time_  = -1;
    }

    /**
     * Adds an element to this TaskBoundedMap. Must be done before passes
     * O(1) time
     * @param name unique identifier of the task
     * @param time time of the task
     * @param dependencies names of the tasks the precede this task
     * @param future names of the tasks that the completion of this task allows to begin
     * TODO: Only pass past tasks, calculate future tasks. Will ensure that is full overlap.
     * TODO: Salt the names to allow for multiple entries of the same identifier
     */
    public void put(String name, float time, String[] dependencies, String[] future)  {
        assert !linked_ : "Task Put into Linked TaskBoundedMap";
        assert !mapping.containsKey(name) : "Repeat name task: " + name;
        assert !name.isEmpty() : "Attempting to put nameless task into map";
        assert time >= 0 : "Task with negative time added to map: " + name + " with time " + time;

        Task task = new Task(name, time);

        if(dependencies.length == 0) {
            assert head_ == null : "Multiple heads being attached to map: " + name + " conflicts with " + head_.name;
            head_ = task;
        }

        if(future.length == 0) {
            assert tail_ == null  : "Multiple tails being attached to map: " + name + " conflicts with " + head_.name;
            tail_ = task;
        }

        TaskLinker linker = new TaskLinker(task, new ArrayList<String>(Arrays.asList(dependencies)), new ArrayList<String>(Arrays.asList(future)));
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
        assert linked_ = true : "Forward passing before linked";
        head_.forwardPassStart();
        project_time_ = tail_.early_end;
    }

    public void backwardPass() {
        assert linked_ = true : "Backward passing before linked";
        assert project_time_ >= 0 : "Negative project time: " + project_time_;
        tail_.backwardPassStart(project_time_);
    }
}
