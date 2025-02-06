package org.example.gobooking.entity.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.User;


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
