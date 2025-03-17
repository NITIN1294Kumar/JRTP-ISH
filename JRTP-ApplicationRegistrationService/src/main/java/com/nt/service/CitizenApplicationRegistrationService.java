package com.nt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nt.bindings.CitizenAppRegistrationInput;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.repository.IApplicationRegistrationRepository;

@Service
public class CitizenApplicationRegistrationService implements ICitizenApplicationRegistrationService {

	@Autowired
	private IApplicationRegistrationRepository citizenRepo;

	@Autowired
	private RestTemplate template;

	@Value("${ar.ssa-web.url}")
	private String endPointUrl;

	@Value("${ar.state}")
	private String tragetState;

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInput inputs) {

		// External Rest api url
//		String SSAWebUrl = "http://localhost:9090/ssa-web-api/find/{ssn}";

		// perform WebService call to check wheather SSN is valid or not and to get
		// state name
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("accept", "application/json");
//		HttpEntity entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = template.exchange(endPointUrl, HttpMethod.GET, null, String.class,
				inputs.getSsn());
		// get state name
		String stateName = response.getBody();
		// register citizen if the belongs to California state (CA)
		if (stateName.equalsIgnoreCase(tragetState)) {
			// prepare the Entity object
			CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			// save the object
			int appId = citizenRepo.save(entity).getAapId();
			return appId;
		}
		return 0;
	}

}
