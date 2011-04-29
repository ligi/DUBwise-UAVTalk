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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Singleton to access the Blocks for the StatusVoice for 
 * speaking and editing
 * Includes the XML import/export stuff
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
@SuppressWarnings("deprecation") // i need this thing at the moment ..
public class StatusVoiceBlockProvider {

    private static StatusVoiceBlockProvider instance=null;
    private Vector<StatusVoiceBlock> block_vector;

    public static StatusVoiceBlockProvider getInstance() {
        if (instance==null)
            instance=new StatusVoiceBlockProvider();
        return instance;
    }

    public Vector<StatusVoiceBlock> getBlockVector() {
        return block_vector;
    }

    public StatusVoiceBlockProvider() {
        block_vector=new Vector<StatusVoiceBlock>();
    }

    public String toXML() {
        String res="<StatusVoiceBlocks>";
        for (StatusVoiceBlock block :block_vector)
            res+="<StatusVoiceBlock>"+block.getText()+"</StatusVoiceBlock>";
        res+="</StatusVoiceBlocks>";
        return res;
    }

    public void parseInput(InputStream in) {
        block_vector.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse (in);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("StatusVoiceBlock");
            for (int i=0;i<items.getLength();i++) {
                Node item = items.item(i);
                if (item.getChildNodes().getLength()>0)
                    block_vector.add(new StatusVoiceBlock(item.getChildNodes().item(0).getNodeValue()));
                else
                    block_vector.add(new StatusVoiceBlock("no text yet"));
            }

        } catch(Exception e) { block_vector.add(new StatusVoiceBlock("error" + e )); }
    }

    public void parseXML(String xml) {
        parseInput( new StringBufferInputStream((xml)));
    }

    public void parseXMLFile(File file) {
        try {
            parseInput(new FileInputStream( file ));
        } catch (FileNotFoundException e) {		}
    }

}
