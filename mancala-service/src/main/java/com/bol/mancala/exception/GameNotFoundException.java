package com.bol.mancala.exception;

public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String uuid) {
    super(String.format("No game board with id %s", uuid));
  }
}
