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

import org.ligi.android.R;
import org.ligi.android.common.preferences.TimePreference;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
/**
 * Activity to let users edit preferences for the StatusVoice
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StatusVoicePreferencesActivity extends PreferenceActivity  {

    private TimePreference pause_pref;
    private ListPreference blockProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusVoicePreferences.init(this);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.list);
    }

    @Override
    protected void onResume() {
        setPreferenceScreen(createPreferenceHierarchy());
        super.onResume();
    }

    public String formatedPause() {
        return ""+StatusVoicePreferences.getPauseTimeInMS() +"ms";
    }
    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        root.setPersistent(true);

        // general settings

        PreferenceCategory voceGeneralPrefCat = new PreferenceCategory(this);
        voceGeneralPrefCat.setTitle("general voice settings");
        root.addPreference(voceGeneralPrefCat);

        blockProfile = new ListPreference(this);
        blockProfile.setTitle("Block Profile");
        blockProfile.setEntries(StatusVoicePreferences.getAllProfileNames());
        blockProfile.setEntryValues(StatusVoicePreferences.getAllProfileNames());
        blockProfile.setDialogTitle("Block Profile");
        blockProfile.setKey(StatusVoicePreferences.KEY_PROFILE);
        blockProfile.setOnPreferenceChangeListener(new ReReadProfileOnChange());
        blockProfile.setSummary("list of things to tell");
        blockProfile.setDefaultValue(StatusVoicePreferences.getProfileName());
        voceGeneralPrefCat.addPreference(blockProfile);

        PreferenceScreen edit_blocks=getPreferenceManager().createPreferenceScreen(this);
        Intent i=new Intent(this,StatusVoiceBlockEditActivity.class);
        edit_blocks.setIntent(i);
        edit_blocks.setTitle("Edit Profile");
        voceGeneralPrefCat.addPreference(edit_blocks);

        pause_pref=new TimePreference(this,null);
        pause_pref.setTitle("pause between blocks");
        pause_pref.setDialogTitle("Pause Time");

        pause_pref.setDialogMessage("Please enter a time in ms to pause between the blocks:");
        pause_pref.setDefaultValue(StatusVoicePreferences.getPauseTimeAsString());
        pause_pref.setKey(StatusVoicePreferences.KEY_VOICE_PAUSE);
        pause_pref.setSummary(formatedPause());
        pause_pref.setOnPreferenceChangeListener(new ResetFeederThreadOnPreferenceChange());
        voceGeneralPrefCat.addPreference(pause_pref);

        ListPreference streamPref = new ListPreference(this);
        streamPref.setTitle("Audio Stream");
        streamPref.setEntries(StatusVoicePreferences.getAllStreamNames());
        streamPref.setEntryValues(StatusVoicePreferences.getAllStreamNames());
        streamPref.setDialogTitle("Audio Stream");
        streamPref.setKey(StatusVoicePreferences.KEY_VOICE_STREAM);

        streamPref.setSummary("where to play the voice");
        streamPref.setOnPreferenceChangeListener(new ResetFeederThreadOnPreferenceChange());
        streamPref.setDefaultValue(StatusVoicePreferences.getStreamName());
        voceGeneralPrefCat.addPreference(streamPref);

        PreferenceScreen debug_voice=getPreferenceManager().createPreferenceScreen(this);
        i=new Intent(this,StatusVoiceDebugActivity.class);
        debug_voice.setIntent(i);
        debug_voice.setTitle("Show");
        debug_voice.setSummary("shows what you should hear");
        voceGeneralPrefCat.addPreference(debug_voice);

        return root;
    }

    class ResetFeederThreadOnPreferenceChange implements OnPreferenceChangeListener {
        public boolean onPreferenceChange(Preference pref, Object obj) {
            if (pref==pause_pref)
                pause_pref.setSummary("" + StatusVoicePreferences.pauseStringToMS((String)obj) + "ms");
            StatusVoiceTTSFeederThread.reinit();
            return true;
        }

    }

    class ReReadProfileOnChange implements OnPreferenceChangeListener {
        public boolean onPreferenceChange(Preference pref, Object obj) {
            StatusVoicePreferences.setProfileName((String)obj);
            StatusVoicePreferences.loadCurrentProfile();
            return true;
        }

    }

}

