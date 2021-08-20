package ish.securit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ish.securit.services.interfaces.PasswordStrengthService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@ExtendWith(SpringExtension.class)
@WebMvcTest(SafeboxApiController.class)
class SafeboxApiControllerUpdateContentEndpointTest extends SecurityHelper {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SafeboxService safeboxService;

    @MockBean
    private PasswordStrengthService passwordStrengthService;


    @Test
    @DisplayName("Specified Basic Auth does not match")
    public void shouldReturnNotAuthorizedWithInvalidCredentials() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("New safebox content");

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
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
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("New safebox content");

        // when
        when(safeboxService.exists(id)).thenReturn(false);

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Requested safebox does not exist")));
    }

    @Test
    @DisplayName("Malformed expected data when Items is not valid")
    public void shouldReturn422WhenItemsIsNull() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .items(null);

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Malformed expected data")));
    }

    @Test
    @DisplayName("Malformed expected data when ID is not valid")
    public void shouldReturn422WhenIdIsNotAValidFormat() throws Exception {
        // Given
        var id = "1234abc";
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("New safebox content");

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(httpBasic(NAME, PASSWORD)))
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
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 03")
                .addItemsItem("New safebox content");

        // when
        when(safeboxService.exists(id)).thenThrow(new RuntimeException("Generic server error"));

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(httpBasic(NAME, PASSWORD)))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", Is.is("Unexpected API error")));
    }

    @Test
    @DisplayName("Content correctly added to the safebox")
    public void shouldReturnOkAndSafeboxContentsWithValidCredentialsAndId() throws Exception {
        // Given
        var id = UUID.randomUUID().toString();
        var requestBody = new ish.securit.openapi.model.InlineObject1()
                .addItemsItem("Safebox content 03")
                .addItemsItem("Safebox content 02")
                .addItemsItem("Safebox content 01")
                .addItemsItem("New safebox content");

        // When
        when(safeboxService.exists(id)).thenReturn(true);

        // Expect
        mockMvc.perform(put("/safebox/" + id + "/items")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .with(httpBasic(NAME, PASSWORD))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}