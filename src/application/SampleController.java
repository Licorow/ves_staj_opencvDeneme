package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import application.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SampleController implements Initializable {

    @FXML
    private ImageView imageView01;


    
 // a timer for acquiring the video stream
 	private ScheduledExecutorService timer;
 	// the OpenCV object that realizes the video capture
 	private VideoCapture capture = new VideoCapture();
 	// a flag to change the button behavior
 	private boolean cameraActive = false;
 	// the id of the camera to be used
 	private static int cameraId =0;
 	
 	/**
 	 * The action triggered by pushing the button on the GUI
 	 *
 	 * @param event
 	 *            the push button event
 	 */
 	String yazi = "";

 	
 	/*
 	protected void startCamera(ActionEvent event)
 	{
 		if (!this.cameraActive)
 		{
 			// start the video capture
 			this.capture.open(cameraId);
 			
 			// is the video stream available?
 			if (this.capture.isOpened())
 			{
 				this.cameraActive = true;
 				
 				// grab a frame every 33 ms (30 frames/sec)
 				Runnable frameGrabber = new Runnable() {

 					@Override
 					public void run()
 					{

 						// effectively grab and process a single frame
 						Mat frame = grabFrame();
 						// convert and show the frame
 						Image imageToShow = Utils.mat2Image(frame);
 						updateImageView(imageView01, imageToShow);
 						

 					}
 				};
 				
 				this.timer = Executors.newSingleThreadScheduledExecutor();
 				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
 				
 				// update the button content
 				//this.button.setText("Stop Camera");
 			}
 			else
 			{
 				// log the error
 				System.err.println("Impossible to open the camera connection...");
 			}
 		}
 		else
 		{
 			// the camera is not active at this point
 			this.cameraActive = false;
 			// update again the button content
 			//this.button.setText("Start Camera");
 			
 			// stop the timer
 			this.stopAcquisition();
 		}
 	}
 	
 
 	 * Get a frame from the opened video stream (if any)
 	 *
 	 * @return the {@link Mat} to show
 	 */

 	private Mat grabFrame()
 	{
 		// init everything
 		Mat frame = new Mat();
 		
 		// check if the capture is open
 		if (this.capture.isOpened())
 		{
 			try
 			{
 				// read the current frame
 				this.capture.read(frame);
 				
 				// if the frame is not empty, process it
 				if (!frame.empty())
 				{
 					//Core.flip(frame, frame, 1);
 					//Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
 					Size size = frame.size();
 					int x = (int)size.width/2;
 					int y = (int)size.height/2;
 					double[] renk = frame.get(y, x);
 					int r = (int)renk[2];
					int g = (int)renk[1];
					int b = (int)renk[0];
 					//System.out.println(r+","+g+","+b);
 					Imgproc.circle(frame, new Point(x,y), 2, new Scalar(b,g,r),1);
 
 					int boyut1 = 50;
 					int boyut2 = 50;
 					int pixel_sayisi = 0; 
 					x-=boyut1/2;
 					y-=boyut2/2;
 					long rr=0, gg=0, bb=0;
 					Imgproc.rectangle(frame, new Point(x-2,y-2), new Point(x+boyut2+2,y+boyut1+2), new Scalar(255,255,255), 2);
					for (int i=x; i<x+boyut1; i++){
						for (int j=y; j<y+boyut2; j++){
							double[] renk2 =  frame.get(j, i);   
							rr+=(long)renk2[2];
							gg+=(long)renk2[1];
							bb+=(long)renk2[0];
							pixel_sayisi++;
							//System.out.println();
						}
					}
					rr=rr/pixel_sayisi;
					gg=gg/pixel_sayisi;
					bb=bb/pixel_sayisi;
					
					String text = "";
					
					if(rr>150 && gg<rr && bb<rr) {
						text = "KIRMIZI";
					}
					else if(rr<gg && gg>120 && bb<gg) {
						text = "YESIL";
					}	
					else if(rr<bb/2 && gg<bb && bb>100) {
						text = "MAVI";
					}
					else if(rr>180 && gg>180 && bb>180) {
						text = "BEYAZ";
					}
					else if(rr<50 && gg<50 && bb<50) {
						text = "SIYAH";
					}
					else {
						
					}
					int rf = 4;
					int x2= x-text.length()*rf/2;
					Imgproc.putText(frame, text, new Point(x2, y-8), 0, 1, new Scalar(255,255,255), 2);


					String text1;
					text1 = "RGB("+(int)rr+","+(int)gg+","+(int)bb+")";
					Imgproc.putText(frame, text1, new Point(45, 36), 0, 1, new Scalar(255,255,255), 2);
					Imgproc.rectangle(frame, new Point(5,7), new Point(40,42), new Scalar((int)bb,(int)gg,(int)rr), -1);
					

 				}
 				
 			}
 			catch (Exception e)
 			{
 				// log the error
 				System.err.println("Exception during the image elaboration: " + e);
 			}
 		}
 		
 		return frame;
 	}
 	
 	/**
 	 * Stop the acquisition from the camera and release all the resources
 	 */
 	private void stopAcquisition()
 	{
 		if (this.timer!=null && !this.timer.isShutdown())
 		{
 			try
 			{
 				// stop the timer
 				this.timer.shutdown();
 				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
 			}
 			catch (InterruptedException e)
 			{
 				// log any exception
 				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
 			}
 		}
 		
 		if (this.capture.isOpened())
 		{
 			// release the camera
 			this.capture.release();
 		}
 	}
 	
 	/**
 	 * Update the {@link ImageView} in the JavaFX main thread
 	 * 
 	 * @param view
 	 *            the {@link ImageView} to update
 	 * @param image
 	 *            the {@link Image} to show
 	 */
 	private void updateImageView(ImageView view, Image image)
 	{
 		Utils.onFXThread(view.imageProperty(), image);
 	}
 	/*
 	private void updateLabel(Label l, String t)
 	{
 		Utils.onFXThread(l.textProperty(), t);
 	}
 	
 	/**
 	 * On application close, stop the acquisition from the camera
 	 */
 	protected void setClosed()
 	{
 		this.stopAcquisition();
 	}


 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

 		if (!this.cameraActive)
 		{
 			// start the video capture
 			this.capture.open(cameraId);
 			
 			// is the video stream available?
 			if (this.capture.isOpened())
 			{
 				this.cameraActive = true;
 				
 				// grab a frame every 33 ms (30 frames/sec)
 				Runnable frameGrabber = new Runnable() {
 					
 					@Override
 					public void run()
 					{
 						// effectively grab and process a single frame
 						Mat frame = grabFrame();
 						// convert and show the frame
 						Image imageToShow = Utils.mat2Image(frame);
 						updateImageView(imageView01, imageToShow);
 					
 					}
 					
 				};
 				
 	
 				this.timer = Executors.newSingleThreadScheduledExecutor();
 				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
 				
 			}
 			else
 			{
 				// log the error
 				System.err.println("Impossible to open the camera connection...");
 			}
 		}
		
	}
}
