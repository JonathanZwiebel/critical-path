import java.util.ArrayList;
import java.util.HashMap;

// TODO: Make mutable
// TODO: Allow for more than just end-to-start relationships
/**
 * This class models a single dependency-based task with a constant completion time and can communicate
 * with past or future tasks to carry out a Forward and Backward pass.
 */
public class Task {
    // TODO: Rename future
    // TODO: Probably don't use HashMap and ArrayLists together
    private HashMap<Task, Float> dependencies_start_map_, future_end_map_;
    public ArrayList<Task> dependencies, future;
    public float early_start, late_start, early_end, late_end, time;
    public float float_time;
    private boolean forward_passed_, backward_passed_;

    /**
     * Sole constructor to construct the task
     * @param time time to complete this task
     * @param dependencies tasks needed before this task
     * @param future tasks that the completion of this task allows to start
     */
    public Task(float time, ArrayList dependencies, ArrayList future) {
        assert time >= 0;
        forward_passed_ = false;
        backward_passed_ = false;
        this.dependencies = dependencies;
        this.future = future;
        this.time = time;
        dependencies_start_map_ = new HashMap<>();
        future_end_map_ = new HashMap<>();
    }

    /**
     * To be called be a task A preceding this task when A has found it's early end time on the forward pass
     * Notifier will add to the dependencies hash map the notification task and its early end time
     * @param notifier preceding task
     * @param early_start early end time of preceding task
     */
    public void forward_notify(Task notifier, float early_start) {
        assert early_start >= 0;
        assert dependencies.contains(notifier);
        assert !forward_passed_ && !backward_passed_;
        dependencies_start_map_.put(notifier, early_start);
    }

    private void forward_pass() {
        assert !forward_passed_ && !backward_passed_;
        // TODO: Assert that the Tasks completely match, not just the size
        assert dependencies_start_map_.size() == dependencies.size();
        forward_passed_ = true;
        // TODO: Ensure that this is the edge value
        early_start = (float) dependencies_start_map_.values().toArray()[0];
        assert early_start >= 0;
        early_end = early_start + time;
        for(Task t : future) {
            t.forward_notify(this, early_end);
        }
    }

    /**
     * To be called be a task B to which this task is a precedent when B has found it's late start time on the
     * backward pass. Notifier will add to the future hash map the notification task and its late start time
     * @param notifier future task
     * @param late_end late start time of preceding task
     */
    public void backward_notify(Task notifier, float late_end) {
        assert forward_passed_ && !backward_passed_;
        assert future.contains(notifier);
        assert late_end >= 0;
        future_end_map_.put(notifier, late_end);
    }

    private void backward_pass() {
        assert forward_passed_ && !backward_passed_;
        assert future_end_map_.size() == future.size();
        backward_passed_ = true;
        late_end = (float) future_end_map_.values().toArray()[0];
        assert late_end >= 0;
        late_start = late_end - time;
        float_time = late_start - early_start;
        assert late_start >= 0;
        assert float_time >= 0;
        for(Task t : dependencies) {
            t.backward_notify(this, late_start);
        }
    }
}
