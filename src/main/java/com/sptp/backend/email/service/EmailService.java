package com.sptp.backend.email.service;

public interface EmailService {
    String sendMessage(String to)throws Exception;
}
