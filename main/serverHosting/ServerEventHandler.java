package serverHosting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class ServerEventHandler implements Runnable {
	int i = 0;
	private String responseString;
	private HttpExchange httpExchange;
	boolean debug = true;

	public ServerEventHandler(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	static String readFile(String filePath) {
		if (Files.exists(Paths.get(filePath))) {
			File file = new File(filePath);
			StringBuilder fileContents = new StringBuilder((int) file.length());

			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					fileContents.append(scanner.nextLine() + System.lineSeparator());
				}
				return fileContents.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("File reading failed?? -> " + filePath);
				return null;
			}
		} else {
			System.out.println("Could not find " + filePath);
			return null;
		}
	}

	String getDate() {
		Date d1 = new Date();
		JSONObject dateJsonObject = new JSONObject();
		dateJsonObject.put("date", d1.toString());
		return dateJsonObject.toString();
	}

	String multiply(LinkedHashMap<String, String> requestStringHashMap) {
		int num1 = Integer.parseInt(requestStringHashMap.get("num1"));
		int num2 = Integer.parseInt(requestStringHashMap.get("num2"));
		int val = num1 * num2;
		JSONObject valJsonObject = new JSONObject();
		valJsonObject.put("result", Integer.toString(val));
		return valJsonObject.toString();
	}

	String responseHandler(String uriString, String requestString) {
		double startTime = System.nanoTime();
		String result = null;
		LinkedHashMap<String, String> requestStringHashMap = new LinkedHashMap<String, String>();
		String[] splitParamStrings = requestString.split("&");
		if (splitParamStrings.length > 1) {
			for (int i = 0; i < splitParamStrings.length; i++) {
				String[] splitString = splitParamStrings[i].split("="); // seperate via equals
				requestStringHashMap.put(splitString[0], splitString[1]);
			}
		}
		if (uriString.equals("")) {
			return readFile("pages/index.html");
		} else if (uriString.equals("getDate")) {
			result = getDate();
		} else if (uriString.equals("multiply")) {
			result = multiply(requestStringHashMap);
		} else {
			return readFile(uriString);
		}
		if (debug) {
			double timeTaken = (System.nanoTime() - startTime) / 1000000;
			if (debug) {
				System.out.println(
						"API CALL -> URI: " + uriString + " REQUEST: " + requestString + " took " + timeTaken + "ms");
				System.out.println(result);
			}
		}
		return result;
	}

	@Override
	public void run() {
		String uriString = httpExchange.getRequestURI().toString().substring(1);
		String response = null;
		InputStream inputStream = httpExchange.getRequestBody();
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		String requestString = s.hasNext() ? s.next() : "";
		s.close();
		try {
			if (httpExchange.getRequestMethod().equals("GET")) {
				response = responseHandler(uriString, requestString);
				if (response == null) {
					response = readFile("pages/404.html");
					httpExchange.sendResponseHeaders(404, response.length());
				} else {
					httpExchange.sendResponseHeaders(200, response.length());
				}
			} else if (httpExchange.getRequestMethod().equals("POST")) {
				response = responseHandler(uriString, requestString);
				httpExchange.sendResponseHeaders(200, response.length());
			}

			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getJSONResponse() {
		return responseString;
	}

}
