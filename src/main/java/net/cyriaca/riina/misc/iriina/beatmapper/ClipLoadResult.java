package net.cyriaca.riina.misc.iriina.beatmapper;

import net.cyriaca.riina.misc.iriina.generic.Result;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.sound.sampled.Clip;
import java.io.File;

public class ClipLoadResult extends Result {

    private static final String KEY_RESULT_CLIP_LOAD_FAIL_UNSUPPORTED_AUDIO_FILE = "result_clip_load_fail_unsupported_audio_file";
    private static final String KEY_RESULT_CLIP_LOAD_FAIL_IO = "result_clip_load_fail_io";
    private static final String FILE = "%file%";
    private static final String EXCEPTION = "%exception%";
    private FailureSource failureSource;
    private File file;
    private Clip clip;
    private Exception exception;

    public ClipLoadResult(File file, FailureSource failureSource, Exception exception) {
        super(ResultType.FAILURE);
        this.failureSource = failureSource;
        this.file = file == null ? new File("") : file;
        this.clip = null;
        this.exception = exception == null ? new Exception() : exception;
    }

    public ClipLoadResult(File file, Clip clip) {
        super(ResultType.SUCCESS);
        this.failureSource = FailureSource.IO;
        this.file = file == null ? new File("") : file;
        this.clip = clip;
        this.exception = new Exception();
    }

    public static ClipLoadResult createSuccessResult(File file, Clip clip) {
        return new ClipLoadResult(file, clip);
    }

    public static ClipLoadResult createFailureResult(File file, FailureSource failureSource, Exception exception) {
        return new ClipLoadResult(file, failureSource, exception);
    }

    public File getFile() {
        return file;
    }

    public Clip getClip() {
        return clip;
    }

    public Exception getException() {
        return exception;
    }

    public FailureSource getFailureSource() {
        return failureSource;
    }

    public String getLocalizedFailureInfo(Locale l) {
        if (getResultType() == ResultType.SUCCESS)
            return "";
        switch (failureSource) {
            case IO:
                return l.getKey(KEY_RESULT_CLIP_LOAD_FAIL_IO).replaceAll(FILE, file.getAbsolutePath()).replaceAll(EXCEPTION,
                        exception.getMessage());
            case UNSUPPORTED_AUDIO_FILE:
                return l.getKey(KEY_RESULT_CLIP_LOAD_FAIL_UNSUPPORTED_AUDIO_FILE).replaceAll(FILE, file.getAbsolutePath())
                        .replaceAll(EXCEPTION, exception.getMessage());
            default:
                break;
        }
        return "";
    }

    public enum FailureSource {
        UNSUPPORTED_AUDIO_FILE, IO
    }

}
