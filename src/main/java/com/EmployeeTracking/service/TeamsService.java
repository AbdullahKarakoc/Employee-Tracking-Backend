package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Teams;
import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.domain.response.TeamsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.TeamsRepository;
import com.EmployeeTracking.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamsRepository teamRepository;

    public List<TeamsResponseDto> getAllTeams() {
        List<Teams> teams = teamRepository.findAll();

        if (teams.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(teams, TeamsResponseDto.class);
    }

    public TeamsResponseDto getTeamById(UUID id) {
        Teams team = findById(id);
        return ObjectMapperUtils.map(team, TeamsResponseDto.class);
    }

    public TeamsResponseDto saveTeam(TeamsRequestDto teamRequestDto) {
        Teams team = ObjectMapperUtils.map(teamRequestDto, Teams.class);
        Teams savedTeam = save(team);
        return ObjectMapperUtils.map(savedTeam, TeamsResponseDto.class);
    }

    public TeamsResponseDto updateTeam(UUID id, TeamsRequestDto teamRequestDto) {
        Teams existingTeam = findById(id);

        ObjectMapperUtils.map(teamRequestDto, existingTeam);

        Teams updatedTeam = save(existingTeam);
        return ObjectMapperUtils.map(updatedTeam, TeamsResponseDto.class);
    }

    public void deleteTeam(UUID id) {
        Teams team = findById(id);
        team.setDeleted(true);
        save(team);
    }

    public Teams save(Teams team) {
        return teamRepository.saveAndFlush(team);
    }

    public Teams findById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Team not found"));
    }
}
