package my.edu.utar.groupassignment;


public class MessageModel {
    private String msgId;
    private String senderId;
    private String message;
    private String imageURI;
    private String receiverId;
    private long timestamp;

    public MessageModel(String msgId, String senderId, String message, String imageURI,  String receiverId) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.imageURI = imageURI;
        this.receiverId = receiverId;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageModel() {
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
