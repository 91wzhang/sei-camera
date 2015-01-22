package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

	Camera camera;
	Sensor sensor;
	MemoryCard memoryCard;
	
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    @Before
    public void setup(){
    	memoryCard  = context.mock(MemoryCard.class);
    	sensor  = context.mock(Sensor.class);                
        camera = new Camera(memoryCard, sensor);
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {                        
         context.checking(new Expectations() {{
        	 exactly(1).of(sensor).powerUp();
         }});
         
         camera.powerOn();
    }
    
    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {        
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerDown();
        }});
        
        camera.powerOff();
    }
    
    @Test
    public void perssingTheShutterWhenPowerOff() {    	                                       
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerDown();
        	never(sensor).readData();
        	never(memoryCard).write(with(any(byte[].class)));
        }});        
        
        camera.powerOff();        
        camera.pressShutter();
        context.assertIsSatisfied();
    }
    
    @Test
    public void perssingTheShutterWithPowerOn() {    	   
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerUp();
        	exactly(1).of(sensor).readData();
        	exactly(1).of(memoryCard).write(with(any(byte[].class)));
        }});
        
        camera.powerOn();
        camera.pressShutter();
    }
    
    @Test
    public void switchingCameraOffWhenWriting() {    	                             
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerUp();
        	exactly(1).of(sensor).readData();
        	exactly(1).of(memoryCard).write(with(any(byte[].class)));
        	never(sensor).powerDown();
        }});        
        
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
    }
    
    @Test
    public void powerDownSensorOnceWritingCompleted() {    	        
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerUp();
        	exactly(1).of(sensor).readData();
        	exactly(1).of(memoryCard).write(with(any(byte[].class)));
        	never(sensor).powerDown();
        }});
        
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();                
                        
        context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerDown();
        }});
        
        camera.writeComplete();
    }

}
