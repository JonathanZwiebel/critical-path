import org.jsfml.graphics.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO: Make mutable
// TODO: Allow for more than just end-to-start relationships
// TODO [MAJOR]: Move the dependencies_ and future_ storage into a client class, avoid repetitions on linkage
/**
 * This class models a single dependency-based task with a constant completion time_ and can communicate
 * with past or future_ tasks to carry out a Forward and Backward pass.
 */
public class Task implements Drawable {
    // TODO: Rename future_
    // TODO: Probably don't use HashMap and ArrayLists together
    private HashMap<Task, Float> dependencies_start_map_, future_end_map_;
    private ArrayList<Task> dependencies_, future_;
    private String name_;
    private float early_start_, late_start_, early_end_, late_end_, time_;
    private float float_time_;
    private TaskDisplay display_;

    /**
     * Sole constructor, to be called by TaskBoundedMap. Creates an task without the reference ArrayLists.
     * @param name unique task identifier
     * @param time time_ to complete this task
     */
    public Task(String name, float time) {

        assert  time >= 0 : "Negative task time_: " + name + " with time_ " + time;
        assert !name.isEmpty() : "Nameless task";
        name_ = name;
        time_ = time;
        display_ = new TaskDisplay(this);
    }

    // TODO: Ensure that there are no collisions between dependencies_ and future_
    /**
     * To be called by TaskBuilder. Links this Task to the dependencies_ and future_ tasks.
     * @param dependencies tasks needed before this task
     * @param future tasks that the completion of this task allows to start
     */
    public void linkTask(ArrayList<Task> dependencies, ArrayList<Task> future) {
        dependencies_ = dependencies;
        future_ = future;
        dependencies_start_map_ = new HashMap<>();
        future_end_map_ = new HashMap<>();
    }

    /**
     * To be called be a task A preceding this task when A has found it's early end time_ on the forward pass
     * Notifier will add to the dependencies_ hash map the notification task and its early end time_
     * @param notifier preceding task
     */
    public void forwardNotify(Task notifier) {
        assert notifier.early_end_ >= 0 : "In " + name_ + ": " + "Negative early end time_ from notifying task " + notifier.name_;
        assert dependencies_.contains(notifier) : "In " + name_ + ": " + "Notifier task not in dependencies_: " + notifier.name_;
        dependencies_start_map_.put(notifier, notifier.early_end_);
        if(dependencies_start_map_.size() == dependencies_.size()) {
            forwardPass();
        }
    }

    /**
     * Carries out the forward pass from this node. Asserts that all of the dependencies_ have been covered in the pass
     * Sets early start to the maximum value in the dependency start map. Sets early end to early start + time_.
     */
    private void forwardPass() {
        // TODO: Assert that the Tasks completely match, not just the size
        assert dependencies_start_map_.size() == dependencies_.size() : "In " + name_ + ": " + "Dependencies HashMap does not match dependencies_ list in size: HashMap Size: " + dependencies_start_map_.size() + "List Size: " + dependencies_.size();
        // TODO: Ensure that this is the edge value
        ArrayList<Float> dependencies_list = new ArrayList();
        for(Object o : dependencies_start_map_.values().toArray()) {
            dependencies_list.add((float) o);
        }
        Collections.sort(dependencies_list);
        early_start_ = dependencies_list.get(dependencies_list.size() -  1);
        assert early_start_ >= 0 : "In " + name_ + ": " + "Negative early start of " + early_start_;
        early_end_ = early_start_ + time_;
        for(Task t : future_) {
            t.forwardNotify(this);
        }
    }

    /**
     * To be called be a task B to which this task is a precedent when B has found it's late start time_ on the
     * backward pass. Notifier will add to the future_ hash map the notification task and its late start time_
     * @param notifier future_ task
     */
    public void backwardNotify(Task notifier) {
        assert future_.contains(notifier) : "In " + name_ + ": " + notifier.name_ + "not found in future_ set";
        assert notifier.late_start_ >= 0 : "In " + name_ + ": " + "Negative late start of " + notifier.late_start_;
        future_end_map_.put(notifier, notifier.late_start_);
        if(future_end_map_.size() == future_.size()) {
            backwardPass();
        }
    }

