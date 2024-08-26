package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.domain.model.Teams;
import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.domain.response.TeamsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.TeamsRepository;
import com.EmployeeTracking.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class TeamsServiceTest {

    @Mock
    private TeamsRepository teamsRepository;

    @InjectMocks
    private TeamsService teamsService;

    private Teams team;
    private TeamsRequestDto teamRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        team = TestDataFactory.createTeam();
        teamRequestDto = TestDataFactory.createTeamRequestDto();

    }

    @Test
    void testSaveTeamSuccess() {
        when(teamsRepository.saveAndFlush(any(Teams.class))).thenReturn(team);

        TeamsResponseDto response = teamsService.saveTeam(teamRequestDto);

        TeamsResponseDto expectedResponse = TestDataFactory.createTeamsResponseDto(team);

        assertNotNull(response);
        assertEquals(expectedResponse.getTeamId(), response.getTeamId(), "Team ID does not match");
        assertEquals(expectedResponse.getTeamName(), response.getTeamName(), "Team Name does not match");
        assertEquals(expectedResponse.getDescription(), response.getDescription(), "Description does not match");
        assertEquals(expectedResponse.getCreatedAt(), response.getCreatedAt(), "Created At does not match");
        assertEquals(expectedResponse.getUpdatedAt(), response.getUpdatedAt(), "Updated At does not match");
        assertEquals(expectedResponse.getCreatedBy(), response.getCreatedBy(), "Created By does not match");
        assertEquals(expectedResponse.getUpdatedBy(), response.getUpdatedBy(), "Updated By does not match");

        verify(teamsRepository, times(1)).saveAndFlush(any(Teams.class));
    }

    @Test
    void testGetAllTeams() {
        when(teamsRepository.findAll()).thenReturn(List.of(team));

        List<TeamsResponseDto> response = teamsService.getAllTeams();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(team.getTeamName(), response.get(0).getTeamName());

        verify(teamsRepository, times(1)).findAll();
    }

    @Test
    void testGetTeamById() {
        UUID id = team.getTeamId();
        when(teamsRepository.findById(id)).thenReturn(Optional.of(team));

        TeamsResponseDto response = teamsService.getTeamById(id);

        assertNotNull(response);
        assertEquals(team.getTeamId(), response.getTeamId());
        assertEquals(team.getTeamName(), response.getTeamName());

        verify(teamsRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateTeam() {
        UUID id = team.getTeamId();
        when(teamsRepository.findById(id)).thenReturn(Optional.of(team));
        when(teamsRepository.saveAndFlush(any(Teams.class))).thenReturn(team);

        TeamsResponseDto response = teamsService.updateTeam(id, teamRequestDto);

        assertNotNull(response);
        assertEquals(team.getTeamId(), response.getTeamId());
        assertEquals(team.getTeamName(), response.getTeamName());

        verify(teamsRepository, times(1)).findById(id);
        verify(teamsRepository, times(1)).saveAndFlush(any(Teams.class));
    }

    @Test
    void testDeleteTeam() {
        UUID id = team.getTeamId();
        when(teamsRepository.findById(id)).thenReturn(Optional.of(team));
        when(teamsRepository.saveAndFlush(any(Teams.class))).thenReturn(team);

        teamsService.deleteTeam(id);

        verify(teamsRepository, times(1)).findById(id);
        verify(teamsRepository, times(1)).saveAndFlush(any(Teams.class));
    }

    @Test
    void testFindById_teamNotFound() {
        UUID id = UUID.randomUUID();
        when(teamsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> teamsService.findById(id));

        verify(teamsRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllTeamsWhenNoTeamsExist() {
        when(teamsRepository.findAll()).thenReturn(Collections.emptyList());

        List<TeamsResponseDto> response = teamsService.getAllTeams();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

}
