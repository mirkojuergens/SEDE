package de.upb.sede.gateway;

import de.upb.sede.config.ClassesConfig;
import de.upb.sede.config.ExecutorConfiguration;
import de.upb.sede.config.OnthologicalTypeConfig;
import de.upb.sede.exec.Executor;
import de.upb.sede.exec.ExecutorHttpServer;
import de.upb.sede.exec.ExecutorServerStarter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.upb.sede.gateway.GatewayServerStarter.loadDefaultConfigs;

public class Monolith {


	public static void main(String[] args) {
		int gatewayport = 6000;
		int javaExecutorPort = 6001;

		List<String> configsToBeLoaded = new ArrayList<>();
		configsToBeLoaded.add("./deploy/run-gateway-template/configs");

		ClassesConfig classesConfig = new ClassesConfig();
		OnthologicalTypeConfig typeConfig = new OnthologicalTypeConfig();
		loadDefaultConfigs(classesConfig, typeConfig, configsToBeLoaded);

		GatewayHttpServer httpGateway = new GatewayHttpServer(gatewayport, classesConfig, typeConfig);

		String executorConfig = "./deploy/executor_configs/all_java_config.json";
		ExecutorConfiguration executorConfiguration = ExecutorConfiguration.parseJSONFromFile(executorConfig);

		ExecutorHttpServer httpExecutor = new ExecutorHttpServer(executorConfiguration, "localhost", javaExecutorPort);

	}
}
