package br.com.lucimarsb.ecommerce.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
