package use_case.job_grouping;

import entity.JobGroup;
import java.util.ArrayList;

/**
 * The output data for the Job Grouping use case.
 */
public class CreateJobGroupOutputData {

    private final String groupName;
    private final ArrayList<JobGroup> jobGroups;

    /**
     * Creates the output data for the Job Grouping use case.
     * @param groupName the name of the created group
     * @param jobGroups all existing job groups of the student
     */
    public CreateJobGroupOutputData(String groupName,
                                    ArrayList<JobGroup> jobGroups) {
        this.groupName = groupName;
        this.jobGroups = new ArrayList<>(jobGroups);
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<JobGroup> getJobGroups() {
        return new ArrayList<>(jobGroups);
    }
}