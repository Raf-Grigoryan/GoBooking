package org.example.gobookingcommon.entity.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.User;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RoleChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User employee;
    @ManyToOne
    private Company company;
}
