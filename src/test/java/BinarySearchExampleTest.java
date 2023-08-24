import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BinarySearchExampleTest {
    private static final int MEASURE = 1024;
    private long start;
    private long finish;
    private long timeElapsed;
    private JSONObject json = new JSONObject();

    @Test
    public void helloTest() {
        int[] intArray = {1, 2, 3, 4, 5, 6, 8, 8, 8, 9, 21, 25, 89, 100, 120, 500, 1000};
        to_check_example.BinarySearchExample binSearch = new to_check_example.BinarySearchExample();
        start = System.nanoTime();
        binSearch.runBinarySearchIteratively(intArray, 89, 0, intArray.length - 1);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.println("timeElapsed_runBinarySearchIteratively : " + timeElapsed);
        json.put("exampleId", String.valueOf(timeElapsed));
        File file = new File("./target/surefire-reports/perfomance.json");
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
