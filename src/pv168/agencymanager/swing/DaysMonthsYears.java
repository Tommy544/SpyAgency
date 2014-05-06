/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author vlado
 */
public class DaysMonthsYears {
    
    private Locale locale;
    private ResourceBundle strings;
    
//    public final String[] days = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", 
//        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23","24", "25",
//        "26", "27", "28", "29", "30", "31"};
    public final String[] DAYS;
    public final String[] MONTHS;
    public final String[] YEARS;
    
    public DaysMonthsYears (Locale locale) {
        this.locale = locale;
        strings = ResourceBundle.getBundle("pv168.agencymanager.swing.Strings", locale);
        
        String[] monthsTmp = {strings.getString("january"),
        strings.getString("february"),
        strings.getString("march"),
        strings.getString("april"),
        strings.getString("may"),
        strings.getString("june"),
        strings.getString("july"),
        strings.getString("august"),
        strings.getString("september"),
        strings.getString("october"),
        strings.getString("november"),
        strings.getString("december")};
        
        MONTHS = monthsTmp;
        
        List<String> strList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            strList.add("" + i);
        }
        DAYS = strList.toArray(new String[0]);
        
        strList = new ArrayList<>();
        for (int i = 2014; i > 1930; i--) {
            strList.add("" + i);
        }
        YEARS = strList.toArray(new String[0]);
    }

}
