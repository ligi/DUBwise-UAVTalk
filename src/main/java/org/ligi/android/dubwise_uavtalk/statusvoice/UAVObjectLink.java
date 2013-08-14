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

import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;
/**
 * class to represent a link to an UAVObject including a static function
 * to replace links in a String with either the value or a human 
 * readable description
 *
 * TODO make some buffer algorithm that detecting links in a string doesnt have to be done
 * every time ( lot of times for FreeText and statusvoice
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectLink {

    private int objid=0;
    private int fieldid=0;		
    private int arraypos=0;		

    private final static String START_TAG="[uvobject";
    private final static String OBJID_START="objid='";
    private final static String FIELDID_START="fieldid='";
    private final static String ARRAYPOS_START="arraypos='";
    private final static String END_TAG="]";
    private String act_extract_str="";

    private int extract(String start,String end) {
        int objid_start_pos=act_extract_str.indexOf(start);
        if (objid_start_pos==-1) 
            return -2;

        objid_start_pos+=start.length();

        int objid_end_pos=act_extract_str.indexOf("'",objid_start_pos); 
        if (objid_end_pos==-1) 
            return -3;

        return Integer.parseInt(act_extract_str.substring(objid_start_pos,objid_end_pos));
    }

    public UAVObjectLink(int objid,int fieldid, int arraypos) {
        this.objid=objid;
        this.fieldid=fieldid;
        this.arraypos=arraypos;
    }
    
    public  UAVObjectLink(UAVObjectFieldDescription d,int arr_pos) {
        this.objid=d.getObjId();
        this.fieldid=d.getFieldId();
        this.arraypos=arr_pos;
    }
    

    public UAVObjectLink(String link) {
        link.replace(START_TAG, "");
        act_extract_str=link;
        objid=extract(OBJID_START,"'");
        fieldid=extract(FIELDID_START,"'");
        arraypos=extract(ARRAYPOS_START,"'");
    }

    /**
     * returns the link string
     */
    public String toString() {
        return START_TAG + " objid='" + objid + "' fieldid='" + fieldid + "' arraypos='" +arraypos + "']";
    }

    /**
     * returns the link string
     */
    public String toHumanoidString() {
        if (!UAVObjects.hasObjectWithID(objid))
            return "unknown object";
        UAVObject act_obj=UAVObjects.getObjectByID(objid);
        String res=act_obj.getObjName();
        try {
            res+=">"+(act_obj.getFieldDescriptions()[fieldid]).getName();

            if (((act_obj.getFieldDescriptions()[fieldid]).getElementNames().length>1))
                res+="["+ (act_obj.getFieldDescriptions()[fieldid]).getElementNames()[fieldid] +"="+ fieldid+"]";

        } catch (Exception e){ res+=">unknown_field"+fieldid; }

        return res;
    }

/*
    public float getFloatValue() {
        UAVObjects.getObjectByID(objid).getField(fieldid,arraypos);
        return 0.0f;
    }
*/
    
    public String getValueAsString() {
        return UAVObjects.getObjectByID(objid).getField(fieldid,arraypos).toString();
    }

    public Integer getAsInt() {
        return (Integer)UAVObjects.getObjectByID(objid).getField(fieldid,arraypos);
    }

    public Byte getAsByte() {
        return (Byte)UAVObjects.getObjectByID(objid).getField(fieldid,arraypos);
    }

    public Long getAsLong() {
        return (Long)UAVObjects.getObjectByID(objid).getField(fieldid,arraypos);
    }
    
    public Float getAsFloat() {
        return (Float)UAVObjects.getObjectByID(objid).getField(fieldid,arraypos);
    }
    
    public void setField(Object obj) {
    	UAVObjects.getObjectByID(objid).setField(fieldid,arraypos,obj);
    }
    
    
    public final static byte REPLACE_MODE_HUMAN_DESCR=0;
    public final static byte REPLACE_MODE_VALUES=1;
    public static String replaceLinksInString(String in,byte replace_mode) {
        String out=in;
        while (out.indexOf(START_TAG)!=-1) {

            int start_pos=out.indexOf(START_TAG);
            int end_pos=out.indexOf(END_TAG,start_pos)+1;
            if (end_pos==-1)
                break; // no closing

            UAVObjectLink lnk=new UAVObjectLink(out.substring(start_pos,end_pos));

            String insert="";
            switch (replace_mode) {
            case REPLACE_MODE_HUMAN_DESCR:
                insert=lnk.toHumanoidString();
                break;
            case REPLACE_MODE_VALUES:
                insert=lnk.getValueAsString();
                break;
            }
            out=out.substring(0, start_pos) + insert + out.substring(end_pos);
        }

        return out;
    }

    public int getObjId() {
        return objid;
    }

    public int getArrayPos() {
        return arraypos;
    }
    public int getFieldId() {
        return fieldid;
    }
}
