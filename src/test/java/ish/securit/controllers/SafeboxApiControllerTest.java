package ish.securit.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SafeboxApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Retrieves the currently stored contents in the safebox identified by the given ID")
    public void shouldReturnOkOnSafeboxItemsEndpointWithValidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/safebox/12345/items"))
                .andDo(print())
                .andExpect(status().isOk());
    }



}