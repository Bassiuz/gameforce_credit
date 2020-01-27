/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Bas de Vaan
 */
public class MoneyFormatter {
    
    public static String formatMoney(BigDecimal amount)
    {
        boolean useMinus = false;
        if (amount.compareTo(new BigDecimal(0)) == -1)
        {
            useMinus = true;
            amount = amount.multiply(new BigDecimal(-1));
        }
        BigDecimal fractionalPart = amount.remainder( BigDecimal.ONE ).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal whole = amount.subtract(fractionalPart.remainder( BigDecimal.ONE )).setScale(0, RoundingMode.HALF_DOWN);
        
        String result = "€" + whole.toPlainString()+ "," + fractionalPart.toPlainString().substring(2);
        if (useMinus)
        {
            result = "- " + result;
        }
        return result;
    }
    
}
