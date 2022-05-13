package com.examples.cdk.constructs;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import software.amazon.awscdk.services.kms.Key;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.constructs.Construct;

/* NOTES

A great use for custom constructs is for creating security approved
resources like S3. 

For this class, we use a custom Props class to take in additional input.
Combined with Lombok, it's a great way to set reasonable defaults, while
still allowing overrides.

Note that CDK's recommendation for Java is to follow their Builder pattern,
but I don't know an easy to generate that compared to use Lombok.

*/

public class SuperSecureS3 extends Construct {
    
    @Builder
    @Data
    public static class Props {
        @Builder.Default 
        private boolean versioned = true;
    }

    @Getter
    private Bucket bucket;

    public SuperSecureS3(Construct scope, String id, Props props) {
        super(scope, id);

        this.bucket = Bucket.Builder.create(this, "S3")
            .encryption(BucketEncryption.KMS)
            .encryptionKey(new Key(this, "Key"))
            .versioned(props.isVersioned())
            .build();
    }
}
