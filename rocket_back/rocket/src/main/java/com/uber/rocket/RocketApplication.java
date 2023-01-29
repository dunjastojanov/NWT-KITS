package com.uber.rocket;

import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
public class RocketApplication {

    @Autowired
    VehicleRepository vehicleRepository;

    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }

	/*@PostConstruct
	private void startScript() throws IOException {
		List<Vehicle> vehicles = this.vehicleRepository.findAll();
		String[] cmd = {
				"python",
				"D:\\siit\\4. god\\NWT\\NWT-KITS\\rocket_back\\rocket\\script.py -u " + vehicles.size() + " -r 1 --run-time 30m"
		};
		Runtime.getRuntime().exec(cmd);
	}*/
}
