package com.EmployeeTracking.util;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.model.Teams;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.enums.ProcessStatus;

import java.time.Instant;
import java.util.UUID;

public class TestDataFactory {

    public static StatusRequestDto createStatusRequestDto() {
        StatusRequestDto statusRequestDto = new StatusRequestDto();
        statusRequestDto.setStatus(ProcessStatus.IN_PROGRESS);
        statusRequestDto.setDescription("In Progress");
        return statusRequestDto;
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
}
