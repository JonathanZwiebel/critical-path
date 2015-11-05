/**
 * Tests the TaskMap, TaskLinker and Task classes
 */
public class TaskMapTester {
    public static void main(String[] args) {
        TaskMap project = new TaskMap();
        project.put("A", 4, new String[]{}, new String[]{"B", "C"});
        project.put("B", 7, new String[]{"A"}, new String[]{"E"});
        project.put("C", 2, new String[]{"A"}, new String[]{"D"});
        project.put("D", 16, new String[]{"C"}, new String[]{"E"});
        project.put("E", 1.5f, new String[]{"B", "D"}, new String[]{});

        project.link();
        project.forwardPass();
        project.backwarPass();

        for(TaskLinker linker : project.mapping.values()) {
            System.out.print(linker.task);
        }
    }
}
