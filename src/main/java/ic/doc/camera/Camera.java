package ic.doc.camera;

public class Camera implements WriteListener {

	private final MemoryCard memoryCard;
	private final Sensor sensor;
	private boolean isPowerOn;
	private boolean isBusy;
	private boolean isPowerOffRequested;

	public Camera(MemoryCard memoryCard, Sensor sensor) {
		this.memoryCard = memoryCard;
		this.sensor = sensor;        
		this.isPowerOn = false;
		this.isBusy = false;
		this.isPowerOffRequested = false;
	}

	public void pressShutter() {
		if (this.isPowerOn) {
			this.isBusy = true;
			this.memoryCard.write(this.sensor.readData());
		} 
	}

	public void powerOn() {
		this.sensor.powerUp();
		this.isPowerOn = true;
	}

	public void powerOff() {
		if (!this.isBusy) {
			this.sensor.powerDown();
		} else {
			this.isPowerOffRequested = true;
		}
		
	}

	@Override
	public void writeComplete() {
		this.isBusy = false;
		if (this.isPowerOffRequested) {
			this.sensor.powerDown();
			this.isPowerOffRequested = false;
		}
	}   
}
