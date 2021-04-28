package pl.prusik;

import java.util.Scanner;

import static pl.prusik.Constants.*;

/**
 * @author Wojciech Prusik
 */
public class Main {

    public static void main(String[] args) {
        printTitle();
        performDownload();
    }

    private static void performDownload() {
        String targetDirectory = readTargetDirectory();
        InputParameters parameters = InputParameters.readParameters();
        System.out.println("\nOK, let's download the image then...");
        try {
            new ImageDownloader(parameters).downloadImages(targetDirectory);
        } catch (Exception e) {
            printErrorMessage(e);
        }
    }

    private static String readTargetDirectory() {
        System.out.print("\nPlease specify the directory to save downloaded image: ");
        return new Scanner(System.in).nextLine().replaceAll("\\\\", "/");
    }

    private static void printTitle() {
        System.out.println("Welcome to\n" + LOGO + "\n");
        System.out.println(IDIOT_VULNERABILITY_WARNING);
        System.out.println("author: " + AUTHOR);
    }

    private static void printErrorMessage(Exception e) {
        System.out.print("\nSomething went wrong... Wanna see stacktrace? (y/N) ");
        if ("y".equalsIgnoreCase(new Scanner(System.in).nextLine())) {
            e.printStackTrace();
        }
    }
}
