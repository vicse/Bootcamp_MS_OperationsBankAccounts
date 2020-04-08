package com.vos.bootcamp.msoperationsbankaccounts.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Constant {

  public static final String DEPOSITO = "Deposito";
  public static final String RETIRO = "Retiro";
  public static final String TRANSFERENCIA = "Transferencia";

  public static final int NUMBER_MAX_TRANSACTIONS = 2;

  public static final Double COMMISSION_AMOUNT = 50.0;

  public static Date TODAY;

  static {
    try {
      TODAY = formatDate(new Date());
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }


  public static Date addDayToADate(Date date, int dias){
    if (dias==0) return date;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_YEAR, dias);
    return calendar.getTime();
  }

  public static Date formatDate(Date date) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = dateFormat.format(date);

    return dateFormat.parse(dateString);


  }







}
