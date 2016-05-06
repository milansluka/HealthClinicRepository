package ehc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateCalendarAdapter extends XmlAdapter<String, XMLGregorianCalendar> {

	  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //2013-11-22

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
