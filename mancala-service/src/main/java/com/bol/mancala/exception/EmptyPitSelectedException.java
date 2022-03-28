package com.bol.mancala.exception;

public class EmptyPitSelectedException extends RuntimeException {

  public EmptyPitSelectedException(Integer pitIndex) {
    super(String.format("Pit #%s is empty.", pitIndex));
  }
}
