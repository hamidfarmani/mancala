package com.bol.mancala.dao;

import static com.bol.mancala.models.MancalaBoardDto.StatusEnum.ONGOING;
import static com.bol.mancala.util.GameConfigConstantValues.*;
import static com.bol.mancala.util.PitsConverter.convertToString;

import com.bol.mancala.models.MancalaBoardDto.StatusEnum;
import java.util.Arrays;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class MancalaBoard {

  @Id private String uuid;

  private String firstPlayerName;
  private String secondPlayerName;
  private Integer playerTurn;
  private String pitsString;

  @Enumerated(EnumType.STRING)
  private StatusEnum statusEnum;

  @Transient private int[] pits;

  public MancalaBoard() {
    uuid = UUID.randomUUID().toString();
    pits = new int[NUMBER_OF_ALL_PITS_AND_STORES];
    Arrays.fill(pits, STONES_INIT_COUNT);
    pits[PLAYER1_STORE_INDEX] = 0;
    pits[PLAYER2_STORE_INDEX] = 0;
    playerTurn = FIRST_PLAYER;
    statusEnum = ONGOING;
    pitsString = convertToString(pits);
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public int[] getPits() {
    return pits;
  }

  public void setPits(int[] pits) {
    this.pits = pits;
  }

  public String getFirstPlayerName() {
    return firstPlayerName;
  }

  public void setFirstPlayerName(String firstPlayerName) {
    this.firstPlayerName = firstPlayerName;
  }

  public String getSecondPlayerName() {
    return secondPlayerName;
  }

  public void setSecondPlayerName(String secondPlayerName) {
    this.secondPlayerName = secondPlayerName;
  }

  public Integer getPlayerTurn() {
    return playerTurn;
  }

  public void setPlayerTurn(Integer playerTurn) {
    this.playerTurn = playerTurn;
  }

  public StatusEnum getStatusEnum() {
    return statusEnum;
  }

  public void setStatusEnum(StatusEnum statusEnum) {
    this.statusEnum = statusEnum;
  }

  public String getPitsString() {
    return pitsString;
  }

  public void setPitsString(String pitsString) {
    this.pitsString = pitsString;
  }

  @Override
  public String toString() {
    return "MancalaBoard{"
        + "uuid='"
        + uuid
        + '\''
        + ", firstPlayerName='"
        + firstPlayerName
        + '\''
        + ", secondPlayerName='"
        + secondPlayerName
        + '\''
        + ", playerTurn="
        + playerTurn
        + ", pitsString='"
        + pitsString
        + '\''
        + ", statusEnum="
        + statusEnum
        + ", pits="
        + Arrays.toString(pits)
        + '}';
  }
}
