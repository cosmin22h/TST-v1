package com.PSproject.TvShowsTracker.validators;

import com.PSproject.TvShowsTracker.constants.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UserRoleSubsetValidator implements ConstraintValidator<UserRoleSubset, Role> {
    private Role[] subset;

    @Override
    public void initialize(UserRoleSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(subset).contains(role);
    }
}
