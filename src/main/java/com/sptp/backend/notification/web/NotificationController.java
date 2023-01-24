package com.sptp.backend.notification.web;

import com.sptp.backend.jwt.service.dto.CustomUserDetails;
import com.sptp.backend.notification.service.NotificationService;
import com.sptp.backend.notification.web.dto.response.NotificationNewResponse;
import com.sptp.backend.notification.web.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getNotificationList(@AuthenticationPrincipal CustomUserDetails userDetails){

        List<NotificationResponse> notificationResponses = notificationService.getNotificationList(userDetails.getMember().getId());

        return ResponseEntity.status(HttpStatus.OK).body(notificationResponses);
    }

    // 알림 삭제
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable(value="notificationId") Long notificationId) {

        notificationService.deleteNotification(notificationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 새로운 알림 유무 조회
    @GetMapping("/notifications/new")
    public ResponseEntity<NotificationNewResponse> getNotificationNew(@AuthenticationPrincipal CustomUserDetails userDetails){

        Boolean checked = notificationService.getNotificationNew(userDetails.getMember());

        NotificationNewResponse notificationNewResponse = NotificationNewResponse.builder()
                .newNotification(checked)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(notificationNewResponse);
    }

}
