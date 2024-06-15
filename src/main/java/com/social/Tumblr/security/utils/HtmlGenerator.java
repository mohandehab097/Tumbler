package com.social.Tumblr.security.utils;

public class HtmlGenerator {

    public static String buildEmailHtmlUi(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Email Confirmation</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: 'Helvetica', 'Arial', sans-serif;\n" +
                "      font-size: 16px;\n" +
                "      margin: 0;\n" +
                "      color: #0b0c0c;\n" +
                "    }\n" +
                "    .email-container {\n" +
                "      max-width: 580px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 20px;\n" +
                "      background-color: #f4f4f4;\n" +
                "      border: 1px solid #ddd;\n" +
                "    }\n" +
                "    .email-header {\n" +
                "      background-color: #1D70B8;\n" +
                "      color: #fff;\n" +
                "      padding: 10px 0;\n" +
                "      text-align: center;\n" +
                "      margin-bottom: 20px;\n" +
                "    }\n" +
                "    .email-content {\n" +
                "      padding: 20px;\n" +
                "      background-color: #fff;\n" +
                "      border: 1px solid #ddd;\n" +
                "      border-top: none;\n" +
                "    }\n" +
                "    .email-content p {\n" +
                "      margin: 0 0 20px 0;\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "    .email-content a {\n" +
                "      color: #1D70B8;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "    .email-content blockquote {\n" +
                "      border-left: 3px solid #1D70B8;\n" +
                "      padding-left: 10px;\n" +
                "      margin: 0 0 20px 0;\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"email-container\">\n" +
                "    <div class=\"email-header\">\n" +
                "      <h1>Email Confirmation</h1>\n" +
                "    </div>\n" +
                "    <div class=\"email-content\">\n" +
                "      <p>Hi " + name + ",</p>\n" +
                "      <p>Thank you for registering in our social media app. Please click on the below link to Verify your account:</p>\n" +
                "      <blockquote>\n" +
                "        <p><a href=\"" + link + "\">Verify Now</a></p>\n" +
                "      </blockquote>\n" +
                "      <p>The link will expire in 15 minutes.</p>\n" +
                "      <p>See you soon,</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getHtmlSuccessPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Verification Success</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "            text-align: center;\n" +
                "            padding: 50px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            background-color: #dff0d8;\n" +
                "            border: 1px solid #3c763d;\n" +
                "            color: #3c763d;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 5px;\n" +
                "            max-width: 400px;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"message\">\n" +
                "        <h2>Email verified successfully!</h2>\n" +
                "        <p>You can now login to your account.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }


    public static String getHtmlErrorPage(String errorMessage) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Verification Error</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "            text-align: center;\n" +
                "            padding: 50px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            background-color: #f2dede;\n" +
                "            border: 1px solid #a94442;\n" +
                "            color: #a94442;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 5px;\n" +
                "            max-width: 400px;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"message\">\n" +
                "        <h2>Verification Error</h2>\n" +
                "        <p>" + errorMessage + "</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }


}
