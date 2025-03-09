package org.example.gobookingcommon.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gobookingcommon.entity.projectFinance.ProjectFinance;
import org.example.gobookingcommon.repository.ProjectFinanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

public class ProjectFinanceServiceImplTest {

    @Mock
    private ProjectFinanceRepository projectFinanceRepository;

    private ProjectFinanceServiceImpl projectFinanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        projectFinanceService = new ProjectFinanceServiceImpl(projectFinanceRepository);
    }

    @Test
    void shouldAddFundsWhenProjectFinanceDoesNotExist() {

        BigDecimal moneyToAdd = BigDecimal.valueOf(100.00);
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.empty());

        projectFinanceService.addFunds(moneyToAdd);

        verify(projectFinanceRepository, times(1)).save(any(ProjectFinance.class));
    }

    @Test
    void shouldAddFundsWhenProjectFinanceExists() {
        BigDecimal moneyToAdd = BigDecimal.valueOf(100.00);
        ProjectFinance existingProjectFinance = new ProjectFinance();
        existingProjectFinance.setBalance(BigDecimal.valueOf(50.00));
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.of(existingProjectFinance));

        projectFinanceService.addFunds(moneyToAdd);

        assertEquals(BigDecimal.valueOf(150.00), existingProjectFinance.getBalance());
        verify(projectFinanceRepository, times(1)).save(existingProjectFinance);
    }

    @Test
    void shouldNotThrowExceptionWhenAddingFundsToExistingBalance() {
        BigDecimal moneyToAdd = BigDecimal.valueOf(200.00);
        ProjectFinance existingProjectFinance = new ProjectFinance();
        existingProjectFinance.setBalance(BigDecimal.valueOf(100.00));
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.of(existingProjectFinance));

        projectFinanceService.addFunds(moneyToAdd);

        assertEquals(BigDecimal.valueOf(300.00), existingProjectFinance.getBalance());
        verify(projectFinanceRepository, times(1)).save(existingProjectFinance);
    }

    @Test
    void shouldHandleDatabaseFailure() {
        BigDecimal moneyToAdd = BigDecimal.valueOf(50.00);
        when(projectFinanceRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> projectFinanceService.addFunds(moneyToAdd));
    }

    @Test
    void shouldReturnProjectFinanceWhenExists() {
        ProjectFinance projectFinance = new ProjectFinance();
        projectFinance.setBalance(BigDecimal.valueOf(500.00));
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.of(projectFinance));

        double result = projectFinanceService.getProjectFinance();

        assertEquals(500.00, result);
    }

    @Test
    void shouldReturnZeroWhenProjectFinanceDoesNotExist() {
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.empty());

        double result = projectFinanceService.getProjectFinance();

        assertEquals(0.0, result);
    }

    @Test
    void shouldReturnZeroWhenProjectFinanceBalanceIsZero() {
        ProjectFinance projectFinance = new ProjectFinance();
        projectFinance.setBalance(BigDecimal.ZERO);
        when(projectFinanceRepository.findById(1)).thenReturn(Optional.of(projectFinance));

        double result = projectFinanceService.getProjectFinance();

        assertEquals(0.0, result);
    }
    
}