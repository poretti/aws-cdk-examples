package com.examples.cdk;

import com.examples.cdk.constructs.SuperSecureS3;
import com.examples.cdk.constructs.SuperSecureS3.Props;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

public class StorageStack extends Stack {

    // Public so they can be used in another Stack
    public Table dynamoTable;
    public Bucket s3Bucket;

    public StorageStack(Construct scope, String id) {
        super(scope, id);

        this.dynamoTable = Table.Builder.create(this, "Table")
            .partitionKey(Attribute.builder()
                .name("id")
                .type(AttributeType.STRING)
                .build())
            .build();

        SuperSecureS3 s5 = new SuperSecureS3(this, "S5", Props.builder()
            .versioned(false)
            .build());

        this.s3Bucket = s5.getBucket();
    }
    
}
