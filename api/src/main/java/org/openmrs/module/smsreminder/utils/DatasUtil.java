/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matse
 */
public class DatasUtil {

    private static final String FORMATO_DATA = "yyyy-MM-dd";
    private static final String _FORMATO_DATA = "yyyyMMdd";
    private static final String FORMATO_DATA_MOSTRAR = "dd-MM-yyyy HH:mm:ss";
    private static final String FORMATO_DATA_HORA = "ddMMyyyyHHmmss";
    private static final String FORMATO_HORA = "HH:mm:ss";
    private static final String FORMATO_MES = "MM";
    private static final String FORMATO_ANO = "yyyy";
    private static final String FORMATAR_DATA_PT = "dd-MM-yyyy";

    public static Date criarData() {
        Calendar calendario = new GregorianCalendar();
        return calendario.getTime();
    }
    public static  int searchWeekendBeforeTargetDay( Calendar calendario,int daysBefore ) {
        int dayOfWeek=0;
        int diaSemana=calendario.get(Calendar.DAY_OF_WEEK);
        calendario.add(diaSemana,daysBefore);
        dayOfWeek=calendario.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static String formatarData(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
        return formatoData.format(data);
    }

    public static String formatarDataPt(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATAR_DATA_PT);
        return formatoData.format(data);
    }
    public static String _formatarData(Date data) {
        DateFormat formatoData = new SimpleDateFormat(_FORMATO_DATA);
        return formatoData.format(data);
    }

    public static String formatarDataMostrar(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_MOSTRAR);
        return formatoData.format(data);
    }

    public static String formatarHora(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_HORA);
        return formatoData.format(data);
    }

    public static Date formatoHora(String data) {
      Date date=null;
            DateFormat formatoData = new SimpleDateFormat(FORMATO_HORA);
           try {   
            date= formatoData.parse(data);
        } catch (ParseException ex) {
           ex.printStackTrace();
        }
        return date;   
    }
    
    
    
    public static String formatarDataHora(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_HORA);
        return formatoData.format(data);
    }

    public static String obterMes(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_MES);
        return formatoData.format(data);
    }

    public static String obterAno(Date data) {
        DateFormat formatoData = new SimpleDateFormat(FORMATO_ANO);
        return formatoData.format(data);
    }
    public static Date formatarMysqlDate(Date data) {
        
        Date dat=null;
        try {
            DateFormat formatoData = new SimpleDateFormat(FORMATO_DATA_MOSTRAR);
                String date=formatoData.format(data);
             dat=formatoData.parse(date);
        } catch (ParseException ex) {
            
        }
        return dat;
    }

    public static String converterMesInteiro(int mes) {
        String moth = "";
        switch (mes) {
            case 0:
                moth = "Janeiro";
                break;
            case 1:
                moth = "Fevereiro";
                break;
            case 2:
                moth = "Março";
                break;
            case 3:
                moth = "Abril";
                break;
            case 4:
                moth = "Maio";
                break;
            case 5:
                moth = "Junho";
                break;
            case 6:
                moth = "Julho";
                break;
            case 7:
                moth = "Agosto";
                break;
            case 8:
                moth = "Setembro";
                break;
            case 9:
                moth = "Outubro";
                break;
            case 10:
                moth = "Novembro";
                break;
            case 11:
                moth = "Dezembro";
                break;
        }
        return moth;
    }
}
