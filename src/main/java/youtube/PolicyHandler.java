package youtube;

import youtube.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    VideoServiceRepository videoServiceRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCheckedPolicy_DeleteVideo(@Payload CheckedPolicy checkedPolicy){

        if(checkedPolicy.isMe()){
            System.out.println("##### listener DeleteVideo : " + checkedPolicy.toJson());
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeletedPolicy_DeleteVideo(@Payload DeletedPolicy deletedPolicy){


        if(deletedPolicy.isMe()){
            if(deletedPolicy.getDeleteVideoId()!=null)
            {
                //videoServiceRepository.deleteById(deletedPolicy.getDeleteVideoId());
                System.out.println("##### 동영상이 삭제되었습니다. : " + deletedPolicy.getDeleteVideoId());
            }
            System.out.println("##### listener DeleteVideo : " + deletedPolicy.toJson());
        }
    }

    // editedComment 실행 시
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEditedComment_EditVideo(@Payload EditedComment editedComment){

        if(editedComment.isMe()){
            if(editedComment.getCommentId() != null) {
                System.out.println("##### listener EditVideo : " + editedComment.toJson());
            }
        }
    }

    // createdComment 실행 시
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCreatedComment_EditVideo(@Payload CreatedComment createdComment){

        System.out.println("##### listener @@@@@@@@@@@@@@@@@@@@@@ : " + createdComment.toJson());

        if(createdComment.isMe()){
            if (createdComment.getCommentId() != null) {
                System.out.println("##### listener EditVideo : " + createdComment.toJson());
                videoServiceRepository.findById(createdComment.getVideoId()).ifPresent(videoService -> {
                    if(createdComment.getChannelId() != null) {
                        videoService.setChannelId(createdComment.getChannelId());
                    } else {
                        videoService.setChannelId(videoService.getChannelId());
                    }
                    videoService.setClientId(createdComment.getClientId());
                    videoService.setVideoId(createdComment.getVideoId());

                    videoService.addCommentCount(1); // 댓글 등록 시, 동영상 댓글 수 추가

                    System.out.println("@@@@@@@@@@@@@@  videoService.getCommentCount() : " + videoService.getCommentCount());

                    videoServiceRepository.save(videoService);

                    System.out.println("##### listener EditVideo : Add Comment Count : " + videoService.toString());
                });
            }
        }
    }


}
