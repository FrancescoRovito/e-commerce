package consegna.progettoecommerce.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.repositories.PurchaseRepository;
import consegna.progettoecommerce.repositories.UserRepository;
import consegna.progettoecommerce.utility.exceptions.UserNotExistsException;
import consegna.progettoecommerce.models.dots.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    
    public Page<PurchaseQueryDTO> findByEmailAndYear(String email, int year, PageRequestAttributes pageRequestAttributes) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();

        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getPage(), pageRequestAttributes.getDimPage());
        return purchaseRepository.getByYear(user.getId(), year, pageRequest);
    }

    public List<Integer> findByConsumer(int idConsumer) throws RuntimeException{
         User user=userRepository.findById(idConsumer);
        if(user==null)
            throw new UserNotExistsException();
        return purchaseRepository.findConsumer(idConsumer);
    }
}
