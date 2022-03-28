package com.bol.mancala.service;

import static com.bol.mancala.models.MancalaBoardDto.StatusEnum.PLAYER1_WON;
import static com.bol.mancala.models.MancalaBoardDto.StatusEnum.PLAYER2_WON;
import static com.bol.mancala.models.MancalaBoardDto.StatusEnum.TIE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.models.MancalaBoardDto.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GamePlayTest {

  private GamePlay underTest;

  @BeforeEach
  void setUp() {
    underTest = new GamePlay();
  }

  @Test
  void sowBoardWithSelectedPitForFirstPlayer() {
    // Given
    int selectedPit = 0;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int expected = 6;

    // When
    int actual = underTest.sowBoardWithSelectedPit(mancalaBoard, selectedPit);

    // Then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void sowBoardWithSelectedPitForFirstPlayerJumpingSecondPlayerStore() {
    // Given
    int selectedPit = 5;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {6, 6, 6, 6, 6, 10, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);
    int expected = 2;

    // When
    int actual = underTest.sowBoardWithSelectedPit(mancalaBoard, selectedPit);

    // Then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void sowBoardWithSelectedPitForSecondPlayer() {
    // Given
    int selectedPit = 7;
    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setPlayerTurn(2);
    int expected = 13;

    // When
    int actual = underTest.sowBoardWithSelectedPit(mancalaBoard, selectedPit);

    // Then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void sowBoardWithSelectedPitForSecondPlayerJumpingFirstPlayerStore() {
    // Given
    int selectedPit = 12;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {6, 6, 6, 6, 6, 10, 0, 6, 6, 6, 6, 6, 10, 0};
    mancalaBoard.setPits(initArray);
    mancalaBoard.setPlayerTurn(2);
    int expected = 9;

    // When
    int actual = underTest.sowBoardWithSelectedPit(mancalaBoard, selectedPit);

    // Then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void applyCaptureStonesRuleFirstPlayer() {
    // Given
    int lastMetPitIndex = 12;
    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setPlayerTurn(2);
    int[] initArray = {6, 6, 6, 1, 0, 1, 0, 6, 6, 6, 6, 6, 1, 0};
    mancalaBoard.setPits(initArray);
    int[] capturedStonesAppliedPits = {0, 6, 6, 1, 0, 1, 0, 6, 6, 6, 6, 6, 0, 7};

    // When
    MancalaBoard actual = underTest.applyCaptureStonesRule(mancalaBoard, lastMetPitIndex);

    // Then
    assertThat(actual.getPits()).isEqualTo(capturedStonesAppliedPits);
  }

  @Test
  void applyCaptureStonesRuleSecondPlayer() {
    // Given
    int lastMetPitIndex = 3;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {6, 6, 6, 1, 0, 1, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);
    int[] capturedStonesAppliedPits = {6, 6, 6, 0, 0, 1, 7, 6, 6, 0, 6, 6, 6, 0};

    // When
    MancalaBoard actual = underTest.applyCaptureStonesRule(mancalaBoard, lastMetPitIndex);

    // Then
    assertThat(actual.getPits()).isEqualTo(capturedStonesAppliedPits);
  }

  @Test
  void applyCaptureStonesRuleNotMet() {
    // Given
    int lastMetPitIndex = 3;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {6, 6, 6, 2, 0, 1, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);
    int[] capturedStonesAppliedPits = {6, 6, 6, 2, 0, 1, 0, 6, 6, 6, 6, 6, 6, 0};

    // When
    MancalaBoard actual = underTest.applyCaptureStonesRule(mancalaBoard, lastMetPitIndex);

    // Then
    assertThat(actual.getPits()).isEqualTo(capturedStonesAppliedPits);
  }

  @Test
  void isPitIndexForPlayer1Correct() {
    // Given
    int selectedPit = 0;

    // When
    boolean actual = underTest.isPitIndexForPlayer1(selectedPit);

    // Then
    assertThat(actual).isTrue();
  }

  @Test
  void isPitIndexForPlayer1InCorrect() {
    // Given
    int selectedPit = 10;

    // When
    boolean actual = underTest.isPitIndexForPlayer1(selectedPit);

    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isPitIndexForPlayer2Correct() {
    // Given
    int selectedPit = 0;

    // When
    boolean actual = underTest.isPitIndexForPlayer2(selectedPit);

    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isPitIndexForPlayer2InCorrect() {
    // Given
    int selectedPit = 10;

    // When
    boolean actual = underTest.isPitIndexForPlayer2(selectedPit);

    // Then
    assertThat(actual).isTrue();
  }

  @Test
  void isSelectedPitCorrect() {
    // Given
    int playerTurn = 1;
    int selectedPit = 0;

    // When
    boolean actual = underTest.isSelectedPitCorrect(selectedPit, playerTurn);

    // Then
    assertThat(actual).isTrue();
  }

  @Test
  void isSelectedPitInCorrect() {
    // Given
    int playerTurn = 2;
    int selectedPit = 0;

    // When
    boolean actual = underTest.isSelectedPitCorrect(selectedPit, playerTurn);

    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isSelectedPitEmpty() {
    // Given
    int selectedPit = 0;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);

    // When
    boolean actual = underTest.isSelectedPitEmpty(selectedPit, mancalaBoard);

    // Then
    assertThat(actual).isTrue();
  }

  @Test
  void isSelectedPitNotEmpty() {
    // Given
    int selectedPit = 0;
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);

    // When
    boolean actual = underTest.isSelectedPitEmpty(selectedPit, mancalaBoard);

    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isGameFinishedWhenJustCreated() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    // When
    boolean actual = underTest.isGameFinished(mancalaBoard);
    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isGameFinishedWhenOngoing() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 0, 0, 10, 0, 0, 10, 0, 0, 1, 0, 0, 0, 20};
    mancalaBoard.setPits(initArray);
    // When
    boolean actual = underTest.isGameFinished(mancalaBoard);
    // Then
    assertThat(actual).isFalse();
  }

  @Test
  void isGameFinishedWhenFinished() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 0, 0, 10, 0, 0, 10, 0, 0, 0, 0, 0, 0, 20};
    mancalaBoard.setPits(initArray);

    // When
    boolean actual = underTest.isGameFinished(mancalaBoard);
    // Then
    assertThat(actual).isTrue();
  }

  @Test
  void wrapGame() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {1, 2, 3, 1, 1, 2, 0, 11, 2, 5, 1, 0, 1, 0};
    mancalaBoard.setPits(initArray);
    int[] wrappedGame = {0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 20};

    // When
    MancalaBoard actual = underTest.wrapGame(mancalaBoard);

    // Then
    assertThat(actual.getPits()).isEqualTo(wrappedGame);
  }

  @Test
  void whenResultIsTie() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);

    // When
    StatusEnum actual = underTest.result(mancalaBoard);

    // Then
    assertThat(actual).isEqualTo(TIE);
  }

  @Test
  void whenPlayerOneWon() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 6, 6, 6, 6, 6, 1, 6, 6, 6, 6, 6, 6, 0};
    mancalaBoard.setPits(initArray);

    // When
    StatusEnum actual = underTest.result(mancalaBoard);

    // Then
    assertThat(actual).isEqualTo(PLAYER1_WON);
  }

  @Test
  void whenPlayerTwoWon() {
    // Given
    MancalaBoard mancalaBoard = new MancalaBoard();
    int[] initArray = {0, 6, 6, 6, 6, 6, 1, 6, 6, 6, 6, 6, 6, 2};
    mancalaBoard.setPits(initArray);

    // When
    StatusEnum actual = underTest.result(mancalaBoard);

    // Then
    assertThat(actual).isEqualTo(PLAYER2_WON);
  }

  @Test
  void changePlayerTurnFromFirstPlayer() {
    // Given
    int firstPlayer = 1;
    int secondPlayer = 2;
    // When
    int actual = underTest.changePlayerTurn(firstPlayer);
    // Then
    assertThat(actual).isEqualTo(secondPlayer);
  }

  @Test
  void changePlayerTurnFromSecondPlayer() {
    // Given
    int firstPlayer = 1;
    int secondPlayer = 2;
    // When
    int actual = underTest.changePlayerTurn(secondPlayer);
    // Then
    assertThat(actual).isEqualTo(firstPlayer);
  }
}
