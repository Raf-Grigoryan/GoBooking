package org.example.gobooking.entity.projectFinance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProjectFinance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    BigDecimal balance;

    public void sum(BigDecimal projectBalance) {
        this.balance = this.balance.add(projectBalance);
    }
}
