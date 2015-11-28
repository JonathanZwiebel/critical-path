import org.jsfml.graphics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Data structure that manages double-linked Tasks and can perform passes on them
 *
 * // TODO [MAJOR]: Allow for put, remove and modification after linkage
 * // TODO: Allow for multiple heads and tails by adding ghosts to the end
 * // TODO: Add backward pass
 */
public class TaskBoundedMap implements Drawable {
    private HashMap<String, TaskBuilder> mapping;
    private TaskBoundedMapDisplay display;
    private Task head_ = null, tail_ = null;
    private float project_time_;

    public TaskBoundedMap() {
        mapping =  new HashMap();
        project_time_  = -1;
        display = new TaskBoundedMapDisplay(this);
    }

    /**
     * Adds an element to this TaskBoundedMap. Must be done before passes
     *
     * @param name unique identifier of the task
     * @param time time of the task
     * @param dependencies names of the tasks the precede this task
     * @param future names of the tasks that the completion of this task allows to begin
     * TODO: Only pass past tasks, calculate future tasks. Will ensure that is full overlap.
     * TODO: Salt the names to allow for multiple entries of the same identifier
     */
    public void put(String name, float time, String[] dependencies, String[] future)  {
        assert !mapping.containsKey(name) : "Repeat name task: " + name;
        assert !name.isEmpty() : "Attempting to put nameless task into map";
        assert time >= 0 : "Task with negative time added to map: " + name + " with time " + time;

        Task task = new Task(name, time);

        TaskBuilder builder = new TaskBuilder(task, new ArrayList<String>(Arrays.asList(dependencies)), new ArrayList<String>(Arrays.asList(future)));
        mapping.put(name, builder);
    }

    /**
     * Adds an element to this TaskBoundedMap without specifying dependencies and future
     *
     * @param name unique identifier of the task
     * @param time time of the task
     */
    public void put(String name, float time) {
        put(name, time, new String[]{}, new String[]{});
    }

    /**
     * Links the dependency task and future task together crating a ES relationship
     *
     * @param dependency past task to link
     * @param future future task to link
     */
    public void link(String dependency, String future) {
        mapping.get(dependency).future_.add(future);
        mapping.get(future).dependencies_.add(dependency);
    }


    /**
     * Links all of the tasks into a double linked list
     */
    public void crit() {
        head_ = null;
        tail_ = null;

        for(TaskBuilder builder : mapping.values()) {
            builder.linkTask(mapping);

            if(builder.task.getDependencies().size() == 0) {
                assert head_ == null : "Multiple heads being attached to map: " + builder.task.getName() + " conflicts with " + head_.getName();
                head_ = builder.task;
            }

            if(builder.task.getFuture().size() == 0) {
                assert tail_ == null  : "Multiple tails being attached to map: " + builder.task.getName() + " conflicts with " + head_.getName();
                tail_ = builder.task;
            }
        }
        forwardPass();
        backwardPass();
    }

    /**
     * Performs a forward pass on all of the tasks starting from the head task
     */
    private void forwardPass() {
        head_.forwardPassStart();
        project_time_ = tail_.getEarlyEnd();
    }

    /**
     * Performs a backward pass on all of the tasks starting from the tail task
     */
    private void backwardPass() {
        assert project_time_ >= 0 : "Negative project time: " + project_time_;
        tail_.backwardPassStart(project_time_);
    }

    /**
     * Changes the completion time for a task
     * @param name Name of task to change time
     * @param new_time New completion time
     */
    public void changeTime(String name, float new_time) {
        mapping.get(name).task.setTime(new_time);
    }

    /**
     * Draws this TaskBoundedMap using the TaskBoundedMapDisplay
     *
     * @param target RenderTarget on which to draw
     * @param states RenderStates with how to draw
     */
    public void draw(RenderTarget target, RenderStates states) {
        display.draw(target, states);
    }

    /**
     * Returns the mapping between titles and TaskBuilders
     *
     * @return the mapping
     */
    public HashMap<String, TaskBuilder> getMapping() {
        return mapping;
    }
}
