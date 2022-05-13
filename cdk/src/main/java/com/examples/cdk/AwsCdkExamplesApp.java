package com.examples.cdk;

import com.examples.cdk.StepFunctionsStack.Props;
import com.examples.cdk.aspects.IamRolePathAspect;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Aspects;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public class AwsCdkExamplesApp {
    public static void main(final String[] args) {
        App app = new App();


        // EXAMPLE 1
        // We make our own custom CDK construct representing a common piece of infrastructure
        new SqsStack(app, "ExampleSqsStack");


        // EXAMPLE 2
        // Let's ramp things by doing enrivonment specific logic and connecting services!
        // We no longer have to use Cfn parameters and Fn::If to determine stack values.
        // This means we find out locally if a value is missing rather than at Cfn deploy time.
        new LambdaSnsStack(app, "ExampleLambdaSnsStack", StackProps.builder()
                .env(Environment.builder()
                        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                        .region("us-west-2")
                        .build())
                .build());
        

        // EXAMPLE 3
        // A fargate service requires a lot to setup (ECS, Load balancer, uploading an image, etc)
        // Let's ramp things by doing enrivonment specific logic and connecting services!
        Stack fargateStack = new FargateStack(app, "ExampleFargateStack");
        Tags.of(fargateStack).add("team", "edward");
        Aspects.of(fargateStack).add(IamRolePathAspect.path("mypath"));


        // EXAMPLE 4 - Full Fancy Mode
        StorageStack storage = new StorageStack(app, "ExampleStorageStack");
        new StepFunctionsStack(app, "ExampleStepFunctionsStack", Props.builder()
            .storageStack(storage)
            .build());

            
        app.synth();
    }
}
