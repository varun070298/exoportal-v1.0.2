package org.exoplatform.services.workflow.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class DefaultFormat extends Format {

	public Object parseObject(String source, ParsePosition pos) {
    pos.setErrorIndex(-1);
    if ( source != null ) {
      pos.setIndex(source.length());
    }
		return source;
	}

	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    if ( obj != null ) {
      toAppendTo.append(obj);
    }
		return toAppendTo;
	}

}
