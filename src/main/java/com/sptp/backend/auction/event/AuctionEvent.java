package com.sptp.backend.auction.event;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.common.NotificationCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuctionEvent {

    private final ArtWork artWork;
    private final NotificationCode notificationCode;
}
