package vn.iotstar.DatingApp.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Account;
import vn.iotstar.DatingApp.Service.AccountServiceImpl;

@RestController
public class TestController {
	private final PasswordEncoder passwordEncoder;
	private final AccountServiceImpl accountService;
	private List<Account> accounts;

    public TestController(PasswordEncoder passwordEncoder, AccountServiceImpl accountService) {
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        initAccounts();
    }
    private void initAccounts() {
        this.accounts = List.of(
            Account.builder().email("abc@gmail.com").password("123456").role("ROLE_USER").build(),
            Account.builder().email("ccc@gmail.com").password("123456").role("ROLE_USER").build()
        );

        for (Account acc : accounts) {
            if (!accountService.findAccountByEmail(acc.getEmail()).isPresent()) {
            	accountService.addNewAccount(acc);
            }
        }
    }
	
	
	// guest user can access it
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		String text = passwordEncoder.encode("hello");
		return ResponseEntity.ok("hello in Bcrypt" + text);
	}
	
	// user can view their own information
	@GetMapping("/account/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Account> getCustomerList(@PathVariable("id") Long id) {
		Account account = this.accounts.stream()
		        .filter(acc -> acc.getId().equals(id))
		        .findFirst()
		        .orElseThrow(() -> new RuntimeException("Account not found"));

		    return ResponseEntity.ok(account);
	}
}
