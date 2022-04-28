package org.demo.config;

import lombok.extern.log4j.Log4j2;
import org.demo.db.modal.WebRoleInfo;
import org.demo.db.modal.WebUserInfo;
import org.demo.db.repository.WebRoleInfoRepository;
import org.demo.db.repository.WebUserInfoRepository;
import org.demo.entity.common.WebUserDetails;
import org.demo.filter.JwtFilter;
import org.demo.handler.CustomAuthenticationFailureHandler;
import org.demo.handler.CustomLogoutSuccessHandler;
import org.demo.handler.CustomSimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.allow-ip:127.0.0.1/32}")
    private List<String> allowIps;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private CustomSimpleUrlAuthenticationSuccessHandler loginSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler loginFailureHandler;
    @Autowired
    private CustomLogoutSuccessHandler logoutHandler;

    @Autowired
    @Lazy
    private WebUserInfoRepository webUserInfoRepository;
    @Autowired
    @Lazy
    private WebRoleInfoRepository webRoleInfoRepository;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userId -> {
            Optional<WebUserInfo> optionalWebUserInfo = webUserInfoRepository.findById(userId);
            if(!optionalWebUserInfo.isPresent()){
                throw new UsernameNotFoundException("User / Role not found");
            }
            WebUserInfo webUserInfo = optionalWebUserInfo.get();
            Optional<WebRoleInfo> optionalWebRoleInfo = webRoleInfoRepository.findById(webUserInfo.getRoleId());
            if(!optionalWebRoleInfo.isPresent()) {
                throw new UsernameNotFoundException("User / Role not found");
            }
            WebRoleInfo webRoleInfo = optionalWebRoleInfo.get();
            Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(webRoleInfo.getRoleId());
            return new WebUserDetails(webUserInfo, authorities);
        });
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().maximumSessions(1).expiredUrl("/login?timeout=true");

        http.formLogin() // 定義當需要使用者登入時候，轉到的登入頁面。
                .loginPage("/login") // 設定登入頁面
                .defaultSuccessUrl("/web/index", true)  // 設定成功導入頁面
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureUrl("/login?error=true")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/web/**").authenticated()     //Web使用Security驗證，需登入
                .antMatchers("/api/**").permitAll()         //API使用Filter驗證，使用Token
                .anyRequest().permitAll()                               //對 剩下API定義規則 >> HTTP 403
                .and().csrf(httpSecurityCsrfConfigurer->httpSecurityCsrfConfigurer.ignoringAntMatchers("/api/**"));     //跨站請求

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout()
                .logoutSuccessHandler(logoutHandler)
                .logoutSuccessUrl("/login?logout=true")
                .addLogoutHandler(logoutHandler)
                .deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true).permitAll();
    }


    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

}
