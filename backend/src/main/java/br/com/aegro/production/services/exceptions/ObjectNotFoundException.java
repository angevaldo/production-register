package br.com.aegro.production.services.exceptions;

import java.util.Arrays;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(Object ... id) {
		super("Resource(s) not found for Id(s): " + Arrays.asList(id));
	}

}
