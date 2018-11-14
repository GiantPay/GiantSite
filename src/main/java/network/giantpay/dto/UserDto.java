package network.giantpay.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.http.HttpServletRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserDto extends AbstractDto {

    private String email;
    private String username;
    private String password;

    public static UserDto fromRequest(final HttpServletRequest request) {
        return UserDto.builder()
                .password(request.getParameter("password"))
                .username(request.getParameter("username"))
                .build();
    }
}
