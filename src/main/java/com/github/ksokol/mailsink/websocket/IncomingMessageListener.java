package com.github.ksokol.mailsink.websocket;

import com.github.ksokol.mailsink.entity.Mail;
import com.github.ksokol.mailsink.repository.MailRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonMap;

/**
 * @author Kamill Sokol
 */
@Component
class IncomingMessageListener {

    private static final String TOPIC_INCOMING_MAIL = "/topic/incoming-mail";

    private final MailRepository mailRepository;
    private final SimpMessagingTemplate template;

    public IncomingMessageListener(MailRepository mailRepository, SimpMessagingTemplate template) {
        this.mailRepository = mailRepository;
        this.template = template;
    }

    @EventListener
    public void handleIncomingEvent(IncomingEvent event) {
        Mail incomingMail = event.getIncomingMail();
        mailRepository.save(incomingMail);
        template.convertAndSend(TOPIC_INCOMING_MAIL, singletonMap("id", incomingMail.getId()));
    }
}
