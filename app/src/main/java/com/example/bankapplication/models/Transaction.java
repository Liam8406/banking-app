package com.example.bankapplication.models;

public class Transaction {

    private String fromId;
    private String toId;
    private double amount;
    private long timestamp;

    public Transaction() {
    }

    public Transaction(String fromId, String toId, double amount, long timestamp) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}