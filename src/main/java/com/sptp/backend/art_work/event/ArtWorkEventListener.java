package com.sptp.backend.art_work.event;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.common.NotificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class ArtWorkEventListener {

    @EventListener
    public void handleArtWorkCreateEvent(ArtWorkCreateEvent artworkCreateEvent){

        ArtWork artwork = artworkCreateEvent.getArtwork();
        NotificationCode notificationCode = artworkCreateEvent.getNotificationCode();
        //log.info(NotificationCode.addArtWorkTitle(artwork.getTitle(), notificationCode));


    }
}
