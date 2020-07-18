package br.com.aegro.production.services.exceptions;

public class ProductivityException extends Throwable {

    private static final long serialVersionUID = 1L;

    public ProductivityException() {
        super("Invalid Productivity");
    }

}
