package sbp.su.springbootdemo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sbp.su.springbootdemo.config.SpringConfig;
import sbp.su.springbootdemo.dao.AccountDAO;
import sbp.su.springbootdemo.services.Accounts;

/**
 * Класс ServerController содержит методы обработки REST-запросов
 * на создание вклада/проверку накоплений по вкладу/закрытие вклада
 *
 * @version 1.0
 * @autor Sergey Proshchaev
 */
@RestController
@RequestMapping("/account")
public class ServerController {

    @Value("${pathDBProperties}")
    private String pathDBProperties;

    private final AccountDAO accountDAO;

    public ServerController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Метод createAccount в классе ServerController вызывает метод createAccount в классе Accounts
     *
     * @param - Integer clientId
     * @param - String accountCurrency
     * @param - Double depositAmount
     * @return - String номер открытого счета клиента
     * Параметры передаются в query string GET-запроса
     * Пример GET-запроса: http://localhost:8080/account/create?clientId=125&accountCurrency=USD&depositAmount=100.00 возвращает "Account opened 1234USD8901234567890"
     */
    @GetMapping("/create")
    public String createAccount(@RequestParam("clientId") Integer clientId,
                                @RequestParam("accountCurrency") String accountCurrency,
                                @RequestParam("depositAmount") Double depositAmount) {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        String accountNumber = context.getBean(Accounts.class).createAccount(clientId, accountCurrency);

        accountDAO.setJdbcUrl(pathDBProperties);
        accountDAO.addToTableAccount(clientId, accountNumber, depositAmount);

        return "Account opened " + accountNumber;

    }

    /**
     * Метод getAccountBalance в классе ServerController вызывает метод getAccountBalance в классе Accounts
     *
     * @param - String accountNumber
     * @return - Double depositAmount
     * Параметры передаются в query string GET-запроса
     * Пример GET-запросов: http://localhost:8080/account/remain?accountNumber=12345678901234567890 возвращает "Account is not defined!"
     * http://localhost:8080/account/remain?accountNumber=1234RUR8901234567890 возвращает "101.03"
     */
    @GetMapping("/remain")
    public String getAccountBalance(@RequestParam("accountNumber") String accountNumber) {

        accountDAO.setJdbcUrl(pathDBProperties);

        Double resultGetAccountBalance = accountDAO.getAccountBalance(accountNumber);

        if (resultGetAccountBalance != null) {
            return resultGetAccountBalance.toString();
        } else {
            return "Account is not defined!";
        }
    }

    /**
     * Метод closeAccount в классе ServerController вызывает метод closeAccount в классе Accounts
     *
     * @param - String accountNumber
     * @return - Boolean true - если счет закрыт, иначе false
     * Параметры передаются в query string GET-запроса
     * Пример GET-запросов: http://localhost:8080/account/close?accountNumber=1234RUR8901234567890 возвращает "Account 1234RUR8901234567890 closed"
     * http://localhost:8080/account/close?accountNumber=123 возвращает "Account is not defined!"
     */
    @GetMapping("/close")
    public String closeAccount(@RequestParam("accountNumber") String accountNumber) {

        accountDAO.setJdbcUrl(pathDBProperties);

        Boolean resultCloseAccount = accountDAO.closeAccount(accountNumber);

        if (resultCloseAccount) {
            return "Account " + accountNumber + " closed";
        } else {
            return "Account is not defined!";
        }

    }

}
