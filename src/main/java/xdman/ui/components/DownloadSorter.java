package xdman.ui.components;

import java.util.Comparator;

import xdman.Config;
import xdman.DownloadEntry;
import xdman.XDMApp;

public class DownloadSorter implements Comparator<String> {

	@Override
	public int compare(String id1, String id2) {
		DownloadEntry o1 = XDMApp.getInstance().getEntry(id1);
		DownloadEntry o2 = XDMApp.getInstance().getEntry(id2);
		
		if (o1 == null && o2 == null) return 0;
		if (o1 == null) return 1;
		if (o2 == null) return -1;
		
		int res = 0;
		switch (Config.getInstance().getSortField()) {
		case 0:// sort by date
			res = Long.compare(o1.getDate(), o2.getDate());
			break;
		case 1:// sort by size
			res = Long.compare(o1.getSize(), o2.getSize());
			break;
		case 2:// sort by name
			String f1 = o1.getFile();
			String f2 = o2.getFile();
			if (f1 == null && f2 == null) res = 0;
			else if (f1 == null) res = 1;
			else if (f2 == null) res = -1;
			else res = f1.compareToIgnoreCase(f2);
			break;
		case 3:// sort by type
			res = Integer.compare(o1.getCategory(), o2.getCategory());
			break;
		default:
			break;
		}
		
		if (Config.getInstance().getSortAsc()) {
			// asc
			return res;
		} else {
			// desc
			return -res;
		}
	}
}
