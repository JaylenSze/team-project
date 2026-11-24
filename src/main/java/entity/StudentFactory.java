package entity;

import java.util.ArrayList;

/**
 * Factory for creating student objects.
 */
public class StudentFactory {

    public Student create(String name, String password, ArrayList<String> courses,
                          ArrayList<String> skills, ArrayList<JobGroup> jobGroups) {
        return new Student(name, password, courses, skills, jobGroups);
    }

    public Student create(String name, String password) {
        return new Student(name, password);
    }
}
