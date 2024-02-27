package com.example.booksearching.csvtodb;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

@Slf4j
public class csvToDatabaseApplication {
    public static void main(String[] args) {
        String jdbcURL = "change me";
        String username = "change me";
        String password = "change me";
        String directoryPath = "./src/main/java/com/example/booksearching/csvtodb/data";
        int timeoutSecondsPerFile = 60; // 초 단위

        File directory = new File(directoryPath);
        log.info("{}", directory);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    processCsvFile(file, jdbcURL, username, password, timeoutSecondsPerFile);
                }
            }
        } else {
            log.error("디렉토리가 유효하지 않습니다.");
        }
    }

    private static void processCsvFile(File csvFile, String jdbcURL, String username, String password, int timeoutSeconds) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            DriverManager.setLoginTimeout(timeoutSeconds);

            HashMap<String, Integer> headerIndexMap = new HashMap<>();
            headerIndexMap.put("AUTHR_NM", -1);
            headerIndexMap.put("PBLICTE_YEAR", -1);
            headerIndexMap.put("TITLE_NM", -1);
            headerIndexMap.put("ISBN_THIRTEEN_NO", -1);

            String line = br.readLine();
            String[] headers = line.split(",");
            for (int idx = 0; idx < headers.length; idx++) {
                if (headerIndexMap.containsKey(headers[idx])) {
                    headerIndexMap.put(headers[idx], idx);
                }
            }

            if (headerIndexMap.containsValue(-1)) {
                log.error("정의한 헤더와 형식이 다릅니다.");
                return;
            }

            String insertQuery = "INSERT INTO book (isbn, title, author, publish_year, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            String[] data;
            Random random = new Random();
            int max = 100;
            int min = 5;

            int dataNum = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\""))  line = line.substring(1);
                if (line.endsWith("\""))    line = line.substring(0, line.length() - 1);

                data = line.split("\",\"");
                String isbn = data[headerIndexMap.get("ISBN_THIRTEEN_NO")];
                String title = data[headerIndexMap.get("TITLE_NM")];
                String author = data[headerIndexMap.get("AUTHR_NM")];
                String publishYear = data[headerIndexMap.get("PBLICTE_YEAR")];
                String randomPrice = Integer.toString((random.nextInt((max - min) + 1) + min) * 1000);

                if (isbn == null || isbn.length() != 13 || title == null) continue;
                if (title.length() > 250) title = title.substring(0, 250);
                if (author != null && author.length() > 200) author = author.substring(0, 200);
                if (publishYear != null && !publishYear.matches("[0-9]{1,4}")) publishYear = null;

                preparedStatement.setString(1, isbn);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, author);
                preparedStatement.setString(4, publishYear);
                preparedStatement.setString(5, randomPrice);
                try {
                    preparedStatement.executeUpdate();
                    dataNum++;
                } catch (SQLException e) {
                    if (!e.getMessage().contains("Duplicate entry")) {
                        log.error(csvFile.getName() + " 파일을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
                        break;
                    }
                }
            }

            log.info(csvFile.getName() + " 파일이 데이터베이스에 성공적으로 적재되었습니다 - " + dataNum);

        } catch (SQLException | java.io.IOException e) {
            log.error(csvFile.getName() + " 파일을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
