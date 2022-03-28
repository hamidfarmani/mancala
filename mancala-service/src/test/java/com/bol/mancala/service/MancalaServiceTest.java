package com.bol.mancala.service;

import static com.bol.mancala.util.PitsConverter.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.exception.EmptyPitSelectedException;
import com.bol.mancala.exception.GameNotFoundException;
import com.bol.mancala.exception.WrongPitSelectedException;
import com.bol.mancala.models.MancalaBoardDto;
import com.bol.mancala.models.SelectedPitDto;
import com.bol.mancala.repository.MancalaRepository;
import com.bol.mancala.util.PitsConverter;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

class MancalaServiceTest {

  @Mock private GamePlay gamePlay;
  @Mock private MancalaRepository mancalaRepository;
  private MancalaService underTest;
  private static MockedStatic<PitsConverter> mockConverter;

  @BeforeAll
  public static void init() {
    mockConverter = mockStatic(PitsConverter.class);
  }

  @AfterAll
  public static void close() {
    mockConverter.close();
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new MancalaService(mancalaRepository, gamePlay);
  }

  @Test
  void createMancalaGame() {
    // Given
    MancalaBoardDto mancalaBoardDto = new MancalaBoardDto();
    mancalaBoardDto.setFirstPlayerName("first");
    mancalaBoardDto.setSecondPlayerName("second");

    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setFirstPlayerName(mancalaBoardDto.getFirstPlayerName());
    mancalaBoard.setSecondPlayerName(mancalaBoardDto.getSecondPlayerName());
    given(mancalaRepository.save(any())).willReturn(mancalaBoard);

    // When
    MancalaBoard actual = underTest.createMancalaGame(mancalaBoardDto);

    // Then
    assertThat(actual.getUuid()).isEqualTo(mancalaBoard.getUuid());
    assertThat(actual.getFirstPlayerName()).isEqualTo(mancalaBoard.getFirstPlayerName());
    assertThat(actual.getSecondPlayerName()).isEqualTo(mancalaBoard.getSecondPlayerName());
  }

  @Test
  void canFindGameByUUID() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setUuid(UUID.randomUUID().toString());
    mancalaBoard.setFirstPlayerName("Hamid");
    mancalaBoard.setSecondPlayerName("Bob");
    int[] initArray = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    String pits = "6,6,6,6,6,6,0,6,6,6,6,6,6,0";

    given(mancalaRepository.findById(anyString())).willReturn(Optional.of(mancalaBoard));
    mockConverter.when(() -> PitsConverter.convertToIntArray(pits)).thenReturn(initArray);

    // When
    MancalaBoard actual = underTest.findGameByUUID(anyString());

    // Then
    assertThat(actual.getUuid()).isEqualTo(mancalaBoard.getUuid());
  }

  @Test
  void canSow() {
    // Given
    SelectedPitDto selectedPitDto = new SelectedPitDto();
    selectedPitDto.setSelectedPit(1);
    MancalaBoard mancalaBoard = new MancalaBoard();

    given(mancalaRepository.findById(anyString())).willReturn(Optional.of(mancalaBoard));
    given(
            gamePlay.isSelectedPitCorrect(
                selectedPitDto.getSelectedPit(), mancalaBoard.getPlayerTurn()))
        .willReturn(true);
    given(gamePlay.sowBoardWithSelectedPit(mancalaBoard, selectedPitDto.getSelectedPit()))
        .willReturn(6);
    given(gamePlay.applyCaptureStonesRule(mancalaBoard, 6)).willReturn(mancalaBoard);
    given(gamePlay.changePlayerTurn(mancalaBoard.getPlayerTurn())).willReturn(1);
    given(gamePlay.isGameFinished(mancalaBoard)).willReturn(false);
    final int[] pits = mancalaBoard.getPits();
    mockConverter
        .when(() -> PitsConverter.convertToString(pits))
        .thenReturn(mancalaBoard.getPitsString());
    given(mancalaRepository.save(any())).willReturn(mancalaBoard);

    // When
    MancalaBoard actual = underTest.sow(selectedPitDto, mancalaBoard.getUuid());

    // Then
    assertThat(actual.getPlayerTurn()).isEqualTo(mancalaBoard.getPlayerTurn());
    assertThat(actual.getPits()).isEqualTo(mancalaBoard.getPits());
  }

  @Test
  void sowShouldThrowExceptionWhenGameNotFound() {
    // Given
    SelectedPitDto selectedPitDto = new SelectedPitDto();
    selectedPitDto.setSelectedPit(1);
    MancalaBoard mancalaBoard = new MancalaBoard();

    // When
    // Then
    assertThatThrownBy(() -> underTest.sow(selectedPitDto, mancalaBoard.getUuid()))
        .isInstanceOf(GameNotFoundException.class);
  }

  @Test
  void sowShouldThrowExceptionWhenWrongPitSelected() {
    // Given
    SelectedPitDto selectedPitDto = new SelectedPitDto();
    selectedPitDto.setSelectedPit(9);
    MancalaBoard mancalaBoard = new MancalaBoard();

    // When
    given(gamePlay.isSelectedPitCorrect(selectedPitDto.getSelectedPit(), 1)).willReturn(false);
    given(mancalaRepository.findById(anyString())).willReturn(Optional.of(mancalaBoard));

    // Then
    assertThatThrownBy(() -> underTest.sow(selectedPitDto, mancalaBoard.getUuid()))
        .isInstanceOf(WrongPitSelectedException.class)
        .hasMessageContaining(
            String.format("Pit #%s is not your pit", selectedPitDto.getSelectedPit()));
  }

  @Test
  void sowShouldThrowExceptionWhenEmptyPitSelected() {
    // Given
    SelectedPitDto selectedPitDto = new SelectedPitDto();
    selectedPitDto.setSelectedPit(0);
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);

    // When
    given(gamePlay.isSelectedPitCorrect(selectedPitDto.getSelectedPit(), 1)).willReturn(true);
    given(gamePlay.isSelectedPitEmpty(selectedPitDto.getSelectedPit(), mancalaBoard))
        .willReturn(true);
    given(mancalaRepository.findById(anyString())).willReturn(Optional.of(mancalaBoard));

    // Then
    assertThatThrownBy(() -> underTest.sow(selectedPitDto, mancalaBoard.getUuid()))
        .isInstanceOf(EmptyPitSelectedException.class)
        .hasMessageContaining(String.format("Pit #%s is empty.", selectedPitDto.getSelectedPit()));
  }
}
