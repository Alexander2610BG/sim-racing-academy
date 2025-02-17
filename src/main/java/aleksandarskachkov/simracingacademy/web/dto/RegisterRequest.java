package aleksandarskachkov.simracingacademy.web.dto;

import aleksandarskachkov.simracingacademy.user.model.Country;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 6, message = "Username must be at least 6 symbols")
    private String username;

    @Pattern(regexp = "/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$/",
            message =  "Password must be at least 6 symbols" +
                    " with uppercase, lowercase, number & special symbol")
    private String password;

    private Country country;
}
