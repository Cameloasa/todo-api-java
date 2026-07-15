package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.EmailDTO;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;

public interface EmailService {
    void sendEmail(EmailDTO dto);
    void sendRegistrationEmail(String email);
    void sendTaskCreatedEmail(String email, TaskDTOView task);
    void sendPasswordResetEmail(String email, String token);
}
    

