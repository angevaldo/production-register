package br.com.aegro.production.domain.entities.exceptions;

import java.util.Arrays;

public class ProductivityException extends Throwable {

    private static final long serialVersionUID = 1L;

    public ProductivityException(Object id) {
        super("Invalid Productivity. Id: " + id);
    }

}
