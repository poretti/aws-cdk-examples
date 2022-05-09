package com.examples.cdk;

import software.constructs.Construct;

import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.sns.Topic;

/* NOTES

Note the usage of `deadLetterQueueEnabled` when creating the lambda.
CDK will create the SQS queue and get it wired up for us!

We have to explicitly give the lambda permissions to publish to the SNS
topic though, because CDK doesn't know what our business logic is
trying to do with SNS.

The stack shows an example of conditionally doing prod logic, like adding 
alarms. Since the logic is built in our CDK code, it'll show up in the 
generated Cloudformation json template.
This is a HUGE benefit compared to deploy-time logic failing at the 
prod stage in a pipeline. It could be the difference between finding a
mistake in seconds vs hours.

*/

public class LambdaSnsStack extends Stack {

    public LambdaSnsStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        Topic topic = new Topic(this, "SNS");

        Function fanout = Function.Builder.create(this, "Fanout")
            .runtime(Runtime.JAVA_11)
            .code(Code.fromAsset("../snslambda/target/fanout-1.0.jar"))
            .handler("com.example.fanout.Handler")
            .environment(Map.of("TOPIC_ARN", topic.getTopicArn()))
            .deadLetterQueueEnabled(true)
            .tracing(Tracing.ACTIVE)
            .timeout(Duration.seconds(10))
            .memorySize(512)
            .build();

        topic.grantPublish(fanout);

        if (isProd(props.getEnv())) {
            // Prod logic
        }
    }

    // In practice, I put logic like this in a Util and check against accountIds.
    private boolean isProd(Environment env) {
        return env.getRegion().equals("us-east-1");
    }
}
