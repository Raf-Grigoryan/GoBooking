package org.example.gobookingcommon.entity.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.user.User;

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
    private String companyPicture;
    @ManyToOne
    private User director;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Address address;
    private boolean valid;
}
