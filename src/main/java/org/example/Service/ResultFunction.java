import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Result {
    private int studentId;
    private int subjectId;
    private int courseId;
    private double midtermScore;
    private double finalScore;
    private double totalMarks;
    private String courseResult;

    public void calculateResult() {
        totalMarks = midtermScore + finalScore;
        if (totalMarks >= 50) {
            courseResult = "Pass";
        } else {
            courseResult = "Fail";
        }
    }

    public static void sortResultsDescending(List<Result> results) {
        Collections.sort(results, (r1, r2) -> Double.compare(r2.finalScore, r1.finalScore));
    }
}
