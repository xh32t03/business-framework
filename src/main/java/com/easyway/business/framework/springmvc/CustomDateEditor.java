package com.easyway.business.framework.springmvc;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import org.springframework.util.StringUtils;
import com.easyway.business.framework.common.exception.BaseException;
import com.easyway.business.framework.constant.Constant;

/**
 * Spring注入日期到bean属性
 * 
 * @author xl.liu
 */
public class CustomDateEditor extends PropertyEditorSupport {

	private final DateFormat dateFormat;

	private final boolean allowEmpty;

	private final int exactDateLength;

	public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}

	public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		}
		else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
		}
		else {
			try {
                if (this.dateFormat != null && text.length() != 13) {
                    setValue(this.dateFormat.parse(text));
                } else {
                    if (text.contains("-")) {
                        if (text.contains(":")) {
                            setValue(Constant.NORM_DATETIME_FORMAT.get().parse(text));
                        } else {
                            setValue(Constant.NORM_DATE_FORMAT.get().parse(text));
                        }
                    } else if (text.length() == 13) {
                        setValue(Date.from(Instant.ofEpochMilli(Long.valueOf(text))));
                    } else {
                        throw new BaseException("可接受时间格式[yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、毫秒数],异常数据：" + text);
                    }
                }
			}
			catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		DateFormat dateFormat = this.dateFormat;
		if (dateFormat == null) {
			dateFormat = Constant.NORM_DATE_FORMAT.get();
		}
		return (value != null ? dateFormat.format(value) : "");
	}
}
