package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for class Camera
 */
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
	 * Test context
	 */
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    /**
     * Sequence of invocations
     */
    Sequence testSequence;
    
    /**
     * Test initialization before every test cases 
     */
    @Before
    public void setup(){
    	memoryCard  = context.mock(MemoryCard.class);
    	sensor  = context.mock(Sensor.class);                
        camera = new Camera(memoryCard, sensor);
        testSequence = context.sequence("testSequence");
    }

    /**
     * Test Case 
     * Switching the camera on powers up the sensor.
     */
    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {                        
         switchOn();         
         camera.powerOn();
    }
    
    /**
     * Test Case
     * Switching the camera off powers down the sensor
     */
    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {    	
        switchOff();        
        camera.powerOff();
    }
    
    /**
     * Test Case
     * Pressing the shutter when the power is off does nothing
     */
    @Test
    public void perssingTheShutterWhenPowerOff() {
    	switchOff();
        camera.powerOff();        

        context.checking(new Expectations() {{
        	never(sensor);
        	never(memoryCard);
        }});        
        
        camera.pressShutter();
    }
    
    /**
     * Test Case
     * Pressing the shutter when the power is on copies data from
     * the sensor to the memory card.
     */
    @Test
    public void perssingTheShutterWithPowerOn() {
    	switchOn();
        camera.powerOn();

    	writingData();               
        camera.pressShutter();
    }
    
    /**
     * Test Case
     * If data is currently being written, switching the camera 
     * off does not power down the sensor.
     */
    @Test
    public void switchingCameraOffWhenWriting() {    	                                                     
        switchOn();        
        camera.powerOn();
        
        writingData();
        camera.pressShutter();        
        
        context.checking(new Expectations() {{
        	never(sensor).powerDown();
        	inSequence(testSequence);
        }});
        
        camera.powerOff();        
    }
    
    /**
     * Test Case
     * Once writing the data has completed, then the camera 
     * powers down the sensor.
     */
    @Test
    public void powerDownSensorOnceWritingCompleted() {    	        
    	switchOn();        
        camera.powerOn();
        
        writingData();
        camera.pressShutter();        
        
        context.checking(new Expectations() {{
        	never(sensor).powerDown();  
        	inSequence(testSequence);
        }});
        
        camera.powerOff(); 
        
        switchOff();        
        camera.writeComplete();
    }
    
    
    /**
     * Helper 
     * Validate the sensor is powered up.
     */    
    private void switchOn(){
    	context.checking(new Expectations() {{
    		exactly(1).of(sensor).powerUp();
    		inSequence(testSequence);
        }});
    }
    
    /**
     * Helper
     * Validate the sensor is powered down.
     */
    private void switchOff(){
    	context.checking(new Expectations() {{
        	exactly(1).of(sensor).powerDown();
        	inSequence(testSequence);
        }});
    }
    
    /**
     * Helper 
     * Validate the data is being copied from the sensor 
     * to the memory card.
     */
    private void writingData(){
    	context.checking(new Expectations() {{
    		exactly(1).of(sensor).readData();
        	inSequence(testSequence);
        	exactly(1).of(memoryCard).write(with(any(byte[].class))); 
        	inSequence(testSequence);        	
        }});
    }
}
