package consegna.progettoecommerce.utility.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import consegna.progettoecommerce.models.requests.ModifyUserRequest;
import consegna.progettoecommerce.models.requests.ProductRequest;
import consegna.progettoecommerce.models.requests.RegisterRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Support {

    public static boolean validUser (RegisterRequest request){
        String regexFirstLastName="^[A-Z][a-z]*(?:\\s[A-Za-z]+)*$";
        String regexEmail= "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return request.getEmail().matches(regexEmail) && request.getFirstName().matches(regexFirstLastName) 
        && request.getLastName().matches(regexFirstLastName) && request.getBudget()>=0;
    }

    public static boolean validUser (ModifyUserRequest request){
        String regexFirstLastName="^[A-Z][a-z]*(?:\\s[A-Za-z]+)*$";
        String regexEmail= "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        return request.getEmail().matches(regexEmail) && request.getFirstName().matches(regexFirstLastName) 
        && request.getLastName().matches(regexFirstLastName) && request.getBudget()>=0;
    }

    public static boolean validPassword(String password){
        String regexPassword="^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$";
        return password.matches(regexPassword);
    }

    public static String dataCorrente(){
        LocalDateTime data=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }

    public static boolean validProduct (ProductRequest request){
        String regexNameType="^[A-Z][a-zA-Z]*(?:\\s[a-zA-Z]+)*$";
        String regexModel= "^[A-Z][a-zA-Z0-9]*(?:\\s[a-zA-Z0-9]+)*$";
        String regexCode="^[0-9]+$";
        return request.getModel().matches(regexModel) && request.getName().matches(regexNameType)
         && request.getType().matches(regexNameType) && request.getCode().matches(regexCode) &&
         request.getQuantity()>0 && request.getPrice()>0;
    }
}