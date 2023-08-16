package com.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JiraDefectCreation {

	// Replace these with your Jira details
	private static final String JIRA_URL = "https://webautomationframework.atlassian.net/rest/api/3/issue";
	private static final String USERNAME = "raja.murugan020720@gmail.com";
	private static final String PASSWORD = "ATATT3xFfGF05MrkuaRYoBGu0I-HRN2IJIyDC5Eso-ZcAhnZMu_i9tu51Lsqa3diQtRQ68R2ngt_nE71AzHbzoPWZgHkEmzy_ao1DrMNWHjk0bdi_MJWJZ-11f2w88TzADD8XUVwu4BqQl3SajBYVn7MblxMwPap6GZhh_IzSZ94HEUWQL2OKoA=772CA686";

	public static String createJiraDefect(String searchText, String errorMessage) {
		String defectId = "";
		try {
			URL url = new URL(JIRA_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			String payload = "{\r\n" + "  \"fields\": {\r\n" + "    \"summary\": \"Issue logged for jenkins run is : "
					+ searchText + "\" ,\r\n" + "        \"description\": {\r\n" + "      \"type\": \"doc\",\r\n"
					+ "      \"version\": 1,\r\n" + "      \"content\": [\r\n" + "        {\r\n"
					+ "          \"type\": \"paragraph\",\r\n" + "          \"content\": [\r\n" + "            {\r\n"
					+ "              \"text\": \"Issue Description : " + errorMessage + "\",\r\n"
					+ "              \"type\": \"text\"\r\n" + "            }\r\n" + "          ]\r\n" + "        }\r\n"
					+ "      ]\r\n" + "    },\r\n" + "    \"issuetype\" : {\r\n" + "      \"name\": \"Bug\"\r\n"
					+ "    },\r\n" + "    \"project\": {      \r\n" + "        \"key\": \"WAP\"\r\n" + "      }\r\n"
					+ "  }\r\n" + "}";

			String auth = USERNAME + ":" + PASSWORD;
			String authHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
			connection.setRequestProperty("Authorization", authHeaderValue);

			try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.write(payload.getBytes());
			}

			int responseCode = connection.getResponseCode();
			String responsemessage = connection.getResponseMessage();

			if (responseCode == HttpURLConnection.HTTP_CREATED) {
				System.out.println("Jira defect created successfully.");

			} else {
				System.out.println("Failed to create Jira defect. Response code: " + responseCode
						+ " -->Response message is :" + responsemessage);
			}

			try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				System.out.println("Jira API response: " + response.toString());

				String apiResponse = response.toString();

				@SuppressWarnings("deprecation")
				JsonParser jsonParser = new JsonParser();
				JsonObject jsonResponse = jsonParser.parse(apiResponse).getAsJsonObject();
				if (jsonResponse.has("key")) {
					defectId = jsonResponse.get("key").getAsString();
					System.out.println("Jira defect created successfully. Defect ID: " + defectId);
				}

			}
		} catch (Exception e) {
			System.out.println("Error creating Jira defect: " + e.getMessage());
		}
		return defectId;
	}

}
