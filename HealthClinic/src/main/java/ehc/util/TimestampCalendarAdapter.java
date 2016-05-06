package ehc.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import ehc.util.DateUtil;

public class TimestampCalendarAdapter extends XmlAdapter<String, XMLGregorianCalendar> {

	  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //2013-11-22T10:58:50

	  @Override
	    public String marshal(XMLGregorianCalendar v) throws Exception {
		  if (v != null)
	        return dateFormat.format(v.toGregorianCalendar().getTime());
		  else
			  return "";
	    }

	    @Override
	    public XMLGregorianCalendar unmarshal(String v) throws Exception {
	    	Date date = dateFormat.parse(v);
	        return DateUtil.convertToXmlGregorianCalendar(date);
	    }

}
