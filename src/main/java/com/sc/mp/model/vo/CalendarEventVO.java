package com.sc.mp.model.vo;

public class CalendarEventVO {
	
	private String id;
	private String title;
	private boolean allDay;
	private String start;
	private String end;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "CalendarEventVO [id=" + id + ", title=" + title + ", allDay=" + allDay + ", start=" + start + ", end="
				+ end + "]";
	}

}
