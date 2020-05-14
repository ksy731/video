package youtube;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

@Entity
@Table(name="VideoService_table")
public class VideoService {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long videoId;
    private Date uploadTime;
    private Long clientId;
    private Long channelId;
    private int viewCount=0;

    private int commentCount = 0; // 댓글수

    @PreUpdate
    public void onPostEdited(){
        EditedVideo editedVideo = new EditedVideo();
        BeanUtils.copyProperties(this, editedVideo);
        editedVideo.publishAfterCommit();

        System.out.println(("**********동영상이 수정되었습니다**********"));
    }

    @PostPersist
    public void onPostUploaded(){
        UploadedVideo uploadedVideo = new UploadedVideo();
        BeanUtils.copyProperties(this, uploadedVideo);
        uploadedVideo.publishAfterCommit();

        System.out.println(("**********동영상이 업로드되었습니다**********"));
    }

    @PreRemove
    public void onPreRemove(){
        DeletedVideo deletedVideo = new DeletedVideo();
        BeanUtils.copyProperties(this, deletedVideo);
        deletedVideo.publishAfterCommit();

        System.out.println(("**********동영상이 삭제되었습니다**********"));
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void addCommentCount(int commentCount) {
        System.out.println("##### Before commentCount : " + commentCount);
        this.commentCount += commentCount;
        System.out.println("##### After commentCount : " + commentCount);
    }
}
