package br.com.omni.services.sample;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation in all classes responsible in test functionalities of OMNI API.
 * The value informed here will be used to determine which URL will be called. 
 * 
 * @author Plusoft
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Functionality {
	String value();
}
