package org.example.gobooking.entity.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phone;
    @ManyToOne
    private User director;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Address address;
    private boolean valid;
}
