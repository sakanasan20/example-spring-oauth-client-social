package tw.niq.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	ClientRegistrationRepository clientRegistrationRepository;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests(authorizeHttpRequests -> 
				authorizeHttpRequests
					.requestMatchers(HttpMethod.GET, "/").permitAll()
					.anyRequest().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.logout(logout -> 
				logout
//					.logoutSuccessUrl("/")
					.logoutSuccessHandler(oidcLogoutSuccessHandler())
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID"));
		
		return http.build();
	}

	private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
		
		OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler = 
				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
		
		oidcClientInitiatedLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8088/");
		
		return oidcClientInitiatedLogoutSuccessHandler;
	}

}
