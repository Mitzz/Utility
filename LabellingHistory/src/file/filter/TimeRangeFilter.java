package file.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.Date;

import utility.CollectionUtility;
import file.model.FileBeanRetriver;

public class TimeRangeFilter implements FileFilter {

	private final Date fromDate;
	private final Date toDate;

	public TimeRangeFilter(Date fromDate, Date toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return true;
		return pathname.lastModified() >= fromDate.getTime() && pathname.lastModified() <= toDate.getTime();
	}

	public static void main(String[] args) {
		FileBeanRetriver beanRetriver = new FileBeanRetriver("D:/HUBSWorkspace/Workspace");
		int YEAR = 2017;
		int MONTH = Calendar.FEBRUARY;
		int DATE = 16;
		int HOUR_OF_DAY = 9;
		int MINUTE = 00;
		
		Calendar fromInstance = Calendar.getInstance();
		fromInstance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		YEAR = 2017;
		MONTH = Calendar.MARCH;
		DATE = 16;
		HOUR_OF_DAY = 13;
		MINUTE = 00;
		
		Calendar toInstance = Calendar.getInstance();
		toInstance.set(YEAR, MONTH, DATE, HOUR_OF_DAY, MINUTE, 0);
		
		TimeRangeFilter filter = new TimeRangeFilter(fromInstance.getTime(), toInstance.getTime());
		
		beanRetriver.addFileFilter(filter);
		
		CollectionUtility.displayCollection(beanRetriver.get(), "\n");
	}
	
	
}
