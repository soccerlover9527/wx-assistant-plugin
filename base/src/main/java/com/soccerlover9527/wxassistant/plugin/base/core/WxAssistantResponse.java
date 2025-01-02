package com.soccerlover9527.wxassistant.plugin.base.core;

import lombok.Data;

@Data
public class WxAssistantResponse {
    /**
     * 群id
     */
    private String gid;
    /**
     * 回复类型 -1:不处理,0:不回复,1:文本,2:图片,3:视频,4:文件
     */
    private int type;
    /**
     * 回复内容,type=1时为文本内容,type=2/3/4时为资源地址
     */
    private String body;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 发送媒体资源前的提示词,会自动撤回
     */
    private String prompt;

    public WxAssistantResponse() {
    }

    @Override
    public String toString() {
        return "WxAssistantResponse{" +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", filename='" + filename + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }

    public static WxAssistantResponse error(String gid, String error) {
        return text(gid, error);
    }

    public static WxAssistantResponse text(String gid, String body) {
        WxAssistantResponse response = new WxAssistantResponse();
        response.setGid(gid);
        response.setType(1);
        response.setBody(body);
        return response;
    }

    public static WxAssistantResponse image(String gid, String uri, String filename) {
        return WxAssistantResponse.image(gid, uri, filename, null);
    }

    public static WxAssistantResponse image(String gid, String uri, String filename, String prompt) {
        WxAssistantResponse response = new WxAssistantResponse();
        response.setGid(gid);
        response.setType(2);
        response.setBody(uri);
        response.setFilename(filename);
        response.setPrompt(prompt);
        return response;
    }

    public static WxAssistantResponse video(String gid, String uri, String filename) {
        return WxAssistantResponse.video(gid, uri, filename, null);
    }

    public static WxAssistantResponse video(String gid, String uri, String filename, String prompt) {
        WxAssistantResponse response = new WxAssistantResponse();
        response.setGid(gid);
        response.setType(3);
        response.setBody(uri);
        response.setFilename(filename);
        response.setPrompt(prompt);
        return response;
    }


    public static WxAssistantResponse file(String gid, String uri, String filename) {
        return WxAssistantResponse.file(gid, uri, filename, null);
    }

    public static WxAssistantResponse file(String gid, String uri, String filename, String prompt) {
        WxAssistantResponse response = new WxAssistantResponse();
        response.setGid(gid);
        response.setType(4);
        response.setBody(uri);
        response.setFilename(filename);
        response.setPrompt(prompt);
        return response;
    }
}