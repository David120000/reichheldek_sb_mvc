package rd.reichheldek_sb_mvc.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import rd.reichheldek_sb_mvc.inmemory_repository.IUserRepository;
import rd.reichheldek_sb_mvc.model.RegisteredUser;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IUserRepository userRepository;
    

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/public/**", "/error", "/css/**").permitAll()
                .requestMatchers("/protected/**", "/js/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") 
                .requestMatchers("/admin", "/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .csrf((csrf) -> csrf
                .disable()
            )
            .formLogin((form) -> form
                .permitAll()
            )
            .logout((logout) -> logout
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
	public PasswordEncoder passwordEncoder(){
	    return new EmptyPasswordEncoder();
	}


    @Bean
    public UserDetailsService userDetailsService() {

        List<RegisteredUser> users = userRepository.getRegisteredUsers();
        List<UserDetails> builtUsers = new ArrayList<>();

        for(RegisteredUser user : users) {

            UserDetails builtUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthority())
                .build();

                builtUsers.add(builtUser);
        }

 
        return new InMemoryUserDetailsManager(builtUsers);
    }

}
