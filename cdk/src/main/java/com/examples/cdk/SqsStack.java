package com.examples.cdk;

import software.constructs.Construct;
import com.examples.cdk.constructs.SqsWithDlq;
import software.amazon.awscdk.Stack;

public class SqsStack extends Stack {

    public SqsStack(Construct scope, String id) {
        super(scope, id);

        SqsWithDlq sqsWithDlq = new SqsWithDlq(this, "Processor");
        sqsWithDlq.alarmify();
    }
}
