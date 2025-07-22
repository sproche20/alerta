package com.uisrael.alerta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.repository.UsuarioRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	@Autowired
	private UsuarioRepository usuarioRep;

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UsuarioModel usuario = usuarioRep.findByCorreo(correo)
		        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		    return new User(
		        usuario.getCorreo(),               // username
		        usuario.getPassword(),             // password
		        usuario.isEnabled(),               // enabled
		        true,                              // accountNonExpired
		        true,                              // credentialsNonExpired
		        true,                              // accountNonLocked
		        AuthorityUtils.createAuthorityList("ROLE_" + usuario.getRol()) // roles con prefijo
		    );
	}

}
