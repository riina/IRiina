package net.cyriaca.riina.misc.iriina.generic;

import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

public abstract class Result {

    private ResultType resultType;

    public Result(ResultType resultType) {
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public boolean failed() {
        return resultType == ResultType.FAILURE;
    }

    public abstract String getLocalizedFailureInfo(Locale l);

    public enum ResultType {
        SUCCESS, FAILURE
    }

}
