package com.uisrael.alerta.config;

import java.beans.BeanProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.repository.BarrioRepository;
import com.uisrael.alerta.repository.UsuarioRepository;
import com.uisrael.alerta.service.impl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	//**admin
	@Bean
	public CommandLineRunner crearAdmin(UsuarioRepository userRepo, PasswordEncoder encoder, BarrioRepository barrioRepo) {
	    return args -> {
	        if (userRepo.findByCorreo("admin@alerta.com").isEmpty()) {
	            BarrioModel barrio = barrioRepo.findByNombre("La Mariscal").orElseGet(() -> 
	                barrioRepo.save(new BarrioModel(null, "La Mariscal", "Quito", null, null))
	            );

	            UsuarioModel admin = new UsuarioModel();
	            admin.setNombre("Administrador General");
	            admin.setCorreo("admin@alerta.com");
	            admin.setPassword(encoder.encode("admin123"));
	            admin.setRol("ADMIN");
	            admin.setCedula("0102030405");
	            admin.setBarrioModel(barrio);
	            admin.setEnabled(true);

	            userRepo.save(admin);
	        }
	    };
	}
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	            		.requestMatchers(
	            			    "/registro", 
	            			    "/registrar", 
	            			    "/css/**", 
	            			    "/js/**", 
	            			    "/img/**",                     // ✅ para íconos del manifest
	            			    "/manifest.json",             // ✅ manifest visible para PWA
	            			    "/service-worker.js"          // ✅ necesario si tienes service worker
	            			).permitAll()
	                // ADMIN
	                .requestMatchers(
	                	    "/alerta/barrioForm", 
	                	    "/alerta/guardarBarrio", 
	                	    "/alerta/barrioTable", 
	                	    "/alerta/editarBarrio/**", 
	                	    "/alerta/eliminarBarrio/**",
	                	    "/admin/policiasPendientes"
	                	).hasRole("ADMIN")
	             // presidente
	                .requestMatchers(
	                    "/alerta/incidentesPresidente",
	                    "/alerta/usuariosPresidente",
	                    "/alerta/enviarNotificacionBarrio",
	                    "/alerta/estadoIncidente/**",
	                    "/alerta/actualizarEstado",
	                    "/usuariosPendientes",
	                    "/habilitarUsuario/**"
	                ).hasRole("PRESIDENTE")
	                //policia
	                .requestMatchers(
		                    "/alerta/formRonda",
		                    "/alerta/rondasPolicia",
		                    "/alerta/notificacionesTable",
		                    "/alerta/incidenteTable"
		                ).hasRole("POLICIA")
	              //ciudadano
	                .requestMatchers("/alerta/notificacionesUsuario").hasAnyRole("PRESIDENTE", "CIUDADANO")
		                .anyRequest().authenticated()
	            )
	            .formLogin(form -> form
	            	    .loginPage("/login").permitAll()
	            	    .defaultSuccessUrl("/alerta/incidenteForm", true) // redirige directamente
	            	)
	            .logout(logout -> logout
	                .logoutSuccessUrl("/login?logout").permitAll()
	            );

	        return http.build();
	    }
	 @Bean
	 public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		 
	        return http.getSharedObject(AuthenticationManagerBuilder.class)
	                .userDetailsService(userDetailsService)
	                .passwordEncoder(passwordEncoder())
	                .and()
	                .build();		 
	 }
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
