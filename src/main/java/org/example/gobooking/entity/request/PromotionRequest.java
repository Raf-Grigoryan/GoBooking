package org.example.gobooking.entity.request;

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
public class PromotionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User requester;
    String message;

}
