package ir.tiroon.echo.controller

import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

import java.security.Principal

@Controller
class WebsocketController {

        @MessageMapping("/message")
        @SendToUser("/queue/reply")
        String processMessageFromClient(@Payload String message, Principal principal) throws Exception {
            System.out.println(principal.name + ":" + message)
            return principal.getName() + ":" + message
        }

        @MessageExceptionHandler
        @SendToUser("/queue/errors")
        String handleException(Throwable exception) {
            return exception.getMessage()
        }
}
