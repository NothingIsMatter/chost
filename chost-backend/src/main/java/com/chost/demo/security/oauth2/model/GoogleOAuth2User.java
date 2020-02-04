package com.chost.demo.security.oauth2.model;

import java.util.Map;

public class GoogleOAuth2User {
    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    private Map<String,Object> attrs;
    public GoogleOAuth2User(Map<String,Object> attrs){
        this.attrs = attrs;
    }
    public String getEmail(){
        return (String) attrs.get("email");
    }
    public String getId(){
        return (String) attrs.get("sub");
    }
    public String getName(){
        return (String) attrs.get("name");
    }
    public String getImageUrl(){
        return (String) attrs.get("picture");
    }


}
