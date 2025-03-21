package aleksandarskachkov.simracingacademy.security;

import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Data
@Getter
@AllArgsConstructor
public class AuthenticationMetadata implements UserDetails {

    private UUID userId;
    private String username;
    private String password;
    private UserRole role;
//    private SubscriptionType subscriptionType;
    private boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        List<SimpleGrantedAuthority> authorities = List.of(

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
//                new SimpleGrantedAuthority(subscriptionType.name())
//        );
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
