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

import java.util.Vector;

import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.android.common.intents.IntentHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * activity to edit the blocks for the StatusVoice
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StatusVoiceBlockEditActivity extends ListActivity {

    private ListActivity myActivity;
    private EditText edit_text;
    private StatusVoiceBlockListAdapter myAdapter;

    private class OpenUAVBrowser implements View.OnClickListener{

        public void onClick(View v) {
            IntentHelper.action4result(myActivity, "PICK_UAVOBJECT",0);
        }

    }

    public void EditBlockAlertDialog(final StatusVoiceBlock block) {
        LinearLayout edit_view=new LinearLayout(myActivity);
        edit_view.setOrientation(LinearLayout.VERTICAL);
        edit_text=new EditText(myActivity);
        edit_text.setText(block.getText());
        edit_view.addView(edit_text);

        Button btn=new Button(myActivity);
        btn.setText("add uavobject");
        class AppendBlock implements DialogInterface.OnClickListener{
            public void onClick(DialogInterface dialog, int which) {
                //getBlockVector().add(new StatusVoiceBlock(edit_text.getText().toString()));
                block.setText(edit_text.getText().toString());
                myAdapter.notifyDataSetChanged();
            }

        }

        btn.setOnClickListener(new OpenUAVBrowser());
        edit_view.addView(btn);

        new AlertDialog.Builder(myActivity).setView(edit_view).setPositiveButton("OK", new AppendBlock()).show();
    }

    class StatusVoiceBlockListAdapter extends BaseAdapter implements OnClickListener {

        public int getCount() {
            return getBlockVector().size()+1;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout lin=new LinearLayout(myActivity);

            if (position<getBlockVector().size()) {

                ImageButton imgb=new ImageButton(myActivity);
                imgb.setImageResource(android.R.drawable.ic_menu_view);
                lin.addView(imgb);

                class ViewBlockAlertOnClick implements View.OnClickListener {
                    public void onClick(View v) {
                        new AlertDialog.Builder(myActivity).setMessage(UAVObjectLink.replaceLinksInString((String)v.getTag(), UAVObjectLink.REPLACE_MODE_VALUES)).show();
                    }

                }
                imgb.setTag(getBlockVector().get(position).getText().toString());
                imgb.setOnClickListener(new ViewBlockAlertOnClick());


                ImageButton del_btn=new ImageButton(myActivity);
                del_btn.setImageResource(android.R.drawable.ic_menu_delete);
                lin.addView(del_btn);

                class DeleteBlockAlertOnClick implements View.OnClickListener {
                    public void onClick(View v) {
                        getBlockVector().removeElementAt((Integer)v.getTag());
                        myAdapter.notifyDataSetChanged();
                    }

                }
                del_btn.setTag((Integer)position);
                del_btn.setOnClickListener(new DeleteBlockAlertOnClick());


                TextView tv=new TextView(myActivity);

                tv.setText(UAVObjectLink.replaceLinksInString(getBlockVector().get(position).getText(),UAVObjectLink.REPLACE_MODE_HUMAN_DESCR) );


                lin.setTag(getBlockVector().get(position));
                lin.addView(tv);
            } else {
                ImageView img=new ImageView(myActivity);
                img.setImageResource(android.R.drawable.ic_menu_add);
                lin.addView(img);
            }

            lin.setOnClickListener(this);

            return lin;
        }

        public void onClick(View view) {
            StatusVoiceBlock act_block=(StatusVoiceBlock)view.getTag();
            if (act_block==null)
                getBlockVector().add(act_block=new StatusVoiceBlock(""));

            EditBlockAlertDialog(act_block);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        //save the xml
        StatusVoicePreferences.saveCurrentProfile();

    }

    public Vector<StatusVoiceBlock> getBlockVector() {
        return StatusVoiceBlockProvider.getInstance().getBlockVector();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity=this;

        this.setTitle("Edit StatusVoice Profile " + StatusVoicePreferences.getProfileName());

        myAdapter=new StatusVoiceBlockListAdapter();
        this.setListAdapter(myAdapter);
    }


    public final static int MENU_SAVE_AS=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0,MENU_SAVE_AS,0,"Save as").setIcon(android.R.drawable.ic_menu_save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case MENU_SAVE_AS:

            class SaveOnClick implements DialogInterface.OnClickListener {
                private EditText myEditText;
                public SaveOnClick(EditText myEditText) {
                    this.myEditText=myEditText;
                }
                public void onClick(DialogInterface dialog, int which) {
                    StatusVoicePreferences.setProfileName(myEditText.getText().toString());
                    StatusVoicePreferences.saveCurrentProfile();
                }

            }

            EditText name_edit=new EditText(this);
            name_edit.setText(StatusVoicePreferences.getProfileName());
            new AlertDialog.Builder(this).setMessage("please enter a name for the profile to save in " + StatusVoicePreferences.getProfilePath()).setTitle("save profile as")
            .setView(name_edit)
            .setNegativeButton(android.R.string.cancel,new DialogDiscarder())
            .setPositiveButton(android.R.string.ok,new SaveOnClick(name_edit))
            .show();


            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( resultCode == Activity.RESULT_OK) {
            UAVObjectLink link=new UAVObjectLink( data.getExtras().getInt("objid") ,  data.getExtras().getInt("fieldid"),  data.getExtras().getInt("arraypos"));
            edit_text.append(link.toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
