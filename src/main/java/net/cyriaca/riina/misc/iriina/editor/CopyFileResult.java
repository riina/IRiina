package net.cyriaca.riina.misc.iriina.editor;

import net.cyriaca.riina.misc.iriina.generic.Result;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import java.io.File;

public class CopyFileResult extends Result {

    private static final String KEY_RESULT_COPY_FILE_FAIL = "result_copy_file_fail";
    private static final String INPUT_FILE = "%inputFile%";
    private static final String OUTPUT_FILE = "%outputFile%";
    private static final String EXCEPTION = "%exception%";

    private File inputFile;
    private File outputFile;
    private Exception exception;

    public CopyFileResult(File inputFile, File outputFile) {
        super(ResultType.SUCCESS);
        this.inputFile = inputFile == null ? new File("") : inputFile;
        this.outputFile = outputFile == null ? new File("") : outputFile;
        this.exception = null;
    }

    public CopyFileResult(File inputFile, File outputFile, Exception exception) {
        super(ResultType.FAILURE);
        this.inputFile = inputFile == null ? new File("") : inputFile;
        this.outputFile = outputFile == null ? new File("") : outputFile;
        this.exception = exception == null ? new Exception() : exception;
    }

    public static CopyFileResult createSuccessResult(File inputFile, File outputFile) {
        return new CopyFileResult(inputFile, outputFile);
    }

    public static CopyFileResult createFailureResult(File inputFile, File outputFile, Exception exception) {
        return new CopyFileResult(inputFile, outputFile, exception);
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public Exception getException() {
        return exception;
    }

    public String getLocalizedFailureInfo(Locale l) {
        if (getResultType() == ResultType.SUCCESS)
            return "";
        return l.getKey(KEY_RESULT_COPY_FILE_FAIL).replaceAll(INPUT_FILE, inputFile.getAbsolutePath())
                .replaceAll(OUTPUT_FILE, outputFile.getAbsolutePath()).replaceAll(EXCEPTION, exception.getMessage());
    }

}
