package org.ciscoadiz.formanalysis.rules;

import org.ciscoadiz.formanalysis.entity.FlagSeverity;

public record FlagResult(
        FlagSeverity severity,
        String code,
        String description
) {}