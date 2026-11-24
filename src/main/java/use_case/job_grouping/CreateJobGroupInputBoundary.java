package use_case.job_grouping;

/**
 * Input boundary for the Job Grouping use case.
 */
public interface CreateJobGroupInputBoundary {

    void execute(CreateJobGroupInputData createJobGroupInputData);

    /**
     * Retrieves all existing job groups for a student.
     * @param username the student's username
     */
    void viewJobGroups(String username);
}