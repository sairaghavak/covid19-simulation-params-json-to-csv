package com.sairaghava.utils;

import static com.sairaghava.utils.Constants.COMMA;
import static com.sairaghava.utils.Constants.NEW_LINE;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.sairaghava.config.ExternalConfiguration;
import com.sairaghava.schema.Covid19Params;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonToCsvConverter {
  private final ExternalConfiguration externalConfig;

  public String runAsWebService(Covid19Params covid19Params) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, IntrospectionException {
    StringBuilder sb = new StringBuilder();
    return generateRows(generateHeaders(sb).append(NEW_LINE), covid19Params).toString();
  }

  private StringBuilder generateHeaders(StringBuilder sb) {
    externalConfig.getCsvHeaders().stream().forEach(column -> sb.append(column + COMMA));
    sb.deleteCharAt(sb.toString().length() - 1); // delete last comma in header
    return sb;
  }

  private StringBuilder generateRows(StringBuilder sb, Covid19Params result)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      IntrospectionException {
    List<String> ageBands = externalConfig.getAgeBands();
    for (int i = 0; i < ageBands.size(); i++) {
      // col1
      sb.append(externalConfig.getAgeBandMap().get(ageBands.get(i)) + COMMA);
      // col2
      sb = getFieldValueWithComma(sb, result.getAgeDistribution(), ageBands.get(i)).append(COMMA);
      // col3
      sb = getFieldValueWithComma(sb, result.getFrac().getSevere(), ageBands.get(i)).append(COMMA);
      // col4
      sb = getFieldValueWithComma(sb, result.getFrac().getCritical(), ageBands.get(i))
          .append(COMMA);
      // col5
      sb = getFieldValueWithComma(sb, result.getFrac().getFatal(), ageBands.get(i)).append(COMMA);
      // col6
      sb = getFieldValueWithComma(sb, result.getFrac().getIsolated(), ageBands.get(i))
          .append(COMMA);
      // col7
      sb = getFieldValueWithComma(sb, result.getRate().getRecovery(), ageBands.get(i))
          .append(COMMA);
      // col8
      sb = getFieldValueWithComma(sb, result.getRate().getSevere(), ageBands.get(i)).append(COMMA);
      // col9
      sb = getFieldValueWithComma(sb, result.getRate().getDischarge(), ageBands.get(i))
          .append(COMMA);
      // col10
      sb = getFieldValueWithComma(sb, result.getRate().getCritical(), ageBands.get(i))
          .append(COMMA);
      // col11
      sb = getFieldValueWithComma(sb, result.getRate().getStabilize(), ageBands.get(i))
          .append(COMMA);
      // col12
      sb = getFieldValueWithComma(sb, result.getRate().getFatality(), ageBands.get(i))
          .append(COMMA);
      // col13
      sb = getFieldValueWithComma(sb, result.getRate().getOverFlowFatality(), ageBands.get(i));
      sb.append(NEW_LINE);
    }
    return sb;
  }

  private StringBuilder getFieldValueWithComma(StringBuilder sb, Object object, String fieldName)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      IntrospectionException {
    sb.append(getFieldValue(object, fieldName));
    return sb;
  }

  private Object getFieldValue(Object object, String fieldName) throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, IntrospectionException {
    return new PropertyDescriptor(fieldName, object.getClass()).getReadMethod().invoke(object);
  }
}
