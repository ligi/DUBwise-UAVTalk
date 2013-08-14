/**************************************************************************
 *
 * License:
 *
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent context! 
 *  This explicitly includes that lethal weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *  
 *  The program is provided AS IS with NO WARRANTY OF ANY KIND, 
 *  INCLUDING THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS 
 *  FOR A PARTICULAR PURPOSE.
 *
 *  If you have questions please write the author marcus bueschleb 
 *  a mail to ligi at ligi dot de
 *  
 *  enjoy life!
 *  ligi
 *
 **************************************************************************/

package org.ligi.android.dubwise_uavtalk.statusvoice;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * Thread which feeds the Android TTS with the current stuff to speak
 * this is the main StatusVoice logic - it takes the Blocks from the 
 * StatusVoiceBlockProvider - checks if they need to be spoken, remembers
 * the actual position and manages pauses
 * 
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StatusVoiceTTSFeederThread implements OnInitListener,Runnable {

    private Activity myActivity;
    private TextToSpeech myTTS;
    private HashMap<String, String> voice_params;
    private boolean init_done=false;

    private static StatusVoiceTTSFeederThread instance=null;

    public static StatusVoiceTTSFeederThread getInstance() {
        return instance;
    }
    public static void init(Activity act) {
        if (instance==null)
            instance=new StatusVoiceTTSFeederThread(act);
    }

    public Activity getActivityContext() {
        return myActivity;
    }

    public static void reinit() {
        instance.shutdown();
        instance=new StatusVoiceTTSFeederThread(instance.getActivityContext());
    }

    public void shutdown() {
        myTTS.shutdown();
        running=false;
    }

    public StatusVoiceTTSFeederThread(Activity context_activity) {

        myActivity=context_activity;
        if (init_done)
            return;
        myTTS = new TextToSpeech(myActivity.getApplication(), this);
    }

    /**
     * called by the TextToSpeakInfo
     */
    public void onInit(int status) {
        myTTS.setLanguage(Locale.US);
        voice_params = new HashMap<String, String>();
        voice_params.put("VOICE", "MALE");        // TODO make gender adjustable
        voice_params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,	String.valueOf(StatusVoicePreferences.getStreamEnum()));
        init_done = true;
        new Thread(this).start();
    }

    private boolean running=true;
    private int act_block=0;
    private final static int SLEEP_MS=200;
    private int pause=0;
    private String last_spoken="nothing";

    public int getPause() {
        return pause;
    }

    public void run() {
        while (running) {
            StatusVoiceBlockProvider bp=StatusVoiceBlockProvider.getInstance();
            if ((!myTTS.isSpeaking())&&(pause<=0)) {
                if (act_block<bp.getBlockVector().size()) {
                    last_spoken=UAVObjectLink.replaceLinksInString(bp.getBlockVector().get(act_block++).getText(),UAVObjectLink.REPLACE_MODE_VALUES);
                    myTTS.speak(last_spoken, TextToSpeech.QUEUE_ADD, voice_params);
                }
                else { // with else so that if 0 elements in block - no problem 
                    act_block=0;
                    pause=StatusVoicePreferences.getPauseTimeInMS();
                }
            }

            try {
                Thread.sleep(SLEEP_MS);
                if (!myTTS.isSpeaking()) 
                    pause-=SLEEP_MS;
            } catch (InterruptedException e) {	}

        }
    }

    public String getLastSpoken() {
        return last_spoken;
    }

    public int  getActBlock() {
        return act_block;
    }
}
