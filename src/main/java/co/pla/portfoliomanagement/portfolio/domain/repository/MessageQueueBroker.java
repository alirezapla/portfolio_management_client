package co.pla.portfoliomanagement.portfolio.domain.repository;

public interface MessageQueueBroker {

    void sendMessage(String message, String portfolioId);
}
