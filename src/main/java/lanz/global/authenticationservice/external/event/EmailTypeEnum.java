package lanz.global.authenticationservice.external.event;

import lombok.Getter;

@Getter
public enum EmailTypeEnum {

    NEW_USER_REGISTERED("new_user_registered.ftl");

    final String template;


    EmailTypeEnum(String template) {
        this.template = template;
    }
}
