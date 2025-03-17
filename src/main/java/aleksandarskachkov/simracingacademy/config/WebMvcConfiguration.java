package aleksandarskachkov.simracingacademy.config;

import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.user.model.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(matchers -> matchers
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/register").permitAll()
                        .anyRequest().authenticated()

//                        // Restrict access to Default content for Default users
//                        .requestMatchers("/videos/track/default/**").access("hasSubscription('DEFAULT')")
//                        .requestMatchers("/videos/module/default/**").access("hasSubscription('DEFAULT')")
//
//                        // Restrict access to Premium content for Premium and Ultimate users
//                        .requestMatchers("/videos/track/premium/**").access("hasSubscription('PREMIUM')")
//                        .requestMatchers("/videos/module/premium/**").access("hasSubscription('PREMIUM')")
//
//                        // Restrict access to Ultimate content for Ultimate users
//                        .requestMatchers("/videos/track/ultimate/**").access("hasSubscription('ULTIMATE')")
//                        .requestMatchers("/videos/module/ultimate/**").access("hasSubscription('ULTIMATE')")
//
//                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
