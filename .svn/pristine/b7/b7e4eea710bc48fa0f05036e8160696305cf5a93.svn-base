/**
 * @author Ilkan Esiyok
 * @date 2014-04-15
 * @category defining class
 * @inherits Comparable
 * @purpose this is a defining class for coordinates in patchMap
 *
 */
import java.util.List;

public class Point implements Comparable<Point> {

	private int xPos;

	private int yPos;

	private List<Point> neighbourList;

	public Point() {
		// TODO Auto-generated constructor stub
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

	@Override
	public int compareTo(Point p) {

		if (p.getxPos() == this.getxPos() && p.getyPos() == this.getyPos()) {
			return 1;
		} else {

			return 0;
		}
	}

	public List<Point> getNeighbourList() {
		return neighbourList;
	}

	public void setNeighbourList(List<Point> neighbourList) {
		this.neighbourList = neighbourList;
	}

}
