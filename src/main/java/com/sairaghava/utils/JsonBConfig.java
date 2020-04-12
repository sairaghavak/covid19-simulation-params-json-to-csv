package com.sairaghava.utils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

public class JsonBConfig {
  private static final JsonbConfig JSONB_CONFIG = new JsonbConfig().withFormatting(Boolean.TRUE);
  private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

  public static Jsonb getJsonb() {
    return JSONB;
  }
}
