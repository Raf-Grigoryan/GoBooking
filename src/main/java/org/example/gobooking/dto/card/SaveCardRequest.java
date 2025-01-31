package org.example.gobooking.dto.card;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveCardRequest {

    @NotBlank(message = "Card number can't be null")
    @Pattern(regexp = "\\d{16}", message = "The card number must contain 16 digits.")
    private String cardNumber;

    @Future(message = "The card expiration date must be in the future.")
    @NotNull(message = "The card expiration date is required.")
    @DateTimeFormat(pattern = "MM/YY")
    private Date expirationDate;

    @NotBlank(message = "CVV code is required")
    @Size(min = 3, max = 4, message = "CVV code must contain 3-4 digits")
    private String cvvCode;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private int userId;

}
