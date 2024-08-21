package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.response.StatusResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.StatusRepository;
import com.EmployeeTracking.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<StatusResponseDto> getAllStatuses() {
        List<Status> statuses = statusRepository.findAll();

        if (statuses.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(statuses, StatusResponseDto.class);
    }

    public StatusResponseDto getStatusById(UUID id) {
        Status status = findById(id);
        return ObjectMapperUtils.map(status, StatusResponseDto.class);
    }

    public StatusResponseDto saveStatus(StatusRequestDto statusRequestDto) {
        Status status = ObjectMapperUtils.map(statusRequestDto, Status.class);
        Status savedStatus = save(status);
        return ObjectMapperUtils.map(savedStatus, StatusResponseDto.class);
    }

    public StatusResponseDto updateStatus(UUID id, StatusRequestDto statusRequestDto) {
        Status existingStatus = findById(id);

        ObjectMapperUtils.map(statusRequestDto, existingStatus);

        Status updatedStatus = save(existingStatus);
        return ObjectMapperUtils.map(updatedStatus, StatusResponseDto.class);
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