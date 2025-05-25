package com.example.T1.model;

import com.example.T1.enums.Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "acc_type")
    private Type type;
    @Column(name = "balance")
    private Long balance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    public Account(Type type, Long balance, Client client) {
        this.type = type;
        this.balance = balance;
        this.client = client;
    }

    public Account(){}

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this); // Устанавливаем обратную связь
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setAccount(null); // Разрываем обратную связь
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", type=" + type +
                ", balance=" + balance +
                ", client=" + client +
                ", transactions=" + transactions +
                '}';
    }
}
