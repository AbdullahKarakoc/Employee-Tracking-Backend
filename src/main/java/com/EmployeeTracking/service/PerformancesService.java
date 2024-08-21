package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Performances;
import com.EmployeeTracking.domain.request.PerformancesRequestDto;
import com.EmployeeTracking.domain.response.PerformancesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.PerformancesRepository;
import com.EmployeeTracking.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformancesService {

    private final PerformancesRepository performancesRepository;

    public List<PerformancesResponseDto> getAllPerformances() {
        List<Performances> performances = performancesRepository.findAll();

        if (performances.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(performances, PerformancesResponseDto.class);
    }

    public PerformancesResponseDto getPerformanceById(UUID id) {
        Performances performance = findById(id);
        return ObjectMapperUtils.map(performance, PerformancesResponseDto.class);
    }

    public PerformancesResponseDto savePerformance(PerformancesRequestDto performancesRequestDto) {
        Performances performance = ObjectMapperUtils.map(performancesRequestDto, Performances.class);
        Performances savedPerformance = save(performance);
        return ObjectMapperUtils.map(savedPerformance, PerformancesResponseDto.class);
    }

    public PerformancesResponseDto updatePerformance(UUID id, PerformancesRequestDto performancesRequestDto) {
        Performances existingPerformance = findById(id);
        ObjectMapperUtils.map(performancesRequestDto, existingPerformance);
        Performances updatedPerformance = save(existingPerformance);
        return ObjectMapperUtils.map(updatedPerformance, PerformancesResponseDto.class);
    }

    public void deletePerformance(UUID id) {
        Performances performance = findById(id);
        performance.setDeleted(true);
        save(performance);
    }

    public Performances save(Performances performance) {
        return performancesRepository.saveAndFlush(performance);
    }

    public Performances findById(UUID id) {
        return performancesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Performance not found"));
    }
}
