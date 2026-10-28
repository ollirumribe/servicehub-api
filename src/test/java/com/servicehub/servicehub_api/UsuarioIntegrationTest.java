package com.servicehub.servicehub_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String login(String email, String senha) throws Exception {
        String resposta = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", senha))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resposta).get("token").asText();
    }

    @Test
    void fluxoCompleto_cadastroLoginEControleDeAcesso() throws Exception {
        // 1. Cadastro público -> 201, sem senha na resposta
        String resposta = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "fullName", "Cliente Teste",
                                "email", "cliente.it@servicehub.com",
                                "password", "senha1234"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(resposta).get("id").asLong();

        // 2. Sem token -> 401
        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isUnauthorized());

        // 3. Login devolve o JWT
        String token = login("cliente.it@servicehub.com", "senha1234");

        // 4. Com token, o próprio usuário se consulta -> 200
        mockMvc.perform(get("/api/users/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("cliente.it@servicehub.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

        // 5. CLIENT tentando listar todos -> 403
        mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        // 6. ADMIN (criado pelo DataInitializer) lista todos -> 200
        String tokenAdmin = login("admin@servicehub.com", "Admin@123");
        mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    void login_comSenhaErrada_deveRetornar401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "admin@servicehub.com",
                                "password", "senhaErrada"))))
                .andExpect(status().isUnauthorized());
    }
}