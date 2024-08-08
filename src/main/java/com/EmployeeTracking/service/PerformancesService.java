package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Performances;
import com.EmployeeTracking.domain.request.PerformancesRequestDto;
import com.EmployeeTracking.domain.response.PerformancesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.PerformancesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformancesService {

    private final PerformancesRepository performancesRepository;
    private final ModelMapper modelMapper;

    public List<PerformancesResponseDto> getAllPerformances() {
        List<Performances> performances = performancesRepository.findAll();
        return performances.stream()
                .map(performance -> modelMapper.map(performance, PerformancesResponseDto.class))
                .toList();
    }

    public PerformancesResponseDto getPerformanceById(UUID id) {
        Performances performance = findById(id);
        return modelMapper.map(performance, PerformancesResponseDto.class);
    }

    public PerformancesResponseDto savePerformance(PerformancesRequestDto performancesRequestDto) {
        Performances performance = modelMapper.map(performancesRequestDto, Performances.class);
        Performances savedPerformance = save(performance);
        return modelMapper.map(savedPerformance, PerformancesResponseDto.class);
    }

    public PerformancesResponseDto updatePerformance(UUID id, PerformancesRequestDto performancesRequestDto) {
        Performances existingPerformance = findById(id);
        modelMapper.map(performancesRequestDto, existingPerformance);
        Performances updatedPerformance = save(existingPerformance);
        return modelMapper.map(updatedPerformance, PerformancesResponseDto.class);
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
