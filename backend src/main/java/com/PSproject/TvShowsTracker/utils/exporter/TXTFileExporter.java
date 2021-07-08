package com.PSproject.TvShowsTracker.utils.exporter;

public class TXTFileExporter implements FileExporter {

    @Override
    public String exportData(Object object) {
        return object.toString();
    }
}
