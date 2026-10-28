package com.servicehub.servicehub_api.Controller;

import com.servicehub.servicehub_api.config.SecurityConfig;
import com.servicehub.servicehub_api.controller.usuarioController;
import com.servicehub.servicehub_api.dto.usuarioRequest;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.Security.CustomUserDetailsService;
import com.servicehub.servicehub_api.Security.JwtService;
import com.servicehub.servicehub_api.service.usuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = usuarioController.class)
@Import(SecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private usuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private usuario usuarioSalvo() {
        usuario u = new usuario();
        u.setId(1L);
        u.setFullName("Maria Silva");
        u.setEmail("maria@servicehub.com");
        u.setPassword("HASH_BCRYPT");
        u.setRole(Role.CLIENT);
        return u;
    }

    @Test
    void criar_comDadosValidos_deveRetornar201SemSenha() throws Exception {
        when(usuarioService.criar(any(usuarioRequest.class))).thenReturn(usuarioSalvo());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullName":"Maria Silva","email":"maria@servicehub.com","password":"senha1234"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("maria@servicehub.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void criar_comDadosInvalidos_deveRetornar400ComOsCampos() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullName":"","email":"abc","password":"123"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.fullName").exists())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.password").exists());

        verify(usuarioService, never()).criar(any());
    }

    @Test
    void listar_semToken_deveRetornar401() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void listar_comoClient_deveRetornar403() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_comoAdmin_deveRetornar200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(usuarioSalvo()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("maria@servicehub.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }
}