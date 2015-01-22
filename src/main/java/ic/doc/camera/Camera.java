package ic.doc.camera;

/**
 * Implementation of the camera class
 * @author Dandan Lyu, Wei Zhang
 */
public class Camera implements WriteListener {

	/**
	 * Member
	 * Memory card of the camera. 
	 */
	private final MemoryCard memoryCard;
	
	/**
	 * Member
	 * Sensor of the camera
	 */
	private final Sensor sensor;
	
	/**
	 * Flag 
	 * If the data is being written.
	 */
	private boolean isBusy;
	
	/**
	 * Flag 
	 * If a sensor power down is requested.
	 */
	private boolean isPowerDownRequested;
	
	/**
	 * Flag
	 * If the camera is powered on.	 
	 */
	private boolean isPowerOn;	

	/**
	 * Constructor	 
	 * @param memoryCard
	 * @param sensor
	 */
	public Camera(MemoryCard memoryCard, Sensor sensor) {
		this.memoryCard = memoryCard;
		this.sensor = sensor;        
		this.isPowerOn = false;
		this.isBusy = false;
		this.isPowerDownRequested = false;
	}

	/**
	 * Method
	 * Power off the camera if not writing data,
	 * else request a power down of the sensor.
	 */
	public void powerOff() {
		if (!this.isBusy) {
			this.sensor.powerDown();
		} else {
			this.isPowerDownRequested = true;
		}		
	}

	/**
	 * Method
	 * Power on the camera; power up the sensor.
	 */
	public void powerOn() {
		this.sensor.powerUp();
		this.isPowerOn = true;
	}

	/**
	 * Method
	 * If camera is on copy data from the sensor 
	 * to the memory card, else do nothing.
	 */
	public void pressShutter() {
		if (this.isPowerOn) {
			this.isBusy = true;
			this.memoryCard.write(this.sensor.readData());
		} 
	}

	/**
	 * Method
	 * Event handler when write complete is signaled.
	 * Set flag isBusy to false;
	 * If a power down is requested, power down the sensor.
	 */
	@Override
	public void writeComplete() {
		this.isBusy = false;
		if (this.isPowerDownRequested) {
			this.sensor.powerDown();
			this.isPowerDownRequested = false;
		}
	}   
}
