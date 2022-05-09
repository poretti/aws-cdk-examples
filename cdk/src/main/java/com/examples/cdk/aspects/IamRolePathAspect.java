package com.examples.cdk.aspects;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.IAspect;
import software.amazon.awscdk.services.iam.CfnRole;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.IConstruct;

public class IamRolePathAspect implements IAspect {

    private String path;

    public static IamRolePathAspect path(String path) {
        return new IamRolePathAspect(path);
    }

    public IamRolePathAspect(@NotNull String path) {
        this.path = path;
    }

    @Override
    public void visit(@NotNull IConstruct node) {
        if (node instanceof Role) {
            CfnRole role = (CfnRole) node.getNode().getDefaultChild();
            role.setPath("/" + path + "/");
        }
    }
}
