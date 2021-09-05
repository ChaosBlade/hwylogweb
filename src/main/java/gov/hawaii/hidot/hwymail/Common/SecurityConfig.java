package gov.hawaii.hidot.hwymail.Common;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LoginService loginService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // Always Pass
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                //Page Auth
                .antMatchers("/code/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/report/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/company/**").access("hasRole('ADMIN')")
                .antMatchers("/office/**").access("hasRole('ADMIN')")
                .antMatchers("/members/**").access("hasRole('ADMIN')")
                .antMatchers("/common/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/main/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/hwylog/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/signaturelog/**").access("hasAnyRole('USERS', 'ADMIN')")
                .antMatchers("/hwyelog/**").access("hasAnyRole('USERS', 'ADMIN')")
                   .antMatchers("/**").permitAll()
                //Login
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("UserName")
                .passwordParameter("PassWord")
                .defaultSuccessUrl("/main")
                .failureUrl("/loginerror")
                .permitAll()
                //Logout
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
/*
                .rememberMe()
                .key("myAppKey")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400)
                .and()
 */

                .exceptionHandling()
                .accessDeniedPage("/main");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.exceptionHandling().accessDeniedPage("/loginerror");
        http.sessionManagement().invalidSessionUrl("/");

    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }



}
