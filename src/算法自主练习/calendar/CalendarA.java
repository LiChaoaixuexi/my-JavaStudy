package 算法自主练习.calendar;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class CalendarA {

    public static void main(String[] args) {

        Calendar calender = Calendar.getInstance();
        /*
        calender.set(Calendar.YEAR,2024);
        calender.set(Calendar.MONTH,3);
        calender.set(Calendar.DATE,9);
        calender.set(Calendar.DAY_OF_WEEK,3);
        calender.set(Calendar.HOUR_OF_DAY,20);
        calender.set(Calendar.MONTH,31);
        calender.set(Calendar.SECOND,31);

         */
        System.out.println(calender.getTime());
    }

}
