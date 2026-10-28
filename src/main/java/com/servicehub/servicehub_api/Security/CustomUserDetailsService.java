package com.servicehub.servicehub_api.Security;

import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Role role = u.getRole() == null ? Role.CLIENT : u.getRole();

        return new User(
                u.getEmail(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }
}