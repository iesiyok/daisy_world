/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category defining class
 * @purpose this is a defining class for patches in patchMap
 *
 */
public class Patch {

	private double temperature;

	private Daisy daisy;

	boolean isNobody;

	private int xPos;

	private int yPos;

	private double albedo;

	/*
	 * Patch constructor
	 * if a patch does not have any daisy in it,
	 * then it is defined nobody 
	 */
	public Patch(int id, double temperature, boolean isNobody, int age,
			int xPos, int yPos, double albedo, String colour) {

		this.temperature = temperature;
		this.isNobody = isNobody;
		Daisy daisy = null;
		if (!isNobody) {
			daisy = new Daisy(id, age, colour, albedo);
		}
		this.daisy = daisy;
		this.xPos = xPos;
		this.yPos = yPos;
		this.albedo = albedo;

	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public Daisy getDaisy() {
		return daisy;
	}

	public void setDaisy(Daisy daisy) {
		this.daisy = daisy;
	}

	public boolean isNobody() {
		return isNobody;
	}

	public void setNobody(boolean isNobody) {
		this.isNobody = isNobody;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public double getAlbedo() {
		return albedo;
	}

	public void setAlbedo(double albedo) {
		this.albedo = albedo;
	}

}
/*
 * Daisy defining class
 */
class Daisy {

	private int daisyId;

	private int age;

	private String colour;// black or white

	private double albedo;

	public Daisy(int daisyId, int age, String colour, double albedo) {
		this.daisyId = daisyId;
		this.age = age;
		this.colour = colour;
		this.albedo = albedo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public double getAlbedo() {
		return albedo;
	}

	public void setAlbedo(double albedo) {
		this.albedo = albedo;
	}

	public int getDaisyId() {
		return daisyId;
	}

	public void setDaisyId(int daisyId) {
		this.daisyId = daisyId;
	}

}
