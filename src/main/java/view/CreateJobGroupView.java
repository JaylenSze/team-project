package view;

import entity.Job;
import entity.JobGroup;
import interface_adapter.job_grouping.CreateJobGroupController;
import interface_adapter.job_grouping.CreateJobGroupState;
import interface_adapter.job_grouping.CreateJobGroupViewModel;
import interface_adapter.job_grouping.CreateJobGroupViewModelListener;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The View for creating a job group.
 */
public class CreateJobGroupView extends JPanel
        implements CreateJobGroupViewModelListener {

    private final CreateJobGroupViewModel viewModel;

    private final JTextField groupNameInputField = new JTextField(15);
    private final JLabel messageField = new JLabel();

    private final JList<String> jobList;
    private final ArrayList<Job> availableJobs;

    private final JButton createGroup;
    private CreateJobGroupController controller;

    private final JButton back;
    private ViewManagerModel viewManagerModel;

    private final JTextArea existingGroups = new JTextArea(10, 30);

    /**
     * Creates the Create Job Group View.
     * @param viewModel the view model
     * @param availableJobs the jobs available for selection
     */
    public CreateJobGroupView(CreateJobGroupViewModel viewModel,
                              ArrayList<Job> availableJobs) {
        this.viewModel = viewModel;
        this.availableJobs = new ArrayList<>(availableJobs);
        this.viewModel.addListener(this);

        final JLabel title = new JLabel("Create Job Group");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel groupNameInfo = new LabelTextPanel(
                new JLabel("Group Name"), groupNameInputField);

        DefaultListModel<String> jobListModel = new DefaultListModel<>();

        for (Job job : availableJobs) {
            jobListModel.addElement(job.getTitle() + " - " + job.getCompany());
        }

        jobList = new JList<>(jobListModel);
        jobList.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        final JScrollPane jobScrollPane = new JScrollPane(jobList);

        createGroup = new JButton("Create Group");
        back = new JButton("Back");

        createGroup.addActionListener(evt -> {
            ArrayList<Job> selectedJobs = new ArrayList<>();

            for (int index : jobList.getSelectedIndices()) {
                selectedJobs.add(this.availableJobs.get(index));
            }

            final CreateJobGroupState currentState = viewModel.getState();

            if (controller != null) {
                controller.execute(
                        currentState.getUsername(),
                        groupNameInputField.getText(),
                        selectedJobs);
            }
        });

        back.addActionListener(evt -> {
            if (viewManagerModel != null) {
                viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
                viewManagerModel.notifyViewChanged();
            }
        });

        existingGroups.setEditable(false);

        final JScrollPane groupsScrollPane =
                new JScrollPane(existingGroups);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(groupNameInfo);
        this.add(new JLabel("Select Jobs"));
        this.add(jobScrollPane);
        this.add(new JLabel("Existing Job Groups"));
        this.add(groupsScrollPane);
        this.add(messageField);
        final JPanel buttons = new JPanel();
        buttons.add(createGroup);
        buttons.add(back);

        this.add(buttons);
    }

    @Override
    public void stateChanged() {
        final CreateJobGroupState state = viewModel.getState();

        if (state.isSuccess()) {
            if (state.getGroupName() != null) {
                messageField.setText(
                        "Successfully created group: " + state.getGroupName());
            } else {
                messageField.setText("");
            }

            existingGroups.setText("");

            for (JobGroup group : state.getJobGroups()) {
                existingGroups.append(group.getName() + "\n");

                for (Job job : group.getJobs()) {
                    existingGroups.append(
                            "    - " + job.getTitle()
                                    + " - " + job.getCompany() + "\n");
                }

                existingGroups.append("\n");
            }

            if (state.getJobGroups().isEmpty()) {
                existingGroups.setText("No existing job groups.");
            }

        } else {
            messageField.setText(state.getErrorMessage());
            existingGroups.setText("");
        }
    }

    public void setController(CreateJobGroupController controller) {
        this.controller = controller;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }
}
