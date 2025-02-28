package org.example.gobookingcommon.service;

import java.math.BigDecimal;

public interface ProjectFinanceService {

    void addFunds(BigDecimal money);

    double getProjectFinance();

}
