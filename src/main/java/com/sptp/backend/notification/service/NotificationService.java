package com.sptp.backend.notification.service;

import com.sptp.backend.common.NotificationCode;
import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.notification.repository.Notification;
import com.sptp.backend.notification.repository.NotificationRepository;
import com.sptp.backend.notification.web.dto.response.NotificationNewResponse;
import com.sptp.backend.notification.web.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public List<NotificationResponse> getNotificationList(Long loginMember){

        List<Notification> findNotificationList = notificationRepository.findByMemberId(loginMember);

        List<NotificationResponse> notificationResponses = findNotificationList.stream()
                .map(m -> new NotificationResponse(m.getId(), m.getTitle(), m.getMessage(), m.getDetails(), m.getData(), m.getCreatedDate()))
                .collect(Collectors.toList());

        markAsRead(findNotificationList);

        return notificationResponses;
    }

    private void markAsRead(List<Notification> notifications) {
        notifications.forEach(Notification::read);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {

        notificationRepository.deleteById(notificationId);
    }

    @Transactional(readOnly = true)
    public Boolean getNotificationNew(Member member) {

        long count = notificationRepository.countByMemberAndChecked(member, false);

        return (count > 0);
    }
}
