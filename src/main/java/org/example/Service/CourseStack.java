import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Result {
    private static List<Course> courses = new ArrayList<>();
    private static int nextCourseId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Update Course");
            System.out.println("4. Delete Course");
            System.out.println("5. Search Course");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    addCourse(scanner);
                    break;
                case 2:
                    viewAllCourses();
                    break;
                case 3:
                    updateCourse(scanner);
                    break;
                case 4:
                    deleteCourse(scanner);
                    break;
                case 5:
                    searchCourse(scanner);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            System.out.println();
        }
    }

    private static void addCourse(Scanner scanner) {
        System.out.println("Enter course details:");
        System.out.print("Course Name: ");
        String courseName = scanner.next();

        Course course = new Course(nextCourseId, courseName);
        courses.add(course);
        nextCourseId++;

        System.out.println("Course added successfully. Course ID: " + course.getCourseId());
    }

    private static void viewAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Courses:");
            for (Course course : courses) {
                System.out.println("Course ID: " + course.getCourseId());
                System.out.println("Course Name: " + course.getCourseName());
                System.out.println();
            }
        }
    }

    private static void updateCourse(Scanner scanner) {
        System.out.print("Enter course ID to update: ");
        int courseId = scanner.nextInt();
        Course course = findCourseById(courseId);

        if (course != null) {
            System.out.println("Enter updated course details:");
            System.out.print("Course Name: ");
            String courseName = scanner.next();

            course.setCourseName(courseName);

            System.out.println("Course with ID " + courseId + " updated successfully.");
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }

    private static void deleteCourse(Scanner scanner) {
        System.out.print("Enter course ID to delete: ");
        int courseId = scanner.nextInt();
        Course course = findCourseById(courseId);

        if (course != null) {
            courses.remove(course);
            System.out.println("Course with ID " + courseId + " deleted successfully.");
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }

    private static void searchCourse(Scanner scanner) {
        System.out.print("Enter course ID to search: ");
        int courseId = scanner.nextInt();
        Course course = findCourseById(courseId);

        if (course != null) {
            System.out.println("Course Found:");
            System.out.println("Course ID: " + course.getCourseId());
            System.out.println("Course Name: " + course.getCourseName());
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }

    private static Course findCourseById(int courseId) {
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                return course;
            }
        }
        return null;
    }
}
