package ic.doc.camera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for class Camera
 */
@RunWith(MockitoJUnitRunner.class)
public class CameraTest {

	/**
	 * Test object
	 */
	Camera camera;
	
	/**
	 * Mock objects
	 */
	Sensor sensor;
	MemoryCard memoryCard;

	/**
     * Test initialization before every test cases 
     */
	@Before
	public void setup() {
		memoryCard = Mockito.mock(MemoryCard.class);
		sensor = Mockito.mock(Sensor.class);
		camera = new Camera(memoryCard, sensor);
	}

	/**
     * Test Case 
     * Switching the camera on powers up the sensor.
     */
	@Test
	public void switchingTheCameraOnPowersUpTheSensor() {
		camera.powerOn();
		Mockito.verify(sensor).powerUp();
	}

	 /**
     * Test Case
     * Switching the camera off powers down the sensor
     */
	@Test
	public void switchingTheCameraOffPowersDownTheSensor() {
		camera.powerOff();
		Mockito.verify(sensor).powerDown();
	}

	 /**
     * Test Case
     * Pressing the shutter when the power is off does nothing
     */
	@Test
	public void perssingTheShutterWhenPowerOff() {
		camera.powerOff();
		camera.pressShutter();
		
		Mockito.verify(sensor).powerDown();
		Mockito.verifyNoMoreInteractions(sensor, memoryCard);
	}

	/**
     * Test Case
     * Pressing the shutter when the power is on copies data from
     * the sensor to the memory card.
     */
	@Test
	public void perssingTheShutterWithPowerOn() {
		camera.powerOn();
		camera.pressShutter();

		Mockito.verify(sensor).readData();
		Mockito.verify(memoryCard).write((Mockito.any(byte[].class)));
	}

	/**
     * Test Case
     * If data is currently being written, switching the camera 
     * off does not power down the sensor.
     */
	@Test
	public void switchingCameraOffWhenWriting() {
		camera.powerOn();
		camera.pressShutter();
		camera.powerOff();

		Mockito.verify(sensor, Mockito.never()).powerDown();
	}

	/**
     * Test Case
     * Once writing the data has completed, then the camera 
     * powers down the sensor.
     */
	@Test
	public void powerDownSensorOnceWritingCompleted() {
		camera.powerOn();
		camera.pressShutter();
		camera.powerOff();

		Mockito.verify(sensor, Mockito.never()).powerDown();

		camera.writeComplete();
		Mockito.verify(sensor).powerDown();

	}

}
