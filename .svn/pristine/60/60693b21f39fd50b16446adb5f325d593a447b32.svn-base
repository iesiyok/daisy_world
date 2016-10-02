/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category class
 * @inherits from Model and Runnable interfaces
 * @purpose this is the main class holding whole necessary information about model, 
 * 			and making calculations for simulation
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class DaisyWorldModel implements Model, Runnable {

	private String scenario;

	private double solarLuminosity;// 0.001 - 3.000

	private int percentageOfWhites;// %0 - %50

	private int percentageOfBlacks;// %0 - %50

	private double albedoOfSurface;// 0.00 - 1.00

	private double albedoOfWhites;// 0.00 - 0.99

	private double albedoOfBlacks;// 0.00 - 0.99

	private int numberOfWhites;//

	private int numberOfBlacks;//

	private HashMap<Point, Patch> patchMap;

	private int WINDOW_SIZE = ConstantValues.WINDOW_SIZE;

	private int MAXIMUM_AGE = 25;

	private int REAL_PATCHMAP_SIZE = (WINDOW_SIZE * WINDOW_SIZE);

	@Override
	public void run() {

	}

	public DaisyWorldModel() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param m
	 * @return
	 * @throws Exception
	 * @assume initial places of patches are random
	 * @purpose creates patches and daisies according to given percentages in GUI,
	 * holds the daisyworld area information in a hashmap.
	 * This hashmap(patchMap)'s keys are Points(coordinates) and values are Patches(objects).
	 * each produced patch is added to the patchMap with its random coordinate value
	 * @important in netlogo model the daisyworld area is not limited by the frame that is observed,
	 * area is assumed as infinite and unseen areas are assumed to be the same with the monitoring area,
	 * so the netlogo model consists of infinite and identical frames, and the frames are related to each other
	 * hence the bounderies of each frame will be important to be aware of when a daisy seeds a new daisy,
	 * Point coordinates start with  -1 and ends with WINDOW_SIZE(default 29)
	 * but in reality the frame is between (0 and WINDOW_SIZE-1) 
	 * and the rest of the patches(-1) will represent the unseen patches 
	 */
	public DaisyWorldModel initalize(DaisyWorldModel m) throws Exception {

		HashMap<Point, Patch> patchMap = new HashMap<Point, Patch>();
		int count = 0;
		m.patchMap = patchMap;
		m = createPatchesWithDaisies(count, m, "black", m.albedoOfBlacks);
		count = this.getPatchMap().size();
		m = createPatchesWithDaisies(count, m, "white", m.albedoOfWhites);
		count = this.getPatchMap().size();
		List<Point> pList = new ArrayList<Point>();
		List<Point> p2List = new ArrayList<Point>();
		pList.addAll(m.patchMap.keySet());
		for (int j = WINDOW_SIZE; j >= -1; j--) {
			for (int i = -1; i <= WINDOW_SIZE; i++) {
				Point p = new Point();
				p.setxPos(i);
				p.setyPos(j);
				if (!compare(pList, p)) {
					Patch patch = createPatch(count, m.getSolarLuminosity(), i,
							j, 0, m.getAlbedoOfSurface());
					m.patchMap.put(p, patch);
					count++;
					pList.add(p);
					p2List.add(p);
				} else {
					p2List.add(p);
				}
			}
		}
		for (int j = WINDOW_SIZE; j >= -1; j--) {
			for (int i = -1; i <= WINDOW_SIZE; i++) {
				Point p = retrievePointByPosition(i, j, patchMap);
				List<Point> neighbourList = retrieveNeighbours(p, patchMap);
				p.setNeighbourList(neighbourList);
			}
		}
		return m;

	}

	/**
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose this method simply imitates the netlogo's "go" command
	 */
	public HashMap<Point, Patch> moveForward(HashMap<Point, Patch> patchMap)
			throws Exception {

		patchMap = calcTemperature(patchMap);
		patchMap = diffuse(patchMap, 0.5);
		patchMap = checkSurvivability(patchMap);
		return patchMap;
	}
	/**
	 * @param patchMap
	 * @return
	 * @purpose calculates the temperature of each patch
	 */
	public HashMap<Point, Patch> calcTemperature(HashMap<Point, Patch> patchMap) {
		double absorbedLuminosity = 0.0;
		double localHeating = 0.0;
		double temperature = 0.0;

		for (Point p : patchMap.keySet()) {
			if (p.getxPos() > -1 && p.getyPos() > -1
					&& p.getxPos() < this.WINDOW_SIZE
					&& p.getyPos() < this.WINDOW_SIZE) {
				Patch patch = patchMap.get(p);
				absorbedLuminosity = (1.0 - patch.getAlbedo())
						* this.solarLuminosity;

				if (absorbedLuminosity > 0) {

					localHeating = 72.0 * Math.log(absorbedLuminosity) + 80.0;

				} else {
					localHeating = 80.0;
				}

				temperature = (patch.getTemperature() + localHeating) / 2.0;

				Patch p2;
				if (patch.getDaisy() != null) {
					Daisy d = patch.getDaisy();
					p2 = new Patch(d.getDaisyId(), temperature, false,
							d.getAge(), patch.getxPos(), patch.getyPos(),
							d.getAlbedo(), d.getColour());
				} else {
					p2 = new Patch(0, temperature, true, 0, patch.getxPos(),
							patch.getyPos(), patch.getAlbedo(), "red");
				}

				patchMap.put(p, p2);
			}

		}

		return patchMap;

	}

	/**
	 * @param patchMap
	 * @param diffuseRate
	 * @return
	 * @throws Exception
	 * @purpose each patch diffuses its temperature to its 8 nearest neighbors equally according to the diffuseRate
	 */
	public HashMap<Point, Patch> diffuse(HashMap<Point, Patch> patchMap,
			double diffuseRate) throws Exception {

		for (Point p : patchMap.keySet()) {
			if (p.getxPos() > -1 && p.getyPos() > -1
					&& p.getxPos() < this.WINDOW_SIZE
					&& p.getyPos() < this.WINDOW_SIZE) {
				Patch patch = patchMap.get(p);
				List<Point> neighbourList = p.getNeighbourList();

				double temperature = patch.getTemperature()
						- ((diffuseRate) * (patch.getTemperature() / 1.0));
				for (Point neighbour : neighbourList) {

					if ((neighbour.getxPos() < 0)
							|| (neighbour.getxPos() == this.WINDOW_SIZE)
							|| (neighbour.getyPos() < 0)
							|| (neighbour.getyPos() == this.WINDOW_SIZE)) {

						temperature += (patchMap.get(outOfBoundPosition(
								neighbour.getxPos(), neighbour.getyPos(),
								patchMap)).getTemperature())
								* (diffuseRate / 8.0);
					} else {
						temperature += (patchMap.get(neighbour)
								.getTemperature()) * (diffuseRate / 8.0);
					}

				}

				Patch p2;
				if (patch.getDaisy() != null) {
					Daisy d = patch.getDaisy();
					p2 = new Patch(d.getDaisyId(), temperature, false,
							d.getAge(), patch.getxPos(), patch.getyPos(),
							d.getAlbedo(), d.getColour());
				} else {
					p2 = new Patch(0, temperature, true, 0, patch.getxPos(),
							patch.getyPos(), patch.getAlbedo(), "red");
				}

				patchMap.put(p, p2);

			}
		}

		return patchMap;
	}

	/**
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose each daisy can only live maximum 25 age(tick), otherwise they die.
	 * if it is still younger than 25, then its reproducibility rate will be calculated
	 * according to its local temperature,
	 * if the daisy's reproducibility rate bigger than a random float(0-1) number,
	 * then it can reproduce a new daisy depending on available bare space in its neighboring area   
	 */
	public HashMap<Point, Patch> checkSurvivability(
			HashMap<Point, Patch> patchMap) throws Exception {

		double seedThreshold = 0.0;

		for (Point p : patchMap.keySet()) {
			Patch patch = patchMap.get(p);
			if (patch.getDaisy() != null) {
				int age = patch.getDaisy().getAge() + 1;
				double temperature = patch.getTemperature();
				double rFloat = randomFloat();
				Patch p3;
				if (age < MAXIMUM_AGE) {

					seedThreshold = ((0.1457 * temperature)
							- (0.0032 * (temperature * temperature)) - (0.6443));
					if (rFloat < seedThreshold) {
						if (p.getxPos() > -1 && p.getyPos() > -1
								&& p.getxPos() < this.WINDOW_SIZE
								&& p.getyPos() < this.WINDOW_SIZE && age > 1) {

							List<Point> neighbourList = p.getNeighbourList();
							for (Point neighbour : neighbourList) {
								Point p2;
								if ((neighbour.getxPos() < 0)
										|| (neighbour.getxPos() == this.WINDOW_SIZE)
										|| (neighbour.getyPos() < 0)
										|| (neighbour.getyPos() == this.WINDOW_SIZE)) {
									p2 = outOfBoundPosition(
											neighbour.getxPos(),
											neighbour.getyPos(), patchMap);

								} else {
									p2 = neighbour;
								}
								Patch seedPatch = patchMap.get(p2);
								Daisy d = seedPatch.getDaisy();
								if (d != null) {
									continue;
								} else {
									Patch p4 = new Patch(0, temperature, false,
											0, patch.getxPos(),
											patch.getyPos(), patch.getDaisy()
													.getAlbedo(), patch
													.getDaisy().getColour());
									patchMap.put(p2, p4);
								}
							}

						}
					}

					Daisy d = patch.getDaisy();
					p3 = new Patch(d.getDaisyId(), temperature, false, age,
							patch.getxPos(), patch.getyPos(), d.getAlbedo(),
							d.getColour());

				} else {
					p3 = new Patch(0, temperature, true, 0, patch.getxPos(),
							patch.getyPos(), this.albedoOfSurface, "red");

				}
				patchMap.put(p, p3);
			}
		}

		return patchMap;

	}
	/**
	 * @param patchMap
	 * @return
	 * @purpose this method calculates the average temperature of all patches
	 */
	public double calculateGlobalTemperature(HashMap<Point, Patch> patchMap) {

		double globalTemperature = 0.0;
		for (Point p : patchMap.keySet()) {
			if (p.getxPos() > -1 && p.getyPos() > -1
					&& p.getxPos() < this.WINDOW_SIZE
					&& p.getyPos() < this.WINDOW_SIZE) {
				globalTemperature += patchMap.get(p).getTemperature();
			}
		}
		return globalTemperature / this.REAL_PATCHMAP_SIZE;
	}
	/**
	 * @param patchMap
	 * @param colour
	 * @return
	 * @purpose this method finds the number of daisies according to its color
	 */
	public int findNumberofDaisies(HashMap<Point, Patch> patchMap, String colour) {

		int numberofDaisy = 0;
		for (Point p : patchMap.keySet()) {
			if (patchMap.get(p).getDaisy() != null) {
				if (patchMap.get(p).getDaisy().getColour().equals(colour)) {
					numberofDaisy++;
				}
			}
		}
		return numberofDaisy;
	}
	private double randomFloat() {
		Random r = new Random();
		double rFloat = r.nextDouble() * 1.0;
		return rFloat;
	}

	/**
	 * @param xPos
	 * @param yPos
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose checks the validity of point's position 
	 */
	private Point outOfBoundPosition(int xPos, int yPos,
			HashMap<Point, Patch> patchMap) throws Exception {

		int x = xPos;
		if (xPos < 0 || xPos == this.WINDOW_SIZE) {
			x = getRealBound(xPos);
		}
		int y = yPos;
		if (yPos < 0 || yPos == this.WINDOW_SIZE) {
			y = getRealBound(yPos);
		}
		return retrievePointByPosition(x, y, patchMap);

	}

	/**
	 * @param pos
	 * @return
	 * @purpose finds the real neighbor patch position
	 */
	private int getRealBound(int pos) {
		int x = 0;
		if (pos < 0) {
			x = pos + this.WINDOW_SIZE;
		} else if (pos > this.WINDOW_SIZE) {
			x = pos - this.WINDOW_SIZE;
		}
		return x;
	}

	/**
	 * @param workbook
	 * @param tempList
	 * @param sheetName
	 * @return
	 * @purpose excel exporter of double-valued global temperature and luminosity
	 */
	public HSSFWorkbook excelWorkbookWriterForDoubleValues(
			HSSFWorkbook workbook, List<Double> tempList, String sheetName) {

		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("Tick");
		HSSFCell cell2 = row.createCell((short) 1);
		cell2.setCellValue(sheetName);
		int rowNum = 1;
		for (Double temp : tempList) {

			row = sheet.createRow(rowNum);
			int cellNum = 0;
			cell = row.createCell((short) cellNum++);
			cell.setCellValue(rowNum - 1);
			cell2 = row.createCell((short) cellNum);
			cell2.setCellValue(temp);
			rowNum++;
		}
		return workbook;
	}

	/**
	 * @param workbook
	 * @param whiteList
	 * @param blackList
	 * @param sheetName
	 * @return
	 * @purpose excel exporter of integer-valued population(daisy numbers)
	 */
	public HSSFWorkbook excelWorkbookWriterForPopulationValues(
			HSSFWorkbook workbook, List<Integer> whiteList,
			List<Integer> blackList, String sheetName) {

		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("Tick");
		HSSFCell cell2 = row.createCell((short) 1);
		cell2.setCellValue("White");
		HSSFCell cell3 = row.createCell((short) 2);
		cell3.setCellValue("Black");
		int rowNum = 1;
		while (rowNum < whiteList.size()) {

			row = sheet.createRow(rowNum);
			int cellNum = 0;
			cell = row.createCell((short) cellNum++);
			cell.setCellValue(rowNum - 1);
			cell2 = row.createCell((short) cellNum++);
			cell2.setCellValue(whiteList.get(rowNum));
			cell3 = row.createCell((short) cellNum++);
			cell3.setCellValue(blackList.get(rowNum));
			rowNum++;
		}
		return workbook;
	}
	/**
	 * @param id
	 * @param m
	 * @param colour
	 * @param albedo
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @purpose creates random daisies in patches
	 */
	public DaisyWorldModel createPatchesWithDaisies(int id, DaisyWorldModel m,
			String colour, double albedo) throws InterruptedException,
			ExecutionException {

		Point pos;
		double temperature = 0;
		int daisyCount = m.getNumberOfBlacks();
		if (colour.equals("black")) {
			daisyCount = m.getNumberOfBlacks();
		} else {
			daisyCount = m.getNumberOfWhites();
		}
		temperature = calculatePatchTemperature(temperature,
				m.getSolarLuminosity(), albedo);
		for (int i = 0; i < daisyCount; i++) {
			List<Point> list = new ArrayList<Point>(m.patchMap.keySet());
			pos = new Point();
			pos = getRandomPosition(list, m.getSHAPE_COUNT());
			int xPos = pos.getxPos();
			int yPos = pos.getyPos();
			int age = getRandomAge();
			Patch p = new Patch(id, temperature, false, age, xPos, yPos,
					albedo, colour);
			m.patchMap.put(pos, p);
			id++;
		}
		return m;
	}

	/**
	 * @param id
	 * @param solarLuminosity
	 * @param xPos
	 * @param yPos
	 * @param temperature
	 * @param albedoOfSurface
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @purpose creates bare ground
	 */
	public Patch createPatch(int id, double solarLuminosity, int xPos,
			int yPos, double temperature, double albedoOfSurface)
			throws InterruptedException, ExecutionException {

		Point pos = new Point();
		pos.setxPos(xPos);
		pos.setyPos(yPos);
		temperature = calculatePatchTemperature(temperature, solarLuminosity,
				albedoOfSurface);

		Patch p = new Patch(id, temperature, true, 0, xPos, yPos,
				albedoOfSurface, "red");

		return p;

	}

	/**
	 * @param temperature
	 * @param solarLuminosity
	 * @param albedo
	 * @return
	 * @purpose calculates temperature of each patch
	 */
	public static double calculatePatchTemperature(double temperature,
			double solarLuminosity, double albedo) {

		double absorbedLuminosity = ((1 - albedo) * solarLuminosity);
		double localHeating = 0.0;
		if (absorbedLuminosity > 0) {
			localHeating = 72 * Math.log(absorbedLuminosity) + 80;
		} else {
			localHeating = 80;
		}

		return (temperature + localHeating) / 2;
	}

	/**
	 * @return
	 * @purpose initial daisy ages are random
	 */
	public static int getRandomAge() {

		Random randomer = new Random();
		return randomer.nextInt(25);

	}

	/**
	 * @param positionList
	 * @param SHAPE_COUNT
	 * @return
	 * @purpose retrieves random bare ground
	 */
	public static Point getRandomPosition(List<Point> positionList,
			int SHAPE_COUNT) {

		Random randomer = new Random();
		Point nPos = new Point();
		while (true) {
			int x = randomer.nextInt(SHAPE_COUNT);
			int y = randomer.nextInt(SHAPE_COUNT);
			nPos.setxPos(x);
			nPos.setyPos(y);
			if (!compare(positionList, nPos)) {
				positionList.add(nPos);
				return nPos;

			}
		}

	}

	/**
	 * @param pList
	 * @param p
	 * @return
	 * @purpose compares two point's position to understand whether they are the same or not
	 */
	private static boolean compare(List<Point> pList, Point p) {

		for (Point point : pList) {
			if (point.compareTo(p) == 1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param p
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose retrieves the neighbors of patches
	 */
	private List<Point> retrieveNeighbours(Point p,
			HashMap<Point, Patch> patchMap) throws Exception {

		int xPos = p.getxPos();
		int yPos = p.getyPos();
		List<Point> possibleNeighbours = createNeighbours(xPos, yPos,
				-1, this.WINDOW_SIZE, patchMap);

		return possibleNeighbours;
	}

	/**
	 * @param xPos
	 * @param yPos
	 * @param least
	 * @param max
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose finds the 8 neighbors according to the position of patches
	 */
	private List<Point> createNeighbours(int xPos, int yPos, int least,
			int max, HashMap<Point, Patch> patchMap) throws Exception {

		List<Point> posList = new ArrayList<Point>();
		Point p;
		int x = 0, y = 0;
		x = xPos - 1;
		y = yPos;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos - 1;
		y = yPos + 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos - 1;
		y = yPos - 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos;
		y = yPos - 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos + 1;
		y = yPos - 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos + 1;
		y = yPos;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos + 1;
		y = yPos + 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		x = xPos;
		y = yPos + 1;
		p = createNeighbour(x, y, least, max, patchMap);
		if (p != null) {
			posList.add(p);
		}

		return posList;
	}

	/**
	 * @param xPos
	 * @param yPos
	 * @param least
	 * @param max
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose returns a valid neighbor
	 */
	private Point createNeighbour(int xPos, int yPos, int least, int max,
			HashMap<Point, Patch> patchMap) throws Exception {
		Point p = null;
		if (validPosition(xPos, yPos, least, max)) {
			p = retrievePointByPosition(xPos, yPos, patchMap);
			return p;
		} else {
			return null;
		}
	}

	/**
	 * @param xPos
	 * @param yPos
	 * @param least
	 * @param max
	 * @return
	 * @purpose checks the validity of position
	 */
	private boolean validPosition(int xPos, int yPos, int least, int max) {
		if (xPos >= least && xPos <= max && yPos >= least && yPos <= max) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param xPos
	 * @param yPos
	 * @param patchMap
	 * @return
	 * @throws Exception
	 * @purpose retrieves the Point object according to the given x and y coordinates 
	 */
	public Point retrievePointByPosition(int xPos, int yPos,
			HashMap<Point, Patch> patchMap) throws Exception {

		for (Point p : patchMap.keySet()) {
			if ((p.getxPos() == xPos) && (p.getyPos() == yPos)) {
				return p;
			}
		}
		throw new Exception("Error=" + xPos + "," + yPos
				+ " can not be found in patchMap");

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void go() {
		// TODO Auto-generated method stub

	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public double getSolarLuminosity() {
		return solarLuminosity;
	}

	public void setSolarLuminosity(double solarLuminosity) {
		this.solarLuminosity = solarLuminosity;
	}

	public int getPercentageOfWhites() {
		return percentageOfWhites;
	}

	public void setPercentageOfWhites(int percentageOfWhites) {
		this.percentageOfWhites = percentageOfWhites;
	}

	public int getPercentageOfBlacks() {
		return percentageOfBlacks;
	}

	public void setPercentageOfBlacks(int percentageOfBlacks) {
		this.percentageOfBlacks = percentageOfBlacks;
	}

	public double getAlbedoOfSurface() {
		return albedoOfSurface;
	}

	public void setAlbedoOfSurface(double albedoOfSurface) {
		this.albedoOfSurface = albedoOfSurface;
	}

	public double getAlbedoOfWhites() {
		return albedoOfWhites;
	}

	public void setAlbedoOfWhites(double albedoOfWhites) {
		this.albedoOfWhites = albedoOfWhites;
	}

	public double getAlbedoOfBlacks() {
		return albedoOfBlacks;
	}

	public void setAlbedoOfBlacks(double albedoOfBlacks) {
		this.albedoOfBlacks = albedoOfBlacks;
	}

	public int getNumberOfWhites() {
		return numberOfWhites;
	}

	public void setNumberOfWhites(int numberOfWhites) {
		this.numberOfWhites = numberOfWhites;
	}

	public int getNumberOfBlacks() {
		return numberOfBlacks;
	}

	public void setNumberOfBlacks(int numberOfBlacks) {
		this.numberOfBlacks = numberOfBlacks;
	}

	public HashMap<Point, Patch> getPatchMap() {
		return patchMap;
	}

	public void setPatchMap(HashMap<Point, Patch> patchMap) {
		this.patchMap = patchMap;
	}

	public int getSHAPE_COUNT() {
		return WINDOW_SIZE;
	}

	public void setSHAPE_COUNT(int sHAPE_COUNT) {
		WINDOW_SIZE = sHAPE_COUNT;
	}

}
