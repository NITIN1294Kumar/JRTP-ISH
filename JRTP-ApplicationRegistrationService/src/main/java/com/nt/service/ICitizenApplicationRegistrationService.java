package com.nt.service;

import com.nt.bindings.CitizenAppRegistrationInput;
import com.nt.exceptions.InvalidSSNException;

public interface ICitizenApplicationRegistrationService {

	public Integer registerCitizenApplication(CitizenAppRegistrationInput inputs) throws InvalidSSNException;

}
