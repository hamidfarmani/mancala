package com.bol.mancala.util;

import java.util.Arrays;

public class PitsConverter {

  public static String convertToString(int[] data) {
    return Arrays.stream(data)
        .mapToObj(String::valueOf)
        .reduce((a, b) -> a.concat(",").concat(b))
        .get();
  }

  public static int[] convertToIntArray(String data) {
    return Arrays.stream(data.split(",")).mapToInt(Integer::parseInt).toArray();
  }
}
