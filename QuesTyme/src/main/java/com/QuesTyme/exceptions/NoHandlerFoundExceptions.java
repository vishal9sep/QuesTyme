package com.QuesTyme.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class NoHandlerFoundExceptions extends RuntimeException{
	
	private String message;


}
