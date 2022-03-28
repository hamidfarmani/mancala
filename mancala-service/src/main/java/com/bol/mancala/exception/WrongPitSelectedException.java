package com.bol.mancala.exception;

public class WrongPitSelectedException extends RuntimeException {

  public WrongPitSelectedException(Integer pitIndex) {
    super(String.format("Pit #%s is not your pit.", pitIndex));
  }
}
