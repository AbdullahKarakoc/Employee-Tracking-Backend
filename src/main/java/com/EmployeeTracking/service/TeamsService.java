package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Teams;
import com.EmployeeTracking.domain.request.TeamsRequestDto;
import com.EmployeeTracking.domain.response.TeamsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.TeamsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamsRepository teamRepository;
    private final ModelMapper modelMapper;

    public List<TeamsResponseDto> getAllTeams() {
        List<Teams> teams = teamRepository.findAll();
        return teams.stream()
                .map(team -> modelMapper.map(team, TeamsResponseDto.class))
                .collect(Collectors.toList());
    }

    public TeamsResponseDto getTeamById(UUID id) {
        Teams team = findById(id);
        return modelMapper.map(team, TeamsResponseDto.class);
    }

    public TeamsResponseDto saveTeam(TeamsRequestDto teamRequestDto) {
        Teams team = modelMapper.map(teamRequestDto, Teams.class);
        Teams savedTeam = save(team);
        return modelMapper.map(savedTeam, TeamsResponseDto.class);
    }

    public TeamsResponseDto updateTeam(UUID id, TeamsRequestDto teamRequestDto) {
        Teams existingTeam = findById(id);

        modelMapper.map(teamRequestDto, existingTeam);

        Teams updatedTeam = save(existingTeam);
        return modelMapper.map(updatedTeam, TeamsResponseDto.class);
    }

    public void deleteTeam(UUID id) {
        Teams team = findById(id);
        team.setDeleted(true); // Assuming there's a 'deleted' field
        save(team);
    }

    private Teams save(Teams team) {
        return teamRepository.saveAndFlush(team);
    }

    private Teams findById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Team not found"));
    }
}
