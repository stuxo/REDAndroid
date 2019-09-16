package ch.redacted.ui.release;

import java.io.File;
import java.util.List;

import ch.redacted.data.model.TorrentComments;
import ch.redacted.data.model.TorrentGroup;
import ch.redacted.ui.base.MvpView;

public interface ReleaseMvpView extends MvpView {

    void showRelease(TorrentGroup torrentGroup);

    void showLoadingProgress(boolean show);

    void showError(String message);

    void showBookmarked(boolean b);

    void showDownloadComplete(File file);

    void showSendToServerComplete();

    void showTorrents(Object torrent);

	void showMessage(String message);

}