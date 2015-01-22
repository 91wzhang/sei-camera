package ic.doc.camera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CameraTest {

	Camera camera;
	Sensor sensor;
	MemoryCard memoryCard;

	@Before
	public void setup() {
		memoryCard = Mockito.mock(MemoryCard.class);
		sensor = Mockito.mock(Sensor.class);
		camera = new Camera(memoryCard, sensor);
	}

	@Test
	public void switchingTheCameraOnPowersUpTheSensor() {
		camera.powerOn();
		Mockito.verify(sensor).powerUp();
	}

	@Test
	public void switchingTheCameraOffPowersDownTheSensor() {
		camera.powerOff();
		Mockito.verify(sensor).powerDown();
	}

	@Test
	public void perssingTheShutterWhenPowerOff() {
		camera.powerOff();
		camera.pressShutter();
		Mockito.verify(sensor).powerDown();
		Mockito.verify(sensor, Mockito.never()).readData();
	}

	@Test
	public void perssingTheShutterWithPowerOn() {
		camera.powerOn();
		camera.pressShutter();

		Mockito.verify(sensor).readData();
		Mockito.verify(memoryCard).write((Mockito.any(byte[].class)));
	}

	@Test
	public void switchingCameraOffWhenWriting() {
		camera.powerOn();
		camera.pressShutter();
		camera.powerOff();

		Mockito.verify(sensor, Mockito.never()).powerDown();
	}

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
