package productservice.pesapal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import productservice.bookings.dto.*;


@RestController
@RequestMapping("api/products/test-key")
@RequiredArgsConstructor
public class PesaPalController {

    private final PesaPal pesaPal;

    @PostMapping
    public AuthenticationResponse testKey(){
        return pesaPal.requestToken();
    }

    @PostMapping("/register-ipn")
    public RegisterIPNResponse registerIpn() {
        return pesaPal.registerIPN();
    }

}
