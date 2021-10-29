package es.upv.grycap.tracer.security;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.QueryParamPresenceRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import es.upv.grycap.tracer.conf.ExceptionHandlerFilter;
import es.upv.grycap.tracer.model.TracerRoles;

@EnableWebSecurity
public class SecurityConfig {
	
	@Configuration
	@Order(1)
	public class BasicJDBCAuthConf extends WebSecurityConfigurerAdapter {
		@Autowired
		private DataSource dataSource;
		@Value("${tracer.admin.name}")
		private String adminName;
		@Value("${tracer.admin.password}")
		private String adminPassword;
		

//		@Autowired
//		public void configureGlobal(AuthenticationManagerBuilder auth) {
//		    auth.authenticationProvider(keycloakAuthenticationProvider);
//		}
		
	      @Override
	        protected void configure(HttpSecurity http) throws Exception {
	            http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class)
	            .requestMatcher(r -> 
	            	r.getHeader("Authorization") != null && r.getHeader("Authorization").startsWith("Basic"))
	            //.csrf().disable()
	            .authorizeRequests(authorize -> {
					try {
						authorize
								
						    .anyRequest().authenticated().and().exceptionHandling()
						    .accessDeniedHandler(new RestAccessDeniedHandler());
					} catch (Exception e) {
						e.printStackTrace();
					}
				})
	                .httpBasic();
	            http.csrf().disable(); 
	        }

	        @Override
	        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        	JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.jdbcAuthentication()
			      .dataSource(dataSource);
			      //.withDefaultSchema()
	        	if (!dataSource.getConnection().getMetaData()
	        			.getTables(null, "", "USERS", null).first()) {
	                configurer = configurer.withDefaultSchema()
	                		.withUser(User.withUsername(adminName)
	    			        .password(passwordEncoder().encode(adminPassword))
	    			        .roles(TracerRoles.TRACER_ADMIN.name()));
	            }			      
	        }

		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
	}
	
	@KeycloakConfiguration
	@Order(2)
	public class KeycloakSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {
		@Autowired
		protected ExceptionHandlerFilter exceptionHandlerFilter;
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) {
		    SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
		    grantedAuthorityMapper.setPrefix("ROLE_");

		    KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
		    auth.authenticationProvider(keycloakAuthenticationProvider);
		}

	    @Bean
	    @Override
	    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	    }

	    @Bean
	    public KeycloakConfigResolver keycloakConfigResolver() {
	        return new KeycloakSpringBootConfigResolver();
	    }

//	    @Override
//	    public void configure(AuthenticationManagerBuilder auth) {
//	        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//	        auth.authenticationProvider(keycloakAuthenticationProvider);
//	    }
	    @Bean
	    @Override
	    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
	        RequestMatcher requestMatcher =
	                new OrRequestMatcher(
	                        new QueryParamPresenceRequestMatcher(OAuth2Constants.ACCESS_TOKEN),
	                        // We're providing our own authorization header matcher
	                        new IgnoreKeycloakProcessingFilterRequestMatcher()
	                );
	        return new KeycloakAuthenticationProcessingFilter(authenticationManagerBean(), requestMatcher);
	    }

	    // Matches request with Authorization header which value doesn't start with "Basic " prefix
	    private class IgnoreKeycloakProcessingFilterRequestMatcher implements RequestMatcher {
	        IgnoreKeycloakProcessingFilterRequestMatcher() {
	        }

	        public boolean matches(HttpServletRequest request) {
	            String authorizationHeaderValue = request.getHeader("Authorization");
	            return authorizationHeaderValue != null && !authorizationHeaderValue.startsWith("Basic ");
	        }
	    }
	    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        super.configure(http);
	        http.addFilterBefore(exceptionHandlerFilter, KeycloakAuthenticationProcessingFilter.class)
	        	.antMatcher("/**").authorizeRequests(authorize -> {
					try {
						authorize
						        .anyRequest().authenticated().and().exceptionHandling()
						        .accessDeniedHandler(new RestAccessDeniedHandler());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

            http.csrf().disable(); 
	    }

	}
}


