package com.bol.mancala.controller;

import com.bol.mancala.api.GameApi;
import com.bol.mancala.dao.MancalaBoard;
import com.bol.mancala.models.MancalaBoardDto;
import com.bol.mancala.models.SelectedPitDto;
import com.bol.mancala.service.MancalaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MancalaController implements GameApi {

  private final Logger LOGGER = LogManager.getLogger(MancalaController.class);

  private final MancalaService mancalaService;

  public MancalaController(MancalaService mancalaService) {
    this.mancalaService = mancalaService;
  }

  @Override
  public ResponseEntity createGame(MancalaBoardDto mancalaBoardDto) {
    MancalaBoard mancalaBoard = mancalaService.createMancalaGame(mancalaBoardDto);
    return new ResponseEntity(mancalaBoard, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity getGame(String boardUUID) {
    MancalaBoard mancalaBoard = mancalaService.findGameByUUID(boardUUID);
    return new ResponseEntity(mancalaBoard, HttpStatus.OK);
  }

  @Override
  public ResponseEntity updateGame(SelectedPitDto selectedPitDto, String boardUUID) {
    MancalaBoard mancalaBoard = mancalaService.sow(selectedPitDto, boardUUID);
    return new ResponseEntity(mancalaBoard, HttpStatus.OK);
  }
}
