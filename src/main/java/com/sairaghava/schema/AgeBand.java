package com.sairaghava.schema;

import javax.json.bind.annotation.JsonbProperty;
import lombok.Data;

// 0-9, 10-19 etc is already in LEXICOGRAPHICAL(A-Z) order which is default order strategy in Json-B
@Data
public class AgeBand {
  @JsonbProperty("0-9")
  private String zeroToNine;
  @JsonbProperty("10-19")
  private String tenToNinteen;
  @JsonbProperty("20-29")
  private String twentyToTwentynine;
  @JsonbProperty("30-39")
  private String thirtyToThirtyNine;
  @JsonbProperty("40-49")
  private String fourtyToFourtyNine;
  @JsonbProperty("50-59")
  private String fiftyToFiftyNine;
  @JsonbProperty("60-69")
  private String sixtyToSixtyNine;
  @JsonbProperty("70-79")
  private String seventyToSeventyNine;
  @JsonbProperty("80+")
  private String eightyPlus;
}
