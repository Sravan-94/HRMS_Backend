package com.zynlogics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zynlogics.Repository.IAttendancerepo;

@Service
public class Employe {

	@Autowired
	private IAttendancerepo attrepo;
	
	
}
