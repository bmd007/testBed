package ir.tiroon.echo.controller

import ir.tiroon.echo.model.User
import ir.tiroon.echo.service.UserServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.*

import java.security.Principal

@RestController
class RestControllers {

    @Autowired
    UserServices userServices

    @Autowired
    private SimpMessageSendingOperations messagingTemplate

    @GetMapping("/trigger")
    @ResponseBody
    void trigger(Principal principal) {
        System.out.println(principal.name+":::::::")
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", principal.name)
    }

    //User controllers
    @GetMapping("/loggedInUser")
    @ResponseBody
    User loggedInUser(Principal principal) {
        userServices.get(principal.name)
    }

    @GetMapping("/users")
    @ResponseBody
    List<User> users() {
        userServices.list()
    }

    @GetMapping("/removeUser/{phoneNumber}")
    ResponseEntity removeUser(@PathVariable("phoneNumber") String phoneNumber, Principal principal) {
        userServices.delete(phoneNumber, principal.name) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build()
    }

//    @Autowired
//    HttpSessionCsrfTokenRepository csrfTokenRepository
//security
//    @GetMapping(path = "/csrf", produces = "text/plain")
//    @ResponseBody
//    String csrfEndpoint(HttpServletRequest request){
//       csrfTokenRepository.loadToken(request).token
//    }
}