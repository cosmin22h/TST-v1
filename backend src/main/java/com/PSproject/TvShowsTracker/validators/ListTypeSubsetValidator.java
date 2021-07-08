package com.PSproject.TvShowsTracker.validators;

import com.PSproject.TvShowsTracker.constants.ListType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ListTypeSubsetValidator implements ConstraintValidator<ListTypeSubset, ListType> {
    private ListType[] subset;

    @Override
    public void initialize(ListTypeSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(ListType listType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(subset).contains(listType);
    }
}
