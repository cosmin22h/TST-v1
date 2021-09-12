package com.PSproject.TvShowsTracker.validators;

import com.PSproject.TvShowsTracker.constants.ReportType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ReportTypeSubsetValidator implements ConstraintValidator<ReportTypeSubset, ReportType> {
    private ReportType[] subset;

    @Override
    public void initialize(ReportTypeSubset constraintAnnotation) {
        this.subset = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(ReportType reportType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(subset).contains(reportType);
    }
}