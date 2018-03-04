package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Request;
import com.uniovi.repositories.RequestRepository;

@Service
public class RequestsService {
	@Autowired
	private RequestRepository requestRepository;

	public List<Request> findSendRequestByUser(Long id) {
		return requestRepository.findAllSentById(id);
	}

}
