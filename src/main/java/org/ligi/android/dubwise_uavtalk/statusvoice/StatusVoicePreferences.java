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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.ligi.tracedroid.logging.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
/**
 * Class to provide/store Preferences for the VoiceOutput
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StatusVoicePreferences {

    private static SharedPreferences shared_prefs;

    public final static String KEY_VOICE_ENABLED="voice_enabled";
    public final static String KEY_VOICE_PAUSE = "voice_pause";
    public final static String KEY_VOICE_STREAM = "voice_stream";
    public final static String KEY_PROFILE = "voice_profile";

    public final static String DEFAULT_PROFILE="silent";
    private static final int DEFAULT_PAUSE = 5000;	

    public static void init(Context context) {
        shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
        StatusVoiceBlockProvider.getInstance().parseXML(shared_prefs.getString("xml", "<StatusVoiceBlocks>"
                +"</StatusVoiceBlocks>" ));
    }

    public static void loadCurrentProfile() {
        StatusVoiceBlockProvider.getInstance().parseXMLFile(new File(getProfileFullFileName()));
    }

    /**
     * @return PauseTime in ms
     */
    public static int getPauseTimeInMS() {
        return pauseStringToMS(shared_prefs.getString(KEY_VOICE_PAUSE,""));
    }

    public static int pauseStringToMS(String pause_str) {

        String[] split= pause_str.split(":");
        if (split.length!=2)
            return DEFAULT_PAUSE;
        else
            try {
                return 1000*(Integer.parseInt(split[0])*60+Integer.parseInt(split[1]));
            }
        catch(Exception e)	{ 
            return DEFAULT_PAUSE; 
        }
    }

    public static String getPauseTimeAsString() {
        return shared_prefs.getString(KEY_VOICE_PAUSE,"00:05");
    }
    public static boolean isVoiceEnabled() {
        return shared_prefs.getBoolean(KEY_VOICE_ENABLED, false);
    }

    public static int getStreamId() {
        System.out.println("stream search -" + getStreamName()+"-");
        int res=getAllStreamNamesAsVector().indexOf(getStreamName());
        System.out.println("stream search RES" + res);
        if (res==-1)
            return 0; // default if not found 

        return res;
    }

    public static String getStreamName() {
        return shared_prefs.getString(KEY_VOICE_STREAM,"no_name");
    }

    public static String[] getAllStreamNames() {
        return new String[] { "Alarm","Notification","Music","System","Voice Call" };
    }

    public static Vector<String> getAllStreamNamesAsVector() {
        Vector<String> res=new Vector<String>();
        for (String s:getAllStreamNames()) {
            System.out.println("stream search in -" +s+"-");
            res.add(s);
        }

        return res;
    }

    public static int[] getAllStreamEnums() {
        return new int[] { AudioManager.STREAM_ALARM,AudioManager.STREAM_NOTIFICATION,AudioManager.STREAM_MUSIC,AudioManager.STREAM_SYSTEM,AudioManager.STREAM_VOICE_CALL };
    }

    public static int getStreamEnum() {
        return getAllStreamEnums()[ getStreamId()];
    }


    public static String[] getAllProfileNames() {
        File f=new File(getProfilePath());
        File[] file_list=f.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.getName().endsWith("."+getProfileFileEnding()) 
                        && (!pathname.getName().equals(DEFAULT_PROFILE+"."+getProfileFileEnding())));
            } });
        int len=1;
        if (file_list!=null)
            len+=file_list.length;

        String[] res=new String[len];
        res[0]=DEFAULT_PROFILE;

        if (file_list!=null) 
            for (int i=0;i<file_list.length;i++)
                res[i+1]=file_list[i].getName().replace("."+getProfileFileEnding(), "");

        return res;
    }



    public static String getProfileName() {
        return shared_prefs.getString(KEY_PROFILE,DEFAULT_PROFILE);
    }

    public static void setProfileName(String new_name) {
        shared_prefs.edit().putString(KEY_PROFILE, new_name).commit();
    }

    public static String getProfilePath() {
        return "/sdcard/DUBwise/voice_profiles";
    }

    public static String getProfileFileEnding() {
        return "svp"; // Status Voice Profile
    }

    public static String getProfileFullFileName() {
        return StatusVoicePreferences.getProfilePath() + "/"+getProfileName()+"."+StatusVoicePreferences.getProfileFileEnding();
    }

    public static void saveCurrentProfile() {
        File f = new File(getProfilePath());

        if (!f.isDirectory())
            f.mkdirs();

        try {
            f=new File(getProfileFullFileName());
            f.createNewFile();

            FileWriter profile_writer = new FileWriter(f);

            BufferedWriter out = new BufferedWriter(profile_writer);

            out.write(StatusVoiceBlockProvider.getInstance().toXML());
            out.close();
            profile_writer.close();
        } catch (IOException e) {
            Log.i("Probem writing profile ( " + getProfileFullFileName() +")\n"+e);
        }


    }
}
