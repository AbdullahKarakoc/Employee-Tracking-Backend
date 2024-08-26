package com.EmployeeTracking.util;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.model.Tasks;
import com.EmployeeTracking.domain.model.Teams;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.request.TasksRequestDto;
import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.domain.response.StatusResponseDto;
import com.EmployeeTracking.domain.response.TasksResponseDto;
import com.EmployeeTracking.domain.response.TeamsResponseDto;
import com.EmployeeTracking.enums.ProcessStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

    public static StatusRequestDto createStatusRequestDto() {
        StatusRequestDto statusRequestDto = new StatusRequestDto();
        statusRequestDto.setStatus(ProcessStatus.IN_PROGRESS);
        statusRequestDto.setDescription("In Progress");
        return statusRequestDto;
    }


    private static StatusResponseDto createStatusResponseDto(Status status) {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        statusResponseDto.setStatus(status.getStatus());
        statusResponseDto.setDescription(status.getDescription());
        return statusResponseDto;
    }

    public static ProjectsRequestDto createProjectRequestDto() {
        ProjectsRequestDto projectRequestDto = new ProjectsRequestDto();
        projectRequestDto.setName("Test Project");
        projectRequestDto.setDescription("This is a test project");
        projectRequestDto.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        projectRequestDto.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        projectRequestDto.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        projectRequestDto.setStatus(createStatusRequestDto());
        return projectRequestDto;
    }

    public static ProjectsResponseDto createProjectsResponseDto(Projects project) {
        ProjectsResponseDto projectsResponseDto = new ProjectsResponseDto();
        projectsResponseDto.setProjectId(project.getProjectId());
        projectsResponseDto.setName(project.getName());
        projectsResponseDto.setDescription(project.getDescription());
        projectsResponseDto.setStartDate(project.getStartDate());
        projectsResponseDto.setDeadline(project.getDeadline());
        projectsResponseDto.setFinishDate(project.getFinishDate());
        projectsResponseDto.setStatus(createStatusResponseDto(project.getStatus()));
        projectsResponseDto.setCreatedAt(project.getCreatedAt());
        projectsResponseDto.setUpdatedAt(project.getUpdatedAt());
        projectsResponseDto.setCreatedBy(project.getCreatedBy());
        projectsResponseDto.setUpdatedBy(project.getUpdatedBy());
        return projectsResponseDto;
    }

    public static Projects createProject(Status status) {
        Projects project = new Projects();
        project.setProjectId(UUID.randomUUID());
        project.setName("Test Project");
        project.setDescription("This is a test project");
        project.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        project.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        project.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        project.setStatus(status);
        return project;
    }

    public static Status createStatus() {
        Status status = new Status();
        status.setStatus(ProcessStatus.IN_PROGRESS);
        status.setDescription("In Progress");
        return status;
    }

    public static Teams createTeam(){
        Teams team = new Teams();
        team.setTeamId(UUID.randomUUID());
        team.setTeamName("Development");
        team.setDescription("Develops and maintains software");
        return team;
    }

    public static TeamsRequestDto createTeamRequestDto(){
        TeamsRequestDto teamRequestDto = new TeamsRequestDto();
        teamRequestDto.setTeamName("Development");
        teamRequestDto.setDescription("Develops and maintains software");
        return teamRequestDto;
    }

    public static TeamsResponseDto createTeamsResponseDto(Teams team) {
        TeamsResponseDto responseDto = new TeamsResponseDto();
        responseDto.setTeamId(team.getTeamId());
        responseDto.setTeamName(team.getTeamName());
        responseDto.setDescription(team.getDescription());
        responseDto.setCreatedAt(team.getCreatedAt());
        responseDto.setUpdatedAt(team.getUpdatedAt());
        responseDto.setCreatedBy(team.getCreatedBy());
        responseDto.setUpdatedBy(team.getUpdatedBy());
        return responseDto;
    }


    public static TasksRequestDto createTasksRequestDto() {
        TasksRequestDto tasksRequestDto = new TasksRequestDto();
        tasksRequestDto.setName("Test Task");
        tasksRequestDto.setStatus(ProcessStatus.IN_PROGRESS);
        tasksRequestDto.setDescription("This is a test task");
        tasksRequestDto.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        tasksRequestDto.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        tasksRequestDto.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        tasksRequestDto.setProjectId(UUID.randomUUID());
        return tasksRequestDto;
    }

    public static TasksResponseDto createTasksResponseDto(Tasks task) {
        TasksResponseDto tasksResponseDto = new TasksResponseDto();
        tasksResponseDto.setTaskId(task.getTaskId());
        tasksResponseDto.setName(task.getName());
        tasksResponseDto.setStatus(task.getStatus());
        tasksResponseDto.setDescription(task.getDescription());
        tasksResponseDto.setStartDate(task.getStartDate());
        tasksResponseDto.setDeadline(task.getDeadline());
        tasksResponseDto.setFinishDate(task.getFinishDate());

        ProjectsResponseDto projectResponseDto = task.getProject() != null
                ? createProjectsResponseDto(task.getProject())
                : null;
        tasksResponseDto.setProject(projectResponseDto);

        tasksResponseDto.setCreatedAt(task.getCreatedAt());
        tasksResponseDto.setUpdatedAt(task.getUpdatedAt());
        tasksResponseDto.setCreatedBy(task.getCreatedBy());
        tasksResponseDto.setUpdatedBy(task.getUpdatedBy());
        return tasksResponseDto;
    }


    public static Tasks createTask(Projects project) {
        Tasks task = new Tasks();
        task.setTaskId(UUID.randomUUID());
        task.setName("Test Task");
        task.setStatus(ProcessStatus.IN_PROGRESS);
        task.setDescription("This is a test task");
        task.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        task.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        task.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        task.setProject(project);
        return task;
    }

}
