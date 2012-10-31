/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.buongiorno.frog.lib.http.HttpException;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface TextReadableResource extends ReadableResource {

	/**
	 * Tries to discover the encoding of the resources and use it to consume this resource as text.
	 * 
	 * @return a {@link Reader}
	 */
	Reader readAsText() throws IOException, HttpException;

	/**
	 * Uses the specified {@link Charset} to consume this resource as text.
	 * 
	 * @return a {@link Reader}
	 */
	Reader readAsText(Charset charset) throws IOException, HttpException;

}
