package ar.edu.itba.paw.webapp.security.config;

import ar.edu.itba.paw.webapp.security.api.*;
import ar.edu.itba.paw.webapp.security.api.basic.BasicAuthenticationProvider;
import ar.edu.itba.paw.webapp.security.api.handlers.AuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.security.api.handlers.AuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.security.api.handlers.CustomAccessDeniedHandler;
import ar.edu.itba.paw.webapp.security.api.jwt.JwtAuthenticationProvider;
import ar.edu.itba.paw.webapp.security.voters.AntMatcherVoter;
import ar.edu.itba.paw.webapp.security.services.implementation.CampusUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.security" })
@PropertySource(value= {"classpath:application.properties"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CampusUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private BasicAuthenticationProvider basicAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    public static final String API_PREFIX = "/api";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(basicAuthenticationProvider).authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BridgeAuthenticationFilter bridgeAuthenticationFilter() throws Exception {
        BridgeAuthenticationFilter bridgeAuthenticationFilter = new BridgeAuthenticationFilter();
        bridgeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        bridgeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        bridgeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return bridgeAuthenticationFilter;
    }


    @Bean
    public AntMatcherVoter antMatcherVoter() { return new AntMatcherVoter();}

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler())
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/announcements").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/announcements/{announcementId}").access("@antMatcherVoter.canAccessAnnouncementById(authentication, #announcementId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/answers/{answerId}").access("@antMatcherVoter.canAccessAnswerById(authentication, #answerId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/exams/{examId}").access("@antMatcherVoter.canAccessExamById(authentication, #examId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/available-years").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/files").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/announcements").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/teachers").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/helpers").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/students").access("@antMatcherVoter.canAccessCourseById(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/exams").access("@antMatcherVoter.isPrivilegedInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/exams/solved").access("@antMatcherVoter.isStudentInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/exams/unsolved").access("@antMatcherVoter.isStudentInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/exams/answers").access("@antMatcherVoter.isPrivilegedInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/courses/{courseId}/exams/average").access("@antMatcherVoter.isStudentInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/timetable").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/files").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/files/categories").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/files/extensions").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET,API_PREFIX + "/files/{fileId}").access("@antMatcherVoter.canAccessFileById(authentication, #fileId)")
                    .antMatchers(HttpMethod.GET, API_PREFIX + "/user").hasAuthority("USER")
                    .antMatchers(HttpMethod.POST, API_PREFIX + "/courses/{courseId}/exams").access("@antMatcherVoter.isPrivilegedInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.POST, API_PREFIX + "/courses/{courseId}/files").access("@antMatcherVoter.isPrivilegedInCourse(authentication, #courseId)")
                    .antMatchers(HttpMethod.POST, API_PREFIX + "/courses").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.POST, API_PREFIX + "/exams/{examId}/answers").access("@antMatcherVoter.canAccessExamById(authentication, #examId)")
                    .antMatchers(HttpMethod.POST, API_PREFIX + "/courses/{courseId}/announcements").access("@antMatcherVoter.canPostAnnouncementByCourseId(authentication, #courseId)")
                    .antMatchers(HttpMethod.DELETE, API_PREFIX + "/exams/{examId}").access("@antMatcherVoter.canDeleteExamById(authentication, #examId)")
                    .antMatchers(HttpMethod.DELETE,API_PREFIX + "/files/{fileId}").access("@antMatcherVoter.canDeleteFileById(authentication, #fileId)")
                    .antMatchers(HttpMethod.DELETE, API_PREFIX + "/answers/{answerId}").access("@antMatcherVoter.canDeleteAnswerById(authentication, #answerId)")
                    .antMatchers(HttpMethod.DELETE, API_PREFIX + "/announcements/{announcementId}").access("@antMatcherVoter.canDeleteAnnouncementById(authentication, #announcementId)")
                    .antMatchers(API_PREFIX + "/users").hasAuthority("ADMIN")
                    .antMatchers(API_PREFIX + "/users/file-number/last").hasAuthority("ADMIN")
                    .antMatchers(API_PREFIX + "/subjects/**").hasAuthority("USER")
                    .antMatchers(API_PREFIX + "/user").hasAuthority("USER")
                    .antMatchers("/**").permitAll()
            .and()
                .addFilterBefore(bridgeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token", "authorization", "X-Total-Pages", "Content-Disposition"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web
           .ignoring()
                .antMatchers("/")
                .antMatchers("/*.js")
                .antMatchers("/*.css")
                .antMatchers("/favicon.ico")
                .antMatchers("/manifest.json");
    }
}