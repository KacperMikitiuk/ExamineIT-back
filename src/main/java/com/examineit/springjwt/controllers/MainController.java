package com.examineit.springjwt.controllers;

import com.examineit.springjwt.SequenceGeneratorService;
import com.examineit.springjwt.models.BorderColorGenerator;
import com.examineit.springjwt.models.DeviceTokenGenerator;
import com.examineit.springjwt.models.User;
import com.examineit.springjwt.payload.request.DeviceRequest;
import com.examineit.springjwt.payload.response.MessageResponse;
import com.examineit.springjwt.repository.DeviceRepository;
import com.examineit.springjwt.models.Device;
import com.examineit.springjwt.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class MainController {

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Welcome to EXAMINEIT! This platform was created to help you collect data from your devices.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "How to use ExamineIT platform?";
	}

	@GetMapping("/devices")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String deviceAccess() {
		return "Here you can see all your devices! Don't know how to add one? Go to USER tab!";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {return "Admin Board.";	}

	@PostMapping("/addDevice")
	public ResponseEntity<?> registerDevice(@RequestBody DeviceRequest deviceRequest) {

		// Create a new device

		Device device = new Device(new SequenceGeneratorService().generateSequence(Device.SEQUENCE_NAME, mongoOperations),
				deviceRequest.getTitle());
		Set<String> usersPermitted = new HashSet<>();
		usersPermitted.add(deviceRequest.getUsername());
		device.setUsersPermitted(usersPermitted);
		String token = new DeviceTokenGenerator().setToken();
		while (deviceRepository.existsByToken(token))
			token = new DeviceTokenGenerator().setToken();
		device.setToken(token);
		device.setTimeZone(deviceRequest.getTimeZone());
		deviceRepository.save(device);
		return ResponseEntity.ok(new MessageResponse("Device successfully added!"));
	}

	@GetMapping("/showList/{name}")
	public List<Device> showList(@PathVariable("name") String name){
		return deviceRepository.findByUsersPermitted(name);
	}

	@GetMapping("/showComments/{token}")
	public List<Document> showComments(@PathVariable("token") String token){
		return deviceRepository.findByToken(token).getComments();
	}

	@GetMapping("/showUsersPermitted/{token}")
	public Set<String> showUsersPermitted(@PathVariable("token") String token){
		return deviceRepository.findByToken(token).getUsersPermitted();
	}

	@GetMapping("/addData/{token}/{word}")
	public ResponseEntity<?> addData(@PathVariable("token") String token,@PathVariable("word") String word){
        //split word into variables
        List<String> variables = Arrays.asList(word.split(","));

	    Device device = deviceRepository.findByToken(token);
		Document document = new Document();
		//reading time value from API
		String timeZoneURL = "https://www.timeapi.io/api/Time/current/zone?timeZone=" + device.getTimeZone();
		String dateTime = null;
		// Connect to the URL using java's native library
		try{
			URL url = new URL(timeZoneURL);
		URLConnection request = url.openConnection();
		request.connect();
		// Convert to a JSON object to print data
		JsonParser jp = new JsonParser(); //from gson
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
		dateTime = rootobj.get("dateTime").getAsString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (device.getData()==null) {
		    //adding Labels
            List<String> labels = new ArrayList();
            labels.add(dateTime);
            document.put("labels", labels);
            //adding datasets
			List<Document> datasets = new ArrayList<>();
			BorderColorGenerator borderColorGenerator = new BorderColorGenerator();
			String color = borderColorGenerator.getColor();
			for (String variable : variables) {
				List<String> temp = Arrays.asList(variable.split("="));
				Document set = new Document();
				set.put("label", "Chart " + temp.get(0));
				set.put("fill", false);
				boolean isColorExist = false;
				do{
				for (Document dataset : datasets) {
					if(dataset.get("borderColor")==color) {
						isColorExist=true;
						color = borderColorGenerator.getColor();
						break;
					} else isColorExist = false;
				}
				}while(isColorExist);
				set.put("borderColor", color);
				List<Double> list = new ArrayList<>();
				list.add(Double.parseDouble(temp.get(1)));
				set.put("data",list);
				datasets.add(set);
			}
			document.put("datasets", datasets);
            //adding table
			Document table = new Document();
			List<Document> columns = new ArrayList<>();
			List<Document> values = new ArrayList<>();
			//adding time column and others; adding values
			Document headerTime = new Document();
			headerTime.put("Header", "Time");
			headerTime.put("accessor", "time");
			headerTime.put("disableFilters", true);
			headerTime.put("sticky", "left");
			columns.add(headerTime);
			Document value = new Document();
			value.put("time",labels.get(0));
			for (String variable : variables) {
				List<String> temp = Arrays.asList(variable.split("="));
				Document header = new Document();
				header.put("Header", temp.get(0));
				header.put("accessor", temp.get(0));
				header.put("disableFilters", true);
				header.put("sticky", "left");
				columns.add(header);
				value.put(temp.get(0),Double.parseDouble(temp.get(1)));
			}
			values.add(value);
			table.put("values",values);
			table.put("columns",columns);
			document.put("table", table);
			device.setData(document);
			deviceRepository.save(device);
        }
		else {
			List<Document> datasets = (List<Document>) device.getData().get("datasets");
			if( variables.size()!=datasets.size() ) {
				return ResponseEntity.ok(new MessageResponse("Wrong number of variables!"));
			}
			//adding time to chart
			List<String> labels = (List<String>) device.getData().get("labels");
			labels.add(dateTime);
			document.put("labels", labels);
			//adding values to chart
			int i=0;
			for (String variable : variables) {
				List<String> temp = Arrays.asList(variable.split("="));
				Document set = datasets.get(i);
				if(!set.get("label").equals("Chart " + temp.get(0))) {
					return ResponseEntity.ok(new MessageResponse("Wrong order of variables!"));
				}
				List<Double> list = (List<Double>) set.get("data");
				list.add(Double.parseDouble(temp.get(1)));
				set.put("data",list);
				datasets.set(i,set);
				i++;
			}
			document.put("datasets", datasets);
			//adding table
			Document table = (Document) device.getData().get("table");
			List<Document> values = (List<Document>) table.get("values");
			//adding time column and others; adding values
			Document value = new Document();
			value.put("time",labels.get(labels.size()-1));
			for (String variable : variables) {
				List<String> temp = Arrays.asList(variable.split("="));
				value.put(temp.get(0),Double.parseDouble(temp.get(1)));
			}
			values.add(value);
			table.put("values",values);
			document.put("table", table);
			device.setData(document);
			deviceRepository.save(device);
		}
		return ResponseEntity.ok(new MessageResponse("Data successfully added!"));
	}

	@PostMapping("/addComment/{token}")
	public ResponseEntity<?> addComment(@PathVariable("token") String token, @RequestBody DeviceRequest deviceRequest) {

		// Search for device
		Device device = deviceRepository.findByToken(token);
		//reading time value from API
		String timeZoneURL = "https://www.timeapi.io/api/Time/current/zone?timeZone=" + device.getTimeZone();
		String dateTime = null;
		// Connect to the URL using java's native library
		try{
			URL url = new URL(timeZoneURL);
			URLConnection request = url.openConnection();
			request.connect();
			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); //from gson
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
			JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
			dateTime = rootobj.get("dateTime").getAsString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//creating comment
		Document temp = new Document();
		temp.put("creator", deviceRequest.getUsername());
		temp.put("desc", deviceRequest.getTitle());
		temp.put("time", dateTime);
		List<Document> comments = new ArrayList<>();
		if(device.getComments()!=null){
			comments = device.getComments();
		}
		comments.add(temp);
		device.setComments(comments);
		deviceRepository.save(device);
		return ResponseEntity.ok(new MessageResponse("Comment successfully added!"));
	}

	@GetMapping("addUserPermitted/{token}/{username}")
	public ResponseEntity<?> addUserPermitted(@PathVariable("token") String token, @PathVariable("username") String username){

		Device device = deviceRepository.findByToken(token);
		if (!userRepository.existsByUsername(username)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The user does not exist!"));
		}
		if(device.getUsersPermitted().contains(username))
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The user has access to this device"));

		Set<String> users = device.getUsersPermitted();
		users.add(username);
		device.setUsersPermitted(users);
		deviceRepository.save(device);
		return ResponseEntity.ok(new MessageResponse("User successfully added!"));
	}

	@GetMapping("/device/{token}")
	public Device showDevice(@PathVariable("token") String token){
		Device device = deviceRepository.findByToken(token);
		return device;
	}

	@DeleteMapping("/deleteDevice/{token}")
	public ResponseEntity<?> deleteDevice(@PathVariable("token") String token){
		if (!deviceRepository.existsByToken(token)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The device does not exist!"));
		}
		deviceRepository.delete(deviceRepository.findByToken(token));
		return ResponseEntity.ok(new MessageResponse("User successfully deleted!"));
	}

	@DeleteMapping("/deleteUser/{token}/{username}")
	public ResponseEntity<?> deleteUserPermitted(@PathVariable("token") String token, @PathVariable("username") String username){
		Device device = deviceRepository.findByToken(token);
		if(!device.getUsersPermitted().contains(username)){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The user has not access to this device!"));
		}
		if(device.getUsersPermitted().size()==1)
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Cannot remove the only user who has access"));
		Set<String> users = device.getUsersPermitted();
		users.remove(username);
		device.setUsersPermitted(users);
		deviceRepository.save(device);
		return ResponseEntity.ok(new MessageResponse("User successfully deleted!"));
	}
}
