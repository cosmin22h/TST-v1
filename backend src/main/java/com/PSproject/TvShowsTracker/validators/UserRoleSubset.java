package com.PSproject.TvShowsTracker.validators;

import com.PSproject.TvShowsTracker.constants.Role;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserRoleSubsetValidator.class)
public @interface UserRoleSubset {
    Role[] anyOf();
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
