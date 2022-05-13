package com.examples.cdk;

import java.util.Map;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.stepfunctions.Chain;
import software.amazon.awscdk.services.stepfunctions.IChainable;
import software.amazon.awscdk.services.stepfunctions.JsonPath;
import software.amazon.awscdk.services.stepfunctions.StateMachine;
import software.amazon.awscdk.services.stepfunctions.tasks.DynamoAttributeValue;
import software.amazon.awscdk.services.stepfunctions.tasks.DynamoPutItem;
import software.amazon.awscdk.services.stepfunctions.tasks.LambdaInvoke;
import software.constructs.Construct;
import software.amazon.awscdk.services.lambda.Runtime;

public class StepFunctionsStack extends Stack {
    
    @lombok.Builder
    public static class Props {
        private StorageStack storageStack;
    }

    public StepFunctionsStack(Construct scope, String id, Props props) {
        super(scope, id);

        StateMachine.Builder.create(this, "SimpleStateMachine")
            .definition(Chain
                .start(lambdaStep("NeatoLambda1"))
                .next(writeToDynamo(props.storageStack.dynamoTable))
                .next(lambdaStep("NeatoLambda2")))
            .build();
    }

    private IChainable lambdaStep(String id) {
        Function lambda = Function.Builder.create(this, id)
            .runtime(Runtime.PYTHON_3_9)
            .code(Code.fromInline("def handler(event, _): return event;"))
            .handler("index.handler")
            .build();

        return LambdaInvoke.Builder.create(this, id + "Task")
            .lambdaFunction(lambda)
            .resultPath("$.lambdaResult")
            .build();
    }

    private IChainable writeToDynamo(Table table) {
        return DynamoPutItem.Builder.create(this, "PutItem")
            .item(Map.of("id", DynamoAttributeValue.fromString(JsonPath.stringAt("$.hello"))))
            .table(table)
            .resultPath("$.dynamoResult")
            .build();
    }

}
