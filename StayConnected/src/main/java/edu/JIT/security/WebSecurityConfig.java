package edu.JIT.security;

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
		http.exceptionHandling().accessDeniedPage("/403");
		http
			.authorizeRequests()
				.antMatchers("/","/login", "/registration", "/home", "/activateAccount", "/confirmation").permitAll()
				.antMatchers("/updateAccount","/updateUserAccount/**", "/manageAccount", "/browseUsers","/postJobOpenings","/browsejobopenings", "/accountActivated", "/viewProfile/**").hasAnyRole("ALUM", "CURR", "FACULTY")
				.antMatchers("/403").permitAll().anyRequest().authenticated()			
				.and()
			.formLogin().failureUrl("/login?error")
				.defaultSuccessUrl("/home")
				.loginPage("/login").permitAll()
				.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?Logout").permitAll();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
				.usersByUsernameQuery(
						"select rid, password, true as active from stayconnected.userlogin where rid=?")
				.authoritiesByUsernameQuery("SELECT stayconnected.authority.rid, stayconnected.userroles.role "
				+ "FROM stayconnected.authority, stayconnected.userroles "
				+ "WHERE authority.userroleid = userroles.uid and stayconnected.authority.rid = ?");
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}