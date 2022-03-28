package com.bol.mancala.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.models.MancalaBoardDto;
import com.bol.mancala.models.SelectedPitDto;
import com.bol.mancala.repository.MancalaRepository;
import com.bol.mancala.service.MancalaService;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class MancalaIntegrationTest {

  @Autowired public MockMvc mockMvc;
  @Autowired private MancalaService mancalaService;
  @Autowired private MancalaRepository mancalaRepository;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {
    mancalaRepository.deleteAll();
  }

  @Test
  void createGame() throws Exception {
    MancalaBoardDto dto = new MancalaBoardDto();
    dto.setFirstPlayerName("Hamid");
    dto.setSecondPlayerName("Bob");

    MvcResult mockMvcResult =
        this.mockMvc
            .perform(
                post("/game")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new Gson().toJson(dto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

    String response = mockMvcResult.getResponse().getContentAsString();
    MancalaBoard actual = new Gson().fromJson(response, MancalaBoard.class);
    assertThat(actual.getFirstPlayerName()).isEqualTo(dto.getFirstPlayerName());
    assertThat(actual.getSecondPlayerName()).isEqualTo(dto.getSecondPlayerName());
  }

  @Test
  void canGetMancalaGame() throws Exception {
    MancalaBoardDto dto = new MancalaBoardDto();
    dto.setFirstPlayerName("Hamid");
    dto.setSecondPlayerName("Bob");

    MvcResult mockMvcPostResult =
        this.mockMvc
            .perform(
                post("/game")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new Gson().toJson(dto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

    String postResponse = mockMvcPostResult.getResponse().getContentAsString();
    MancalaBoard savedMancalaGame = new Gson().fromJson(postResponse, MancalaBoard.class);

    MvcResult mockMvcGetResult =
        this.mockMvc
            .perform(
                get(String.format("/game/%s", savedMancalaGame.getUuid()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String getResponse = mockMvcGetResult.getResponse().getContentAsString();
    MancalaBoard actual = new Gson().fromJson(getResponse, MancalaBoard.class);
    assertThat(actual.getFirstPlayerName()).isEqualTo(dto.getFirstPlayerName());
    assertThat(actual.getSecondPlayerName()).isEqualTo(dto.getSecondPlayerName());
    assertThat(actual.getUuid()).isEqualTo(savedMancalaGame.getUuid());
  }

  @Test
  void createNewGameAndSow() throws Exception {
    MancalaBoardDto dto = new MancalaBoardDto();
    dto.setFirstPlayerName("Hamid");
    dto.setSecondPlayerName("Bob");

    MvcResult mockMvcPostResult =
        this.mockMvc
            .perform(
                post("/game")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new Gson().toJson(dto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

    String postResponse = mockMvcPostResult.getResponse().getContentAsString();
    MancalaBoard savedMancalaGame = new Gson().fromJson(postResponse, MancalaBoard.class);

    int[] expectedPitsArray = {6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0};
    SelectedPitDto selectedPit = new SelectedPitDto();
    selectedPit.setSelectedPit(1);
    MvcResult mockMvcPutResult =
        this.mockMvc
            .perform(
                put(String.format("/game/%s", savedMancalaGame.getUuid()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new Gson().toJson(selectedPit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String putResponse = mockMvcPutResult.getResponse().getContentAsString();
    MancalaBoard actual = new Gson().fromJson(putResponse, MancalaBoard.class);
    assertThat(actual.getFirstPlayerName()).isEqualTo(dto.getFirstPlayerName());
    assertThat(actual.getSecondPlayerName()).isEqualTo(dto.getSecondPlayerName());
    assertThat(actual.getUuid()).isEqualTo(savedMancalaGame.getUuid());
    assertThat(actual.getPits()).isEqualTo(expectedPitsArray);
  }
}
