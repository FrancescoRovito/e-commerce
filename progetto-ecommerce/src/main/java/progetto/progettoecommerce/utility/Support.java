package progetto.progettoecommerce.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;
import progetto.progettoecommerce.entities.User;

@UtilityClass
public class Support {
    public static boolean validUser (User u){
        String regexFirstLastName="^[A-Za-z]+(?:\\s[A-Za-z]+)*$";
        String regexEmail= "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        //inserire regexPassword
        return u.getEmail().matches(regexEmail) && u.getFirstName().matches(regexFirstLastName) 
        && u.getLastName().matches(regexFirstLastName) && u.getBudget()>=0;
    }

    public static String dataCorrente(){
        LocalDateTime data=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }
}