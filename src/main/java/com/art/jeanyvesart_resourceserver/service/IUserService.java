package com.art.jeanyvesart_resourceserver.service;

import com.art.jeanyvesart_resourceserver.exceptionHandler.UserAlreadyExistException;
import com.art.jeanyvesart_resourceserver.dto.CustomerDto;

public interface IUserService<E> {
    E registerNewCustomerAccount(CustomerDto userDto) throws UserAlreadyExistException;
}