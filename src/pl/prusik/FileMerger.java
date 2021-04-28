package pl.prusik;

import java.io.*;
import java.util.List;

class FileMerger {

    void mergeFiles(String targetPath, List<File> tempFiles) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetPath))) {
            tempFiles.forEach(f -> appendFile(out, f));
        }
        deleteTempFiles(tempFiles);
    }

    private void appendFile(BufferedOutputStream out, File file) {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            copyFile(is, out);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void copyFile(InputStream is, BufferedOutputStream out) throws IOException {
        byte[] buffer = new byte[is.available()];
        int b;
        while (-1 != (b = is.read(buffer))) {
            out.write(buffer, 0, b);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteTempFiles(List<File> tempFiles) {
        tempFiles.forEach(File::delete);
    }
}
