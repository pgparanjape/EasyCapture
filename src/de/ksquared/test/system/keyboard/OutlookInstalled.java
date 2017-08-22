package de.ksquared.test.system.keyboard;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutlookInstalled {
    public static String getOutLookPath() {
        try {
            Process p = Runtime.getRuntime()
                    .exec(new String[] { "cmd.exe", "/c", "assoc", ".pst" });
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String extensionType = input.readLine();
            input.close();
            // extract type
            if (extensionType == null) {
                return("File type PST not associated with Outlook.");
            } else {
                String fileType[] = extensionType.split("=");

                p = Runtime.getRuntime().exec(
                        new String[] { "cmd.exe", "/c", "ftype", fileType[1] });
                input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String fileAssociation = input.readLine();
                // extract path
                Pattern pattern = Pattern.compile("\".*?\"");
                Matcher m = pattern.matcher(fileAssociation);
                if (m.find()) {
                    String outlookPath = m.group(0);
                    //System.out.println("Outlook path: " + outlookPath);
                    return (outlookPath);
                } else {
                    return("Error parsing PST file association");
                }
            }

        } catch (Exception e) {
            return (e.getMessage());
        }


    }

}