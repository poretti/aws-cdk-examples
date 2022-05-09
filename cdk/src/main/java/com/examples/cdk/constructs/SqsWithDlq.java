package com.examples.cdk.constructs;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.cloudwatch.Alarm;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class SqsWithDlq extends Construct {

    private Queue dlq;

    public SqsWithDlq(@NotNull Construct scope, @NotNull String id) {
        super(scope, id);

        this.dlq = new Queue(this, "DLQ");

        Queue.Builder.create(this, "Queue")
            .retentionPeriod(Duration.days(14))
            .deadLetterQueue(DeadLetterQueue.builder()
                .maxReceiveCount(3)
                .queue(dlq)
                .build())
            .build();
    }
    
    public void alarmify() {
        Alarm.Builder.create(this, "OhNoAlarm")
            .evaluationPeriods(1)
            .metric(dlq.metricApproximateNumberOfMessagesVisible())
            .threshold(1)
            .build();
    }
}
