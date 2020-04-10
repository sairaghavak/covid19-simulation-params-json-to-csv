package com.sairaghava.schema;

import javax.json.bind.annotation.JsonbPropertyOrder;
import lombok.Data;

@JsonbPropertyOrder({"recovery", "severe", "discharge", "critical", "stabilize", "fatality",
    "overFlowFatality"})
@Data
public class Rate {
  private Recovery recovery;
  private Severe severe;
  private Discharge discharge;
  private Critical critical;
  private Stabilize stabilize;
  private Fatality fatality;
  private OverFlowFatality overFlowFatality;
}
