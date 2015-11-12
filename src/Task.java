import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO: Make mutable
// TODO: Allow for more than just end-to-start relationships
// TODO [MAJOR]: Move the dependencies and future storage into a client class, avoid repetitions on linkage
/**
 * This class models a single dependency-based task with a constant completion time and can communicate
 * with past or future tasks to carry out a Forward and Backward pass.
 */
public class Task {
    // TODO: Rename future
    // TODO: Probably don't use HashMap and ArrayLists together
    private HashMap<Task, Float> dependencies_start_map_, future_end_map_;
    public ArrayList<Task> dependencies, future;
    public String name;
    public float early_start, late_start, early_end, late_end, time;
    public float float_time;
    private boolean forward_passed_, backward_passed_, linked_;

    /**
     * Sole constructor, to be called by TaskBoundedMap. Creates an task without the reference ArrayLists.
     * @param name unique task identifier
     * @param time time to complete this task
     */
    public Task(String name, float time) {
        assert  time >= 0 : "Negative task time: " + name + " with time " + time;
        assert !name.isEmpty() : "Nameless task";
        forward_passed_ = false;
        backward_passed_ = false;
        linked_ = false;
        this.name = name;
        this.time = time;
    }

    // TODO: Ensure that there are no collisions between dependencies and future
    /**
     * To be called by TaskLinker. Links this Task to the dependencies and future tasks.
     * @param dependencies tasks needed before this task
     * @param future tasks that the completion of this task allows to start
     */
    public void linkTask(ArrayList<Task> dependencies, ArrayList<Task> future) {
        assert linked_ == false : "Duplicate call to linkTask";
        this.dependencies = dependencies;
        this.future = future;
        dependencies_start_map_ = new HashMap<>();
        future_end_map_ = new HashMap<>();
        linked_ = true;
    }

    /**
     * To be called be a task A preceding this task when A has found it's early end time on the forward pass
     * Notifier will add to the dependencies hash map the notification task and its early end time
     * @param notifier preceding task
     */
    public void forwardNotify(Task notifier) {
        assert notifier.early_end >= 0 : "Negative early end time from notifying task " + notifier.name;
        assert dependencies.contains(notifier);
        assert !forward_passed_ && !backward_passed_;
        dependencies_start_map_.put(notifier, notifier.early_end);
        if(dependencies_start_map_.size() == dependencies.size()) {
            forwardPass();
        }
    }

    /**
     * Carries out the forward pass from this node. Asserts that all of the dependencies have been covered in the pass
     * Sets early start to the maximum value in the dependency start map. Sets early end to early start + time.
     */
    private void forwardPass() {
        assert !forward_passed_ && !backward_passed_;
        // TODO: Assert that the Tasks completely match, not just the size
        assert dependencies_start_map_.size() == dependencies.size();
        forward_passed_ = true;
        // TODO: Ensure that this is the edge value
        ArrayList<Float> dependencies_list = new ArrayList();
        for(Object o : dependencies_start_map_.values().toArray()) {
            dependencies_list.add((float) o);
        }
        Collections.sort(dependencies_list);
        early_start = dependencies_list.get(dependencies_list.size() -  1);
        assert early_start >= 0;
        early_end = early_start + time;
        for(Task t : future) {
            t.forwardNotify(this);
        }
    }

    /**
     * To be called be a task B to which this task is a precedent when B has found it's late start time on the
     * backward pass. Notifier will add to the future hash map the notification task and its late start time
     * @param notifier future task
     */
    public void backwardNotify(Task notifier) {
        assert forward_passed_ && !backward_passed_;
        assert future.contains(notifier);
        assert notifier.late_start >= 0;
        future_end_map_.put(notifier, notifier.late_start);
        if(future_end_map_.size() == future.size()) {
            backwardPass();
        }
    }

    /**
     * Carries out the backward pass from this node. Asserts that all of the future tasks have been covered in the pass
     * Sets late end to the maximum value in the dependency start map. Sets early end to early start + time.
     * Sets the float time to late start - early start
     */
    private void backwardPass() {
        assert forward_passed_ && !backward_passed_;
        // TODO: Assert that the Tasks completely match, not just the size
        assert future_end_map_.size() == future.size();
        backward_passed_ = true;
        // TODO: Ensure that this is the edge value
        ArrayList<Float> future_list = new ArrayList();
        for(Object o : future_end_map_.values().toArray()) {
            future_list.add((float) o);
        }
        Collections.sort(future_list);
        late_end = future_list.get(0);
        assert late_end >= 0;
        late_start = late_end - time;
        float_time = late_start - early_start;
        assert late_start >= 0;
        assert float_time >= 0;
        for(Task t : dependencies) {
            t.backwardNotify(this);
        }
    }

    /**
     * Starts the forward pass of the entire TaskBoundedMap
     * To only be called on the single head element
     */
    public void forwardPassStart() {
        assert dependencies.size() == 0;
        assert !forward_passed_ && !backward_passed_;
        forward_passed_ = true;
        early_start = 0;
        early_end = time;
        for(Task t : future) {
            t.forwardNotify(this);
        }
    }

    /**
     * Starts the backward pass of the entire TaskBoundedMap
     * To only be called on the single tail element
     * @param project_time the optimal project completion time
     */
    public void backwardPassStart(float project_time) {
        assert future_end_map_.size() == 0;
        assert forward_passed_ && !backward_passed_;
        assert project_time > 0;
        backward_passed_ = true;
        late_end = project_time;
        late_start = late_end - time;
        float_time = late_start - early_start;
        assert float_time == 0;
        assert late_start >= 0;
        for(Task t : dependencies) {
            t.backwardNotify(this);
        }
    }

    /**
     * Prints out all of the computed time values
     * @return All of computed time values
     */
    public String toString() {
        String ret = "";
        ret += "Name: " + name + " | Time: " + time;
        if(forward_passed_) {
            ret += "\nEarly Start: " + early_start + " Early End: " + early_end;
            if(backward_passed_) {
                ret += "\nLate Start: " + late_start + " Late End: " + late_end;
                ret += "\nFloat: " + float_time;
            }
        }
        ret += "\n";
        return ret;
    }
}
