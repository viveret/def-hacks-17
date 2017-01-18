package com.viveret.safemessage.model.msg;

import java.util.Date;

/**
 * Created by viveret on 1/18/17.
 */

public interface IMessage extends Comparable<IMessage> {
    Date getDate();

    String getTextContent();

    String getTextTitle();
}
