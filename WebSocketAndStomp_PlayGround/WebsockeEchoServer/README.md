## Installation Guide: 
* - Create a database named "fan_q2_amini" 
DATABASE fan_q2_amini CHARACTER SET utf8 COLLATE utf8_persian_ci;"<br>(Pay attention to charset in the command!) 
* - Give full access to the user for newly created database.  
* - Hit localhost:8080/register.xhtml in browser
* - Main page is located on localhost:8080/view/main.html 




@Autowired
    SimpMessagingTemplate messagingTemplate
    
 messagingTemplate.convertAndSendToUser(
                    cre.removedUser.phoneNumber,
                    "/queue/removedCollaborations",
                    om.writeValueAsString(cre.noteId)
            )
            
            
            
            
            
            
            
            
            
            https://uploads.toptal.io/blog/image/129598/toptal-blog-image-1555593632876-e8be5fa57853689bab282bb8be341130.png
            
            
              @Override
              public void registerStompEndpoints(StompEndpointRegistry
               registry) {
                registry.addEndpoint("/mywebsockets")
                    .setAllowedOrigins("mydomain.com").withSockJS();
              }
            
              @Override
              public void configureMessageBroker(MessageBrokerRegistry config){ 
                config.enableSimpleBroker("/topic/", "/queue/");
                config.setApplicationDestinationPrefixes("/app");
              }
            
            String username = ...
            this.simpMessagingTemplate.convertAndSendToUser();
               username, "/queue/greetings", "Hello " + username);
            
            @MessageMapping("/greetings")
            @SendToUser("/queue/greetings")
            public String reply(@Payload String message,
               Principal user) {
             return  "Hello " + message;
            }
            
            function connect() {
             var socket = new SockJS('/greetings');
             stompClient = Stomp.over(socket);
             stompClient.connect({}, function (frame) {
               stompClient.subscribe('/user/queue/greetings', function (greeting) {
                 showGreeting(JSON.parse(greeting.body).name);
               });
             });
            }
            
            function sendName() {
             stompClient.send("/app/greetings", {}, $("#name").val());
            }