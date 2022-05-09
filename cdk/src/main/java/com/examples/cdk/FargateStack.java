package com.examples.cdk;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.constructs.Construct;

/* NOTES
We use an "L3" or pattern construct from CDK to really show how much
it can do for us. The equivalent yaml is ~600 lines.

This will automatically
- Build our docker image
- Upload our docker image (in a way that won't overwrite already used images)
- Create a LoadBalancer, ECS setup, IAM permissions, and all that 
  hairy AWS Networking stuff like security groups, VPC, subnets, etc

*/

public class FargateStack extends Stack {
    
    public FargateStack(Construct scope, String id) {
        super(scope, id);

        ApplicationLoadBalancedFargateService.Builder.create(this, "HelloWorldService")
        .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                .image(ContainerImage.fromAsset("../server/"))
                .build())
        .build();
    }
}
