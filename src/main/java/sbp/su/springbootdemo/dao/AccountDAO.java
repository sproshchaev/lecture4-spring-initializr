package sbp.su.springbootdemo.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountDAO {

    private String jdbcUrl;

    @Value("${pathDBProperties}")
    private String pathDBProperties;

    @Value("${pathDBProperties2}")
    private String pathDBProperties2;


    public AccountDAO() {

    }

    /**
     * Setter-метод setJdbcUrl инициализирует переменные класса для JDBC
     * в зависимости от версии операционной системы
     * Значение свойств подключения в переменной pathDBProperties
     * файла src/main/resources/db.properties
     *
     * @param - String путь к файлу базы данных от каталога Documents
     */
    public void setJdbcUrl(String pathDBProperties) {

        this.jdbcUrl = jdbcUrlOS(pathDBProperties);

    }

    /**
     * Метод jdbcUrlOS инициализирует переменные класса для JDBC
     * в зависимости от версии операционной системы
     * Значение свойств подключения в переменной pathDBProperties
     * файла src/main/resources/db.properties
     *
     * @param - String путь к файлу базы данных от каталога Documents
     */
    private static String jdbcUrlOS(String pathDBProperties) {

        String osName = System.getProperty("os.name");

        if (osName.contains("Mac OS")) {
            return "jdbc:sqlite:/Users/sproshchaev/" + pathDBProperties;
        } else {
            return "jdbc:sqlite:/Users/Сергей/" + pathDBProperties;
        }

    }


    /**
     * Метод addToTableAccount добавляет новый счет в таблицу Accounts
     *
     * @param - Integer clientId идентификатор клиента
     * @param - String accountNumber номер счета (20 знаков)
     * @param - Double depositAmount сумма первоначального пополнения счета
     * @return - true, если счет был добавлен, иначе false
     */
    public boolean addToTableAccount(Integer clientId, String accountNumber, Double depositAmount) {

        System.out.println("pathDBProperties2=" + pathDBProperties2);

        boolean resultAddToTableAccount = false;

        int resultExecuteUpdate = 0;

        final String createPersonQuery = "insert into Accounts(clientId, accountNumber, depositAmount, status) values(?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(createPersonQuery)) {

            statement.setInt(1, clientId);
            statement.setString(2, accountNumber);
            statement.setDouble(3, depositAmount);
            statement.setString(4, "1");

            resultExecuteUpdate = statement.executeUpdate();

            resultAddToTableAccount = true;

        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultAddToTableAccount;
    }

    /**
     * Метод getAccountBalance возвращает остаток на счете
     *
     * @param - String accountNumber номер счета (20 знаков)
     * @return - Double остаток средств в валюте счета
     */
    public Double getAccountBalance(String accountNumber) {

        Double resultGetAccountBalance = null;

        final String createPersonQuery = "select * from Accounts where (accountNumber = ?)";

        List<Double> accountList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(createPersonQuery)) {

            statement.setString(1, accountNumber);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                accountList.add(resultSet.getDouble("depositAmount"));
            }

            if (accountList.size() == 1) {
                resultGetAccountBalance = accountList.get(0);
            }

        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultGetAccountBalance;
    }


    /**
     * Метод closeAccount закрывает счет
     * В методе используется try-with-resources, параметризированный SQL-запроса в PreparedStatement
     *
     * @param - String accountNumber номер закрываемого счета
     * @return - true если счет закрыт, false если счет не найден
     */
    public boolean closeAccount(String accountNumber) {

        boolean resultCloseAccount = false;

        int resultExecuteUpdate = 0;

        final String createPersonQuery = "update Accounts set status = ? where (accountNumber = ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(createPersonQuery)) {

            statement.setString(1, "2");
            statement.setString(2, accountNumber);

            resultExecuteUpdate = statement.executeUpdate();

            if (resultExecuteUpdate > 0) resultCloseAccount = true;

        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultCloseAccount;
    }


}
