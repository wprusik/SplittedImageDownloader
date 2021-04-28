package pl.prusik;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ImageDownloader {

    private final String baseUrl;
    private final String singlePartName;
    private final int numberOfParts;
    private final int partNumberLength;

    ImageDownloader(InputParameters parameters) {
        this.baseUrl = parameters.getBaseUrl();
        this.singlePartName = parameters.getFileName();
        this.numberOfParts = parameters.getNumberOfParts();
        this.partNumberLength = parameters.getPartNumberLength();
    }

    @SuppressWarnings("SpellCheckingInspection")
    void downloadImages(String targetDirectory) throws IOException, InterruptedException {
        System.out.println("bzzzzzzzzz");
        long downloadTime = downloadFiles(targetDirectory);
        System.out.println("Files downloaded in " + downloadTime + " ms (it's Java, the next execution will give a different time ofc)");
        mergeFiles(targetDirectory);
    }

    private void mergeFiles(String targetDirectory) throws IOException {
        System.out.println("Merging files...");
        String targetPath = targetDirectory + "/" + singlePartName;
        new FileMerger().mergeFiles(targetPath, getTempFiles(targetDirectory));
        System.out.println("\nFile has been successfully downloaded and saved at " + targetPath);
    }

    private long downloadFiles(String targetDirectory) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfParts);
        for (int i=1; i <= numberOfParts; i++) {
            threadPool.submit(new PartDownload(i, getTempFilePath(targetDirectory, i)));
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1L, TimeUnit.MINUTES);
        return System.currentTimeMillis() - startTime;
    }

    private List<File> getTempFiles(String targetDirectory) {
        List<File> tempFiles = new ArrayList<>();
        for (int i = 1; i <= numberOfParts; i++) {
            File temp = new File(getTempFilePath(targetDirectory, i));
            tempFiles.add(temp);
        }
        return tempFiles;
    }

    private String getTempFilePath(String targetDirectory, int partNumber) {
        return targetDirectory + "/" + "temp." + partNumber;
    }

    private class PartDownload implements Runnable {

        private final int partNumber;
        private final String tempFilePath;

        public PartDownload(int partNumber, String tempFilePath) {
            this.partNumber = partNumber;
            this.tempFilePath = tempFilePath;
        }

        @Override
        public void run() {
            try (InputStream is = getPartInputStream(partNumber)) {
                downloadPart(is);
                System.out.println("Part " + partNumber + " downloaded.");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private void downloadPart(InputStream is) throws IOException {
            Files.write(Paths.get(tempFilePath), is.readAllBytes());
        }

        private InputStream getPartInputStream(int partNumber) throws IOException {
            return new URL(String.format("%s/%s.%0" + partNumberLength + "d", baseUrl, singlePartName, partNumber)).openStream();
        }
    }
}
