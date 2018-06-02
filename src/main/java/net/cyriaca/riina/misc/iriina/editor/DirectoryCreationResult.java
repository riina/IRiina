package net.cyriaca.riina.misc.iriina.editor;

import net.cyriaca.riina.misc.iriina.generic.Result;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import java.io.File;

public class DirectoryCreationResult extends Result {

    private static final String KEY_RESULT_DIRECTORY_CREATION_FAIL = "result_directory_creation_fail";
    private static final String DIRECTORY = "%directory%";

    private File directory;

    public DirectoryCreationResult(ResultType resultType, File directory) {
        super(resultType);
        this.directory = directory;
    }

    public static DirectoryCreationResult createSuccessResult(File directory) {
        return new DirectoryCreationResult(ResultType.SUCCESS, directory);
    }

    public static DirectoryCreationResult createFailureResult(File directory) {
        return new DirectoryCreationResult(ResultType.FAILURE, directory);
    }

    public String getLocalizedFailureInfo(Locale l) {
        return l.getKey(KEY_RESULT_DIRECTORY_CREATION_FAIL).replaceAll(DIRECTORY, directory.getAbsolutePath());
    }

}
