package ir.tiroon;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.time.Duration;

@EnableBinding({Sink.class, Source.class, Processor.class, myBinding.class, anotherBinding.class})
@SpringBootApplication
public class DemoApplication {

    //this class contain some samples of usage, not a working and well defined scenario

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String handle(String value) {
        System.out.println("Received: " + value);
        return value.toUpperCase();
    }

    @StreamListener(myBinding.TOPIC2)
    @SendTo(myBinding.TOPIC1)
    public String handle2(String value) {
        System.out.println("Received: " + value);
        return value.toUpperCase();
    }


    @Autowired
    @Qualifier("mySource")
    Source output;
    public void sayHello(String name) {
        output.output().send(MessageBuilder.withPayload(name).build());
    }

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<>("Hello Spring Cloud Stream");
    }

    /*
    when you consume from the same binding using @StreamListener annotation, a pub-sub model
    is used. Each method annotated with @StreamListener receives its own
    copy of a message, and each one has its own consumer group.
    However, if you consume from the same binding by using one of the Spring Integration
    annotation (such as @Aggregator, @Transformer, or @ServiceActivator),
    those consume in a competing model. No individual consumer group is created for each subscription.
     */
    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public Object transform(String message) {
        return message.toUpperCase();
    }

    //AND reactive support->
    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<String> receive(@Input(Processor.INPUT) Flux<String> input) {
        return input.map(s -> s.toUpperCase());
    }

    @StreamListener
    public void receive(@Input(Processor.INPUT) Flux<String> input,
                        @Output(Processor.OUTPUT) FluxSender output) {
        output.send(input.map(s -> s.toUpperCase()));
    }

    // the resulting messages in the Flux are sent to the output channel of the Source.
    @StreamEmitter //No input possible with emitter
    @Output(Source.OUTPUT)
    public Flux<String> emit() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(l -> "Hello World");
    }

    @StreamEmitter
    @Output(Source.OUTPUT)
    public void emit1(FluxSender output) {
        output.send(Flux.interval(Duration.ofSeconds(1))
                .map(l -> "Hello World"));
    }

    @StreamEmitter
    @Output(Source.OUTPUT)
    @Bean
    public Publisher<Message<String>> emit2() {
        return IntegrationFlows.from(() ->
                        new GenericMessage<>("Hello World"),
                e -> e.poller(p -> p.fixedDelay(1)))
                .toReactivePublisher();
    }
}

interface myBinding {

    String TOPIC1 = "topic1";
    String TOPIC2 = "topic2";
    String TOPIC3 = "topic3";
    String TOPIC4 = "topic4";

    @Output(myBinding.TOPIC1)
    MessageChannel myMessageChannel();//MessageChannel is for sending out messages; this comes from spring messaging

    @Input(myBinding.TOPIC2)
    SubscribableChannel mySubscribableChannel();//SubscribableChannel is for receiving in messages; this comes from spring messaging

    @Input(myBinding.TOPIC3)
    PollableMessageSource orders();//PollableMessageSource is for polling synchronously which provides things like consumption rate control messages;
    // this comes from spring cloud stream binder

    @Output(myBinding.TOPIC4)
    MessageSource myMessageSource();// this is part of spring integration

}

interface anotherBinding {
    String TOPIC1 = "topic1";
    String TOPIC2 = "topic2";

    @Input(anotherBinding.TOPIC1)
    Sink mySink();//More general version of  MessageChannel; this comes from spring cloud stream messaging

    @Output(anotherBinding.TOPIC2)
    Source mySource();//More general version of SubscribableChannel; this comes from spring cloud stream messaging
}