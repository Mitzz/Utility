package file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

public class ModifiedAfterFilter implements FileFilter {

	private Date date;

	public ModifiedAfterFilter(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return true;
		return pathname.lastModified() >= date.getTime();
	}

}
