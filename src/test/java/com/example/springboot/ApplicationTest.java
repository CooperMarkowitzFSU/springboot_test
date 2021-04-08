package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    //recitation tests (LOOK AT LAST TWO FOR ASSIGNMENT)
    @Test
    void testHistory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=testing")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("testing")));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=stringToDelete")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=stringToDelete").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("does not exist")));
    }

    @Test
    void testSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=toBeSearched")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=notToBeSearched")).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("toBeSearched")));

        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("notToBeSearched")));

        mockMvc.perform(MockMvcRequestBuilders.get("/search?search_text=toBeSearched").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("toBeSearched")));
        mockMvc.perform(MockMvcRequestBuilders.get("/search?search_text=toBeSearched").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("toBeSearched"))));

    }

    //Assignment Tests
    @Test
    void cantBeFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=testing")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=testing")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("testing"))));
        /*posts testing string, deletes testing string and then since history page would not show testing,
        the third line tests negation of containsString method*/
    }

    @Test
    void isCaseSensitive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=testing")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=Testing").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("does not exist")));
        /*tries to delete Testing with uppercase T instead of lower like input post did. If does not exist is found on response
        then the delete function WOULD BE case sensitive*/
    }
}