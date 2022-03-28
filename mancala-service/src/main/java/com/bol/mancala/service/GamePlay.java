package com.bol.mancala.service;

import static com.bol.mancala.util.GameConfigConstantValues.*;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.models.MancalaBoardDto.StatusEnum;
import org.springframework.stereotype.Component;

@Component
public class GamePlay {

  /**
   * This method will start the game with selected pit
   *
   * @param board
   * @param selectedPit
   * @return The index of last pit
   */
  public int sowBoardWithSelectedPit(MancalaBoard board, int selectedPit) {
    int pitIndex;
    int lastMetPitIndex = 0;
    int[] pits = board.getPits();
    int stonesToSplit = pits[selectedPit];
    pits[selectedPit] = 0;
    for (pitIndex = (selectedPit + 1) % NUMBER_OF_ALL_PITS_AND_STORES;
        stonesToSplit > 0;
        pitIndex = (pitIndex + 1) % NUMBER_OF_ALL_PITS_AND_STORES) {
      if ((board.getPlayerTurn() == FIRST_PLAYER && pitIndex == PLAYER2_STORE_INDEX)
          || (board.getPlayerTurn() == SECOND_PLAYER && pitIndex == PLAYER1_STORE_INDEX)) {
        continue;
      } else {
        pits[pitIndex]++;
        stonesToSplit--;
      }
      lastMetPitIndex = pitIndex;
    }
    board.setPits(pits);

    return lastMetPitIndex;
  }

  public MancalaBoard applyCaptureStonesRule(MancalaBoard board, int lastMetPitIndex) {
    int[] pits = board.getPits();
    if (pits[lastMetPitIndex] == 1) {
      int mirrorIndex = Math.abs(NUMBER_OF_PITS - lastMetPitIndex);
      int opponentsPit = pits[mirrorIndex];
      if (opponentsPit != 0) {
        int sumBothPits = opponentsPit + pits[lastMetPitIndex];
        if (board.getPlayerTurn() == FIRST_PLAYER && isPitIndexForPlayer1(lastMetPitIndex)) {
          pits[lastMetPitIndex] = 0;
          pits[mirrorIndex] = 0;
          pits[PLAYER1_STORE_INDEX] += sumBothPits;
        } else if (board.getPlayerTurn() == SECOND_PLAYER
            && isPitIndexForPlayer2(lastMetPitIndex)) {
          pits[lastMetPitIndex] = 0;
          pits[mirrorIndex] = 0;
          pits[PLAYER2_STORE_INDEX] += sumBothPits;
        }
        board.setPits(pits);
      }
    }
    return board;
  }

  public boolean isPitIndexForPlayer1(int index) {
    return index >= 0 && index <= 5;
  }

  public boolean isPitIndexForPlayer2(int index) {
    return index >= 7 && index <= 12;
  }

  public boolean isSelectedPitCorrect(int selectedPitIndex, int player) {
    if (player == FIRST_PLAYER) {
      return isPitIndexForPlayer1(selectedPitIndex);
    } else {
      return isPitIndexForPlayer2(selectedPitIndex);
    }
  }

  public boolean isSelectedPitEmpty(int selectedPitIndex, MancalaBoard board) {
    return board.getPits()[selectedPitIndex] == 0;
  }

  public boolean isGameFinished(MancalaBoard board) {
    int[] pits = board.getPits();
    boolean isPlayer1Done = true;
    boolean isPlayer2Done = true;
    for (int i = 0; i < NUMBER_OF_ALL_PITS_AND_STORES; i++) {
      if (isPitIndexForPlayer1(i) && pits[i] != 0) {
        isPlayer1Done = false;
      }
      if (isPitIndexForPlayer2(i) && pits[i] != 0) {
        isPlayer2Done = false;
      }
    }
    return isPlayer1Done || isPlayer2Done;
  }

  public MancalaBoard wrapGame(MancalaBoard board) {
    int[] pits = board.getPits();
    for (int i = 0; i < NUMBER_OF_ALL_PITS_AND_STORES; i++) {
      if (isPitIndexForPlayer1(i) && pits[i] != 0) {
        pits[PLAYER1_STORE_INDEX] += pits[i];
        pits[i] = 0;
      }
      if (isPitIndexForPlayer2(i) && pits[i] != 0) {
        pits[PLAYER2_STORE_INDEX] += pits[i];
        pits[i] = 0;
      }
    }
    board.setPits(pits);
    return board;
  }

  public StatusEnum result(MancalaBoard board) {
    int[] pits = board.getPits();
    if (pits[PLAYER1_STORE_INDEX] > pits[PLAYER2_STORE_INDEX]) {
      return StatusEnum.PLAYER1_WON;
    } else if (pits[PLAYER1_STORE_INDEX] < pits[PLAYER2_STORE_INDEX]) {
      return StatusEnum.PLAYER2_WON;
    } else {
      return StatusEnum.TIE;
    }
  }

  public int changePlayerTurn(int playerTurn) {
    return playerTurn % 2 + 1;
  }
}
