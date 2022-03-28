package com.bol.mancala.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.models.MancalaBoardDto;
import com.bol.mancala.service.MancalaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MancalaControllerTest {
  private MancalaController underTest;
  @Mock private MancalaService mancalaService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new MancalaController(mancalaService);
  }

  @Test
  void createGame() {
    // Given
    MancalaBoardDto mancalaBoardDto = new MancalaBoardDto();
    mancalaBoardDto.setFirstPlayerName("first");
    mancalaBoardDto.setSecondPlayerName("second");

    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setFirstPlayerName(mancalaBoardDto.getFirstPlayerName());
    mancalaBoard.setSecondPlayerName(mancalaBoardDto.getSecondPlayerName());

    given(mancalaService.createMancalaGame(mancalaBoardDto)).willReturn(mancalaBoard);

    // When
    ResponseEntity<MancalaBoard> actual = underTest.createGame(mancalaBoardDto);

    // Then
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getBody().getFirstPlayerName()).isEqualTo(mancalaBoard.getFirstPlayerName());
  }

  @Test
  void canFindGame() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    given(mancalaService.findGameByUUID(anyString())).willReturn(mancalaBoard);

    // When
    ResponseEntity<MancalaBoard> actual = underTest.getGame(mancalaBoard.getUuid());

    // Then
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual.getBody().getUuid()).isEqualTo(mancalaBoard.getUuid());
  }

  @Test
  void updateGame() {}
}
