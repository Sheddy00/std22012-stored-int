package com.hei.school.endpoint.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class StoredIntControllerTest {
  @Autowired private MockMvc mockMvc;

  private final File testFile = new File("/tmp/stored-int.txt");

  @BeforeEach
  void cleanup() throws Exception {
    if (testFile.exists()) {
      Files.delete(testFile.toPath());
    }
  }

  @Test
  void test_first_call_creates_file() throws Exception {
    mockMvc
        .perform(get("/stored-int"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.storedInt").isNumber());
    assert (testFile.exists());
  }

  @Test
  void test_subsequent_calls_read_file() throws Exception {
    Files.writeString(testFile.toPath(), "42");

    mockMvc
        .perform(get("/stored-int"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.storedInt", is(42)));
  }

  @Test
  void test_error_handling() throws Exception {
    Files.writeString(testFile.toPath(), "not-a-number");

    mockMvc
        .perform(get("/stored-int"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").exists());
  }
}
