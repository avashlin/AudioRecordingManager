/**INSERT PACKAGE NAME HERE**/

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Avashlin Moodley on 2015/03/23.
 *
 * the purpose of this class is to allow for audio recording to occur on an android device
 * by making calls to the start recording and stop recording methods
 */
public class AudioRecordingManager
{
    private static final String FILE_EXT_WAV = ".wav";
    private static final String OUTPUT_FOLDER = "AudioRecordings";

    //Media Recorder variable which will be
    // initialized on every call to startRecording
    private MediaRecorder recorder = null;

    //the sole instance of this class that can be used
    private static AudioRecordingManager instance;

    /**
     * private constructor to enforce that an instance
     * can only be created from within this class
     */
    private AudioRecordingManager()
    {

    }

    /**
     * enforces the singleton pattern which only allows for one instance of
     * this class to exist in the program execution
     * @return
     */
    public static AudioRecordingManager getInstance()
    {
        if(instance == null)
        {
            instance = new AudioRecordingManager();
        }
        return instance;
    }

    /**
     * private helper function to generate the complete
     * file path with filename and file extension
     * @param fileName - the name of the output file
     * @return - the path at which the output file will be saved
     *           with filename and extension included
     */
    private String getFilePath(String fileName) {

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, OUTPUT_FOLDER);

        //set the permissions for the output file
        file.setReadable(true,false);
        file.setExecutable(true,false);
        file.setWritable(true,false);

        if (!file.exists())
        {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + fileName + FILE_EXT_WAV);
    }

    /**
     * this method is responsible for initializing and
     * signalling that recording should begin
     * @param fileName the name to assign to the output audio file
     *                 DO NOT INCLUDE THE FILE EXTENSION
     *                 THIS CLASS DEFAULTS THE FILE EXTENSION
     *                 TO WAV
     */
    public void startRecording(String fileName)
    {
        //initialize the media recorder
        recorder = new MediaRecorder();

        //add settings to the media recorder
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        //specify the details of the output file
        recorder.setOutputFile(getFilePath(fileName));

        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        //prepare and start the media recorder
        try
        {
            recorder.prepare();
            recorder.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * calling this method signals the end of recording
     */
    public void stopRecording()
    {
        if (null != recorder)
        {
            recorder.stop();
            recorder.reset();
            recorder.release();

            recorder = null;
        }
    }

    /**
     * media recorder error listener
     */
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener()
    {
        @Override
        public void onError(MediaRecorder mr, int what, int extra)
        {
            Log.d("AUDIO RECORDER ERROR","Error: " + what + ", " + extra);
        }
    };

    /**
     * media recorder warning/info listener
     */
    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener()
    {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra)
        {
            Log.d("AUDIO RECORDER WARNING","Warning: " + what + ", " + extra);
        }
    };
}
