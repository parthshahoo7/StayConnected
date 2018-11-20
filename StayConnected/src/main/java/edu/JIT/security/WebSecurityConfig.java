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

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/registration", "/home").permitAll()
				.and()
			.exceptionHandling().accessDeniedPage("/denied")
				.and()
			.formLogin().failureUrl("/login?error")
				.defaultSuccessUrl("/home")
				.loginPage("/login").permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?Logout").permitAll();
				//.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				//.permitAll();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
				.usersByUsernameQuery(
						"select RID, password, true as active" + " from stayconnected.UserLogin where RID=?")
				.authoritiesByUsernameQuery("select RID , UserRoleID from " + "stayconnected.Authority where RID=?");
	}
	/*
	 FOR REFERENCE
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/homepage", "/login", "/listProducts", "/Logo.png").permitAll()
				.antMatchers("/addProducts", "/deleteProduct", "/purchaseOrders", "/orderDetails/**").hasAnyRole("ADMIN")
				.antMatchers("/product/**", "/searchProducts", "/shoppingCart", "/orderConfirmation").hasAnyRole("CUSTOMER")
				.antMatchers("/addProduct").hasAnyRole("EMP", "ADMIN")
				.and()
			.exceptionHandling().accessDeniedPage("/403")
				.and()
			.formLogin()
				.failureUrl("/login?error")
				.defaultSuccessUrl("/home")
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?Logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll();
	}
*/
	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}