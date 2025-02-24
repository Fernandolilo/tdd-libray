package com.systempro.library.service;

import java.util.List;

public interface EmailService {

	void sendEmail(String message, List<String> mails);

}
