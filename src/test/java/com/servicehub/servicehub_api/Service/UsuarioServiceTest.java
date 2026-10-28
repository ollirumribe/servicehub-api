package com.servicehub.servicehub_api.Service;

import com.servicehub.servicehub_api.dto.usuarioRequest;
import com.servicehub.servicehub_api.exception.BusinessException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private usuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private usuarioService usuarioService;

    private usuarioRequest cadastro(String nome, String email, Role role) {
        usuarioRequest dados = new usuarioRequest();
        dados.setFullName(nome);
        dados.setEmail(email);
        dados.setPassword("senha1234");
        dados.setRole(role);
        return dados;
    }

    @Test
    void criar_deveSalvarSenhaComHashERoleClientPorPadrao() {
        // Arrange
        when(usuarioRepository.existsByEmail("maria@servicehub.com")).thenReturn(false);
        when(passwordEncoder.encode("senha1234")).thenReturn("HASH_BCRYPT");
        when(usuarioRepository.save(any(usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        usuario salvo = usuarioService.criar(cadastro("Maria Silva", "  MARIA@servicehub.com ", null));

        // Assert
        assertEquals("HASH_BCRYPT", salvo.getPassword());
        assertEquals(Role.CLIENT, salvo.getRole());
        assertEquals("maria@servicehub.com", salvo.getEmail());
        verify(usuarioRepository).save(any(usuario.class));
    }

    @Test
    void criar_deveSanitizarNomeContraXss() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("HASH");
        when(usuarioRepository.save(any(usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuario salvo = usuarioService.criar(cadastro("<b>Maria</b>", "maria@servicehub.com", null));

        assertEquals("&lt;b&gt;Maria&lt;/b&gt;", salvo.getFullName());
    }

    @Test
    void criar_comEmailJaCadastrado_deveLancarBusinessException() {
        when(usuarioRepository.existsByEmail("maria@servicehub.com")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> usuarioService.criar(cadastro("Maria", "maria@servicehub.com", null)));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void criar_comoAdmin_deveLancarBusinessException() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(BusinessException.class,
                () -> usuarioService.criar(cadastro("Hacker", "hacker@servicehub.com", Role.ADMIN)));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorId_inexistente_deveLancarResourceNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void deletar_inexistente_naoDeveChamarDeleteById() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(99L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void verificarAcesso_clienteSoAcessaOsProprioDados() {
        usuario cliente = new usuario();
        cliente.setId(1L);
        cliente.setEmail("cliente@servicehub.com");
        cliente.setRole(Role.CLIENT);
        when(usuarioRepository.findByEmail("cliente@servicehub.com")).thenReturn(Optional.of(cliente));

        assertDoesNotThrow(() -> usuarioService.verificarAcesso(1L, "cliente@servicehub.com"));
        assertThrows(AccessDeniedException.class,
                () -> usuarioService.verificarAcesso(2L, "cliente@servicehub.com"));
    }
}