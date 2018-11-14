package edu.JIT.web;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private DataSource dataSource;
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/", "/registration").permitAll()
				.and()
			.exceptionHandling().accessDeniedPage("/denied").and()
			.formLogin()
				.failureUrl("/login?error")
				.defaultSuccessUrl("/registration")
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.permitAll();
		}
		
		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder).usersByUsernameQuery("select RID, password, true as active" + 
			 " from stayconnected.UserLogin where RID=?").authoritiesByUsernameQuery("select RID , UserRoleID from "
			 		+ "stayconnected.Authority where RID=?");
		}	
		
		@Bean(name="passwordEncoder")
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder(); }
	}

