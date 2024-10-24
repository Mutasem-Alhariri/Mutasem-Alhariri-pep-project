package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
 private final AccountDao accountDao = new AccountDao();

 /**
 * @param account Tha account to be created
 * @return the account including the generated account_id if successful, null otherwise
 */
public Account createAccount(Account account) {
    if(validateAccount(account)) {
       return accountDao.save(account);
    }
    return account;
 }

 /**
  * Authenticate the account with the given username and password
  * @param account the account to be authenticated
  * @return the account if it exists
  */
 public Account login(Account account) {
    Account authenticated = accountDao.authenticate(account.getUsername(), account.getPassword());
    if(authenticated == null) {
        return account;
    }
    return authenticated;
 }
 /**
  * TO check if the account is a valid account that meets the requirements.
  * @param account the accouunt to be validated.
  * @return true if the account is valid, false otherwise.
  */
private boolean validateAccount(Account account) {
    if((account.getAccount_id() != 0) || (account.getUsername() == null)
        || (account.getPassword() == null)) {
        return false;
    }
    if(account.getUsername().trim().isEmpty() || account.getPassword().trim().length() < 4) {
        return false;
    }
    if(accountDao.getAccountByUsername(account.getUsername()) != null) {
        return false;
    }
    return true;
}
}
