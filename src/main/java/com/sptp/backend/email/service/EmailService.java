package com.sptp.backend.email.service;

public interface EmailService {
    void sendIdMessage(String to)throws Exception;
    void sendNewPasswordMessage(String email, String password)throws Exception;
}
