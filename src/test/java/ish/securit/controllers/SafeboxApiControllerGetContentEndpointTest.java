package ish.securit.controllers;

import ish.securit.dtos.SafeboxContent;
import ish.securit.services.interfaces.PasswordStrengthService;
import ish.securit.services.interfaces.SafeboxService;
import ish.securit.utils.SecurityHelper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@ExtendWith(SpringExtension.class)
@WebMvcTest(SafeboxApiController.class)
class SafeboxApiControllerGetContentEndpointTest extends SecurityHelper {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SafeboxService safeboxService;

    @MockBean
    private PasswordStrengthService passwordStrengthService;


    @BeforeEach
    public void setup() {
        when(this.userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(NAME, this.passwordEncoder.encode(PASSWORD), new ArrayList<>()));
    }

    @Test
    @DisplayName("Specified Basic Auth does not match")
    public void shouldReturnNotAuthorizedWithInvalidCredentials() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();

        // Expect
        mockMvc.perform(get("/safebox/" + id + "/items"))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Specified Basic Auth does not match")));
    }

    @Test
    @DisplayName("Requested safebox does not exist")
    public void shouldReturn404IfIdDoesNotExist() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();

        // when
        when(safeboxService.exists(id)).thenReturn(false);

        // Expect
        mockMvc.perform(get("/safebox/" + id + "/items").with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Requested safebox does not exist")));
    }

    @Test
    @DisplayName("Malformed expected data")
    public void shouldReturnMalformedException() throws Exception {
        // Given
        var id = "1234abc";

        // Expect
        mockMvc.perform(get("/safebox/" + id + "/items").with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Malformed expected data")));
    }

    @Test
    @DisplayName("Unexpected API error")
    public void shouldReturnAnyServerError() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();

        // when
        when(safeboxService.exists(id)).thenThrow(new RuntimeException("Generic server error"));

        // Expect
        mockMvc.perform(get("/safebox/" + id + "/items")
                        .with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Unexpected API error")));
    }

    @Test
    @DisplayName("Safebox contents correctly retrieved")
    public void shouldReturnOkAndSafeboxContentsWithValidCredentialsAndId() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();
        var response = List.of(
                SafeboxContent.builder().content("Safebox content 03").build(),
                SafeboxContent.builder().content("Safebox content 02").build(),
                SafeboxContent.builder().content("Safebox content 01").build()
        );

        // When
        when(safeboxService.exists(id)).thenReturn(true);
        when(safeboxService.getSafeboxContents(id)).thenReturn(response);

        // Expect
        mockMvc.perform(get("/safebox/" + id + "/items")
                        .with(httpBasic(NAME, PASSWORD))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items[0]", Is.is("Safebox content 03")))
                .andExpect(jsonPath("$.items[1]", Is.is("Safebox content 02")))
                .andExpect(jsonPath("$.items[2]", Is.is("Safebox content 01")));
    }
}