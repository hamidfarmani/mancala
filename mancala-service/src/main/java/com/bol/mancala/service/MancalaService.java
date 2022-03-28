package com.bol.mancala.service;

import static com.bol.mancala.models.MancalaBoardDto.StatusEnum.ONGOING;
import static com.bol.mancala.util.GameConfigConstantValues.PLAYER1_STORE_INDEX;
import static com.bol.mancala.util.GameConfigConstantValues.PLAYER2_STORE_INDEX;
import static com.bol.mancala.util.PitsConverter.*;
import static com.bol.mancala.util.PitsConverter.convertToIntArray;

import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.exception.EmptyPitSelectedException;
import com.bol.mancala.exception.GameNotFoundException;
import com.bol.mancala.exception.WrongPitSelectedException;
import com.bol.mancala.models.MancalaBoardDto;
import com.bol.mancala.models.MancalaBoardDto.StatusEnum;
import com.bol.mancala.models.SelectedPitDto;
import com.bol.mancala.repository.MancalaRepository;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MancalaService {

  private final Logger LOGGER = LogManager.getLogger(MancalaService.class);
  private final GamePlay gamePlay;
  private final MancalaRepository mancalaRepository;

  public MancalaService(MancalaRepository mancalaRepository, GamePlay gamePlay) {
    this.mancalaRepository = mancalaRepository;
    this.gamePlay = gamePlay;
  }

  public MancalaBoard createMancalaGame(MancalaBoardDto mancalaDto) {
    MancalaBoard mancalaBoard = new MancalaBoard();
    mancalaBoard.setFirstPlayerName(mancalaDto.getFirstPlayerName());
    mancalaBoard.setSecondPlayerName(mancalaDto.getSecondPlayerName());
    mancalaBoard.setStatusEnum(ONGOING);
    mancalaBoard.setPitsString(convertToString(mancalaBoard.getPits()));
    mancalaBoard = mancalaRepository.save(mancalaBoard);
    LOGGER.info("Mancala '{}' created.", mancalaBoard.getUuid());
    return mancalaBoard;
  }

  public MancalaBoard findGameByUUID(String uuid) {
    MancalaBoard board =
        mancalaRepository.findById(uuid).orElseThrow(() -> new GameNotFoundException(uuid));
    board.setPits(convertToIntArray(board.getPitsString()));
    LOGGER.info("Found game by uuid of: {}", board.getUuid());
    return board;
  }

  public MancalaBoard sow(SelectedPitDto selectedPitDto, String boardUUID) {
    MancalaBoard mancalaBoard = findGameByUUID(boardUUID);
    int selectedPit = selectedPitDto.getSelectedPit();
    Integer playerTurn = mancalaBoard.getPlayerTurn();
    if (!gamePlay.isSelectedPitCorrect(selectedPit, playerTurn)) {
      throw new WrongPitSelectedException(selectedPit);
    }
    if (gamePlay.isSelectedPitEmpty(selectedPit, mancalaBoard)) {
      throw new EmptyPitSelectedException(selectedPit);
    }
    LOGGER.info(
        "Current status: {}, Selected pit: {}",
        Arrays.toString(mancalaBoard.getPits()),
        selectedPit);
    int lastPitIndex = gamePlay.sowBoardWithSelectedPit(mancalaBoard, selectedPit);
    if (lastPitIndex != PLAYER1_STORE_INDEX && lastPitIndex != PLAYER2_STORE_INDEX) {
      mancalaBoard = gamePlay.applyCaptureStonesRule(mancalaBoard, lastPitIndex);
      mancalaBoard.setPlayerTurn(gamePlay.changePlayerTurn(playerTurn));
    }
    if (gamePlay.isGameFinished(mancalaBoard)) {
      mancalaBoard = gamePlay.wrapGame(mancalaBoard);
      StatusEnum status = gamePlay.result(mancalaBoard);
      mancalaBoard.setStatusEnum(status);
    }
    mancalaBoard.setPitsString(convertToString(mancalaBoard.getPits()));
    mancalaBoard = mancalaRepository.save(mancalaBoard);
    return mancalaBoard;
  }
}
