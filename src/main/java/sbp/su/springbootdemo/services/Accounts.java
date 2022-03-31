package sbp.su.springbootdemo.services;

/**
 * Класс Account содержит методы открытия вклада, закрытия вклада, информации о текущем остатке
 *
 * @version 1.0
 * @autor Sergey Proshchaev
 */
public class Accounts {

    /**
     * Метод createAccount генерирует 20-ти значный счет клиента в указанной валюте
     *
     * @param - Integer уникальный номер клиента
     * @param - String код валюты RUR, USD
     * @return - String номер открытого счета клиента
     */
    public String createAccount(Integer clientId, String accountCurrency) {

        String accountNumber = "";

        accountNumber = "1234" + accountCurrency + "8901234567890";

        return accountNumber;

    }

}
