/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category GUI class
 * @inherits Runnable
 * @purpose this is the starting point of the GUI
 * and it consists of form components,charts and grid components to observe daisy world life
 *
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Window extends JFrame implements Runnable {

	Timer chartUpdateTimer, moveTimer, paintGridTimer;
	int index = 0;
	private static int WINDOW_SIZE = ConstantValues.WINDOW_SIZE;
	private static final long serialVersionUID = 1L;
	boolean firstClick = true;
	boolean setupClick = false;

	double albedoBlacks = 0.25;
	double albedoWhites = 0.5;
	double albedoSurface = 0.5;
	int blackPercentage = 0;
	int whitePercentage = 0;
	double luminosity = 0.8;
	int countBlacks = 0;
	int countWhites = 0;
	String lumChoice = ConstantValues.LABEL_SCENARIO_MAINTAIN_CURRENT;

	/*
	 * The following lists are for excel export
	 * lumList = luminosity values list per tick 
	 * tempList = global temperature values list per tick
	 * whitePopList = number of white daisies list per tick
	 * blackPopList = number of black daisies list per tick
	 */
	List<Double> lumList = new ArrayList<Double>();
	List<Double> tempList = new ArrayList<Double>();
	List<Integer> whitePopList = new ArrayList<Integer>();
	List<Integer> blackPopList = new ArrayList<Integer>();

	public Window(DaisyWorldModel m) {

		super("Daisyworld Simulation");
		setSize(1200, 900);
		this.setLayout(new GridLayout(1, 2));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setAutoscrolls(true);
		JPanel panel2 = createGridComponents(m);
		panel = createFormComponents(panel, m, panel2);
		add(panel, BorderLayout.CENTER);
		add(panel2, BorderLayout.CENTER);
	}

	/**
	 * @param m
	 * @return
	 * @purpose creates elements of daisyworld grid,
	 * each of elements in grid represents a patch
	 */
	private JPanel createGridComponents(final DaisyWorldModel m) {
		GridLayout layout = new GridLayout(WINDOW_SIZE, WINDOW_SIZE);
		final JPanel panel = new JPanel(layout);
		for (int i = 0; i < WINDOW_SIZE; i++) {
			for (int j = 0; j < WINDOW_SIZE; j++) {
				JButton b = new JButton(new ImageIcon());
				b.setBackground(Color.red);
				b.repaint();
				panel.add(b);
			}
		}
		/*
		 * this timer is a thread working in series
		 * it calls the swingworker to update the daisyworld grid by each time(tick) 
		 */
		paintGridTimer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DaisyWorldGridUpdateSwingWorker w = new DaisyWorldGridUpdateSwingWorker();
				w.m = m;
				w.panel = panel;
				w.execute();
			}
		});

		return panel;
	}

	/**
	 * @author ie
	 * @extends javax.swing.SwingWorker
	 * @purpose this class is a part of concurrency
	 * and it contributes to update the grid table asynchronously while the calculations(go precedure)
	 * are being processed by another thread
	 *
	 */
	class DaisyWorldGridUpdateSwingWorker extends javax.swing.SwingWorker<List<ImageIcon>, Void> {

		JPanel panel;
		DaisyWorldModel m;

		@Override
		protected List<ImageIcon> doInBackground() throws Exception {

			List<ImageIcon> iconList = new ArrayList<ImageIcon>();
			java.awt.Component[] components = panel.getComponents();
			HashMap<Point, Patch> patchMap = m.getPatchMap();
			int xPos = 0, yPos = 0;
			for (Point p : patchMap.keySet()) {

				if (p.getxPos() > -1 && p.getyPos() > -1
						&& p.getxPos() < WINDOW_SIZE
						&& p.getyPos() < WINDOW_SIZE) {
					xPos = p.getxPos();
					yPos = p.getyPos();
					int pos = (WINDOW_SIZE - 1 - yPos) * WINDOW_SIZE + xPos;
					Patch patch = patchMap.get(p);
					ImageIcon icon = null;
					JButton button = (JButton) components[pos];
					if (!patch.isNobody) {
						Image wImg = ImageIO.read(this.getClass().getResource(
								"resources/white.png"));
						Image bImg = ImageIO.read(this.getClass().getResource(
								"resources/black.png"));
						if (patch.getDaisy().getColour().equals("white")) {
							icon = new ImageIcon(wImg);
						} else if (patch.getDaisy().getColour().equals("black")) {
							icon = new ImageIcon(bImg);
						}
					}
					button.setIcon(icon);

				}

			}

			return iconList;
		}

		@Override
		protected void done() {

		}

	};

	/**
	 * @param m
	 * @return
	 * @purpose this method assign the values of form components to DaisyWorldModel object,
	 * this method works when the user hits the setup button (like in netlogo)
	 */
	private DaisyWorldModel assignValues(DaisyWorldModel m) {

		m.setAlbedoOfBlacks(albedoBlacks);
		m.setAlbedoOfWhites(albedoWhites);
		m.setAlbedoOfSurface(albedoSurface);
		m.setPercentageOfBlacks(blackPercentage);
		m.setPercentageOfWhites(whitePercentage);
		m.setSolarLuminosity(luminosity);
		countBlacks = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
				* blackPercentage / 100.0);
		countWhites = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
				* whitePercentage / 100.0);
		m.setNumberOfBlacks(countBlacks);
		m.setNumberOfWhites(countWhites);
		m.setScenario(lumChoice);
		return m;
	}

	/**
	 * @param panel
	 * @param m
	 * @param panel2
	 * @return
	 * @purpose all visible objects(except the grid) are created in this method 
	 */
	private JPanel createFormComponents(JPanel panel, final DaisyWorldModel m,
			final JPanel panel2) {

		final JButton bSetup = new JButton("Setup");

		// temperature chart
		final XYSeriesCollection tempDataset = new XYSeriesCollection();
		final XYSeries tempSeries = new XYSeries("Temperature");
		tempDataset.addSeries(tempSeries);
		final JFreeChart tempChart = createChart(tempDataset, "", "",
				"Temperature", -10, 40);
		final XYPlot tempPlot = tempChart.getXYPlot();
		tempPlot.setBackgroundPaint(Color.white);

		// luminosity chart
		XYSeriesCollection lumDataset = new XYSeriesCollection();
		final XYSeries lumSeries = new XYSeries("Luminosity");
		lumDataset.addSeries(lumSeries);
		final JFreeChart lumChart = createChart(lumDataset, "", "",
				"Luminosity", 0, 3);
		final XYPlot lumPlot = lumChart.getXYPlot();
		lumPlot.setBackgroundPaint(Color.white);

		// population chart
		XYSeriesCollection popDataset = new XYSeriesCollection();
		final XYSeries whitePopSeries = new XYSeries("White");
		popDataset.addSeries(whitePopSeries);
		final XYSeries blackPopSeries = new XYSeries("Black");
		popDataset.addSeries(blackPopSeries);
		final JFreeChart popChart = createChart(popDataset, "", "",
				"Population", 0, 900);
		final XYPlot popPlot = popChart.getXYPlot();
		popPlot.setBackgroundPaint(Color.white);
		final JButton bGo = new JButton("Go");
		bGo.setEnabled(false);

		final JLabel lCurrentWhiteDaisyCount = new JLabel();
		final JLabel lCurrentBlackDaisyCount = new JLabel();
		final JLabel lCurrentGlobalTemperature = new JLabel();
		JLabel textCurrentWhiteDaisyCount = new JLabel(
				ConstantValues.CURRENT_WHITE_DAISY);
		JLabel textCurrentBlackDaisyCount = new JLabel(
				ConstantValues.CURRENT_BLACK_DAISY);
		JLabel textCurrentGlobalTemperature = new JLabel(
				ConstantValues.CURRENT_GLOBAL_TEMPERATURE);

		final JButton bExcel = new JButton("Export to Excel");
		bExcel.setEnabled(false);
		/*
		 * export to excel button event listener
		 */
		bExcel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HSSFWorkbook workbook = new HSSFWorkbook();
				workbook = m.excelWorkbookWriterForDoubleValues(workbook,
						lumList, "Luminosity");
				workbook = m.excelWorkbookWriterForDoubleValues(workbook,
						tempList, "Temperature");
				workbook = m.excelWorkbookWriterForPopulationValues(workbook,
						whitePopList, blackPopList, "Population");
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				FileOutputStream out;
				try {
					out = new FileOutputStream(new File(
							ConstantValues.EXPORT_PATH + "result_"
									+ dateFormat.format(date) + ".xls"));
					workbook.write(out);
					out.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(null,
						"Excel written successfully..");

			}
		});
		/*
		 * setup button event listener
		 * assigns new values to DaisyWorldModel object, clears the charts
		 * makes the object ready to start to process 
		 */
		bSetup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					assignValues(m);
					setupClick = true;
					m.initalize(m);
					tempSeries.clear();
					lumSeries.clear();
					whitePopSeries.clear();
					blackPopSeries.clear();
					if (chartUpdateTimer.isRunning()) {
						chartUpdateTimer.stop();
					}
					if (moveTimer.isRunning()) {
						moveTimer.stop();
					}
					if (paintGridTimer.isRunning()) {
						paintGridTimer.stop();
					}
					index = 0;
					bGo.setEnabled(true);

					DaisyWorldGridUpdateSwingWorker w = new DaisyWorldGridUpdateSwingWorker();
					w.m = m;
					w.panel = panel2;
					w.execute();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		/*
		 * starts to process(go procedure)
		 */
		bGo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (firstClick) {
					chartUpdateTimer.start();
					moveTimer.start();
					paintGridTimer.start();
					bExcel.setEnabled(false);
					bSetup.setEnabled(false);

				} else {
					chartUpdateTimer.stop();
					moveTimer.stop();
					paintGridTimer.stop();
					bExcel.setEnabled(true);
					bSetup.setEnabled(true);
				}

			}
		});

		bGo.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (firstClick && bGo.isEnabled()) {
					firstClick = false;
					bGo.setBackground(Color.green);
				} else {
					firstClick = true;
					bGo.setBackground(null);
				}
			}
		});

		// sliders
		JSlider slPercentageWhites = new JSlider(JSlider.HORIZONTAL, 0, 50, 20);
		final JLabel lStartofwhites = new JLabel(
				ConstantValues.LABEL_WHITE_PERCENTAGE
						+ slPercentageWhites.getValue());

		whitePercentage = slPercentageWhites.getValue();
		countWhites = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
				* whitePercentage / 100.0);

		slPercentageWhites.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lStartofwhites
						.setText(ConstantValues.LABEL_WHITE_PERCENTAGE
								+ js.getValue());
				whitePercentage = js.getValue();
				countWhites = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
						* js.getValue() / 100.0);
			}
		});

		JSlider slAlbedoWhites = new JSlider(JSlider.HORIZONTAL, 0, 99, 25);
		final JLabel lAlbedoWhites = new JLabel(
				ConstantValues.LABEL_ALBEDO_WHITE
						+ slAlbedoWhites.getValue() * 0.01);
		albedoWhites = slAlbedoWhites.getValue() * 0.01;

		slAlbedoWhites.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lAlbedoWhites.setText(ConstantValues.LABEL_ALBEDO_WHITE
						+ js.getValue() * 0.01);
				albedoWhites = js.getValue()* 0.01;
			}
		});

		JSlider slPercentageBlacks = new JSlider(JSlider.HORIZONTAL, 0, 50, 20);
		final JLabel lStartofBlacks = new JLabel(
				ConstantValues.LABEL_BLACK_PERCENTAGE
						+ slPercentageBlacks.getValue());
		blackPercentage = slPercentageBlacks.getValue();
		countBlacks = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
				* blackPercentage / 100.0);
		slPercentageBlacks.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lStartofBlacks
						.setText(ConstantValues.LABEL_BLACK_PERCENTAGE
								+ js.getValue());
				m.setPercentageOfBlacks(js.getValue());
				m.setNumberOfBlacks((int) Math.round(WINDOW_SIZE * WINDOW_SIZE
						* js.getValue() / 100.0));
				blackPercentage = js.getValue();
				countBlacks = (int) Math.round(WINDOW_SIZE * WINDOW_SIZE
						* js.getValue() / 100.0);
			}
		});

		JSlider slAlbedoBlacks = new JSlider(JSlider.HORIZONTAL, 0, 99, 25);
		final JLabel lAlbedoBlacks = new JLabel(
				ConstantValues.LABEL_ALBEDO_BLACK
						+ slAlbedoBlacks.getValue() * 0.01);
		albedoBlacks = slAlbedoBlacks.getValue() * 0.01;
		slAlbedoBlacks.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lAlbedoBlacks.setText(ConstantValues.LABEL_ALBEDO_BLACK
						+ js.getValue() * 0.01);
				albedoBlacks = js.getValue()* 0.01;
			}
		});

		final JSlider slSolarLimunosity = new JSlider(JSlider.HORIZONTAL, 1,
				3000, 800);
		final JLabel lSolarLimunosity = new JLabel(
				ConstantValues.LABEL_SOLAR_LUMINOSITY
						+ slSolarLimunosity.getValue() * 0.001);
		luminosity = slSolarLimunosity.getValue() * 0.001;
		slSolarLimunosity.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lSolarLimunosity
						.setText(ConstantValues.LABEL_SOLAR_LUMINOSITY
								+ js.getValue() * 0.001);
				luminosity = js.getValue()* 0.001;
			}
		});

		JSlider slAlbedoSurface = new JSlider(JSlider.HORIZONTAL, 0, 100, 40);
		final JLabel lAlbedoSurface = new JLabel(
				ConstantValues.LABEL_ALBEDO_SURFACE
						+ slAlbedoSurface.getValue() * 0.01);
		albedoSurface = slAlbedoSurface.getValue() * 0.01;
		slAlbedoSurface.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider js = (JSlider) e.getSource();
				lAlbedoSurface
						.setText(ConstantValues.LABEL_ALBEDO_SURFACE
								+ js.getValue() * 0.01);
				albedoSurface = js.getValue()* 0.01;
			}
		});

		// scenario label
		final JLabel lScenario = new JLabel(ConstantValues.LABEL_SCENARIO);
		final JComboBox cScenario = new JComboBox();
		for (String lum : ConstantValues.daisyScenarioMap.keySet()) {
			cScenario.addItem(lum);
		}
		cScenario.setSelectedIndex(2);
		lumChoice = cScenario.getSelectedItem().toString();
		cScenario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lumChoice = cScenario.getSelectedItem().toString();
				luminosity = ConstantValues.daisyScenarioMap.get(lumChoice);
				lSolarLimunosity
						.setText(ConstantValues.LABEL_SOLAR_LUMINOSITY
								+ luminosity);
			}
		});
		/*
		 * this timer updates the charts asynchronously
		 * calculates the luminosity level, global temperature and number of daisies to show in charts
		 */
		chartUpdateTimer = new Timer(100, new ActionListener() {

			int whitePopulation = 0;
			int blackPopulation = 0;
			double temperature = 0.0;
			double solarLuminosity = m.getSolarLuminosity();

			@Override
			public void actionPerformed(ActionEvent e) {

				if(lumChoice.equals(ConstantValues.LABEL_SCENARIO_RAMP_UP_DOWN)){
					if (index > 200 && index <= 400) {

						m.setSolarLuminosity(m.getSolarLuminosity() + 0.005);
						slSolarLimunosity.setValue((int) (m.getSolarLuminosity() * 1000));
						lSolarLimunosity
								.setText(ConstantValues.LABEL_SOLAR_LUMINOSITY
										+ m.getSolarLuminosity());
					} else if (index > 600 && index <= 850) {
						m.setSolarLuminosity(m.getSolarLuminosity() - 0.0025);
						slSolarLimunosity.setValue((int) (m.getSolarLuminosity() * 1000));
						lSolarLimunosity
								.setText(ConstantValues.LABEL_SOLAR_LUMINOSITY
										+ m.getSolarLuminosity());
					}
				}
				

				temperature = m.calculateGlobalTemperature(m.getPatchMap());
				tempSeries.add(index, temperature);
				tempList.add(temperature);

				whitePopulation = m.findNumberofDaisies(m.getPatchMap(),
						"white");
				whitePopSeries.add(index, whitePopulation);
				whitePopList.add(whitePopulation);
				blackPopulation = m.findNumberofDaisies(m.getPatchMap(),
						"black");
				blackPopSeries.add(index, blackPopulation);
				blackPopList.add(blackPopulation);
				solarLuminosity = m.getSolarLuminosity();
				lumSeries.add(index, solarLuminosity);
				lumList.add(solarLuminosity);

				ValueAxis tempDomain = tempChart.getXYPlot().getDomainAxis();
				ValueAxis tempRangedomain = tempChart.getXYPlot()
						.getRangeAxis();
				ValueAxis popDomain = popChart.getXYPlot().getDomainAxis();
				ValueAxis lumDomain = lumChart.getXYPlot().getDomainAxis();
				if ((tempDomain.getRange().getUpperBound() - index) == 50) {

					tempDomain.setRange(0, tempDomain.getRange()
							.getUpperBound() + 25);
					popDomain.setRange(0,
							popDomain.getRange().getUpperBound() + 25);
					lumDomain.setRange(0,
							lumDomain.getRange().getUpperBound() + 25);
				}
				if (tempRangedomain.getRange().getUpperBound() - temperature < 15) {
					tempRangedomain.setRange(0, temperature + 20);
				}

				lCurrentWhiteDaisyCount.setText(String.valueOf(whitePopulation));
				lCurrentBlackDaisyCount.setText(String.valueOf(blackPopulation));
				lCurrentGlobalTemperature.setText(String.valueOf(temperature));
				index++;
			}
		});
		/*
		 * this timer helps to continue to the process
		 * calls moveForward method(go)
		 * calculates new temperatures for each patch
		 * diffuses the temperatures
		 * checks survivability 
		 * 
		 */
		moveTimer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					m.moveForward(m.getPatchMap());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		panel.add(bSetup);
		panel.add(bGo);
		panel.add(bExcel);
		panel.add(slPercentageWhites);
		panel.add(lStartofwhites);
		panel.add(slAlbedoWhites);
		panel.add(lAlbedoWhites);
		panel.add(slPercentageBlacks);
		panel.add(lStartofBlacks);
		panel.add(slAlbedoBlacks);
		panel.add(lAlbedoBlacks);
		panel.add(lScenario);
		panel.add(cScenario);
		panel.add(slSolarLimunosity);
		panel.add(lSolarLimunosity);
		panel.add(slAlbedoSurface);
		panel.add(lAlbedoSurface);
		panel.add(lCurrentWhiteDaisyCount);
		panel.add(lCurrentBlackDaisyCount);
		panel.add(lCurrentGlobalTemperature);
		panel.add(textCurrentWhiteDaisyCount);
		panel.add(textCurrentBlackDaisyCount);
		panel.add(textCurrentGlobalTemperature);

		ChartPanel lumCp = new ChartPanel(lumChart);
		panel.add(lumCp);
		ChartPanel tempCp = new ChartPanel(tempChart);
		panel.add(tempCp);
		ChartPanel popCp = new ChartPanel(popChart);
		panel.add(popCp);

		lStartofwhites.setHorizontalAlignment(SwingConstants.RIGHT);
		lAlbedoWhites.setHorizontalAlignment(SwingConstants.RIGHT);
		lStartofBlacks.setHorizontalAlignment(SwingConstants.RIGHT);
		lAlbedoBlacks.setHorizontalAlignment(SwingConstants.RIGHT);
		lSolarLimunosity.setHorizontalAlignment(SwingConstants.RIGHT);
		lAlbedoSurface.setHorizontalAlignment(SwingConstants.RIGHT);

		Insets insets = panel.getInsets();
		Dimension size = bSetup.getPreferredSize();
		bSetup.setBounds(5 + insets.left, 5 + insets.top, size.width,
				size.height);

		bGo.setBounds(5 + insets.left, 40 + insets.top, size.width, size.height);

		size = slPercentageWhites.getPreferredSize();
		slPercentageWhites.setBounds(95 + insets.left, 5 + insets.top,
				size.width, size.height);
		lStartofwhites.setBounds(87 + insets.left, 18 + insets.top, size.width,
				size.height);
		slAlbedoWhites.setBounds(95 + insets.left, 40 + insets.top, size.width,
				size.height);
		lAlbedoWhites.setBounds(87 + insets.left, 53 + insets.top, size.width,
				size.height);

		slPercentageBlacks.setBounds(290 + insets.left, 5 + insets.top,
				size.width, size.height);
		lStartofBlacks.setBounds(282 + insets.left, 18 + insets.top,
				size.width, size.height);
		slAlbedoBlacks.setBounds(290 + insets.left, 40 + insets.top,
				size.width, size.height);
		lAlbedoBlacks.setBounds(282 + insets.left, 53 + insets.top, size.width,
				size.height);

		lScenario.setBounds(0 + insets.left, 90 + insets.top, size.width,
				size.height);
		cScenario.setBounds(0 + insets.left, 105 + insets.top, size.width,
				size.height);
		slSolarLimunosity.setBounds(0 + insets.left, 135 + insets.top,
				size.width, size.height);
		lSolarLimunosity.setBounds(-8 + insets.left, 148 + insets.top,
				size.width, size.height);
		slAlbedoSurface.setBounds(0 + insets.left, 170 + insets.top,
				size.width, size.height);
		lAlbedoSurface.setBounds(-8 + insets.left, 183 + insets.top,
				size.width, size.height);
		lumCp.setBounds(0 + insets.left, 200 + insets.top, 260, 200);
		tempCp.setBounds(270 + insets.left, 90 + insets.top, 260, 200);
		popCp.setBounds(270 + insets.left, 300 + insets.top, 260, 200);
		bExcel.setBounds(300 + insets.left, 520 + insets.top, 150, 40);
		textCurrentBlackDaisyCount.setBounds(10 + insets.left,
				450 + insets.top, 150, 20);
		lCurrentBlackDaisyCount.setBounds(152 + insets.left, 450 + insets.top,
				150, 20);
		textCurrentWhiteDaisyCount.setBounds(10 + insets.left,
				475 + insets.top, 150, 20);
		lCurrentWhiteDaisyCount.setBounds(152 + insets.left, 475 + insets.top,
				150, 20);
		textCurrentGlobalTemperature.setBounds(10 + insets.left,
				500 + insets.top, 150, 20);
		lCurrentGlobalTemperature.setBounds(152 + insets.left,
				500 + insets.top, 150, 20);

		return panel;
	}

	/**
	 * @param dataset
	 * @param topLabel
	 * @param xLabel
	 * @param yLabel
	 * @param minRange
	 * @param maxRange
	 * @return
	 * @purpose a general create method for all charts
	 */
	private JFreeChart createChart(final XYDataset dataset, String topLabel,
			String xLabel, String yLabel, int minRange, int maxRange) {
		final JFreeChart result = ChartFactory.createXYLineChart(topLabel,
				xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, true,
				false); // include legend

		final XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setRange(0, 100);
		ValueAxis range = plot.getRangeAxis();
		range.setRange(minRange, maxRange);
		return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
