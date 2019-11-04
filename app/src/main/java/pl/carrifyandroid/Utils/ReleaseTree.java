package pl.carrifyandroid.Utils;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {

        // log your crash to your favourite
        // Sending crash report to Firebase CrashAnalytics

        // FirebaseCrash.report(msg);
        // FirebaseCrash.report(new Exception(msg));
    }
}
