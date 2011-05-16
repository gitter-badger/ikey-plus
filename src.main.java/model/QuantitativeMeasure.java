package model;

/**
 * @author Florian Causse
 * @created 06-avr.-2011
 */
public class QuantitativeMeasure {

	private Double max = null;
	private Double mean = null;
	private Double min = null;
	private Double sd = null;
	private Double uMethLower = null;
	private Double uMethUpper = null;

	/**
	 * constructor
	 */
	public QuantitativeMeasure() {

	}

	/**
	 * get the maximum value
	 * 
	 * @return Double, the maximum value
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * set the maximum value
	 * 
	 * @param Double
	 *            , the maximum value
	 */
	public void setMax(Double max) {
		this.max = max;
	}

	/**
	 * get the mean value
	 * 
	 * @return Double, the mean value
	 */
	public Double getMean() {
		return mean;
	}

	/**
	 * set the mean value
	 * 
	 * @param Double
	 *            , the mean value
	 */
	public void setMean(Double mean) {
		this.mean = mean;
	}

	/**
	 * get the minimum value
	 * 
	 * @return Double, the minimum value
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * set the minimum value
	 * 
	 * @param Double
	 *            , the minimum value
	 */
	public void setMin(Double min) {
		this.min = min;
	}

	/**
	 * get the standard deviation value
	 * 
	 * @return Double, the standard deviation value
	 */
	public Double getSD() {
		return sd;
	}

	/**
	 * set the standard deviation value
	 * 
	 * @param Double
	 *            , the standard deviation value
	 */
	public void setSD(Double sd) {
		this.sd = sd;
	}

	/**
	 * get the normal lower value
	 * 
	 * @return Double, the normal lower value
	 */
	public Double getUMethLower() {
		return uMethLower;
	}

	/**
	 * set the normal lower value
	 * 
	 * @param Double
	 *            , the normal lower value
	 */
	public void setUMethLower(Double uMethLower) {
		this.uMethLower = uMethLower;
	}

	/**
	 * get the normal upper value
	 * 
	 * @return Double, the normal upper value
	 */
	public Double getUMethUpper() {
		return uMethUpper;
	}

	/**
	 * set the normal upper value
	 * 
	 * @param Double
	 *            , the normal upper value
	 */
	public void setUMethUpper(Double uMethUpper) {
		this.uMethUpper = uMethUpper;
	}

	/**
	 * get the string representation
	 * 
	 * @return String, the string representation
	 */
	public String toString() {
		return "Min=" + min + "  Max=" + max + "  Mean=" + mean + "  SD=" + sd
				+ "  UMethLower=" + uMethLower + "  UMethUpper=" + uMethUpper;
	}

}