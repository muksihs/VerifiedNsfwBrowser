package steem.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import steem.models.CommentList.CommentItem;

@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
public class CommentList extends ArrayList<CommentItem> {
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class CommentItem {
		private Discussion comment;

		public Discussion getComment() {
			return comment;
		}

		public void setComment(Discussion comment) {
			this.comment = comment;
		}
	}
}
