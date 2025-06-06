package com.example.T1.component;

import com.example.T1.enums.Type;
import com.example.T1.model.Account;
import com.example.T1.model.Client;
import com.example.T1.model.Transaction;
import com.example.T1.repository.AccountRepository;
import com.example.T1.repository.ClientRepository;
import com.example.T1.repository.TransactionRepository;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataGenerator {
    private final ClientRepository clientRepository;
    private final Faker faker = new Faker();
    private Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    public DataGenerator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    public void generateData(){
        int extremum = 20;

        if (clientRepository.count() < extremum){
            logger.info("БД пуста. Происходит заполнение.");
            for (int i = 0; i < extremum; ++i){

                Client client = new Client();
                client.setFirstName(faker.name().firstName());
                client.setLastName(faker.name().lastName());
                client.setMiddleName(faker.name().nameWithMiddle());

                Account account = new Account();
                account.setType(i % 2 == 0 ? Type.CREDIT : Type.DEBIT);
                account.setBalance(faker.number().randomNumber(6, true));
                client.addAccount(account);

                Transaction transaction = new Transaction();
                transaction.setValue(faker.number().randomNumber(4, true));
                transaction.setTimestamp(LocalDateTime.now().plusMinutes(i));
                account.addTransaction(transaction);


                clientRepository.save(client);
            }
            logger.info("Данные успешно сгенерированы.");
        } else{
            logger.info("БД уже содержит данные.");
        }
    }
}
