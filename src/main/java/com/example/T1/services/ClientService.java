package com.example.T1.services;


import com.example.T1.annotations.Cached;
import com.example.T1.annotations.LogDataSourceError;
import com.example.T1.annotations.Metric;
import com.example.T1.dto.ClientDto;
import com.example.T1.exceptions.UserNotFoundException;
import com.example.T1.mappers.ClientMapper;
import com.example.T1.model.Client;
import com.example.T1.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientMapper clientMapper){
        this.clientMapper = clientMapper;
    }

    @Metric
    @LogDataSourceError
    public Client createClient(ClientDto clientDto){
        Client clientForSaving = clientMapper.dtoToModel(clientDto);

        return clientRepository.save(clientForSaving);
    }

    @LogDataSourceError
    @Metric
    //@Cacheable(value = "client", key = "#id")
    @Cached(cacheName = "clients", key = "#id")
    public Client getClient(Long id){
        logger.info("Fetching client with ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });
    }

    @Metric
    @LogDataSourceError
    public Client updateClient(Long id, ClientDto clientDto){
        logger.info("Fetching client with ID: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });
        client.setFirstName(clientDto.getFirst());
        client.setLastName(clientDto.getLast());
        client.setMiddleName(clientDto.getMiddle());

        return clientRepository.save(client);
    }

    @LogDataSourceError
    public Client deleteClient(Long id){
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", id);
                    return new UserNotFoundException(id);
                });

        clientRepository.delete(client);
        return client;
    }

}