package com.Spotify.oauth2.api;

public enum StatusCode {
    CODE_200(200,""),
    CODE_201(201, "Created"),
    CODE_204(204, "No Content"),
    CODE_400(400, "Missing required field: name"),
    CODE_401(401, "Invalid access token"),
    CODE_404(404, "Resource not found"),
    CODE_403(403, "Forbidden"),
    CODE_400_InvalidPlayListID(400, "Invalid base62 id"),
    CODE_200_EmptyBody(200, ""),
    CODE_401_No_token_provided(401, "No token provided"),
    CODE_400_InvalidTrackURI(400,"InvalidTrackIRI"),
    CODE_400_ValidBearer_authentication(400,"Only valid bearer authentication supported");


    public final int code;
    public final String msg;

    StatusCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
//    public int getCode(){
//        return code;
//    }
//
//    public String getMsg(){
//        return msg;
//    }
}
