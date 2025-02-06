package org.example.gobooking.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.company.Company;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String pictureName;
    private boolean enable;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    private String token;
    private boolean accountNonLocked;
    @ManyToOne
    private Company company;
}
