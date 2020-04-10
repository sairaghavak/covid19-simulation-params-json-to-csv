package com.sairaghava.schema;

import javax.json.bind.annotation.JsonbPropertyOrder;
import lombok.Data;

@Data
@JsonbPropertyOrder({"ageDistribution", "frac", "rate"})
public class Covid19Params {
  private AgeDistribution ageDistribution;
  private Frac frac;
  private Rate rate;
}
