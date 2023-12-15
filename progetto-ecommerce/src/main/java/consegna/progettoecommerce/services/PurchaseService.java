package consegna.progettoecommerce.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Purchase;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.dots.PurchaseQueryDTO;
import consegna.progettoecommerce.models.requests.PurchaseRequest;
import consegna.progettoecommerce.repositories.PurchaseRepository;
import consegna.progettoecommerce.repositories.UserRepository;
import consegna.progettoecommerce.utility.exceptions.UserNotExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    
    /*public List<Purchase> findByEmailAndYear(String email, int year) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        Date date = calendar.getTime();
        return purchaseRepository.getByYear(user.getId(), date);
    }*/

}
