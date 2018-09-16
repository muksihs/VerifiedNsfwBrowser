package steem.models;

import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import steem.models.CommentList.CommentItem;

@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
public class CommentList extends ArrayList<CommentItem> {
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class CommentItem {
		private String blog;
		@JsonProperty("reblog_on")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
		private Date reblogOn;
		public String getBlog() {
			return blog;
		}

		public void setBlog(String blog) {
			this.blog = blog;
		}

		public Date getReblogOn() {
			return reblogOn;
		}

		public void setReblogOn(Date reblogOn) {
			this.reblogOn = reblogOn;
		}

		public long getEntryId() {
			return entryId;
		}

		public void setEntryId(long entryId) {
			this.entryId = entryId;
		}

		@JsonProperty("entry_id")
		private long entryId;
		private Discussion comment;

		public Discussion getComment() {
			return comment;
		}

		public void setComment(Discussion comment) {
			this.comment = comment;
		}
	}
}
