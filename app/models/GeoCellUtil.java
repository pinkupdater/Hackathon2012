package models;

import java.util.List;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.BoundingBox;

public class GeoCellUtil {

	private static final double KM_DEGREES = 0.009d;

	public static List<String> getCells(Double centerLat, Double centerLong,
			int radius) {
		double degrees = (double) radius * KM_DEGREES;
		double latN = Math.max(0, centerLat - degrees);
		double latS = Math.max(0, centerLat + degrees);
		double lonW = Math.max(0, centerLong - degrees);
		double lonE = Math.max(0, centerLong + degrees);

		BoundingBox bb = new BoundingBox(latN, lonE, latS, lonW);
		List<String> cells = GeocellManager.bestBboxSearchCells(bb, null);
		return cells;
	}
}
