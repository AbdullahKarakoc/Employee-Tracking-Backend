package com.EmployeeTracking.util;

import com.EmployeeTracking.domain.model.*;
import com.EmployeeTracking.domain.request.*;
import com.EmployeeTracking.domain.response.*;
import com.EmployeeTracking.enums.ProcessStatus;
import org.springframework.scheduling.config.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static CommentsRequestDto createCommentsRequestDto() {
        CommentsRequestDto commentsRequestDto = new CommentsRequestDto();
        commentsRequestDto.setTitle("Test Comment");
        commentsRequestDto.setDescription("This is a test comment");
        commentsRequestDto.setEmployeeId(UUID.randomUUID());
        commentsRequestDto.setTaskId(UUID.randomUUID());
        return commentsRequestDto;
    }

    public static CommentsResponseDto createCommentsResponseDto(Comments comment) {
        CommentsResponseDto commentsResponseDto = new CommentsResponseDto();
        commentsResponseDto.setCommentId(comment.getCommentId());
        commentsResponseDto.setTitle(comment.getTitle());
        commentsResponseDto.setDescription(comment.getDescription());

        // Assuming EmployeeResponseDto and TasksResponseDto are properly defined and created elsewhere
        commentsResponseDto.setEmployee(createEmployeeResponseDto(comment.getEmployee()));
        commentsResponseDto.setTask(createTasksResponseDto(comment.getTask()));

        commentsResponseDto.setCreatedAt(comment.getCreatedAt());
        commentsResponseDto.setUpdatedAt(comment.getUpdatedAt());
        commentsResponseDto.setCreatedBy(comment.getCreatedBy());
        commentsResponseDto.setUpdatedBy(comment.getUpdatedBy());
        return commentsResponseDto;
    }

    public static Comments createComment(Employee employee, Tasks task) {
        Comments comment = new Comments();
        comment.setCommentId(UUID.randomUUID());
        comment.setTitle("Test Comment");
        comment.setDescription("This is a test comment");
        comment.setEmployee(employee);
        comment.setTask(task);
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());
        comment.setCreatedBy("testUser");
        comment.setUpdatedBy("testUser");
        return comment;
    }

    public static EmployeeResponseDto createEmployeeResponseDto(Employee employee) {
        EmployeeResponseDto responseDto = new EmployeeResponseDto();
        responseDto.setEmployeeId(employee.getEmployeeId());
        responseDto.setFirstname(employee.getFirstname());
        responseDto.setLastname(employee.getLastname());
        responseDto.setDateOfBirth(employee.getDateOfBirth());
        responseDto.setEmail(employee.getEmail());
        responseDto.setPhone(employee.getPhone());

        // Rolleri DTO'ya dönüştür
        List<RoleResponseDto> roleResponseDtos = employee.getRoles().stream()
                .map(role -> {
                    RoleResponseDto roleResponseDto = new RoleResponseDto();
                    roleResponseDto.setName(role.getName()); // setName metodunu kullanarak roleName'i ayarla
                    return roleResponseDto;
                })
                .collect(Collectors.toList());
        responseDto.setRoles(roleResponseDtos);

        responseDto.setCreatedAt(employee.getCreatedAt());
        responseDto.setUpdatedAt(employee.getUpdatedAt());

        return responseDto;
    }

    public static Employee createEmployee() {
        // Önce diğer nesneleri oluşturun
        Status status = TestDataFactory.createStatus();
        Projects project = createProject(status);
        Tasks task = createTask(project);
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID());
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setDateOfBirth(Instant.parse("1990-01-01T00:00:00Z"));
        employee.setEmail("john.doe@example.com");
        employee.setPhone("1234567890");
        employee.setPassword("password"); // Bu, gerçek uygulamalarda şifre hashlenmelidir
        employee.setAccountLocked(false);
        employee.setEnabled(true);
        employee.setDeleted(false);

        // Rol ve diğer ilişkili nesneler için metodları çağırıyoruz
        employee.setRoles(Collections.singletonList(createRole())); // Tek bir rol oluşturun

        // Diğer ilişkili nesneler (Team, Performance, ProjectRole, Task, Comment) oluşturulabilir
        employee.setTeam(createTeam()); // Eğer takım eklemeniz gerekiyorsa
        employee.setPerformance(createPerformance()); // Eğer performans eklemeniz gerekiyorsa
        employee.setProjectRole(createProjectRole()); // Eğer proje rolü eklemeniz gerekiyorsa
        employee.setTask(task); // Görev ekleyin
        employee.setComment(Collections.singletonList(createComment(employee, task))); // Yorumlar ekleyin

        return employee;
    }

    public static Role createRole() {
        Role role = new Role();
        role.setRoleId(UUID.randomUUID());
        role.setName("ROLE_USER");
        role.setCreatedDate(Instant.now());
        role.setLastModifiedDate(Instant.now());
        return role;
    }

    public static RoleResponseDto createRoleResponseDto(Role role) {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setName(role.getName());
        return roleResponseDto;
    }

    public static Performances createPerformance() {
        Performances performance = new Performances();
        performance.setPerformanceId(UUID.randomUUID());
        performance.setTotalPoint(85);
        performance.setCommentAmount(10);
        performance.setCompletedTask(5);
        performance.setCreatedAt(Instant.now());
        performance.setUpdatedAt(Instant.now());
        performance.setCreatedBy("test_user");
        performance.setUpdatedBy("test_user");
        return performance;
    }

    public static ProjectRoles createProjectRole() {
        ProjectRoles projectRole = new ProjectRoles();
        projectRole.setProjectRoleId(UUID.randomUUID());
        projectRole.setEmployeeRole("Developer");
        projectRole.setDescription("Responsible for development tasks.");
        projectRole.setCreatedAt(Instant.now());
        projectRole.setUpdatedAt(Instant.now());
        projectRole.setCreatedBy("test_user");
        projectRole.setUpdatedBy("test_user");
        return projectRole;
    }

}
