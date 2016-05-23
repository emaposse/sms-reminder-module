package org.openmrs.module.smsreminder.utils;

/**
 * Created by Nelson.Mahumane on 01-10-2015.
 */
public class Validator {

 public static  String cellNumberValidator( String cellNumber){
        cellNumber=cellNumber.replace(" ","");
        if(cellNumber.length()==9){
            cellNumber="+258"+cellNumber;
        }
        if(cellNumber.length()==12){
            cellNumber="+"+cellNumber;
        }
        if(cellNumber.length()==13){
            if (cellNumber.startsWith("+2588")){
                String cell=cellNumber.substring(6);
                char op=cellNumber.charAt(5);

                if (op!='2'&&op!='4'&&op!='6'&&op!='7'){
                    return "";
                }
                for (char letra : cell.toCharArray())  {
                    if(letra < '0' || letra > '9') {
                        return "";
                    }
                }
                return cellNumber;
            }else{
                return "";
            }
        }
        return "";
    }

}
