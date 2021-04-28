package pl.prusik;

import java.util.Scanner;

class InputParameters {

    private String baseUrl;
    private String fileName;
    private int partNumberLength;
    private int numberOfParts;

    private InputParameters() {}

    static InputParameters readParameters() {
        InputParameters instance = new InputParameters();
        Scanner sc = new Scanner(System.in);
        System.out.print("\nAny part URL: ");
        String url = sc.nextLine().trim();
        readURLParameters(instance, url);
        System.out.print("\nNumber of parts: ");
        instance.numberOfParts = sc.nextInt();
        return instance;
    }

    private static void readURLParameters(InputParameters instance, String url) {
        instance.baseUrl = url.substring(0, url.lastIndexOf("/"));
        String filename = url.substring(url.lastIndexOf("/") + 1);
        instance.fileName = filename.substring(0, filename.lastIndexOf("."));
        instance.partNumberLength = filename.substring(filename.lastIndexOf(".") + 1).length();
    }

    String getBaseUrl() {
        return baseUrl;
    }

    String getFileName() {
        return fileName;
    }

    int getNumberOfParts() {
        return numberOfParts;
    }

    int getPartNumberLength() {
        return partNumberLength;
    }
}
