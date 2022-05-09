package com.example.fanout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import lombok.Data;

public class Handler implements RequestHandler<Handler.FanoutMessage, Void> {

    private static SnsClient sns = SnsClient.create();

    private static String topic = System.getenv("TOPIC_ARN");

    @Override
    public Void handleRequest(FanoutMessage input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Received a message! " + input);
        
        PublishRequest snsMessage = PublishRequest.builder()
            .message(input.getMessage())
            .topicArn(topic)
            .build();

        try {
            sns.publish(snsMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    
    @Data
    public static class FanoutMessage {
        private String message;
    }
}
