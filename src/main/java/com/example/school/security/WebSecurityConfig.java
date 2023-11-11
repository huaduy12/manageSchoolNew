package com.example.school.security;

import com.example.school.service.account.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(
                        configurer -> configurer
//                        .requestMatchers("/").permitAll() // các trang web này sẽ được bỏ qua xác thực
//                        .anyRequest().authenticated() // tất cả các trang web đều sẽ bị xác thực
                                .requestMatchers("/css/**","/icons/**","/images/**","/js/**","/plugins/**").permitAll()
                                .requestMatchers("/error/showPage403","/contact").permitAll()
                                .requestMatchers("/user/**","/home","/").hasAnyRole("ADMIN","MANAGER","USER")
                                // phân quyền với giáo viên
                                .requestMatchers("/teacher/**").hasRole("MANAGER")
                                // phân quyền với giáo viên chủ nhiệm xem thông tin
                                .requestMatchers("/manage/class/classDetail/**").hasAnyRole("MANAGER")
                                .requestMatchers("/manage/class/subject/**").hasAnyRole("MANAGER")
                                .requestMatchers("/manage/class/subjectTeacher/**").hasAnyRole("MANAGER")
                                // phân quyền với giáo viên bộ môn để cập nhập điểm
                                .requestMatchers("/manage/score/subject_score/**").hasAnyRole("MANAGER")
                                // phân quyền với học sinh
                                .requestMatchers("/student/**").hasAnyRole("USER")
                                // phân quyền với admin
                                .requestMatchers("/manage/**","/account/**").hasAnyRole("ADMIN")
                )
                .formLogin(   // đều hướng sang form login tự custom
                        form->form
                                .loginPage("/showLoginPage")
                                .loginProcessingUrl("/xac-thuc")// form html ở login submit sẽ gửi về đây để xác thực
                                .defaultSuccessUrl("/home")
                                .permitAll()
                )

                .logout( // cho phép tất cả đều logout
                        logout->logout.permitAll()

                )
                .exceptionHandling(
                        configurer -> configurer.accessDeniedPage("/error/showPage403")
                );

        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // mã hóa dữ liệu t database
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService){
        DaoAuthenticationProvider daoAuthenticationProvider =new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
        //hàm này sẽ goi hàm loadUserByUsername từ UserServiceImp do đã implement tư UserService
    }



}
