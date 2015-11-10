/**
 * Tests the TaskMap, TaskLinker and Task classes
 */
public class TaskMapTester {
    public static void main(String[] args) {
        TaskMap project = new TaskMap();
        customTest(project);

        project.link();
        project.forwardPass();
        project.backwarPass();

        for(TaskLinker linker : project.mapping.values()) {
            System.out.print(linker.task);
        }
    }

    public static void customTest(TaskMap project) {
        project.put("A", 4, new String[]{}, new String[]{"B", "C"});
        project.put("B", 7, new String[]{"A"}, new String[]{"E"});
        project.put("C", 2, new String[]{"A"}, new String[]{"D"});
        project.put("D", 16, new String[]{"C"}, new String[]{"E"});
        project.put("E", 1.5f, new String[]{"B", "D"}, new String[]{});
    }

    // http://www.lindsay-sherwin.co.uk/project_framework/images/cpa_spaghetti.jpg
    public static void spaghettiTest(TaskMap project) {
        project.put("Get Ingrediants", 5, new String[]{}, new String[]{"Cook Spaghetti", "Prepare Egg Sauce", "Cook Bacon"});


    }
}
