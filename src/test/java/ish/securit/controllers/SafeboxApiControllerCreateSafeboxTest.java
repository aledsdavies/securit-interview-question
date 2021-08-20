package ish.securit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ish.securit.services.interfaces.SafeboxService;
import ish.securit.utils.SecurityHelper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@ExtendWith(SpringExtension.class)
@WebMvcTest(SafeboxApiController.class)
class SafeboxApiControllerCreateSafeboxTest extends SecurityHelper {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SafeboxService safeboxService;

    @Test
    @DisplayName("Safebox already exists")
    public void shouldReturn409IfSafeboxAlreadyExists() throws Exception {
        // Given
        var request = new ish.securit.openapi.model.InlineObject()
                .name("Secure safebox 01")
                .password("extremelySecurePassword");

        // When
        when(safeboxService.checkIfNameExists(anyString())).thenReturn(true);

        // Expect
        mockMvc.perform(post("/safebox")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Safebox already exists")));
    }

    @Test
    @DisplayName("Malformed expected data when Safebox password is null")
    public void shouldReturnMalformedExceptionWhenSafeboxPasswordIsNull() throws Exception {
        // Given
        var request = objectMapper.writeValueAsString(new ish.securit.openapi.model.InlineObject()
                .name("Secure safebox 01")
                .password(null) // I am not allowed to change the swagger configs otherwise I would be able to validate empty strings also
        );
        // Expect
        mockMvc.perform(post("/safebox")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Malformed expected data")));
    }

    @Test
    @DisplayName("Malformed expected data when Safebox name is null")
    public void shouldReturnMalformedExceptionWhenSafeboxNameIsNull() throws Exception {
        // Given
        var request = new ish.securit.openapi.model.InlineObject()
                .name(null) // I am not allowed to change the swagger configs otherwise I would be able to validate empty strings also
                .password("extremelySecurePassword");

        // Expect
        mockMvc.perform(post("/safebox")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Malformed expected data")));
    }

    @Test
    @DisplayName("Unexpected API error")
    public void shouldReturnAnyServerError() throws Exception {
        // Given
        var request = new ish.securit.openapi.model.InlineObject()
                .name("Secure safebox 01")
                .password("extremelySecurePassword");


        // When
        when(this.safeboxService.checkIfNameExists(anyString())).thenThrow(new RuntimeException("General Server Error For Testing"));

        // Expect
        mockMvc.perform(post("/safebox")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Unexpected API error")));
    }

    @Test
    @DisplayName("Safebox contents correctly retrieved")
    public void shouldReturnSafeboxIDWithValidObject() throws Exception {
        // Given
        var createdId = UUID.randomUUID().toString();
        var request = new ish.securit.openapi.model.InlineObject()
                .name("Secure safebox 01")
                .password("extremelySecurePassword");

        // When
        when(safeboxService.checkIfNameExists(anyString())).thenReturn(false);
        when(safeboxService.createSafebox(any())).thenReturn(createdId);

        // Expect
        mockMvc.perform(post("/safebox")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(createdId)));
    }
}