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

package org.ligi.android.dubwise_uavtalk.uavtalk;

import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

public class UAVObjectFieldHelper {


    /**
     * return the value of a field as string 
     * 
     * @param field_desc
     * 
     * @return the value as string
     */
    public static String getFieldValueStr(UAVObjectFieldDescription field_desc) {
        return getFieldValueStr(field_desc,(byte)0);
    }

    /**
     * return the value of a field as string 
     * 
     * @param field_desc
     * @param elm - the element
     * 
     * @return the value as string
     */
    public static String getFieldValueStr(UAVObjectFieldDescription field_desc,byte elm) {
        Object act_val_obj=UAVObjects.getObjectByID(field_desc.getObjId()).getField(field_desc.getFieldId(),elm);
        switch (field_desc.getType()) {
        case UAVObjectFieldDescription.FIELDTYPE_INT8:
            return ""+((Byte)act_val_obj);

        case UAVObjectFieldDescription.FIELDTYPE_INT16:
        case UAVObjectFieldDescription.FIELDTYPE_INT32:
        case UAVObjectFieldDescription.FIELDTYPE_UINT16:
        case UAVObjectFieldDescription.FIELDTYPE_UINT8:
            return ""+(Integer)act_val_obj;	

        case UAVObjectFieldDescription.FIELDTYPE_UINT32:
            return ""+(Long)act_val_obj;	

        case UAVObjectFieldDescription.FIELDTYPE_ENUM:
            return field_desc.getEnumOptions()[(Byte)act_val_obj];

        case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
            return "" + (Float)act_val_obj;

        }
        return "error";
    }
}
