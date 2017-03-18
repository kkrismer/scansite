package edu.mit.scansite.shared.transferobjects;

import java.sql.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsEntry implements IsSerializable, Comparable<NewsEntry> {

	private int id = -1;

	private String text;
	private Date date;
	private User user;
	private String title;

	public NewsEntry() {
	}

	public NewsEntry(String title, String text, Date date, User user) {
		this.title = title;
		this.text = text;
		this.date = date;
		this.user = user;
	}

	public NewsEntry(int id, String title, String text, Date date, User user) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.date = date;
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		Formatter formatter = new Formatter();
		this.text = formatter.replaceMagicQuotes(text);
	}

	public String geHtmlText() {
		Formatter formatter = new Formatter();
		return formatter.getHtml(text);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Formatter formatter = new Formatter();
		this.title = formatter.replaceMagicQuotes(title);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(NewsEntry o) {
		Date i = this.date;
		Date u = o.getDate();
		if (i != null && u != null) {
			return i.compareTo(u);
		} else {
			if (i == null && u == null) {
				return 0;
			} else if (i == null) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
