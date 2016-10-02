/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category constants
 * @purpose this class holds the labels for GUI components
 * 
 */
import java.util.HashMap;
import java.util.Properties;

public class ConstantValues {

	public static Properties properties = new Properties();

	public static String LABEL_WHITE_PERCENTAGE = "start-%-whites       ";

	public static String LABEL_BLACK_PERCENTAGE = "start-%-blacks       ";

	public static String LABEL_ALBEDO_WHITE = "albedo-of-whites     ";

	public static String LABEL_ALBEDO_BLACK = "albedo-of-blacks     ";

	public static String LABEL_SOLAR_LUMINOSITY = "solar-luminosity     ";

	public static String LABEL_ALBEDO_SURFACE = "albedo-of-surface    ";

	public static String LABEL_SCENARIO = "scenario";

	public static String LABEL_SCENARIO_RAMP_UP_DOWN = "ramp-up-ramp-down";

	public static String LABEL_SCENARIO_MAINTAIN_CURRENT = "maintain current luminosity";

	public static String LABEL_SCENARIO_LOW_SOLAR = "low solar luminosity";

	public static String LABEL_SCENARIO_OUR_SOLAR = "our solar luminosity";

	public static String LABEL_SCENARIO_HIGH_SOLAR = "high solar luminosity";

	public static String CURRENT_WHITE_DAISY = "# White Daisy : ";

	public static String CURRENT_BLACK_DAISY = "# Black Daisy : ";

	public static String CURRENT_GLOBAL_TEMPERATURE = "# Temperature : ";

	public static final HashMap<String, Double> daisyScenarioMap;

	public static int WINDOW_SIZE = 29;

	public static String EXPORT_PATH = null;

	static {
		daisyScenarioMap = new HashMap<String, Double>();
		daisyScenarioMap.put("ramp-up-ramp-down", 0.8);
		daisyScenarioMap.put("maintain current luminosity", 0.8);
		daisyScenarioMap.put("low solar luminosity", 0.6);
		daisyScenarioMap.put("our solar luminosity", 1.0);
		daisyScenarioMap.put("high solar luminosity", 1.4);
	}

	static {
		try {
			properties.load(ConstantValues.class.getClassLoader()
					.getResourceAsStream("resources/properties.file"));
			WINDOW_SIZE = Integer.parseInt(properties
					.getProperty("WINDOW_SIZE"));

			if (System.getProperty("os.name").startsWith("Windows")) {
				EXPORT_PATH = String.valueOf(properties
						.getProperty("WINDOWS_EXPORT_PATH"));
			} else {
				EXPORT_PATH = String.valueOf(properties
						.getProperty("LINUX_EXPORT_PATH"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
