package vn.iotstar.DatingApp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import vn.iotstar.DatingApp.Dto.TestDto;

@RestController
public class TestController {
//	private final PasswordEncoder passwordEncoder;
//	private final AccountServiceImpl accountService;
//	private List<Account> accounts;
//
////    public TestController(PasswordEncoder passwordEncoder, AccountServiceImpl accountService) {
////        this.passwordEncoder = passwordEncoder;
////        this.accountService = accountService;
////        initAccounts();
////    }
//    private void initAccounts() {
//        this.accounts = List.of(
//            Account.builder().email("abc@gmail.com").password("123456").role("ROLE_USER").build(),
//            Account.builder().email("ccc@gmail.com").password("123456").role("ROLE_USER").build()
//        );
//
//        for (Account acc : accounts) {
//            if (!accountService.findAccountByEmail(acc.getEmail()).isPresent()) {
//            	accountService.addNewAccount(acc);
//            }
//        }
//    }


//	// guest user can access it
//	@GetMapping("/hello")
//	public ResponseEntity<String> hello() {
//		String text = passwordEncoder.encode("hello");
//		return ResponseEntity.ok("hello in Bcrypt" + text);
//	}
//
//	// user can view their own information
//	@GetMapping("/account/{id}")
//	@PreAuthorize("hasAuthority('ROLE_USER')")
//	public ResponseEntity<Account> getCustomerList(@PathVariable("id") Long id) {
//		Account account = this.accounts.stream()
//		        .filter(acc -> acc.getId().equals(id))
//		        .findFirst()
//		        .orElseThrow(() -> new RuntimeException("Account not found"));
//
//		    return ResponseEntity.ok(account);
//	}
	private static final Logger log = LoggerFactory.getLogger(SettingsController.class);
	@PostMapping("/test") // Dùng một đường dẫn khác để test
	public ResponseEntity<?> testSettings(@Valid @RequestBody TestDto testDto) {
	    log.info("Received Test DTO: {}", testDto.getDatingPurpose());
	    return ResponseEntity.ok("OK");
	}

}