    /**
     * Carries out the backward pass from this node. Asserts that all of the future_ tasks have been covered in the pass
     * Sets late end to the maximum value in the dependency start map. Sets early end to early start + time_.
     * Sets the float time_ to late start - early start
     */
    private void backwardPass() {
        // TODO: Assert that the Tasks completely match, not just the size
        assert future_end_map_.size() == future_.size()  : "In " + name_ + ": " + "Future HashMap does not match dependencies_ list in size: HashMap Size: " + future_end_map_.size() + "List Size: " + future_.size();;
        // TODO: Ensure that this is the edge value
        ArrayList<Float> future_list = new ArrayList();
        for(Object o : future_end_map_.values().toArray()) {
            future_list.add((float) o);
        }
        Collections.sort(future_list);
        late_end_ = future_list.get(0);
        assert late_end_ >= 0 : "In " + name_ + ": " + "Negative late end of " + late_end_;
        late_start_ = late_end_ - time_;
        float_time_ = late_start_ - early_start_;
        assert late_start_ >= 0 : "In " + name_ + ": " + "Negative late start of " + late_start_;
        assert float_time_ >= 0 : "In " + name_ + ": " + "Negative float time_ of " + float_time_;
        for(Task t : dependencies_) {
            t.backwardNotify(this);
        }
    }

    /**
     * Starts the forward pass of the entire TaskBoundedMap
     * To only be called on the single head element
     */
    public void forwardPassStart() {
        assert dependencies_.size() == 0 : "In " + name_ + ": " + "Attempting to start forward pass on Task with dependencies_";
        early_start_ = 0;
        early_end_ = time_;
        for(Task t : future_) {
            t.forwardNotify(this);
        }
    }

    /**
     * Starts the backward pass of the entire TaskBoundedMap
     * To only be called on the single tail element
     * @param project_time the optimal project completion time_
     */
    public void backwardPassStart(float project_time) {
        assert future_end_map_.size() == 0 : "In " + name_ + ": " + " Attempting to start backward pass on task with future_";
        assert project_time > 0 : "In " + name_ + ": " + "Project time_ is negative: " + project_time;
        late_end_ = project_time;
        late_start_ = late_end_ - time_;
        float_time_ = late_start_ - early_start_;
        assert float_time_ == 0 : "In " + name_ + ": " + "Attempting to start backward pass on non critical element with float time_: " + float_time_;
        assert late_start_ >= 0 : "In " + name_ + ": " + "Negative late start of " + late_start_;
        for(Task t : dependencies_) {
            t.backwardNotify(this);
        }
    }

    /**
     * Prints out all of the computed time_ values
     * @return All of computed time_ values
     */
    public String toString() {
        String ret = "";
        ret += "\nName: " + name_ + " | Time: " + time_;ret += "\nEarly Start: " + early_start_ + " Early End: " + early_end_;
        ret += "\nLate Start: " + late_start_ + " Late End: " + late_end_;
        ret += "\nFloat: " + float_time_;
        return ret;
    }


    /**
     * Changes the time_ on this task
     * //TODO[Medium]: Get this to initiate redrawing
     *
     * @param time new time_
     */
    public void setTime(float time) {
        time_ = time;
    }

    public void draw(RenderTarget target, RenderStates states) {
        display_.draw(target, states);
    }

    public ArrayList<Task> getDependencies() {
        return dependencies_;
    }

    public ArrayList<Task> getFuture() {
        return future_;
    }

    public String getName() {
        return name_;
    }

    public float getEarlyStart() {
        return early_start_;
    }

    public float getLateStart() {
        return late_start_;
    }

    public float getEarlyEnd() {
        return early_end_;
    }

    public float getLateEnd() {
        return late_end_;
    }

    public float getTime() {
        return time_;
    }

    public float getFloatTime() {
        return float_time_;
    }
}
