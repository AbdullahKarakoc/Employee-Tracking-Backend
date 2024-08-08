package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.response.StatusResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;

    public List<StatusResponseDto> getAllStatuses() {
        List<Status> statuses = statusRepository.findAll();
        return statuses.stream()
                .map(status -> modelMapper.map(status, StatusResponseDto.class))
                .toList();
    }

    public StatusResponseDto getStatusById(UUID id) {
        Status status = findById(id);
        return modelMapper.map(status, StatusResponseDto.class);
    }

    public StatusResponseDto saveStatus(StatusRequestDto statusRequestDto) {
        Status status = modelMapper.map(statusRequestDto, Status.class);
        Status savedStatus = save(status);
        return modelMapper.map(savedStatus, StatusResponseDto.class);
    }

    public StatusResponseDto updateStatus(UUID id, StatusRequestDto statusRequestDto) {
        Status existingStatus = findById(id);

        modelMapper.map(statusRequestDto, existingStatus);

        Status updatedStatus = save(existingStatus);
        return modelMapper.map(updatedStatus, StatusResponseDto.class);
    }

    public void deleteStatus(UUID id) {
        Status status = findById(id);
        status.setDeleted(true);
        save(status);
    }

    public Status save(Status status) {
        return statusRepository.saveAndFlush(status);
    }

    public Status findById(UUID id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Status not found"));
    }
}
