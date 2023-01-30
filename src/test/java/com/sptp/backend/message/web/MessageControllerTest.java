package com.sptp.backend.message.web;

import com.sptp.backend.art_work.repository.ArtWork;
import com.sptp.backend.art_work.repository.ArtWorkRepository;
import com.sptp.backend.auction.repository.Auction;
import com.sptp.backend.auction.repository.AuctionRepository;
import com.sptp.backend.chat_room.repository.ChatRoom;
import com.sptp.backend.chat_room.repository.ChatRoomRepository;
import com.sptp.backend.common.Factory.ArtWorkFactory;
import com.sptp.backend.common.Factory.AuctionFactory;
import com.sptp.backend.common.Factory.ChatRoomFactory;
import com.sptp.backend.common.Factory.MemberFactory;
import com.sptp.backend.common.StompFrameHandlerImpl;
import com.sptp.backend.jwt.web.JwtTokenProvider;
import com.sptp.backend.jwt.web.dto.TokenDto;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import com.sptp.backend.message.repository.MessageRepository;
import com.sptp.backend.message.web.dto.MessageRequest;
import com.sptp.backend.message.web.dto.MessageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MessageControllerTest {

    @LocalServerPort
    private int port;
    private BlockingQueue<MessageResponse> messages;

    private final MemberFactory memberFactory = new MemberFactory();
    private final AuctionFactory auctionFactory = new AuctionFactory();
    private final ArtWorkFactory artWorkFactory = new ArtWorkFactory();
    private final ChatRoomFactory chatRoomFactory = new ChatRoomFactory();

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ArtWorkRepository artWorkRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    Member member;
    Member artist;
    Auction auction;
    ArtWork artWork;
    ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        messages = new LinkedBlockingDeque<>();
        member = memberRepository.save(memberFactory.createMember("poo", "poo@naver.com"));
        artist = memberRepository.save(memberFactory.createArtist("mira", "mira@naver.com"));
        auction = auctionRepository.save(auctionFactory.createProcessingAuction());
        artWork = artWorkRepository.save(artWorkFactory.createArtWork(artist, auction));
        chatRoom = chatRoomRepository.save(chatRoomFactory.createChatRoom(artist, member, artWork));
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
        chatRoomRepository.deleteAll();
        artWorkRepository.deleteAll();
        auctionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("메시지 전송")
    void message() throws ExecutionException, InterruptedException, TimeoutException {
        MessageRequest messageRequest = MessageRequest.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(member.getId())
                .message("hello")
                .build();

        //Setting
        WebSocketStompClient webSocketStompClient = WebSocketStompClient();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        //Connection
        HttpHeaders headers = new HttpHeaders(new LinkedMultiValueMap<>());
        TokenDto tokenDto = jwtTokenProvider.createToken(member.getUserId(), member.getRoles());
        headers.add("Authorization", tokenDto.getAccessToken());
        System.out.println("tokenDto.getAccessToken() = " + tokenDto.getAccessToken());
        final WebSocketHttpHeaders socketHttpHeaders = new WebSocketHttpHeaders(headers);
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect("ws://localhost:" + port + "/ws-connection", new StompSessionHandlerAdapter() {
                });
        System.out.println("\"ws://localhost:\" + port + \"/ws-connection\" = " + "ws://localhost:" + port + "/ws-connection");
        System.out.println("connect = " + connect);
        StompSession stompSession = connect.get(120, TimeUnit.SECONDS);

        stompSession.subscribe(String.format("/queue/chat-rooms/%s", chatRoom.getId()),
                new StompFrameHandlerImpl(new MessageRequest(), messages));
        stompSession.send("/app/send", messageRequest);

        MessageResponse messageResponse = messages.poll(5, TimeUnit.SECONDS);

        //Then
        assertThat(messageResponse.getMessage()).isEqualTo(messageRequest.getMessage());
    }

    private WebSocketStompClient WebSocketStompClient() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }
}
