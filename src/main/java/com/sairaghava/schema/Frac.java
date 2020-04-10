package com.sairaghava.schema;

import javax.json.bind.annotation.JsonbPropertyOrder;
import lombok.Data;

@JsonbPropertyOrder({"severe", "critical", "fatal", "isolated"})
@Data
public class Frac {
  private Severe severe;
  private Critical critical;
  private Fatal fatal;
  private Isolated isolated;
}
