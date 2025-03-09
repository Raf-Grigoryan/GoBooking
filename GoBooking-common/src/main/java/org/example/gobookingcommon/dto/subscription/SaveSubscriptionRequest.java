package org.example.gobookingcommon.dto.subscription;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveSubscriptionRequest {
    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @Positive(message = "Длительность должна быть положительным числом")
    @Max(value = 12, message = "Длительность не может превышать 12 месяцев")
    private int duration;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;

    @Min(value = 1, message = "Количество сотрудников должно быть минимум 1")
    private int employeeCount;
}
