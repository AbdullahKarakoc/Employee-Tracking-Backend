package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Performances;
import com.EmployeeTracking.domain.request.PerformancesRequestDto;
import com.EmployeeTracking.domain.response.PerformancesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.PerformancesRepository;
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

class PerformancesServiceTest {

    @Mock
    private PerformancesRepository performancesRepository;

    @InjectMocks
    private PerformancesService performancesService;

    private Performances performance;
    private PerformancesRequestDto performanceRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        performance = TestDataFactory.createPerformance();
        performanceRequestDto = TestDataFactory.createPerformanceRequestDto();
    }

    @Test
    void testGetAllPerformances() {
        when(performancesRepository.findAll()).thenReturn(List.of(performance));

        List<PerformancesResponseDto> response = performancesService.getAllPerformances();

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        assertEquals(1, response.size(), "Response size should be 1");
        assertEquals(performance.getTotalPoint(), response.get(0).getTotalPoint(), "Total points do not match");

        verify(performancesRepository, times(1)).findAll();
    }

    @Test
    void testGetPerformanceById() {
        UUID id = performance.getPerformanceId();
        when(performancesRepository.findById(id)).thenReturn(Optional.of(performance));

        PerformancesResponseDto response = performancesService.getPerformanceById(id);

        assertNotNull(response, "Response should not be null");
        assertEquals(performance.getPerformanceId(), response.getPerformanceId(), "Performance ID does not match");
        assertEquals(performance.getTotalPoint(), response.getTotalPoint(), "Total points do not match");

        verify(performancesRepository, times(1)).findById(id);
    }

    @Test
    void testSavePerformanceSuccess() {
        Performances performanceToSave = ObjectMapperUtils.map(performanceRequestDto, Performances.class);
        when(performancesRepository.saveAndFlush(any(Performances.class))).thenReturn(performance);

        PerformancesResponseDto response = performancesService.savePerformance(performanceRequestDto);
        PerformancesResponseDto expectedResponse = ObjectMapperUtils.map(performance, PerformancesResponseDto.class);

        assertNotNull(response, "Response should not be null");
        assertEquals(expectedResponse.getPerformanceId(), response.getPerformanceId(), "Performance ID does not match");
        assertEquals(expectedResponse.getTotalPoint(), response.getTotalPoint(), "Total points do not match");
        assertEquals(expectedResponse.getCommentAmount(), response.getCommentAmount(), "Comment amount does not match");
        assertEquals(expectedResponse.getCompletedTask(), response.getCompletedTask(), "Completed task does not match");
        assertEquals(expectedResponse.getCreatedAt(), response.getCreatedAt(), "Created At does not match");
        assertEquals(expectedResponse.getUpdatedAt(), response.getUpdatedAt(), "Updated At does not match");
        assertEquals(expectedResponse.getCreatedBy(), response.getCreatedBy(), "Created By does not match");
        assertEquals(expectedResponse.getUpdatedBy(), response.getUpdatedBy(), "Updated By does not match");

        verify(performancesRepository, times(1)).saveAndFlush(any(Performances.class));
    }

    @Test
    void testUpdatePerformance() {
        UUID id = performance.getPerformanceId();
        when(performancesRepository.findById(id)).thenReturn(Optional.of(performance));
        Performances updatedPerformance = ObjectMapperUtils.map(performanceRequestDto, Performances.class);
        when(performancesRepository.saveAndFlush(any(Performances.class))).thenReturn(updatedPerformance);

        PerformancesResponseDto response = performancesService.updatePerformance(id, performanceRequestDto);

        assertNotNull(response, "Response should not be null");
        assertEquals(updatedPerformance.getPerformanceId(), response.getPerformanceId(), "Performance ID does not match");
        assertEquals(updatedPerformance.getTotalPoint(), response.getTotalPoint(), "Total points do not match");

        verify(performancesRepository, times(1)).findById(id);
        verify(performancesRepository, times(1)).saveAndFlush(any(Performances.class));
    }

    @Test
    void testDeletePerformance() {
        UUID id = performance.getPerformanceId();
        when(performancesRepository.findById(id)).thenReturn(Optional.of(performance));

        performancesService.deletePerformance(id);

        verify(performancesRepository, times(1)).findById(id);
        verify(performancesRepository, times(1)).saveAndFlush(any(Performances.class));
    }

    @Test
    void testFindById_performanceNotFound() {
        UUID id = UUID.randomUUID();
        when(performancesRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> performancesService.findById(id), "Expected DataNotFoundException to be thrown");

        verify(performancesRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllPerformancesWhenNoPerformancesExist() {
        when(performancesRepository.findAll()).thenReturn(Collections.emptyList());

        List<PerformancesResponseDto> response = performancesService.getAllPerformances();

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be empty");
    }
}
